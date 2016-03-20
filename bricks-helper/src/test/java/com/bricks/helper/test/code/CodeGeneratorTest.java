package com.bricks.helper.test.code;

import org.junit.Assert;
import org.junit.Test;

import com.bricks.helper.code.CodeGenReq;
import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.CodeGenerator;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CodeGeneratorTest implements LogAble {

	CodeGenerator<?> cg = new CodeGenerator<CodeGenReq>() {
		@Override
		public CodeGenResp generate(CodeGenReq request) {
			return null;
		}
	};

	@Test
	public void test() throws Exception {
		Assert.assertEquals("d:/a/a/", cg.parsePath("d:\\a.a"));
	}
}
