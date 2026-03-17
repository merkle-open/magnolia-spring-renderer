package com.merkle.oss.magnolia.di.bridge;

import info.magnolia.objectfactory.Components;
import info.magnolia.objectfactory.NoSuchComponentException;

import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class SpringBridgeListableBeanFactory extends DefaultListableBeanFactory {

    public SpringBridgeListableBeanFactory(@Nullable final BeanFactory internalParentBeanFactory) {
        super(internalParentBeanFactory);
    }

    @Override
    public @Nullable Object doResolveDependency(
            final DependencyDescriptor descriptor,
            @Nullable final String beanName,
            @Nullable final Set<String> autowiredBeanNames,
            @Nullable final TypeConverter typeConverter
    ) throws BeansException {
        try {
            return super.doResolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
        } catch (NoSuchBeanDefinitionException e) {
            try {
                return Components.getComponent(descriptor.getDependencyType());
            } catch (NoSuchComponentException ignored) {
                throw e;
            }
        }
    }
}
