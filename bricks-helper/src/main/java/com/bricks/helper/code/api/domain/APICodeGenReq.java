package com.bricks.helper.code.api.domain;

import java.util.ArrayList;
import java.util.List;

import com.bricks.helper.code.CodeGenReq;
import com.bricks.helper.code.CodeType;

/**
 * @author bricks <long1795@gmail.com>
 */
public class APICodeGenReq extends CodeGenReq {
	private static final long serialVersionUID = 1L;

	private List<API> apis;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#codeType()
	 */
	@Override
	public CodeType codeType() {
		return CodeType.API;
	}

	public List<API> getApis() {
		return apis;
	}

	public void addApi(API api) {
		if (apis == null || apis.isEmpty()) {
			apis = new ArrayList<>();
		}
		apis.add(api);
	}

	public void setApis(List<API> apis) {
		this.apis = apis;
	}

}
