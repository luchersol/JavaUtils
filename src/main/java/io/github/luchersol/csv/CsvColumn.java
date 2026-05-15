package io.github.luchersol.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
    int index();

    String name() default "";

    boolean required() default false;

    String format() default "";

    String defaultValue() default "";
}