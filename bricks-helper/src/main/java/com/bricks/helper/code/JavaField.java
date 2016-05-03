package com.bricks.helper.code;

import com.bricks.lang.BaseObject;
import com.bricks.utils.StringUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class JavaField extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String name;

	private Class<?> valType;

	private Class<?> keyType;

	private GenericType genericType;

	private String comment;

	private String defaultVal;

	boolean toBasicType = false;

	public String toString() {
		StringBuilder sb = new StringBuilder("private ");
		sb.append(genericStr()).append(" ").append(name);
		if (defaultVal != null) {
			sb.append(" = ").append(defaultVal);
		}
		sb.append(";");
		return sb.toString();
	}

	public String genericStr() {
		if (genericType == null) {
			genericType = GenericType.Non;
		}
		if (keyType != null) {
			return genericType.getFieldType(toBasicType, keyType.getSimpleName(), valType.getSimpleName());
		}
		return genericType.getFieldType(toBasicType, valType.getSimpleName());
	}

	public String setterName() {
		return StringUtil.columnToProperty("set_" + name);
	}

	public String getterName() {
		return StringUtil.columnToProperty("get_" + name);
	}

	public JavaField() {}

	/**
	 * @param valType
	 * @param name
	 * @param genericType
	 * @param comment
	 * @param defaultVal
	 */
	public JavaField(Class<?> valType, String name, GenericType genericType, String comment, String defaultVal) {
		super();
		this.valType = valType;
		this.name = name;
		this.genericType = genericType;
		this.comment = comment;
		this.defaultVal = defaultVal;
	}

	/**
	 * @param valType
	 * @param name
	 * @param genericType
	 * @param comment
	 */
	public JavaField(Class<?> valType, String name, GenericType genericType, String comment) {
		super();
		this.valType = valType;
		this.name = name;
		this.genericType = genericType;
		this.comment = comment;
	}

	/**
	 * @param valType
	 * @param name
	 * @param comment
	 * @param defaultVal
	 */
	public JavaField(Class<?> valType, String name, String comment, String defaultVal) {
		super();
		this.valType = valType;
		this.name = name;
		this.comment = comment;
		this.defaultVal = defaultVal;
	}

	/**
	 * @param valType
	 * @param name
	 * @param comment
	 */
	public JavaField(Class<?> valType, String name, String comment) {
		super();
		this.valType = valType;
		this.name = name;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public JavaField setName(String name) {
		this.name = name;
		return this;
	}

	public Class<?> getValType() {
		return valType;
	}

	public JavaField setValType(Class<?> valType) {
		this.valType = valType;
		return this;
	}

	public GenericType getGenericType() {
		return genericType;
	}

	public JavaField setGenericType(GenericType genericType) {
		this.genericType = genericType;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public JavaField setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public JavaField setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
		return this;
	}

	public Class<?> getKeyType() {
		return keyType;
	}

	public JavaField setKeyType(Class<?> keyType) {
		this.keyType = keyType;
		return this;
	}

	public boolean isToBasicType() {
		return toBasicType;
	}

	public JavaField setToBasicType(boolean toBasicType) {
		this.toBasicType = toBasicType;
		return this;
	}

}
