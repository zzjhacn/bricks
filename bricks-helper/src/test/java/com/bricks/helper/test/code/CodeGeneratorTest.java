package com.bricks.helper.test.code;

import org.junit.Assert;
import org.junit.Test;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.GenericType;
import com.bricks.helper.code.JavaField;
import com.bricks.helper.code.api.APICodeGenerator;
import com.bricks.helper.code.api.domain.API;
import com.bricks.helper.code.api.domain.APICodeGenReq;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CodeGeneratorTest implements LogAble {

	APICodeGenerator cg = new APICodeGenerator();

	@Test
	public void test() throws Exception {
		Assert.assertEquals("d:/a/a/", cg.parsePath("d:\\a.a"));

		API api = new API("com.bricks.helper.test.code.api", "TestApi", "代码生成测试");
		api.addField(new JavaField(String.class, "name", "NAME", "\"tom\""));
		api.addField(new JavaField(Integer.class, "age", "AGE", "1").setToBasicType(true));
		api.addField(new JavaField(Integer.class, "luckyNums", GenericType.List, "LUCKY NUMBERS"));
		api.addField(new JavaField(Integer.class, "luckyNumCnts", GenericType.Map, "LUCKY NUMBER MAP").setKeyType(Integer.class));

		APICodeGenReq request = new APICodeGenReq();
		request.addApi(api);
		CodeGenResp resp = cg.generate(request);
		log().info(resp.toString());
	}

}
