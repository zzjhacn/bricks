package com.bricks.facade;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bricks.dal.BaseEO;
import com.bricks.dal.DAO;
import com.bricks.dal.Page;
import com.bricks.dal.QueryResult;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class DefaultController<E extends BaseEO, D extends DAO<E>> implements LogAble {
	protected D dao;

	protected abstract String subPath();

	@ModelAttribute("currPath")
	public String populateTypes() {
		return subPath();
	}

	@RequestMapping({ "/", "/index" })
	public ModelAndView index(final Integer currPage, final E cond) {
		int page = (currPage == null || currPage < 1) ? 1 : currPage;
		QueryResult<E> data = dao.queryResult(cond, page, Page.DEFAULT_PAGE_SIZE);
		return new ModelAndView(subPath() + "/index").addObject("pages", new Page(1, data.getCount().intValue(), data.getData().size())).addObject("datas", data.getData());
	}

	@RequestMapping({ "/detail" })
	public ModelAndView detail(final Long id) {
		if (id == null) {
			return new ModelAndView("jsonView").addObject("msg", "fail");
		}
		return new ModelAndView("jsonView").addObject("msg", "succ").addObject("detail", dao.select(id));
	}

	@RequestMapping({ "/save" })
	public ModelAndView save(final E e) {
		dao.save(e);
		return new ModelAndView("jsonView").addObject("msg", "succ");
	}

	@RequestMapping({ "/delete" })
	public ModelAndView delete(final Long id) {
		dao.deleteById(id);
		return new ModelAndView("jsonView").addObject("msg", "succ");
	}

}
