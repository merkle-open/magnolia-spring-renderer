package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.renderer.Renderer;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpringRenderer implements Renderer {
    public static final String NAME = "spring";

    private final DispatcherServletProvider dispatcherServletProvider;

    @Inject
    public SpringRenderer(final DispatcherServletProvider dispatcherServletProvider) {
        this.dispatcherServletProvider = dispatcherServletProvider;
    }

    @Override
    public void render(final RenderingContext ctx, final Map<String, Object> contextObjects) throws RenderException {
        final WebContext webContext = MgnlContext.getWebContext();
        final HttpServletRequest request = webContext.getRequest();
        final HttpServletResponse response = webContext.getResponse();

        final HttpServletRequest requestWrapper = new SpringRendererHttpServletRequestWrapper(request, ctx, contextObjects);
        MgnlContext.push(requestWrapper, response);
        try {
            dispatcherServletProvider.get().service(requestWrapper, response);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            MgnlContext.pop();
        }
    }
}
