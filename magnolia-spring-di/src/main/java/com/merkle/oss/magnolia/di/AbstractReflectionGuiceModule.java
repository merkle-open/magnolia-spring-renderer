package com.merkle.oss.magnolia.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

public abstract class AbstractReflectionGuiceModule implements Module {

	protected <T> void addBindings(
			final Binder binder,
			final Class<T> clazz,
			final Annotation annotation,
			final Set<String> packagesToScan,
			final Class<? extends T>... additionalTasks) {
		addBindings(
				clazz,
				Multibinder.newSetBinder(binder, clazz, annotation),
				packagesToScan,
				additionalTasks
		);
	}

	protected <T> void addBindings(
			final Binder binder,
			final Class<T> clazz,
			final Set<String> packagesToScan,
			final Class<? extends T>... additionalTasks) {
		addBindings(
				clazz,
				Multibinder.newSetBinder(binder, clazz),
				packagesToScan,
				additionalTasks
		);
	}

	private <T> void addBindings(
			final Class<T> clazz,
			final Multibinder<T> binder,
			final Set<String> packagesToScan,
			final Class<? extends T>... additionalTasks) {
		Stream
				.concat(
						getClasses(Set.of(new AssignableTypeFilter(clazz)), packagesToScan),
						Arrays.stream(additionalTasks)
				)
				.forEach(implementationClazz ->
						bind(binder, (Class<T>)implementationClazz)
				);
	}

	protected <T> void bind(final Multibinder<T> binder, final Class<T> implementationClazz) {
		binder.addBinding().to(implementationClazz);
	}

	protected Stream<Class<?>> getClasses(final Set<TypeFilter> filters, final Set<String> packagesToScan) {
		return packagesToScan
				.stream()
				.flatMap(packageToScan ->
						getClasses(filters, packageToScan)
				);
	}

	private Stream<Class<?>> getClasses(final Set<TypeFilter> filters, final String packageToScan) {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		filters.forEach(provider::addIncludeFilter);
		return provider
				.findCandidateComponents(packageToScan)
				.stream()
				.map(BeanDefinition::getBeanClassName)
				.map(this::getClass)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(subTypeClazz ->
						!Modifier.isAbstract(subTypeClazz.getModifiers())
				)
				.map(currentClazz -> (Class<?>) currentClazz);
	}

	private Optional<Class<?>> getClass(String clazzName) {
		try {
			return Optional.of(Class.forName(clazzName));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	}
}
