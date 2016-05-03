package com.bricks.helper.test.code;

import org.junit.Test;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.dal.DalCodeGenerator;
import com.bricks.helper.code.dal.domain.DBCodeGenReq;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DalCodeGeneratorTest implements LogAble {

	DalCodeGenerator cg = new DalCodeGenerator();

	@Test
	public void test() throws Exception {
		DBCodeGenReq req = new DBCodeGenReq().fromMySql("com.bricks.helper.test.code.db",
				"jdbc:mysql://localhost:3306/qdm114587546_db?user=root&password=111111&useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=UTC", "wzs_");
		CodeGenResp resp = cg.generate(req);
		log().info(resp.toString());
	}
}
