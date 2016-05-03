package com.bricks.helper.code;

import com.bricks.lang.BaseObject;
import com.bricks.lang.log.LogAble;

/**
 * 代码生成请求类
 * 
 * @author bricks <long1795@gmail.com>
 */
public abstract class CodeGenReq extends BaseObject implements LogAble {
	private static final long serialVersionUID = 1L;

	/**
	 * 代码类型
	 * 
	 * @return
	 */
	public abstract CodeType codeType();

	public String outputPath() {
		return "output/" + codeType().toString() + "/";
	}
}
