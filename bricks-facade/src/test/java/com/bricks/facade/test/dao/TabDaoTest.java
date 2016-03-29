package com.bricks.facade.test.dao;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.facade.codegen.dao.tab.Tab;
import com.bricks.facade.codegen.dao.tab.TabDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-test.xml")
public class TabDaoTest extends AbsDaoTest<Tab, TabDao> {

	@Resource
	public void setDao(TabDao tabDao) {
		this.dao = tabDao;
	}

	protected Tab newE() {
		Tab tab = new Tab();
		tab.setTabCode("newCode");
		tab.setTabName("newName");
		return tab;
	}
}
