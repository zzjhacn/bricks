package com.bricks.helper.code.webapp.domain;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class Attribute extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String attCode;

	private String attName;

	private int attLen = 32;

	private Class<?> attType = String.class;

	private Class<?> collectionType;

	private boolean nullAble;

	private String defaultVal;

	private boolean queryAble = true;

	private String rege;

	private String range;

	private String memo;

	private boolean display = true;

	private boolean editable = true;

	/**
	 * @param attCode
	 * @param attName
	 */
	public Attribute(String attCode, String attName) {
		super();
		this.attCode = attCode;
		this.attName = attName;
	}

	public void addToDomain(Domain domain) {
		domain.addAttribute(this);
	}

	public String getAttCode() {
		return attCode;
	}

	public void setAttCode(String attCode) {
		this.attCode = attCode;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public int getAttLen() {
		return attLen;
	}

	public void setAttLen(int attLen) {
		this.attLen = attLen;
	}

	public Class<?> getAttType() {
		return attType;
	}

	public void setAttType(Class<?> attType) {
		this.attType = attType;
	}

	public Class<?> getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(Class<?> collectionType) {
		this.collectionType = collectionType;
	}

	public boolean isNullAble() {
		return nullAble;
	}

	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	public String getDefaultVal(){
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal){
		this.defaultVal = defaultVal;
	}

	public boolean isQueryAble() {
		return queryAble;
	}

	public void setQueryAble(boolean queryAble) {
		this.queryAble = queryAble;
	}

	public String getRege() {
		return rege;
	}

	public void setRege(String rege) {
		this.rege = rege;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
