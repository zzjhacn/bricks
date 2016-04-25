package com.bricks.utils;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class StringUtil {
	private StringUtil() {}

	public static String columnToProperty(String columnName) {
		return columnToProperty(columnName, "");
	}

	public static String columnToProperty(String columnName, String prefix) {
		if (prefix == null || "".equals(prefix.trim())) {
			prefix = "";
		}
		if (!"".equals(prefix) && columnName.startsWith(prefix)) {
			columnName = columnName.replaceFirst(prefix, "");
		}
		char spliter = '_';
		StringBuilder sb = new StringBuilder(prefix);
		int i = 0;
		boolean flag = false;
		for (char c : columnName.toCharArray()) {
			if (c == spliter && i > 0) {
				flag = true;
				continue;
			}
			if (flag) {
				sb.append(("" + c).toUpperCase());
			} else {
				sb.append(c);
			}
			flag = false;
			i++;
		}
		return sb.toString();
	}

	public static String propertyToColumn(String propertyName) {
		return propertyToColumn(propertyName, "");
	}

	public static String propertyToColumn(String propertyName, String prefix) {
		String spliter = "_";
		if (prefix == null || "".equals(prefix.trim())) {
			prefix = "";
		}
		StringBuilder sb = new StringBuilder(prefix);
		if (!"".equals(prefix)) {
			sb.append(spliter);
		}
		int i = 0;
		for (char c : propertyName.toCharArray()) {
			if (c >= 'A' && c <= 'Z' && i > 0) {
				sb.append(spliter);
			}
			sb.append(c);
			i++;
		}
		return sb.toString().toLowerCase();
	}

	public static String parseArgsAsString(Object[] args) {
		if (args == null || args.length == 0) {
			return "NaN";
		}
		StringBuilder sb = new StringBuilder();
		Arrays.asList(args).forEach(a -> {
			if (a == null) {
				sb.append("null,");
			} else {
				sb.append("(").append(a.getClass().getSimpleName()).append(")").append(JSONObject.toJSONString(a)).append(",");
			}
		});
		return sb.toString();
	}
}
