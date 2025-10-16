package me.duanbn.snailfish.core.commandbus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.api.command.Command;
import me.duanbn.snailfish.api.dto.Response;
import me.duanbn.snailfish.core.commandbus.CommandInterceptorI;

/**
 * 
 * @author zhilin
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Component
public @interface CommandExecutor {

	Class<? extends CommandInterceptorI<? extends Command, ? extends Response>>[] interceptors() default {};

}
