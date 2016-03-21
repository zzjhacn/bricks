package com.bricks.facade.codegen.dao.tab;

import com.bricks.dal.BaseEO;

/**
 * @author bricks <long1795@gmail.com>
 */
public class Tab extends BaseEO {
	private static final long serialVersionUID = 1L;

	private Long idProj;//

	private String tabCode;// 项目标识

	private String tabName;// 项目名

	private Integer display;//

	private Integer editable;//

	/**
	* Returns value of idProj
	* @return
	*/
	public Long getIdProj() {
		return idProj;
	}

	/**
	* Sets new value of idProj
	* @param
	*/
	public void setIdProj(Long idProj) {
		this.idProj = idProj;
	}

	/**
	* Returns value of tabCode
	* @return
	*/
	public String getTabCode() {
		return tabCode;
	}

	/**
	* Sets new value of tabCode
	* @param
	*/
	public void setTabCode(String tabCode) {
		this.tabCode = tabCode;
	}

	/**
	* Returns value of tabName
	* @return
	*/
	public String getTabName() {
		return tabName;
	}

	/**
	* Sets new value of tabName
	* @param
	*/
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	/**
	* Returns value of display
	* @return
	*/
	public Integer getDisplay() {
		return display;
	}

	/**
	* Sets new value of display
	* @param
	*/
	public void setDisplay(Integer display) {
		this.display = display;
	}

	/**
	* Returns value of editable
	* @return
	*/
	public Integer getEditable() {
		return editable;
	}

	/**
	* Sets new value of editable
	* @param
	*/
	public void setEditable(Integer editable) {
		this.editable = editable;
	}
}
