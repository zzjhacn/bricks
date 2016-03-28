package com.bricks.facade.test.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bricks.dal.BaseEO;
import com.bricks.dal.DAO;
import com.bricks.lang.log.LogAble;
import com.bricks.utils.reflect.ReflectUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class AbsDaoTest<E extends BaseEO, D extends DAO<E>> implements LogAble {

	protected D dao;

	@SuppressWarnings("unchecked")
	protected E cond() throws Exception {
		Class<Object> clazz = ReflectUtil.getSuperClassGenricType(getClass(), 0);
		return (E) clazz.newInstance();
	}

	abstract protected E newE();

	@Before
	public abstract void setDao();

	@Test
	public void test() throws Exception {
		E cond = cond();

		log().info("[{}] result deleted.", dao.delete(cond));

		E e = newE();
		e.setId(1L);
		Long id = dao.insertWithId(e);
		log().info("proj inserted, id=[{}]", id);

		e = newE();
		id = dao.insert(e);
		log().info("proj inserted, id=[{}]", id);

		e = dao.select(e.getId());
		dao.update(e);
		e = dao.select(e.getId());
		log().info("select result is [{}].", e);

		log().info("Count of entity is [{}]", dao.count(cond));

		cond.setIds(Arrays.asList(1L, e.getId()));
		cond.setCreateTimeB(BaseEO.getDate(-1));
		List<E> projs = dao.query(cond, 1, 10);
		log().info("[{}] entities found.", projs.size());
		projs.forEach(p -> {
			log().info(p.toString());
		});

		log().info("Sum of id is[{}]", dao.sum("id", cond));

		log().info("[{}] result deleted.", dao.deleteById(1L));
		cond = cond();
		log().info("[{}] result deleted.", dao.delete(cond));
	}
}
