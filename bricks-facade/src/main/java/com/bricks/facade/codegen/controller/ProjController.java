package com.bricks.facade.codegen.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.bricks.facade.DefaultController;
import com.bricks.facade.codegen.dao.proj.Proj;
import com.bricks.facade.codegen.dao.proj.ProjDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@Controller()
@RequestMapping({ "/proj" })
public class ProjController extends DefaultController<Proj, ProjDao> {

	@Resource
	public void setDao(ProjDao dao) {
		this.dao = dao;
	}

	protected String subPath(){
		return "proj";
	}
}
