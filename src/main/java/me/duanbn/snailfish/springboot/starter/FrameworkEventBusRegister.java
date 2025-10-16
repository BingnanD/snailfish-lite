package me.duanbn.snailfish.springboot.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.eventbus.EventBus.EventBusInjector;
import me.duanbn.snailfish.core.eventbus.EventHandlerRegister;

public class FrameworkEventBusRegister implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerEventBusInjector(importingClassMetadata, registry);
		registerEventHandlerRegister(importingClassMetadata, registry);
	}

	private void registerEventBusInjector(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(EventBusInjector.class);
		rootBeanDefinition.setDependsOn(EventHandlerRegister.class.getName());
		registry.registerBeanDefinition(EventBusInjector.class.getName(), rootBeanDefinition);
	}

	private void registerEventHandlerRegister(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(EventHandlerRegister.class);
		registry.registerBeanDefinition(EventHandlerRegister.class.getName(), rootBeanDefinition);
	}

}
