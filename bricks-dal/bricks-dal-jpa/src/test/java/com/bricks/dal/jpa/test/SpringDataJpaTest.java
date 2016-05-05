package com.bricks.dal.jpa.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.dal.jpa.test.dal.ProjDao;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/test.xml")
public class SpringDataJpaTest implements LogAble {
	@Resource
	ProjDao dao;

	@Test
	public void test() throws Exception {
		log().info("{}", dao.findById(1L));
	}
}
