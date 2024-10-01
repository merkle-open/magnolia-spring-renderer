package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.core.Channel;
import info.magnolia.cms.security.MgnlUser;
import info.magnolia.cms.security.User;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateDefinition;

import java.util.Optional;
import java.util.Set;

import javax.jcr.Node;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MagnoliaHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Set<Class<?>> supportedParameterTypes = Set.of(
            Node.class,
            TemplateDefinition.class,
            AreaDefinition.class,
            AggregationState.class,
            WebContext.class,
            Context.class,
            User.class,
            MgnlUser.class,
            Channel.class
    );

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return supportedParameterTypes.stream().anyMatch(supportedParameterType ->
                parameter.getParameterType().isAssignableFrom(supportedParameterType)
        );
    }

    @Override
    public Object resolveArgument(
            final MethodParameter methodParameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        if (methodParameter.getParameterType().isAssignableFrom(Node.class)) {
            final RenderingContext renderingContext = Components.getComponent(RenderingContext.class);
            if (hasSubsequentParametersOfType(methodParameter, Node.class)) {
                return renderingContext.getMainContent();
            }
            return Optional.ofNullable(renderingContext.getCurrentContent()).orElseGet(renderingContext::getMainContent);
        }
        if (methodParameter.getParameterType().isAssignableFrom(TemplateDefinition.class)) {
            return Components.getComponent(RenderingContext.class).getRenderableDefinition();
        }
        if (methodParameter.getParameterType().isAssignableFrom(AreaDefinition.class)) {
            return Components.getComponent(RenderingContext.class).getRenderableDefinition();
        }
        if (methodParameter.getParameterType().isAssignableFrom(AggregationState.class)) {
            return MgnlContext.getAggregationState();
        }
        if (methodParameter.getParameterType().isAssignableFrom(Context.class)) {
            return MgnlContext.getInstance();
        }
        if (methodParameter.getParameterType().isAssignableFrom(WebContext.class)) {
            return MgnlContext.getWebContext();
        }
        if (methodParameter.getParameterType().isAssignableFrom(User.class)) {
            return MgnlContext.getUser();
        }
        if (methodParameter.getParameterType().isAssignableFrom(MgnlUser.class)) {
            return MgnlContext.getUser();
        }
        if (methodParameter.getParameterType().isAssignableFrom(Channel.class)) {
            return MgnlContext.getAggregationState().getChannel();
        }
        return null;
    }

    private boolean hasSubsequentParametersOfType(final MethodParameter methodParameter, final Class<?> clazz) {
        final Class<?>[] parameterTypes = methodParameter.getMethod().getParameterTypes();
        for (int i = methodParameter.getParameterIndex() + 1; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            if (parameterType.equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
