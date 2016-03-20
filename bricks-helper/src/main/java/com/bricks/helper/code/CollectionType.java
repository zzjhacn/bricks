package com.bricks.helper.code;

import java.util.Map;
import java.util.List;

/**
 * @author bricks <long1795@gmail.com>
 */
public enum CollectionType {
  Map(Map.class),
  List(List.class);

	private String clazzName;

	private String shortName;

	CollectionType(Class<?> clazz) {
		this.clazzName = clazz.getName();
		this.shortName = clazz.getSimpleName();
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
