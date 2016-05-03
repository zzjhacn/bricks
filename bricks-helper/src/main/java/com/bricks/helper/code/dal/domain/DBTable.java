package com.bricks.helper.code.dal.domain;

import com.bricks.helper.code.JavaClass;
import com.bricks.helper.code.JavaField;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DBTable extends JavaClass {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DBTable() {
		super();
	}

	/**
	 * @param pkg
	 * @param clazzName
	 * @param comment
	 */
	public DBTable(String pkg, String clazzName, String comment) {
		super(pkg, clazzName, comment);
	}

	public void addField(JavaField field) {
		if (field.getName().equals("id") || field.getName().equals("createTime") || field.getName().equals("updateTime") || field.getName().equals("version")) {
			return;
		}
		super.addField(field);
	}

}
