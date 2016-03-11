package com.bricks.helper.code;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author bricks <long1795@gmail.com>
 */
public enum FieldType {
	STRING(String.class),    //
	LONG(Long.class),    //
	INGETER(Integer.class),    //
	BOOLEAN(Boolean.class),    //
	DATE(Date.class),    //
	SQLDATE(java.sql.Date.class),   //
	BIGDECIMAL(BigDecimal.class);

	private String clazzName;

	private String shortName;

	FieldType(Class<?> clazz) {
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
