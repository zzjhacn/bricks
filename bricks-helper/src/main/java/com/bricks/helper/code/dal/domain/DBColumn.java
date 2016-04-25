package com.bricks.helper.code.dal.domain;

import com.bricks.lang.BaseObject;
import com.bricks.utils.StringUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DBColumn extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String dbNamePrefix;
	private String dbName;
	private String dbType;
	private int length;
	private String comment;
	private String javaName;
	private Class<?> javaType;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getJavaName() {
		if (javaName == null) {
			javaName = StringUtil.columnToProperty(dbName, dbNamePrefix);
		}
		return javaName;
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}

	public Class<?> getJavaType() {
		if (javaType == null) {
			javaType = TypeUtil.toJavaType(dbType);
		}
		return javaType;
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

}
