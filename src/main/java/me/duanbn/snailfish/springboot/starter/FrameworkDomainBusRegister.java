package me.duanbn.snailfish.springboot.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.domain.DomainBus.DomainBusInjector;
import me.duanbn.snailfish.core.domain.DomainObjectResgister;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternRegister;

public class FrameworkDomainBusRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerDomainBusInjector(importingClassMetadata, registry);
        registerDomainPatternRegister(importingClassMetadata, registry);
        registerDomainObjectResgister(importingClassMetadata, registry);
    }

    private void registerDomainBusInjector(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(DomainBusInjector.class);
        rootBeanDefinition.setDependsOn(DomainPatternRegister.class.getName(),
                DomainObjectResgister.class.getName());
        registry.registerBeanDefinition(DomainBusInjector.class.getName(), rootBeanDefinition);
    }

    private void registerDomainPatternRegister(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(DomainPatternRegister.class);
        registry.registerBeanDefinition(DomainPatternRegister.class.getName(), rootBeanDefinition);
    }

    private void registerDomainObjectResgister(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(DomainObjectResgister.class);
        registry.registerBeanDefinition(DomainObjectResgister.class.getName(), rootBeanDefinition);
    }

}
