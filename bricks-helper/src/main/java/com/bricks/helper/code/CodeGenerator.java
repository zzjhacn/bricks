package com.bricks.helper.code;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface CodeGenerator<R extends CodeGenReq> extends LogAble {

	/**
	 * 缺省的路径分隔符 : "/"
	 */
	final String FILE_SEPARATOR = "/";

	/**
	 * 将路径中的"\"和"."替换为"/"
	 *
	 * @param path
	 *            原始路径 如: "D:\path\subpath"
	 * @return 修饰后的路径 如: "D:/path/subpath/"
	 */
	default String parsePath(final String path) {
		String result = path.replaceAll("[\\\\\\.]", FILE_SEPARATOR);
		return result.endsWith(FILE_SEPARATOR) ? result : result + FILE_SEPARATOR;
	}

	/**
	 * 代码生成
	 *
	 * @param request
	 * @return
	 */
	CodeGenResp generate(R request);

}
