package com.merkle.oss.magnolia.renderer.spring;

import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.module.site.Site;
import info.magnolia.module.site.SiteManager;
import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.context.RenderingContext;

import java.util.Locale;
import java.util.Optional;

import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MagnoliaDelegatingSpringLocaleResolver implements LocaleResolver {
    private final SiteManager siteManager = Components.getComponent(SiteManager.class);

    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final RenderingContext renderingContext = Components.getComponent(RenderingContext.class);
        return Optional
                .ofNullable(renderingContext.getCurrentContent())
                .or(() -> Optional.ofNullable(renderingContext.getMainContent()))
                .map(siteManager::getAssignedSite)
                .map(Site::getI18n)
                .map(I18nContentSupport::getLocale)
                .orElse(null);
    }

    @Override
    public void setLocale(final HttpServletRequest request, final HttpServletResponse response, final Locale locale) {}
}
