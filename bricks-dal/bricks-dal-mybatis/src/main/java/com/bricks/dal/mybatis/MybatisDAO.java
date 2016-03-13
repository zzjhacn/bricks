package com.bricks.dal.mybatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;

import com.bricks.dal.BaseEO;
import com.bricks.dal.DAO;
import com.bricks.dal.StatementName;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class MybatisDAO<O extends BaseEO> extends StatementName implements DAO<O>, InitializingBean {

	@Resource//(name = "sqlSessionFactory")
	protected SqlSessionFactory sqlSessionFactory;

	protected SqlSessionTemplate template;

	@Override
	public void afterPropertiesSet() throws Exception {
		template = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
	}

	@Override
	public Long insert(O o) {
		template.insert(statementName_Insert(), o);
		return o.getId();
	}

	@Override
	public int batchInsert(List<O> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return template.insert(statementName_BatchInsert(), list);
	}

	@Override
	public Long insertWithId(O o) {
		template.insert(statementName_InsertWithId(), o);
		return o.getId();
	}

	@Override
	public int batchInsertWithId(List<O> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return template.insert(statementName_BatchInsertWithId(), list);
	}

	@Override
	public O select(Long i, boolean lock, boolean nowait) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", i);
		params.put("lock", lock);
		params.put("nowait", nowait);
		return template.selectOne(statementName_Select(), params);
	}

	@Override
	public List<O> query(O o, int pageNo, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		if (pageNo >= PAGE_FIRST && pageSize > 0) {
			params.put("start", (pageNo - 1) * pageSize);
			params.put("size", pageSize);
		}
		List<O> r = template.selectList(statementName_Query(), params);
		if (r == null) {
			r = new ArrayList<O>();
		}
		return r;
	}

	@Override
	public Long count(O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		return (Long) template.selectOne(statementName_Count(), params);
	}

	@Override
	public BigDecimal sum(String col, O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		params.put("col", col);
		BigDecimal sum = (BigDecimal) template.selectOne(statementName_Sum(), params);
		if (sum == null) {
			return BigDecimal.ZERO;
		}
		return sum;
	}

	@Override
	public int update(O o) {
		int cnt = template.update(statementName_Update(), o);
		if (cnt == 0) {
			throw new OptimisticLockException("Error when update " + o);
		}
		return cnt;
	}

	@Override
	public int delete(O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		return template.delete(statementName_Delete(), params);
	}

	@Override
	public int deleteById(Long i) {
		return template.delete(statementName_DeleteById(), i);
	}

}
