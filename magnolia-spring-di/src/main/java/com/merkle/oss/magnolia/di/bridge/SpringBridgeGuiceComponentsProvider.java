package com.merkle.oss.magnolia.di.bridge;

import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.objectfactory.NoSuchComponentException;
import info.magnolia.objectfactory.ParameterResolver;
import info.magnolia.objectfactory.guice.GuiceComponentProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.inject.Injector;
import com.google.inject.Key;

import jakarta.inject.Provider;

public class SpringBridgeGuiceComponentsProvider extends GuiceComponentProvider implements ComponentProvider {
    private final GuiceComponentProvider delegate;

    public SpringBridgeGuiceComponentsProvider(final GuiceComponentProvider delegate) {
        super(delegate.getTypeMappings(), delegate.getParent());
        this.delegate = delegate;
    }

    @Override
    public <T> Class<? extends T> getImplementation(final Class<T> type) {
        return delegate.getImplementation(type);
    }

    @Override
    public <T> T getComponent(final Class<T> type) throws NoSuchComponentException {
        try {
            return delegate.getComponent(type);
        } catch (final NoSuchComponentException e) {
            return getBean(type, e);
        }
    }

    @Override
    public <T> T newInstanceWithParameterResolvers(final Key<T> type, final ParameterResolver... parameterResolvers) {
        return delegate.newInstanceWithParameterResolvers(type, Stream.concat(
                Arrays.stream(parameterResolvers),
                Stream.of(parameter ->
                        getBean(parameter.getParameterType()).map(bean -> (Object)bean).orElse(ParameterResolver.UNRESOLVED)
                )
        ).toArray(ParameterResolver[]::new));
    }

    @Override
    public GuiceComponentProvider getParent() {
        return delegate.getParent();
    }

    @Override
    public Injector getInjector() {
        return delegate.getInjector();
    }

    @Override
    public <T> Provider<T> getProvider(final Class<T> type) {
        return delegate.getProvider(type);
    }

    @Override
    public void injectMembers(final Object instance) {
        delegate.injectMembers(instance);
    }

    private <T> T getBean(final Class<T> type, final NoSuchComponentException e) throws NoSuchComponentException {
        return getBean(type).orElseThrow(() -> e);
    }
    private <T> Optional<T> getBean(final Class<T> type) {
        return Optional.ofNullable(ApplicationContextBridge.APPLICATION_CONTEXT).flatMap(applicationContext -> {
            try {
                return Optional.of(applicationContext.getBean(type));
            } catch (NoSuchBeanDefinitionException e) {
                return Optional.empty();
            }
        });
    }

    public static class ApplicationContextBridge implements ApplicationContextAware {
        private static ApplicationContext APPLICATION_CONTEXT;

        @Override
        public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
            APPLICATION_CONTEXT = applicationContext;
        }
    }
}
