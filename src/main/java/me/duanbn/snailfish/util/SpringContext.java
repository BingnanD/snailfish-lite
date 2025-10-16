package me.duanbn.snailfish.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.AccessLevel;
import lombok.Setter;

public class SpringContext {

    @Setter(AccessLevel.PRIVATE)
    private static ApplicationContext appCtx;

    public static class FactoryInjector implements ApplicationContextAware {
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            SpringContext.setAppCtx(applicationContext);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) appCtx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return appCtx.getBean(clazz);
    }

}
