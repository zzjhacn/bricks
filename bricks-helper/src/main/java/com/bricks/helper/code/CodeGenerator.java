package com.bricks.helper.code;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface CodeGenerator<R extends CodeGenReq> extends LogAble {

	/**
	 * 代码生成
	 *
	 * @param request
	 * @return
	 */
	CodeGenResp generate(R request);

}
