package com.bricks.facade.dao.proj;

import org.springframework.stereotype.Service;

import com.bricks.dal.mybatis.MybatisDAO;

/**
 * @author bricks <long1795@gmail.com>
 */
@Service
public class ProjDao extends MybatisDAO<Proj> {

	protected String buildStatementName(String suffix) {
		return getClass().getName() + "." + suffix;
	}

}
