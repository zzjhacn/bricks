package com.bricks.lang;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * 基类，增加对象标识，用于分布式系统中的对象跟踪
 * 
 * @author bricks <long1795@gmail.com>
 */
public class BaseObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String __id = UUID.randomUUID().toString();

	public String get__id() {
		return __id;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Class<?> c = getClass();
		while (!c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (Modifier.isStatic(f.getModifiers())) {
					return;
				}
				try {
					if (!f.isAccessible()) {
						f.setAccessible(true);
					}
					Object o = f.get(this);
					sb.append(f.getName()).append("=[").append(getVal(o)).append("];");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			c = c.getSuperclass();
		}
		return sb.toString();
	}

	String getVal(Object o) {
		if (o == null) {
			return "null";
		}
		if (o instanceof Collection<?>) {
			Collection<?> co = (Collection<?>) o;
			if (co.isEmpty()) {
				return "{}";
			}
			Object[] os = co.toArray();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < os.length; i++) {
				if (i > 3) {
					sb.append("...");
					break;
				}
				sb.append(os[i]).append(";");
			}
			return sb.toString();
		}
		if (o instanceof java.sql.Date) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			return sf.format(o);
		}
		if (o instanceof java.util.Date) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sf.format(o);
		}
		return o.toString();
	}
}
