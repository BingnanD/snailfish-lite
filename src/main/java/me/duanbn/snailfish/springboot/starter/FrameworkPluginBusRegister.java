package me.duanbn.snailfish.springboot.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.pluginbus.InternalPluginLoader;
import me.duanbn.snailfish.core.pluginbus.PluginBus.PluginBusInjector;
import me.duanbn.snailfish.core.pluginbus.PluginRegister;

public class FrameworkPluginBusRegister implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerPluginBusInjector(importingClassMetadata, registry);
		registerPluginRegister(importingClassMetadata, registry);
		registerInternalPluginLoader(importingClassMetadata, registry);

	}

	private void registerPluginBusInjector(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(PluginBusInjector.class);
		rootBeanDefinition.setDependsOn("pluginRegister");
		registry.registerBeanDefinition(PluginBusInjector.class.getName(), rootBeanDefinition);
	}

	private void registerInternalPluginLoader(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(InternalPluginLoader.class);
		registry.registerBeanDefinition("internalPluginLoader", rootBeanDefinition);
	}

	private void registerPluginRegister(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(PluginRegister.class);
		registry.registerBeanDefinition("pluginRegister", rootBeanDefinition);
	}

}
