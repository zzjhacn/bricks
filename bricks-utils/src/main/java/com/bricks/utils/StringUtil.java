package com.bricks.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * String助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public final class StringUtil {
	private StringUtil() {}

	/**
	 * （数据库）列名转换为（java类）属性名<br/>
	 * 下划线命名法转换为驼峰命名法
	 * 
	 * @param columnName
	 *            （数据库）列名
	 * @return （java类）属性名
	 */
	public static String columnToProperty(final String columnName) {
		return columnToProperty(columnName, null);
	}

	/**
	 * （数据库）列名转换为（java类）属性名<br/>
	 * 下划线命名法转换为驼峰命名法
	 * 
	 * @param nColumnName
	 *            （数据库）列名
	 * @param nPrefix
	 *            （数据库）列名前缀
	 * @return （java类）属性名
	 */
	public static String columnToProperty(final String columnName, final String prefix) {
		final String nPrefix = prefix == null || "".equals(prefix.trim()) ? "" : prefix;
		final String nColumnName = !"".equals(nPrefix) && columnName.startsWith(nPrefix) ? columnName.replaceFirst(nPrefix, "") : columnName;

		char spliter = '_';
		StringBuilder sb = new StringBuilder();
		int i = 0;
		boolean flag = false;
		for (char c : nColumnName.toCharArray()) {
			if (c == spliter && i >= 0) {
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

	/**
	 * （java类）属性名转换为（数据库）列名<br/>
	 * 驼峰命名法转换为下划线命名法
	 * 
	 * @param propertyName
	 *            （java类）属性名
	 * @return （数据库）列名
	 */
	public static String propertyToColumn(String propertyName) {
		return propertyToColumn(propertyName, "");
	}

	/**
	 * （java类）属性名转换为（数据库）列名<br/>
	 * 驼峰命名法转换为下划线命名法
	 * 
	 * @param propertyName
	 *            （java类）属性名
	 * @param prefix
	 *            （数据库）列名前缀
	 * @return （数据库）列名
	 */
	public static String propertyToColumn(String propertyName, String prefix) {
		final String spliter = "_";
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

	/**
	 * 将数组拼装为字符串
	 * 
	 * @param args
	 * @return
	 */
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

	public static String format(String pattern, Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}

	public static String format(String pattern, String... strings) {
		StringBuilder sb = new StringBuilder();
		boolean ignoreNext = false;
		int i = 0;
		for (char c : pattern.toCharArray()) {
			if (c == '\\') {
				ignoreNext = true;
				continue;
			}
			if (!ignoreNext && c == '?' && strings.length > i) {
				sb.append(strings[i++]);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
