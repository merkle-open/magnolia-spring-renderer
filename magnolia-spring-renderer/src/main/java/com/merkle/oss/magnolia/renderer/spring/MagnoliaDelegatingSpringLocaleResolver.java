package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.module.site.Site;
import info.magnolia.module.site.SiteManager;
import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.context.RenderingContext;

import java.util.Locale;

import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MagnoliaDelegatingSpringLocaleResolver implements LocaleResolver {
    private final SiteManager siteManager = Components.getComponent(SiteManager.class);

    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final RenderingContext renderingContext = Components.getComponent(RenderingContext.class);
        final Site site = siteManager.getAssignedSite(renderingContext.getMainContent());
        return site.getI18n().getLocale();
    }

    @Override
    public void setLocale(final HttpServletRequest request, final HttpServletResponse response, final Locale locale) {}
}
