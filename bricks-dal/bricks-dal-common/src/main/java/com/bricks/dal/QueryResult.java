package com.bricks.dal;

import java.util.List;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class QueryResult<T extends BaseEO> extends BaseObject {
	private static final long serialVersionUID = 1L;

	private Long count;

	private Integer size;

	private List<T> data;

	public QueryResult() {

	}

	public QueryResult(List<T> data) {
		setData(data);
		if (data != null) {
			count = (long) data.size();
		}
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<T> getData() {
		return data;
	}

	public final void setData(List<T> data) {
		this.data = data;
		if (data != null) {
			this.size = data.size();
		}
	}

}
