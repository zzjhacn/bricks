package com.bricks.helper.code.dal.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bricks <long1795@gmail.com>
 */
public final class TypeUtil {
	private TypeUtil() {}

	private static ConcurrentHashMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();

	static {
		typeMap.put("varchar", String.class);
		typeMap.put("varchar2", String.class);
		typeMap.put("smallint", Integer.class);
		typeMap.put("int", Integer.class);
		typeMap.put("bigint", Long.class);
		typeMap.put("integer", Long.class);
		typeMap.put("date", java.sql.Date.class);
		typeMap.put("datetime", Date.class);
		typeMap.put("timestamp", Date.class);
		typeMap.put("number", BigDecimal.class);
	}

	public static Class<?> toJavaType(String dbType) {
		return typeMap.get(dbType.toLowerCase());
	}
}
