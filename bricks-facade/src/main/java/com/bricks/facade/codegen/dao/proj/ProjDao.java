package com.bricks.facade.codegen.dao.proj;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
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

	@Override
	public void afterPropertiesSet() throws Exception {
		SqlSource sql = new RawSqlSource(sqlSessionFactory.getConfiguration(), "select count(0) from t_proj", Proj.class);
		MappedStatement.Builder sb = new MappedStatement.Builder(sqlSessionFactory.getConfiguration(), buildStatementName("count"), sql, SqlCommandType.SELECT);
		ResultMapping.Builder mapping = new ResultMapping.Builder(sqlSessionFactory.getConfiguration(), "id", "id", String.class);
		// ResultMap.Builder rb = new ResultMap.Builder(sqlSessionFactory.getConfiguration(), "count", Long.class, Arrays.asList(mapping.build()));
		ResultMap.Builder rb = new ResultMap.Builder(sqlSessionFactory.getConfiguration(), "count", Long.class, new ArrayList<>());
		sb.resultMaps(Arrays.asList(rb.build()));
		sqlSessionFactory.getConfiguration().addMappedStatement(sb.build());
		super.afterPropertiesSet();
	}
}
