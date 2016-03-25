package com.bricks.facade.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.facade.codegen.dao.proj.Proj;
import com.bricks.facade.codegen.dao.proj.ProjDao;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-test.xml")
public class TabDaoTest implements LogAble {

	@Resource
	private ProjDao dao;

	@Test
	public void test() throws Exception {
		log().info("Count of tab is [{}]", dao.count(new Proj()));
	}
}
