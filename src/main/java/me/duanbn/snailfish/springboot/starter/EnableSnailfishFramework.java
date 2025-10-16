package me.duanbn.snailfish.springboot.starter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ FrameworkImportRegister.class, FrameworkDomainBusRegister.class, FrameworkCommandBusRegsiter.class,
        FrameworkEventBusRegister.class, FrameworkPluginBusRegister.class, FrameworkOrmRegister.class })
public @interface EnableSnailfishFramework {

    String[] scanPackages();

    boolean enableLog() default false;

    boolean enableDDL() default false;

    boolean enableSQLLog() default false;

}
