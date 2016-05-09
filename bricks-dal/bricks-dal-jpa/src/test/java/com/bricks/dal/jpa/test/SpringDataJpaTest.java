package com.bricks.dal.jpa.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.dal.jpa.test.dal.Person;
import com.bricks.dal.jpa.test.dal.PersonDao;
import com.bricks.dal.jpa.test.dal.Proj;
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

	@Resource
	PersonDao pdao;

	@Test
	public void test2() throws Exception {
		Person p = new Person();
		p.setBirthday(new Date());
		p.setName("john");
		pdao.save(p);
	}

	@Test
	public void test() throws Exception {
		Proj p = new Proj();
		p.setProjCode("code");
		p.setProjName("name");
		p = dao.save(p);

		log().info(">>>>>{}", p.getId());
		log().info(">>>>>{}", dao.findById(1L));
		log().info(">>>>>{}", dao.countByIdGreaterThan(1L));
		log().info(">>>>>{}", (dao.findByIdGreaterThan(1L, new QPageRequest(0, 10))).getTotalElements());
		log().info("{}", dao.findById(1L));
	}
}
