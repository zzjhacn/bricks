package com.bricks.facade.codegen.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.bricks.facade.DefaultController;
import com.bricks.facade.codegen.dao.tab.Tab;
import com.bricks.facade.codegen.dao.tab.TabDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@Controller()
@RequestMapping({ "/tab" })
public class TabController extends DefaultController<Tab, TabDao> {

	@Resource
	public void setDao(TabDao dao) {
		this.dao = dao;
	}

	protected String subPath(){
		return "tab";
	}
}
