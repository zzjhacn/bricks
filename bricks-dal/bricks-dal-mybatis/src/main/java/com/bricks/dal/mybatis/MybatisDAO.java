package com.bricks.dal.mybatis;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
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
import org.apache.ibatis.scripting.xmltags.SetSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
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

	@Resource
	protected SqlSessionFactory sqlSessionFactory;

	@Resource
	protected SqlSessionTemplate batchTemplate;

	@Resource
	protected SqlSessionTemplate simpleTemplate;

	protected Configuration cfg;
	protected Class<Object> clazz;

	@Override
	public void afterPropertiesSet() throws Exception {
		cfg = sqlSessionFactory.getConfiguration();
		clazz = ReflectUtil.getSuperClassGenricType(getClass(), 0);
		simpleExtension();
	}

	private void simpleExtension() {
		cfg.addMappedStatement(count());
		cfg.addMappedStatement(query());
		cfg.addMappedStatement(select());
		cfg.addMappedStatement(insert(statementName_Insert(), false, false));
		cfg.addMappedStatement(insert(statementName_InsertWithId(), true, false));
		cfg.addMappedStatement(insert(statementName_BatchInsert(), false, true));
		cfg.addMappedStatement(insert(statementName_BatchInsertWithId(), true, true));
		cfg.addMappedStatement(delete());
		cfg.addMappedStatement(deleteById());
		cfg.addMappedStatement(update());
	}

	protected MappedStatement select() {
		String id = statementName_Select();

		SqlNode select = new TextSqlNode("select " + selectSql() + " from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t") + " where id=#{id}");
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, new IfSqlNode(new TextSqlNode(" for update"), "lock")));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, clazz, resultMapping());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(id, sql, Arrays.asList(rb.build()), pb.build());
	}

	protected MappedStatement query() {
		String id = statementName_Query();

		SqlNode limit = new IfSqlNode(new TextSqlNode(" limit #{start} #{size} "), "start != null");
		SqlNode select = new MixedSqlNode(Arrays.asList(new StaticTextSqlNode("select "), limit,
				new StaticTextSqlNode(selectSql() + " from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t"))));
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, where()));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, clazz, resultMapping());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(id, sql, Arrays.asList(rb.build()), pb.build());
	}

	protected MappedStatement count() {
		String id = statementName_Count();

		SqlNode select = new TextSqlNode("select count(0) from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t"));
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, where()));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(id, sql, Arrays.asList(rb.build()), pb.build());
	}

	protected MappedStatement insert(final String id, final boolean withId, final boolean batch) {
		SqlNode insert = insertSql(withId, batch);

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());

		return mappedStatement(id, insert, Arrays.asList(rb.build()), pb.build(), SqlCommandType.INSERT);
	}

	protected MappedStatement update() {
		String id = statementName_Update();

		SqlNode insert = updateSql();

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());

		return mappedStatement(id, insert, Arrays.asList(rb.build()), pb.build(), SqlCommandType.UPDATE);
	}

	protected MappedStatement delete() {
		String id = statementName_Delete();

		SqlNode select = new TextSqlNode("delete from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t"));
		SqlNode sql = new MixedSqlNode(Arrays.asList(select, where()));

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(id, sql, Arrays.asList(rb.build()), pb.build(), SqlCommandType.DELETE);
	}

	protected MappedStatement deleteById() {
		String id = statementName_DeleteById();

		SqlNode sql = new TextSqlNode("delete from " + StringUtil.propertyToColumn(clazz.getSimpleName(), "t") + "  where id = #{id}");

		ResultMap.Builder rb = new ResultMap.Builder(cfg, id, Long.class, new ArrayList<>());
		ParameterMap.Builder pb = new ParameterMap.Builder(cfg, "defaultParameterMap", clazz, new ArrayList<ParameterMapping>());
		return mappedStatement(id, sql, Arrays.asList(rb.build()), pb.build());
	}

	protected MappedStatement mappedStatement(final String id, final SqlNode sqlNode, final List<ResultMap> rmList, final ParameterMap pm, final SqlCommandType sct) {
		SqlSource sql = new DynamicSqlSource(cfg, sqlNode);
		MappedStatement.Builder msb = new MappedStatement.Builder(cfg, id, sql, sct);
		msb.resultMaps(rmList);
		msb.parameterMap(pm);
		msb.keyProperty("id");
		if (sct == SqlCommandType.INSERT) {
			msb.keyGenerator(new Jdbc3KeyGenerator());
		}
		return msb.build();
	}

	protected MappedStatement mappedStatement(final String id, final SqlNode sqlNode, final List<ResultMap> rmList, final ParameterMap pm) {
		return mappedStatement(id, sqlNode, rmList, pm, SqlCommandType.SELECT);
	}

	protected SqlNode where() {
		List<SqlNode> condList = new ArrayList<>();
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseObject.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && Modifier.isPrivate(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
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

	protected SqlNode insertSql(final boolean withId, final boolean batch) {
		StringBuilder properties = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");

		if (withId) {
			properties.append("id,");
			values.append("#{id},");
		}

		Class<Object> c = clazz;
		while (!c.getName().equals(BaseEO.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& Modifier.isPrivate(f.getModifiers())) {
					properties.append(StringUtil.propertyToColumn(f.getName())).append(",");
					values.append("#{").append(batch ? "item." : "").append(f.getName()).append("},");
				}
			});
			c = c.getSuperclass();
		}

		properties.append("create_time,update_time,version)");
		values.append("current_timestamp,current_timestamp,1)");

		StringBuilder insert = new StringBuilder("insert into ").append(StringUtil.propertyToColumn(clazz.getSimpleName(), "t"));
		insert.append(properties);
		insert.append(" values ");
		SqlNode valueNode = null;
		if (batch) {
			valueNode = new ForEachSqlNode(cfg, new TextSqlNode(values.toString()), "list", "index", "item", "", "", ",");
		} else {
			valueNode = new TextSqlNode(values.toString());
		}
		return new MixedSqlNode(Arrays.asList(new TextSqlNode(insert.toString()), valueNode));
	}

	protected SqlNode updateSql() {
		StringBuilder update = new StringBuilder("update ").append(StringUtil.propertyToColumn(clazz.getSimpleName(), "t"));
		StringBuilder where = new StringBuilder("where id=#{id} and version=#{version}");

		StringBuilder set = new StringBuilder();
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseEO.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& Modifier.isPrivate(f.getModifiers())) {
					set.append(StringUtil.propertyToColumn(f.getName())).append("=#{").append(f.getName()).append("}, ");
				}
			});
			c = c.getSuperclass();
		}
		set.append(" version = version + 1, update_time = current_timestamp");

		SqlNode setNode = new SetSqlNode(cfg, new TextSqlNode(set.toString()));
		return new MixedSqlNode(Arrays.asList(new TextSqlNode(update.toString()), setNode, new TextSqlNode(where.toString())));
	}

	protected String selectSql() {
		StringBuilder select = new StringBuilder(" ");
		Class<Object> c = clazz;
		while (!c.getName().equals(BaseObject.class.getName()) && !c.getName().equals(Object.class.getName())) {
			Arrays.asList(c.getDeclaredFields()).forEach(f -> {
				if (!Modifier.isAbstract(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& !Modifier.isStatic(f.getModifiers())
						&& Modifier.isPrivate(f.getModifiers())) {
					select.append(StringUtil.propertyToColumn(f.getName())).append(",");
				}
			});
			c = c.getSuperclass();
		}
		return select.deleteCharAt(select.length() - 1).toString();
	}

	// unused
	List<ParameterMapping> parameterMapping() {
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

	protected List<ResultMapping> resultMapping() {
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
		batchTemplate.insert(statementName_Insert(), o);
		return o.getId();
	}

	@Override
	public int batchInsert(List<O> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return batchTemplate.insert(statementName_BatchInsert(), list);
	}

	@Override
	public Long insertWithId(O o) {
		batchTemplate.insert(statementName_InsertWithId(), o);
		return o.getId();
	}

	@Override
	public int batchInsertWithId(List<O> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return batchTemplate.insert(statementName_BatchInsertWithId(), list);
	}

	@Override
	public O select(Long i, boolean lock, boolean nowait) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", i);
		params.put("lock", lock);
		params.put("nowait", nowait);
		return batchTemplate.selectOne(statementName_Select(), params);
	}

	@Override
	public List<O> query(O o, int pageNo, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		RowBounds rb = new RowBounds();
		if (pageNo >= PAGE_FIRST && pageSize > 0) {
			params.put("start", (pageNo - 1) * pageSize);
			params.put("size", pageSize);
			// rb = new RowBounds((pageNo - 1) * pageSize, pageSize);
		}
		List<O> r = batchTemplate.selectList(statementName_Query(), params, rb);
		if (r == null) {
			r = new ArrayList<O>();
		}
		return r;
	}

	@Override
	public Long count(O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		return (Long) batchTemplate.selectOne(statementName_Count(), params);
	}

	@Override
	public BigDecimal sum(String col, O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		params.put("col", col);
		BigDecimal sum = (BigDecimal) batchTemplate.selectOne(statementName_Sum(), params);
		if (sum == null) {
			return BigDecimal.ZERO;
		}
		return sum;
	}

	@Override
	public int update(O o) {
		int cnt = simpleTemplate.update(statementName_Update(), o);
		if (cnt == 0) {
			throw new OptimisticLockException("Error when update " + o);
		}
		return cnt;
	}

	@Override
	public int delete(O o) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cond", o);
		return simpleTemplate.delete(statementName_Delete(), params);
	}

	@Override
	public int deleteById(Long i) {
		return simpleTemplate.delete(statementName_DeleteById(), i);
	}

}
