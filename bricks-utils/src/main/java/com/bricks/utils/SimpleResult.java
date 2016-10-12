package com.bricks.utils;

import java.util.Map;

import com.bricks.lang.BaseObject;
import com.bricks.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * @author bricks <long1795@gmail.com>
 */
public class SimpleResult extends BaseObject {
	private static final long serialVersionUID = 1L;

	public static final String STATE_SUCC = "0000";

	private String state;

	private Map<String, Object> ext = Maps.newHashMap();

	public SimpleResult(String state) {
		this.state = state;
	}

	public static SimpleResult succ() {
		return new SimpleResult(STATE_SUCC);
	}

	public SimpleResult ext(String key, Object val) {
		ext.put(key, val);
		return this;
	}

	public SimpleResult ext(String key, String pattern, String... args) {
		ext.put(key, StringUtil.format(pattern, args));
		return this;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, Object> getExt() {
		return ext;
	}

	public Object getExtByKey(String key) {
		return ext.get(key);
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}

}
