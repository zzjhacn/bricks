package com.bricks.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bricks.dal.Page;
import com.bricks.dal.QueryResult;
import com.bricks.facade.dao.proj.Proj;
import com.bricks.facade.dao.proj.ProjDao;

/**
 * @author bricks <long1795@gmail.com>
 */
@Controller
public class DefaultController {

	@Resource
	private ProjDao dao;

	//@ModelAttribute("allProjs")
	public List<Proj> populateTypes() {
		List<Proj> data = dao.query(new Proj());
		// if(data == null || data.size() < 10){
		// 	initData();
		// }
		return data;
	}

	@RequestMapping({ "/", "/index" })
	public ModelAndView showSeedstarters(final Integer currPage) {
		int page = (currPage == null || currPage < 1) ? 1 : currPage;
		QueryResult<Proj> data = dao.queryResult(new Proj(), page, Page.DEFAULT_PAGE_SIZE);
		return new ModelAndView("tables").addObject("pages", new Page(1, data.getCount().intValue(), data.getData().size())).addObject("allProjs", data.getData());
	}

	@RequestMapping({ "/detail" })
	public ModelAndView showSeedstarters(final Long id) {
		if(id == null){
			return new ModelAndView("jsonView").addObject("msg", "fail");
		}
		return new ModelAndView("jsonView").addObject("msg", "succ").addObject("detail", dao.select(id));
	}

	@RequestMapping({ "/save" })
	public ModelAndView showSeedstarters(final Proj proj) {
		dao.save(proj);
		return new ModelAndView("jsonView").addObject("msg", "succ");
	}

	private void initData(){
		for(int i = 0; i < 25; i++){
			Proj p = new Proj();
			p.setProjCode("testCode" + i);
			p.setProjName("test project name " + i);
			dao.save(p);
		}
	}

}
