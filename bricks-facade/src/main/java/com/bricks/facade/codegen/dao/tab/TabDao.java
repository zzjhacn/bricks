package com.bricks.facade.codegen.dao.tab;

import org.springframework.stereotype.Service;

import com.bricks.dal.mybatis.MybatisDAO;

/**
 * @author bricks <long1795@gmail.com>
 */
@Service
public class TabDao extends MybatisDAO<Tab> {

	protected String buildStatementName(String suffix) {
		return getClass().getName() + "." + suffix;
	}
}
