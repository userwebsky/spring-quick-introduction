package pl.robertprogramista.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking audit logs
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    /**
     * Specifies whether to log all data from the request
     * @return true or false
     */
    boolean notOnlyArgs() default false;
}
