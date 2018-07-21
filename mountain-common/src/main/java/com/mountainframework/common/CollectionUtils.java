package com.mountainframework.common;

import java.util.Collection;

public class CollectionUtils {

	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <T> boolean isNotEmpty(Collection<T> collection) {
		return collection != null && !collection.isEmpty();
	}

}
