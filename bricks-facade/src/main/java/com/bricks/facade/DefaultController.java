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
		List<Proj> data = dao.query(new Proj());
		if(data == null || data.isEmpty()){
			initData();
		}
		return data;
	}

	@RequestMapping({ "/", "/index" })
	public String showSeedstarters() {
		return "tables";
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
