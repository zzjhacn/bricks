package com.bricks.helper.code;

import java.util.Map;

import com.bricks.utils.StringUtil;

import java.util.List;

/**
 * @author bricks <long1795@gmail.com>
 */
public enum GenericType {
	Map("Map<?, ?>", Map.class), List("List<?>", List.class), Non("?", null) {
		public String getFieldType(boolean toBasicType, String... strings) {
			if (strings == null || strings.length != 1) {
				return super.getFieldType(toBasicType, strings);
			}
			if (toBasicType) {
				String valType = strings[0];
				switch (valType) {
				case "Short":
					return "short";
				case "Integer":
					return "int";
				case "Long":
					return "long";
				case "Double":
					return "double";
				case "Float":
					return "float";
				case "Boolean":
					return "boolean";
				case "Byte":
					return "byte";
				case "Character":
					return "char";
				default:
					return super.getFieldType(toBasicType, strings);
				}
			}
			return super.getFieldType(toBasicType, strings);
		}
	},
	Clazz("Class<?>", Class.class);

	private String fieldPattern;

	private String clazzName;

	private String shortName;

	GenericType(String fieldPattern, Class<?> clazz) {
		this.fieldPattern = fieldPattern;
		if (clazz != null) {
			this.clazzName = clazz.getName();
			this.shortName = clazz.getSimpleName();
		}
	}

	public String getFieldType(boolean toBasicType, String... strings) {
		return StringUtil.format(fieldPattern, strings);
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
