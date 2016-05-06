package com.bricks.dal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
<<<<<<< HEAD
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
=======
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
>>>>>>> origin/master

import com.bricks.dal.ann.Cond;
import com.bricks.dal.ann.Operator;
import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
<<<<<<< HEAD
@MappedSuperclass
=======
@Embeddable
>>>>>>> origin/master
public abstract class BaseEO extends BaseObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
<<<<<<< HEAD
	protected Long id;
=======
	private Long id;
>>>>>>> origin/master

	@Cond(target = "id", operator = Operator.IN)
	transient private List<Long> ids;

<<<<<<< HEAD
	protected Date createTime;
=======
	@Column(name = "create_time")
	private Date createTime;
>>>>>>> origin/master

	@Cond(target = "createTime", operator = Operator.GTE)
	transient private Date createTimeB;

	@Cond(target = "createTime", operator = Operator.LTE)
	transient private Date createTimeE;

<<<<<<< HEAD
	protected Date updateTime;
=======
	@Column(name = "update_time")
	private Date updateTime;
>>>>>>> origin/master

	@Cond(target = "updateTime", operator = Operator.GTE)
	transient private Date updateTimeB;

	@Cond(target = "updateTime", operator = Operator.LTE)
	transient private Date updateTimeE;

	@Column
<<<<<<< HEAD
	protected Integer version;
=======
	private Integer version;
>>>>>>> origin/master

	public static Date getDate(int delta) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, delta);
		return c.getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Date getCreateTimeB() {
		return createTimeB;
	}

	public void setCreateTimeB(Date createTimeB) {
		this.createTimeB = createTimeB;
	}

	public Date getCreateTimeE() {
		return createTimeE;
	}

	public void setCreateTimeE(Date createTimeE) {
		this.createTimeE = createTimeE;
	}

	public Date getUpdateTimeB() {
		return updateTimeB;
	}

	public void setUpdateTimeB(Date updateTimeB) {
		this.updateTimeB = updateTimeB;
	}

	public Date getUpdateTimeE() {
		return updateTimeE;
	}

	public void setUpdateTimeE(Date updateTimeE) {
		this.updateTimeE = updateTimeE;
	}

}
