package com.bricks.helper.code.api.domain;

import com.bricks.helper.code.CodeGenReq;
import com.bricks.helper.code.CodeType;

/**
 * @author bricks <long1795@gmail.com>
 */
public class API extends CodeGenReq {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#codeType()
	 */
	@Override
	public CodeType codeType() {
		return CodeType.API;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#outputPath()
	 */
	@Override
	public String outputPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
