package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.renderer.Renderer;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SpringRenderer implements Renderer {
    public static final String NAME = "spring";

    private final DispatcherServletProvider dispatcherServletProvider;
    private final SpringRendererHttpServletRequestWrapper.Factory requestWrapperFactory;
    private final SpringRendererHttpServletResponseWrapper.Factory responseWrapperFactory;

    @Inject
    public SpringRenderer(
            final DispatcherServletProvider dispatcherServletProvider,
            final SpringRendererHttpServletRequestWrapper.Factory requestWrapperFactory,
            final SpringRendererHttpServletResponseWrapper.Factory responseWrapperFactory
    ) {
        this.dispatcherServletProvider = dispatcherServletProvider;
        this.requestWrapperFactory = requestWrapperFactory;
        this.responseWrapperFactory = responseWrapperFactory;
    }

    @Override
    public void render(final RenderingContext ctx, final Map<String, Object> contextObjects) throws RenderException {
        final WebContext webContext = MgnlContext.getWebContext();
        final HttpServletRequest request = requestWrapperFactory.create(webContext.getRequest(), ctx, contextObjects);
        final HttpServletResponse response = responseWrapperFactory.create(webContext.getResponse(), ctx, contextObjects);

        MgnlContext.push(request, response);
        try {
            dispatcherServletProvider.get().service(request, response);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            MgnlContext.pop();
        }
    }
}
