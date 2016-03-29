package com.bricks.facade.test.dao;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.facade.codegen.dao.proj.Proj;
import com.bricks.facade.codegen.dao.proj.ProjDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-test.xml")
public class ProjDaoTest extends AbsDaoTest<Proj, ProjDao> {

	@Resource
	public void setDao(ProjDao projDao) {
		this.dao = projDao;
	}

	protected Proj newE() {
		Proj proj = new Proj();
		proj.setProjCode("newCode");
		proj.setProjName("newName");
		return proj;
	}
}
