package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.rendering.context.RenderingContext;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class SpringRendererHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SpringRendererHttpServletResponseWrapper(final HttpServletResponse response) {
        super(response);
    }

    @Override
    public void resetBuffer() {
        LOG.debug("skipping response.resetBuffer() - since we are in spring renderer");
    }

    @Override
    public void reset() {
        LOG.debug("skipping response.reset() - since we are in spring renderer");
    }

    public static class Factory {
        public SpringRendererHttpServletResponseWrapper create(
                final HttpServletResponse response,
                final RenderingContext renderingContext,
                final Map<String, Object> contextObjects
        ) {
            return new SpringRendererHttpServletResponseWrapper(response);
        }
    }
}
