package com.bricks.utils.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public class ReflectUtil {
	private ReflectUtil() {}

	@SuppressWarnings("unchecked")
	public static Class<Object> getSuperClassGenricType(final Class<?> clazz, final int index) {
		// 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		// 返回表示此类型实际类型参数的 Type 对象的数组。
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<Object>) params[index];
	}
}
