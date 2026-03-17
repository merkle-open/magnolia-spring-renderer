package com.merkle.oss.magnolia.di.bridge;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class SpringBridgeAnnotationConfigWebApplicationContext extends AnnotationConfigWebApplicationContext {
    public SpringBridgeAnnotationConfigWebApplicationContext() {}

    @Override
    protected SpringBridgeListableBeanFactory createBeanFactory() {
        return new SpringBridgeListableBeanFactory(getInternalParentBeanFactory());
    }
}
