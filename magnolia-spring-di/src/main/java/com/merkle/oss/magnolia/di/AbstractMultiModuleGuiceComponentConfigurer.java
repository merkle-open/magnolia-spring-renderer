package com.merkle.oss.magnolia.di;

import info.magnolia.objectfactory.guice.AbstractGuiceComponentConfigurer;

import java.util.Set;

import com.google.inject.Module;

public abstract class AbstractMultiModuleGuiceComponentConfigurer extends AbstractGuiceComponentConfigurer {
	private final Set<Module> guiceModules;

	protected AbstractMultiModuleGuiceComponentConfigurer(final Set<Module> guiceModules) {
		this.guiceModules = guiceModules;
	}

	@Override
	protected void configure() {
		guiceModules.forEach(module ->
				module.configure(binder())
		);
	}
}
