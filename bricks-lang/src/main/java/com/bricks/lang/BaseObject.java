package com.bricks.lang;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author bricks <long1795@gmail.com>
 */
public class BaseObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String __id = UUID.randomUUID().toString();

	public String get__id() {
		return __id;
	}

	public String toString() {
		// TODO toString
		return super.toString();
	}
}
