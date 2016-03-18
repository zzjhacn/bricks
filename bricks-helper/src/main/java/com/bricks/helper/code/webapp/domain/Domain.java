package com.bricks.helper.code.webapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class Domain extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String domainCode;

	private String domainName;

	private boolean display = true;

	private boolean editable = true;

	private List<Attribute> attributes = new ArrayList<>();

	/**
	 * @param domainCode
	 * @param domainName
	 */
	public Domain(String domainCode, String domainName) {
		super();
		this.domainCode = domainCode;
		this.domainName = domainName;
	}

	public void appendToApp(App app) {
		app.addDomain(this);
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
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

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
}
