package com.bricks.helper.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class JavaMethod extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String modifiers = "public";
	private String name;
	private Class<?> returnType;
	private Map<Class<?>, String> parameters;
	private List<Class<?>> exceptionTypes;

	public JavaMethod() {}

	/**
	 * @param name
	 * @param returnType
	 * @param parameters
	 * @param exceptionTypes
	 */
	public JavaMethod(String name, Class<?> returnType, Map<Class<?>, String> parameters, List<Class<?>> exceptionTypes) {
		super();
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.exceptionTypes = exceptionTypes;
	}

	/**
	 * @param name
	 * @param returnType
	 * @param parameters
	 */
	public JavaMethod(String name, Class<?> returnType, Map<Class<?>, String> parameters) {
		super();
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public Map<Class<?>, String> getParameters() {
		return parameters;
	}

	public void addParameter(Class<?> clazz, String parameter) {
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		parameters.put(clazz, parameter);
	}

	public void setParameters(Map<Class<?>, String> parameters) {
		this.parameters = parameters;
	}

	public List<Class<?>> getExceptionTypes() {
		return exceptionTypes;
	}

	public void addExceptionTypes(Class<?> exceptionType) {
		if (exceptionTypes == null) {
			exceptionTypes = new ArrayList<>();
		}
		exceptionTypes.add(exceptionType);
	}

	public void setExceptionTypes(List<Class<?>> exceptionTypes) {
		this.exceptionTypes = exceptionTypes;
	}
}
