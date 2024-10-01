package com.merkle.oss.magnolia.renderer.spring.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

/**
 * Disable cache - Sets cache-control response header.
 * <br>
 * Can be used to disable caching of a whole page template or api endpoint
 * <br>
 * <br>See {@link DisableCacheHandlerInterceptorAdapter}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DisableCache {
	long ttlInSeconds() default 0;

	/**
	 * Must have no args constructor!
	 */
	Class<? extends Predicate<HttpServletRequest>> condition() default DefaultCondition.class;

	class DefaultCondition implements Predicate<HttpServletRequest> {
		@Override
		public boolean test(final HttpServletRequest request) {
			return true;
		}
	}
}
