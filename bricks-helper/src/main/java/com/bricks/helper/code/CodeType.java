package com.bricks.helper.code;

/**
 * @author bricks <long1795@gmail.com>
 */
public enum CodeType {
	API("../template/api/"),  //
	DAO("../template/dal/"),  //
	WEBAPP("../template/webapp/"),  //
	FSM("../template/stat/");

	private String templatePath;

	CodeType(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplatePath() {
		return templatePath;
	}
}
