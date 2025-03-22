package it.aredegalli.auctoritas.service.validation.annotation;

import java.lang.annotation.*;

/**
 * Annotation to check the existence of an entity in the repository.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(EntityExistences.class)
public @interface EntityExistence {

    /**
     * The repository class to be used for the existence check.
     *
     * @return the repository class
     */
    Class<?> repository();

    /**
     * The name of the parameter that holds the entity ID.
     *
     * @return the ID parameter name
     */
    String idParam();

    /**
     * The method name to be used for the existence check.
     * Defaults to "existsById".
     *
     * @return the method name
     */
    String method() default "existsById";

    /**
     * Whether to perform the existence check.
     * Defaults to true.
     *
     * @return true if the check should be performed, false otherwise
     */
    boolean check() default true;

    /**
     * The message to be used if the entity is not found.
     * Defaults to "Entity not found".
     *
     * @return the not found message
     */
    String notFoundMessage() default "Entity not found";

    /**
     * The message to be used if the entity already exists.
     * Defaults to "Entity already exists".
     *
     * @return the conflict message
     */
    String conflictMessage() default "Entity already exists";

    /**
     * The log level to be used for logging.
     * Defaults to "warn".
     *
     * @return the log level
     */
    String logLevel() default "warn";
}
