package org.livoniawarriors.Robot2019.subsystems.diagnostic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to flag methods
 * as diagnosable, i.e. able to be
 * checked by the Diagnostic Handler
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Diagnosable {

    double range() default 1;
    double exp() default 0;
    boolean expBoolean() default false;
} 
