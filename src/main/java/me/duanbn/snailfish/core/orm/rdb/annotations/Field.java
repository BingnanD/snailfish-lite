package me.duanbn.snailfish.core.orm.rdb.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Field {

    String value() default "";

    boolean isNull() default true;

    boolean index() default false;

    int length() default 255;

    boolean primaryKey() default false;

    boolean jsonField() default false;

    String comment() default "";

}
