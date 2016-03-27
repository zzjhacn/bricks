package com.bricks.facade.test;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.dal.BaseEO;
import com.bricks.facade.codegen.dao.proj.Proj;
import com.bricks.facade.codegen.dao.proj.ProjDao;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-test.xml")
public class ProjDaoTest implements LogAble {

	@Resource
	private ProjDao dao;

	@Test
	public void test() throws Exception {
		Proj cond = new Proj();

		log().info("select result is [{}].", dao.select(22L));
		log().info("Count of projs is [{}]", dao.count(cond));

		cond.setIds(Arrays.asList(20L, 21L));
		cond.setCreateTimeB(BaseEO.getDate(-100));
		List<Proj> projs = dao.query(cond, 1, 10);
		log().info("[{}] projs found.", projs.size());
		projs.forEach(p -> {
			log().info(p.toString());
		});

	}
}
