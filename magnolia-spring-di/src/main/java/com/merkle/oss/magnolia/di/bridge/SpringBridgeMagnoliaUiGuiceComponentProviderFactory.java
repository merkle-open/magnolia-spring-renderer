package com.merkle.oss.magnolia.di.bridge;

import static java.util.stream.Collectors.toList;

import info.magnolia.event.EventBus;
import info.magnolia.event.SystemEventBus;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.objectfactory.configuration.ComponentConfigurer;
import info.magnolia.objectfactory.guice.GuiceComponentProvider;
import info.magnolia.ui.api.app.AppDescriptor;
import info.magnolia.ui.api.app.registry.AppDescriptorRegistry;
import info.magnolia.ui.framework.ioc.MagnoliaUiGuiceComponentProviderFactory;

import jakarta.inject.Inject;
import jakarta.inject.Named;

public class SpringBridgeMagnoliaUiGuiceComponentProviderFactory extends MagnoliaUiGuiceComponentProviderFactory {

    @Inject
    public SpringBridgeMagnoliaUiGuiceComponentProviderFactory(
            @Named(SystemEventBus.NAME) final EventBus systemEventBus,
            final ModuleRegistry moduleRegistry,
            final AppDescriptorRegistry appDescriptorRegistry
    ) {
        super(systemEventBus, moduleRegistry, () -> appDescriptorRegistry.getAllDefinitions().stream().map(AppDescriptor::getName).collect(toList()));
    }

    @Override
    public GuiceComponentProvider create(final ComponentConfigurer... additionalModules) {
        return new SpringBridgeGuiceComponentsProvider(
                super.create(additionalModules)
        );
    }
}
