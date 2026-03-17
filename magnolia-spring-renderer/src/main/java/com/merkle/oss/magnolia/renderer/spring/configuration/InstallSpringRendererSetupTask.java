package com.merkle.oss.magnolia.renderer.spring.configuration;

import info.magnolia.jcr.nodebuilder.AbstractNodeOperation;
import info.magnolia.jcr.nodebuilder.ErrorHandler;
import info.magnolia.jcr.nodebuilder.NodeOperation;
import info.magnolia.jcr.nodebuilder.Ops;
import info.magnolia.jcr.nodebuilder.task.ErrorHandling;
import info.magnolia.jcr.nodebuilder.task.NodeBuilderTask;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractRepositoryTask;
import info.magnolia.module.delta.TaskExecutionException;
import info.magnolia.repository.RepositoryConstants;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import com.merkle.oss.magnolia.renderer.spring.SpringRenderer;

public class InstallSpringRendererSetupTask extends AbstractRepositoryTask {
	private static final String TASK_NAME = "SpringRendererRenderer Configuration Task";
	private static final String TASK_DESCRIPTION = "This task configures the spring renderer.";
	private static final String PATH = "/modules/rendering/renderers";

	public InstallSpringRendererSetupTask() {
		super(TASK_NAME, TASK_DESCRIPTION);
	}

	@Override
	protected void doExecute(final InstallContext ctx) throws TaskExecutionException {
		new NodeBuilderTask(getName(), getDescription(), ErrorHandling.strict, RepositoryConstants.CONFIG, PATH,
				Ops.getOrAddNode(SpringRenderer.NAME, NodeTypes.ContentNode.NAME).then(
						setOrAddProperty("class", getHandlebarsRendererImplementationClass().getName())
				)
		).execute(ctx);
	}

	protected Class<? extends SpringRenderer> getHandlebarsRendererImplementationClass() {
		return SpringRenderer.class;
	}

	public static NodeOperation setOrAddProperty(final String name, final Object newValue) {
		return new AbstractNodeOperation() {
			@Override
			protected Node doExec(final Node context, final ErrorHandler errorHandler) throws RepositoryException {
				final Value value = PropertyUtil.createValue(newValue, context.getSession().getValueFactory());
				context.setProperty(name, value);
				return context;
			}
		};
	}
}
