package me.duanbn.snailfish.springboot.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.commandbus.CommandBus.CommandExecutorInjector;
import me.duanbn.snailfish.core.commandbus.CommandExecutorRegister;

public class FrameworkCommandBusRegsiter implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerCommandExecutorInjector(importingClassMetadata, registry);
		registerCommandExecutorRegister(importingClassMetadata, registry);
	}

	private void registerCommandExecutorInjector(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(CommandExecutorInjector.class);
		registry.registerBeanDefinition(CommandExecutorInjector.class.getName(), rootBeanDefinition);
	}

	private void registerCommandExecutorRegister(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(CommandExecutorRegister.class);
		registry.registerBeanDefinition(CommandExecutorRegister.class.getName(), rootBeanDefinition);
	}

}
