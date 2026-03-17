package com.merkle.oss.magnolia.di.bridge;

import info.magnolia.cms.beans.config.ConfigLoader;
import info.magnolia.cms.beans.config.VersionConfig;
import info.magnolia.cms.i18n.MessagesManager;
import info.magnolia.cms.pddescriptor.ProductDescriptorExtractor;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.objectfactory.Components;
import info.magnolia.objectfactory.guice.GuiceComponentProvider;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

public class SpringBridgeConfigLoader extends ConfigLoader {

    @Inject
    public SpringBridgeConfigLoader(
            final ModuleManager moduleManager,
            final ModuleRegistry moduleRegistry,
            final ProductDescriptorExtractor productDescriptorExtractor,
            final MessagesManager messagesManager,
            final VersionConfig versionConfig,
            final ServletContext context
    ) {
        super(moduleManager, moduleRegistry, productDescriptorExtractor, messagesManager, versionConfig, context);
    }

    @Override
    public void load() {
        super.load();
        Components.setComponentProvider(new SpringBridgeGuiceComponentsProvider(
                (GuiceComponentProvider) Components.getComponentProvider()
        ));
    }
}
