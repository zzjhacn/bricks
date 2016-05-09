package com.bricks.dal.jpa.test.dal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bricks.dal.BaseEO;

/**
 * @author bricks <long1795@gmail.com>
 */
@Entity
@Table(name = "t_person")
public class Person extends BaseEO {
	private static final long serialVersionUID = 1L;

	private String name;

	private Date birthday;

	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
