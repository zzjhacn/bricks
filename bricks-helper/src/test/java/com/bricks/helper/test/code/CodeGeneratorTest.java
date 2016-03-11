package com.bricks.helper.test.code;

import org.junit.Assert;
import org.junit.Test;

import com.bricks.helper.code.CodeGenerator;
import com.bricks.helper.code.impl.CodeGeneratorImpl;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CodeGeneratorTest implements LogAble {

	CodeGenerator cg = new CodeGeneratorImpl();

	@Test
	public void test() throws Exception {
		Assert.assertEquals("d:/a/a/", cg.parsePath("d:\\a.a"));
	}
}
