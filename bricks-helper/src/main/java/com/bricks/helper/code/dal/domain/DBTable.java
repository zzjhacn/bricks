package com.bricks.helper.code.dal.domain;

import com.bricks.helper.code.CodeGenReq;
import com.bricks.helper.code.CodeType;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DBTable extends CodeGenReq {
	private static final long serialVersionUID = 1L;

	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#codeType()
	 */
	@Override
	public CodeType codeType() {
		return CodeType.DAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#outputPath()
	 */
	@Override
	public String outputPath() {
		return "./" + name + "/";
	}

}
