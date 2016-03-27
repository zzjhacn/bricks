package com.bricks.utils;

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
}
