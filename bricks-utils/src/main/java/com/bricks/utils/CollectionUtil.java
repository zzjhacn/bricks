package com.bricks.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CollectionUtil {
	private CollectionUtil() {}

	public static <T> List<T> removeDup(List<T> lst) {
		final List<T> l = new ArrayList<>();
		for (T t : lst) {
			if (!l.contains(t))
				l.add(t);
		}
		return l;
	}

}
