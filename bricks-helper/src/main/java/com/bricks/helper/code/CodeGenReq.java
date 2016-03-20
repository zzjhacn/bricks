package com.bricks.helper.code;

import com.bricks.lang.BaseObject;
import com.bricks.helper.code.CodeType;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class CodeGenReq extends BaseObject {
	private static final long serialVersionUID = 1L;

	public abstract CodeType codeType();

	public abstract String outputPath();
}
