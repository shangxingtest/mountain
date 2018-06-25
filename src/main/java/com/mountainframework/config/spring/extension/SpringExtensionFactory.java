package com.mountainframework.config.spring.extension;

import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Sets;

public class SpringExtensionFactory {

	private static final Set<ApplicationContext> contexts = Sets.newConcurrentHashSet();

	public static void addApplicationContext(ApplicationContext context) {
		contexts.add(context);
	}

	public static void removeApplicationContext(ApplicationContext context) {
		contexts.remove(context);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Class<T> beanType) {
		for (ApplicationContext context : contexts) {
			Object bean = context.getBean(name);
			if (beanType.isInstance(bean)) {
				return (T) bean;
			}
		}
		return null;
	}

}
