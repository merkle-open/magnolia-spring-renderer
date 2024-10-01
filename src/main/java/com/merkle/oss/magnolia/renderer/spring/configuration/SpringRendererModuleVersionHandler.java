package com.merkle.oss.magnolia.renderer.spring.configuration;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.Task;
import info.magnolia.module.model.Version;

import java.util.List;

import javax.inject.Inject;

public class SpringRendererModuleVersionHandler extends DefaultModuleVersionHandler {
	private final InstallSpringRendererSetupTask installSpringRendererSetupTask;

	@Inject
	public SpringRendererModuleVersionHandler(final InstallSpringRendererSetupTask installSpringRendererSetupTask) {
		this.installSpringRendererSetupTask = installSpringRendererSetupTask;
	}

	@Override
	protected final List<Task> getExtraInstallTasks(final InstallContext installContext) { // when module node does not exist
		return List.of(installSpringRendererSetupTask);
	}

	@Override
	protected final List<Task> getDefaultUpdateTasks(final Version forVersion) { //on every module update
		return List.of(installSpringRendererSetupTask);
	}
}
