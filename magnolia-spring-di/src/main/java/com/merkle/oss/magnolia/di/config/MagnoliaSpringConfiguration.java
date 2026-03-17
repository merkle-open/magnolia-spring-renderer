package com.merkle.oss.magnolia.di.config;


import org.springframework.context.annotation.Bean;

import com.merkle.oss.magnolia.di.bridge.SpringBridgeGuiceComponentsProvider;

public class MagnoliaSpringConfiguration {
    @Bean
    public SpringBridgeGuiceComponentsProvider.ApplicationContextBridge applicationContextBridge() {
        return new SpringBridgeGuiceComponentsProvider.ApplicationContextBridge();
    }
}
