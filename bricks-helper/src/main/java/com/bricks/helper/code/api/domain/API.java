package com.bricks.helper.code.api.domain;

import com.bricks.helper.code.JavaClass;

/**
 * @author bricks <long1795@gmail.com>
 */
public class API extends JavaClass {
	private static final long serialVersionUID = 1L;

	public API() {}

	/**
	 * @param pkg
	 * @param clazzName
	 * @param comment
	 */
	public API(String pkg, String clazzName, String comment) {
		super(pkg, clazzName, comment);
	}
}
