package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.renderer.Renderer;
import info.magnolia.rendering.template.configured.ConfiguredRenderableDefinition;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class MagnoliaTemplateView extends AbstractTemplateView {
    private final Renderer renderer;

    public MagnoliaTemplateView(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void renderMergedTemplateModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (!(request instanceof SpringRendererHttpServletRequestWrapper springRendererRequest)) {
            throw new RenderException("Request is not wrapped by " + SpringRendererHttpServletRequestWrapper.class.getName());
        }

        final RenderingContext renderingContext = springRendererRequest.getRenderingContext();
        ((ConfiguredRenderableDefinition) renderingContext.getRenderableDefinition()).setTemplateScript(getUrl());
        renderer.render(renderingContext, merge(model, springRendererRequest.getContextObjects()));
    }

    private Map<String, Object> merge(final Map<String, Object> first, final Map<String, Object> second) {
        return Stream
                .concat(
                        first.entrySet().stream(),
                        second.entrySet().stream()
                )
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value2));
    }

    public static class Resolver extends AbstractTemplateViewResolver {
        private final Class<? extends Renderer> rendererClass;

        public Resolver(final Class<? extends Renderer> rendererClass) {
            this.rendererClass = rendererClass;
            setViewClass(requiredViewClass());
        }

        @Override
        protected Class<?> requiredViewClass() {
            return MagnoliaTemplateView.class;
        }

        @Override
        protected AbstractUrlBasedView instantiateView() {
            return (getViewClass() == MagnoliaTemplateView.class ? new MagnoliaTemplateView(Components.getComponent(rendererClass)) : super.instantiateView());
        }
    }
}
