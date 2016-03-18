package com.bricks.helper.code.webapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class App extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String appName;

	private String memo;

	private List<Domain> domains = new ArrayList<>();

	/**
	 * @param appName
	 * @param memo
	 */
	public App(String appName, String memo) {
		super();
		this.appName = appName;
		this.memo = memo;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void addDomain(Domain domain) {
		domains.add(domain);
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

}
