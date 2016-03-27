package com.bricks.dal.mybatis;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;

import com.bricks.dal.BaseEO;
import com.bricks.dal.DAO;
import com.bricks.dal.StatementName;
import com.bricks.dal.ann.Cond;
import com.bricks.dal.ann.Operator;
import com.bricks.lang.BaseObject;
import com.bricks.utils.StringUtil;
import com.bricks.utils.reflect.ReflectUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class MybatisDAO<O extends BaseEO> extends StatementName implements DAO<O>, InitializingBean {

	final static Lock lock = new ReentrantLock();

	@Resource
	protected SqlSessionFactory sqlSessionFactory;

	protected static SqlSessionTemplate template;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (template == null) {
			lock.lock();
			try {
				if (template == null) {
					template = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
				}
			} finally {
				lock.unlock();
			}
		}
		Configuration cfg = sqlSessionFactory.getConfiguration();
		Class<Object> clazz = ReflectUtil.getSuperClassGenricType(getClass(), 0);
		simpleExtension(cfg, clazz);

		// sqlSessionFactory.getConfiguration().getMappedStatements().forEach(ms -> {
		// if (ms.getId().startsWith(clazz.getName())) {
		// log().info("----->>>>>id=[{}],resultMap=[{}],resultMap[0]=[{}]", ms.getId(), ms.getResultMaps().size(),
		// ms.getResultMaps().isEmpty() ? "non" : ms.getResultMaps().get(0).getId(),
		// ms.getResultMaps().isEmpty() ? "non" : ms.getResultMaps().get(0).getType());
		// }
		// });
	}

	private void simpleExtension(final Configuration cfg, final Class<Object> clazz) {
		cfg.addMappedStatement(count(cfg, clazz));
		cfg.addMappedStatement(query(cfg, clazz));
		cfg.addMappedStatement(select(cfg, clazz));
		cfg.addMappedStatement(insert(cfg, clazz));
	}

	private MappedStatement select(final Configuration cfg, final Class<Object> clazz) {
		String id = buildStatementName("select");

		SqlNode select = new TextSqlNode("select * from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t") + " where id=#{id}");
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, new IfSqlNode(new TextSqlNode(" for update"), "lock")));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, clazz, resultMapping(cfg, clazz));
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(cfg, id, sql, Arrays.asList(rb.build()), pb.build());
	}

	private MappedStatement query(final Configuration cfg, final Class<Object> clazz) {
		String id = buildStatementName("query");

		SqlNode limit = new IfSqlNode(new TextSqlNode(" limit #{start} #{size} "), "start != null");
		SqlNode select = new MixedSqlNode(Arrays.asList(new StaticTextSqlNode("select "), limit,
				new StaticTextSqlNode(" * from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t"))));
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, where(cfg, clazz)));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, clazz, resultMapping(cfg, clazz));
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(cfg, id, sql, Arrays.asList(rb.build()), pb.build());
	}

	private MappedStatement count(final Configuration cfg, final Class<Object> clazz) {
		String id = buildStatementName("count");

		SqlNode select = new TextSqlNode("select count(0) from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t"));
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, where(cfg, clazz)));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(cfg, id, sql, Arrays.asList(rb.build()), pb.build());
	}

	private MappedStatement insert(final Configuration cfg, final Class<Object> clazz) {
		String id = buildPrivateStatementName("insert");

		SqlNode insert = new TextSqlNode("insert into " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t") + " values");
		SqlNode sqlNode = new MixedSqlNode(Arrays.asList(insert, where(cfg, clazz)));
		SqlSource sql = new DynamicSqlSource(cfg, sqlNode);

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Integer.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		MappedStatement.Builder msb = new MappedStatement.Builder(cfg, id, sql, SqlCommandType.INSERT);
		msb.resultMaps(Arrays.asList(rb.build()));
		msb.parameterMap(pb.build());
		return msb.build();
	}

	private MappedStatement mappedStatement(final Configuration cfg, final String id, final SqlNode sqlNode, final List<ResultMap> rmList,
			final ParameterMap pm) {
		SqlSource sql = new DynamicSqlSource(cfg, sqlNode);
		MappedStatement.Builder msb = new MappedStatement.Builder(cfg, id, sql, SqlCommandType.SELECT);
		msb.resultMaps(rmList);
		msb.parameterMap(pm);
		return msb.build();
	}

	private SqlNode where(final Configuration cfg, final Class<Object> clazz) {
		List<SqlNode> condList = new ArrayList<>();
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseObject.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && Modifier.isPrivate(f.getModifiers())) {
					// handle ids createTimeB ...
					Cond condAnn = null;
					String target = f.getName();
					Operator operator = Operator.EQ;
					if (f.isAnnotationPresent(Cond.class)) {
						condAnn = f.getAnnotation(Cond.class);
						if (!"".equals(condAnn.target().trim())) {
							target = condAnn.target();
						}
						operator = condAnn.operator();
					}

					StringBuilder test = new StringBuilder("cond.").append(f.getName()).append(" != null");
					if (Operator.IN == operator) {
						SqlNode in = new TextSqlNode(" and " + StringUtil.propertyToColumn(target) + " in ");
						SqlNode each = new ForEachSqlNode(cfg, new TextSqlNode("#{item}"), "cond." + f.getName(), "index", "item", "(", ")", ",");
						in = new MixedSqlNode(Arrays.asList(in, each));
						SqlNode cond = new IfSqlNode(in, test.toString());
						condList.add(cond);
					} else {
						StringBuilder sb = new StringBuilder(" and ").append(StringUtil.propertyToColumn(target)).append(operator.getStr()).append("#{cond.").append(f.getName())
								.append("}");
						SqlNode cond = new IfSqlNode(new StaticTextSqlNode(sb.toString()), test.toString());
						condList.add(cond);
					}
				}
			});
			c = c.getSuperclass();
		}
		SqlNode cond = new MixedSqlNode(condList);
		SqlNode where = new WhereSqlNode(cfg, cond);
		return where;
	}

	// not used
	List<ParameterMapping> parameterMapping(final Configuration cfg, final Class<Object> clazz) {
		final List<ParameterMapping> pmList = new ArrayList<>();
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseObject.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())
						&& Modifier.isPrivate(f.getModifiers())) {
					ParameterMapping.Builder pmb = new ParameterMapping.Builder(cfg, f.getName(), f.getType());
					pmList.add(pmb.build());
				}
			});
			c = c.getSuperclass();
		}
		return pmList;
	}

	private List<ResultMapping> resultMapping(final Configuration cfg, final Class<Object> clazz) {
		final List<ResultMapping> rmList = new ArrayList<>();
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseObject.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())
						&& Modifier.isPrivate(f.getModifiers())) {
					ResultMapping.Builder rmb = new ResultMapping.Builder(cfg, f.getName(), StringUtil.propertyToColumn(f.getName()), f.getType());
					rmList.add(rmb.build());
				}
			});
			c = c.getSuperclass();
		}

		return rmList;
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
