package me.duanbn.snailfish.core.eventbus.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EventHandler
 *
 * @author zhilin
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Component
public @interface EventHandler {

    /**
     * priority
     *
     * @return
     */
    int priority() default 0;

}
