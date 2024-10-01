package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.template.RenderableDefinition;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.WebUtils;

public class SpringRendererHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final RenderingContext renderingContext;
    private final Map<String, Object> contextObjects;

    public SpringRendererHttpServletRequestWrapper(
            final HttpServletRequest request,
            final RenderingContext renderingContext,
            final Map<String, Object> contextObjects
    ) {
        super(request);
        this.renderingContext = renderingContext;
        this.contextObjects = contextObjects;
    }

    public RenderingContext getRenderingContext() {
        return renderingContext;
    }

    public Map<String, Object> getContextObjects() {
        return contextObjects;
    }

    private String getHandlerPath() {
        final RenderableDefinition renderableDefinition = renderingContext.getRenderableDefinition();
        return "/" + renderableDefinition.getId();
    }

    @Override
    public Object getAttribute(final String name) {
        switch (name) {
            case WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE:
                return getContextPath() + getHandlerPath();
            case WebUtils.INCLUDE_CONTEXT_PATH_ATTRIBUTE:
                return getContextPath();
            case WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE:
                return getHandlerPath();
            case WebUtils.INCLUDE_PATH_INFO_ATTRIBUTE:
                return null;
            case WebUtils.INCLUDE_QUERY_STRING_ATTRIBUTE:
                return getQueryString();
            default:
                return super.getAttribute(name);
        }
    }
}
