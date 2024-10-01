package com.merkle.oss.magnolia.renderer.spring;

import org.springframework.web.servlet.DispatcherServlet;

public interface DispatcherServletProvider {
    DispatcherServlet get();
}
