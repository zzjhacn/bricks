package com.bricks.utils;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class StringUtil {
	private StringUtil() {}

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
