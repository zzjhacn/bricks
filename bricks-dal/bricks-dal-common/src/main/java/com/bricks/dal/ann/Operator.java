package com.bricks.dal.ann;

/**
 * @author bricks <long1795@gmail.com>
 */
public enum Operator {
	IN("in"), EQ("="), LT("<"), GT(">"), LTE("<="), GTE(">="), LIKE("like");

	private String str;

	Operator(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}
}
