package com.bricks.dal;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class StatementName {

	protected final String buildPrivateStatementName(String suffix) {
		return this.getClass().getName() + "." + suffix;
	}

	protected String buildStatementName(String suffix) {
		return buildPrivateStatementName(suffix);
	}

	protected String statementName_BatchInsert() {
		return buildStatementName("batchInsert");
	}

	protected String statementName_Insert() {
		return buildStatementName("insert");
	}

	protected String statementName_BatchInsertWithId() {
		return buildStatementName("batchInsertWithId");
	}

	protected String statementName_InsertWithId() {
		return buildStatementName("insertWithId");
	}

	protected String statementName_Select() {
		return buildStatementName("select");
	}

	protected String statementName_Count() {
		return buildStatementName("count");
	}

	protected String statementName_Sum() {
		return buildStatementName("sum");
	}

	protected String statementName_Query() {
		return buildStatementName("query");
	}

	protected String statementName_Update() {
		return buildStatementName("update");
	}

	protected String statementName_Delete() {
		return buildStatementName("delete");
	}

	protected String statementName_DeleteById() {
		return buildStatementName("deleteById");
	}
}
