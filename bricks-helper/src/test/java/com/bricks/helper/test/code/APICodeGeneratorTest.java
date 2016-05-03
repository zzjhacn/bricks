package com.bricks.helper.test.code;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.GenericType;
import com.bricks.helper.code.JavaField;
import com.bricks.helper.code.api.APICodeGenerator;
import com.bricks.helper.code.api.domain.API;
import com.bricks.helper.code.api.domain.APICodeGenReq;
import com.bricks.lang.log.LogAble;
import com.bricks.utils.file.FileUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class APICodeGeneratorTest implements LogAble {

	APICodeGenerator cg = new APICodeGenerator();

	@Test
	public void test() throws Exception {
		Assert.assertEquals("d:/a/a/", FileUtil.parsePath("d:\\a.a"));

		API api = new API("com.bricks.helper.test.code.api", "TestApi", "代码生成测试");
		api.addField(new JavaField(String.class, "name", "NAME", "\"tom\""));
		api.addField(new JavaField(Integer.class, "age", "AGE", "1").setToBasicType(true));
		api.addField(new JavaField(BigDecimal.class, "value", "VALUE", "BigDecimal.ZERO"));
		api.addField(new JavaField(Integer.class, "luckyNums", GenericType.List, "LUCKY NUMBERS", "new ArrayList<>()"));
		api.addField(new JavaField(Integer.class, "luckyNumCnts", GenericType.Map, "LUCKY NUMBER MAP").setKeyType(Integer.class));
		api.addImport(ArrayList.class);

		API api2 = new API("com.bricks.helper.test.code.api", "TestApi2", "代码生成测试");
		api2.addField(new JavaField(String.class, "name", "NAME", "\"tom\""));
		api2.addField(new JavaField(Integer.class, "age", "AGE", "1").setToBasicType(true));
		api2.addField(new JavaField(BigDecimal.class, "value", "VALUE", "BigDecimal.ZERO"));
		api2.addField(new JavaField(Integer.class, "luckyNums", GenericType.List, "LUCKY NUMBERS", "new ArrayList<>()"));
		api2.addField(new JavaField(Integer.class, "luckyNumCnts", GenericType.Map, "LUCKY NUMBER MAP").setKeyType(Integer.class));
		api2.addImport(ArrayList.class);

		APICodeGenReq request = new APICodeGenReq();
		request.addApi(api);
		request.addApi(api2);
		CodeGenResp resp = cg.generate(request);
		log().info(resp.toString());
	}

	@Test
	public void test2() throws Exception {

		API api = new API("com.bricks.helper.code.dal.domain", "DBTable", "数据库表");
		api.addField(new JavaField(String.class, "tableNamePrefix", "表名前缀", "\"t_\""));
		api.addField(new JavaField(String.class, "colNamePrefix", "列名前缀"));

		APICodeGenReq request = new APICodeGenReq();
		request.addApi(api);
		CodeGenResp resp = cg.generate(request);
		log().info(resp.toString());
	}

}
