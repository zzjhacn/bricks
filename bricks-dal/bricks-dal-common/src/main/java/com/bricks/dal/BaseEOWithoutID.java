package com.bricks.dal;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.bricks.dal.ann.Cond;
import com.bricks.dal.ann.Operator;
import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEOWithoutID extends BaseObject {
	private static final long serialVersionUID = 1L;

	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Cond(target = "createTime", operator = Operator.GTE)
	transient private Date createTimeB;

	@Cond(target = "createTime", operator = Operator.LTE)
	transient private Date createTimeE;

	@Column(name = "UPDATE_TIME")
	private Date updateTime;

	@Cond(target = "updateTime", operator = Operator.GTE)
	transient private Date updateTimeB;

	@Cond(target = "updateTime", operator = Operator.LTE)
	transient private Date updateTimeE;

	private Integer version;

	@PreUpdate
	public void preUpdate() {
		updateTime = new Date();
		version = version == null ? 0 : version + 1;
	}

	@PrePersist
	public void prePersist() {
		Date now = new Date();
		createTime = now;
		updateTime = now;
		version = 0;
	}

	public static Date getDate(int delta) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, delta);
		return c.getTime();
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
