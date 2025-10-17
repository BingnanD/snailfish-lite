package me.duanbn.snailfish.springboot.starter;

import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.Bootstrap;
import me.duanbn.snailfish.core.Bootstrap.BootstrapAttribute;
import me.duanbn.snailfish.core.RegisterFactory;
import me.duanbn.snailfish.core.domain.DomainBus.DomainBusInjector;
import me.duanbn.snailfish.core.eventbus.EventBus.EventBusInjector;
import me.duanbn.snailfish.core.pluginbus.PluginBus.PluginBusInjector;
import me.duanbn.snailfish.core.ruleengine.RuleFactoryImpl;
import me.duanbn.snailfish.util.SpringContext.FactoryInjector;
import me.duanbn.snailfish.util.collection.Lists;

public class FrameworkImportRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBootstrap(importingClassMetadata, registry);
        registerSpringFactory(importingClassMetadata, registry);
        registerRegisterFactory(importingClassMetadata, registry);
        registerRuleFactoryImpl(importingClassMetadata, registry);
    }

    private void registerBootstrap(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String, Object> annotationAttributesMap = importingClassMetadata
                .getAnnotationAttributes(EnableSnailfishFramework.class.getName());
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationAttributesMap);
        boolean enableLog = annotationAttributes.getBoolean("enableLog");
        boolean enableDDL = annotationAttributes.getBoolean("enableDDL");
        boolean enableSQLLog = annotationAttributes.getBoolean("enableSQLLog");
        String[] scanPackages = annotationAttributes.getStringArray("scanPackages");

        RootBeanDefinition bootstrapAttr = new RootBeanDefinition(BootstrapAttribute.class);
        MutablePropertyValues bootstrapAttrProperties = bootstrapAttr.getPropertyValues();
        bootstrapAttrProperties.add("enableLog", enableLog);
        bootstrapAttrProperties.add("enableDDL", enableDDL);
        bootstrapAttrProperties.add("enableSQLLog", enableSQLLog);
        registry.registerBeanDefinition(BootstrapAttribute.class.getName(), bootstrapAttr);

        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Bootstrap.class);
        rootBeanDefinition.setDependsOn(BootstrapAttribute.class.getName(), DomainBusInjector.class.getName(),
                EventBusInjector.class.getName(),
                PluginBusInjector.class.getName(), FactoryInjector.class.getName());
        MutablePropertyValues propertyValues = rootBeanDefinition.getPropertyValues();
        propertyValues.add("packages", Lists.newArrayList(scanPackages));
        registry.registerBeanDefinition(Bootstrap.class.getName(), rootBeanDefinition);
    }

    private void registerSpringFactory(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(FactoryInjector.class);
        registry.registerBeanDefinition(FactoryInjector.class.getName(), rootBeanDefinition);
    }

    private void registerRegisterFactory(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RegisterFactory.class);
        registry.registerBeanDefinition(RegisterFactory.class.getName(), rootBeanDefinition);
    }

    private void registerRuleFactoryImpl(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RuleFactoryImpl.class);
        registry.registerBeanDefinition(RuleFactoryImpl.class.getName(), rootBeanDefinition);
    }

}
