package com.bricks.dal.jpa.test.dal;

<<<<<<< HEAD
=======
import javax.persistence.Column;
>>>>>>> origin/master
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bricks.dal.BaseEO;

/**
 * @author bricks <long1795@gmail.com>
 */
@Entity
@Table(name = "t_proj")
public class Proj extends BaseEO {
	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	private String projCode;// 项目标识

=======
	@Column
	private String projCode;// 项目标识

	@Column
>>>>>>> origin/master
	private String projName;// 项目名

	/**
	 * return 项目标识
	 */
	public String getProjCode() {
		return projCode;
	}

	/**
	 * @param projCode
	 *            项目标识
	 */
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	/**
	 * return 项目名
	 */
	public String getProjName() {
		return projName;
	}

	/**
	 * @param projName
	 *            项目名
	 */
	public void setProjName(String projName) {
		this.projName = projName;
	}

}
