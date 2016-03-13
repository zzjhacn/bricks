package com.bricks.dal;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface DAO<O extends BaseEO> {
	static int PAGE_FIRST = 1;
	static int PAGE_SIZE = 20;

	default Long save(O o) {
		if (o.getId() == null) {
			return insert(o);
		}
		update(o);
		return 0L;
	}

	Long insert(O o);

	default int batchInsert(List<O> list) {
		for (O o : list) {
			insert(o);
		}
		return list.size();
	}

	Long insertWithId(O o);

	default int batchInsertWithId(List<O> list) {
		for (O o : list) {
			insertWithId(o);
		}
		return list.size();
	}

	default O select(Long i) {
		return select(i, false, false);
	}

	default O lock(Long i) {
		return select(i, true, false);
	}

	/**
	 * 
	 * @param i
	 *            主键
	 * @param lock
	 *            是否锁定
	 * @param nowait
	 *            是否立即返回（该参数在mysql中无效）
	 * @return
	 */
	O select(Long i, boolean lock, boolean nowait);

	Long count(O o);

	BigDecimal sum(String col, O o);

	default List<O> query(O o) {
		return query(o, -1, -1);
	}

	default O selectOne(O o) {
		List<O> os = query(o, PAGE_FIRST, 1);
		if (os == null || os.isEmpty()) {
			return null;
		}
		return os.get(0);
	}

	/**
	 * 分页查询
	 * 
	 * @param o
	 *            查询条件
	 * @param pageNo
	 *            页码（从1开始）
	 * @param pageSize
	 *            单页数量
	 * @return
	 */
	List<O> query(O o, int pageNo, int pageSize);

	/**
	 * 分页查询
	 * 
	 * @param o
	 *            查询条件
	 * @param pageNo
	 *            页码（从1开始）
	 * @param pageSize
	 *            单页数量
	 * @return
	 */
	default QueryResult<O> queryResult(O o, int pageNo, int pageSize) {
		QueryResult<O> result = new QueryResult<O>();
		result.setData(query(o, pageNo, pageSize));
		result.setCount(count(o));
		return result;
	}

	int update(O o);

	default List<O> batchUpdate(List<O> list) {
		for (O o : list) {
			update(o);
		}
		return list;
	}

	int delete(O o);

	int deleteById(Long i);
}
