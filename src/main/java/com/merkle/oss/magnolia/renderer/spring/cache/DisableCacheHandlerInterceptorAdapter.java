package com.merkle.oss.magnolia.renderer.spring.cache;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

public class DisableCacheHandlerInterceptorAdapter implements AsyncHandlerInterceptor {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
		if (handler instanceof HandlerMethod) {
			getAnnotation((HandlerMethod) handler).filter(disableCache -> evaluate(disableCache, request)).ifPresent(disableCache -> {
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=" + disableCache.ttlInSeconds());
				response.setHeader("Pragma", "no-cache");
			});
		}
	}

	private Optional<DisableCache> getAnnotation(final HandlerMethod handlerMethod) {
		return Optional.ofNullable(handlerMethod.getMethod().getAnnotation(DisableCache.class)).or(() ->
				Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(DisableCache.class))
		);
	}

	private boolean evaluate(final DisableCache disableCache, final HttpServletRequest request) {
		try {
			return disableCache.condition().getDeclaredConstructor().newInstance().test(request);
		} catch (Exception e) {
			LOG.error("Failed to evaluate disable cache", e);
			return true;
		}
	}
}
