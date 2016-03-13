package com.bricks.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bricks.facade.dao.proj.Proj;
import com.bricks.facade.dao.proj.ProjDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@Controller
public class DefaultController {

	@Resource
	private ProjDao dao;

	@ModelAttribute("allProjs")
	public List<Proj> populateTypes() {
		return dao.query(new Proj());
	}

	@RequestMapping({ "/", "/index" })
	public String showSeedstarters() {
		return "tables";
	}

}
