package me.duanbn.snailfish.springboot.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import me.duanbn.snailfish.core.orm.rdb.DataSourceConfig;
import me.duanbn.snailfish.core.orm.rdb.DataSourceManager;

public class FrameworkOrmRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerDataSourceConfig(importingClassMetadata, registry);
        registerDataSourceManager(importingClassMetadata, registry);
    }

    private void registerDataSourceManager(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(DataSourceManager.class);
        registry.registerBeanDefinition(DataSourceManager.class.getName(), rootBeanDefinition);
    }

    private void registerDataSourceConfig(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(DataSourceConfig.class);
        registry.registerBeanDefinition(DataSourceConfig.class.getName(), rootBeanDefinition);
    }

}
