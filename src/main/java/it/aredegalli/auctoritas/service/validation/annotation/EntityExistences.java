package it.aredegalli.auctoritas.service.validation.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityExistences {
    EntityExistence[] value();
}
