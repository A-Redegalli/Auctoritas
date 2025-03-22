package it.aredegalli.auctoritas.service.validation.annotation;

import it.aredegalli.auctoritas.exception.ConflictException;
import it.aredegalli.auctoritas.exception.NotFoundException;
import it.aredegalli.auctoritas.repository.UUIDRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Aspect for checking the existence of an entity before executing a method.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ExistenceAspect {

    private final ApplicationContext context;

    /**
     * Checks the existence of an entity based on the provided annotation.
     *
     * @param joinPoint       the join point representing the method execution
     * @param entityExistence the annotation containing the existence check configuration
     * @throws Throwable if an error occurs during the existence check
     */
    @Before("@annotation(entityExistence)")
    public void checkExistence(JoinPoint joinPoint, EntityExistence entityExistence) throws Throwable {
        Object paramValue = getParamValue(joinPoint, entityExistence);

        Object repoBean = context.getBean(entityExistence.repository());

        boolean exists;

        if (entityExistence.method().equals("existsById")) {
            if (!(repoBean instanceof UUIDRepository<?> repository)) {
                throw new RuntimeException("Invalid repository: " + entityExistence.repository().getName());
            }
            exists = repository.existsById((UUID) paramValue);
        } else {
            Method method = entityExistence.repository().getMethod(entityExistence.method(), paramValue.getClass());
            exists = (boolean) method.invoke(repoBean, paramValue);
        }

        logWithLevel(entityExistence.logLevel(),
                entityExistence.repository().getSimpleName(), entityExistence.idParam(), paramValue, exists);

        if (entityExistence.check() && !exists) {
            throw new NotFoundException(entityExistence.notFoundMessage());
        } else if (!entityExistence.check() && exists) {
            throw new ConflictException(entityExistence.conflictMessage());
        }
    }

    /**
     * Retrieves the value of the parameter specified in the annotation.
     *
     * @param joinPoint       the join point representing the method execution
     * @param entityExistence the annotation containing the parameter configuration
     * @return the value of the specified parameter
     */
    private Object getParamValue(JoinPoint joinPoint, EntityExistence entityExistence) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(entityExistence.idParam())) {
                return args[i];
            }
        }

        throw new RuntimeException("Parameter '" + entityExistence.idParam() + "' not found or null");
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param level the log level
     * @param args  the arguments to be included in the log message
     */
    private void logWithLevel(String level, Object... args) {
        switch (level.toLowerCase()) {
            case "info":
                log.info("[API] Entity in {} with {}={} existence: {}", args);
                break;
            case "error":
                log.error("[API] Entity in {} with {}={} existence: {}", args);
                break;
            case "debug":
                log.debug("[API] Entity in {} with {}={} existence: {}", args);
                break;
            default:
                log.warn("[API] Entity in {} with {}={} existence: {}", args);
        }
    }
}