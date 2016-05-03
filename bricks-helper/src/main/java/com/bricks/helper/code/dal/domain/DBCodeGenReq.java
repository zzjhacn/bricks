package com.bricks.helper.code.dal.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bricks.helper.code.CodeGenReq;
import com.bricks.helper.code.CodeType;
import com.bricks.helper.code.JavaField;
import com.bricks.lang.exception.SysException;
import com.bricks.utils.StringUtil;
import com.mysql.cj.jdbc.Driver;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DBCodeGenReq extends CodeGenReq {
	private static final long serialVersionUID = 1L;

	private List<DBTable> tables;

	public DBCodeGenReq fromMySql(final String pkg, final String url) {// TODO 暂仅支持MYSQL
		return fromMySql(pkg, url, "");
	}

	public DBCodeGenReq fromMySql(final String pkg, final String url, final String prefix) {// TODO 暂仅支持MYSQL
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(Driver.class.getName());
		ds.setUrl(url);
		JdbcTemplate template = new JdbcTemplate(ds);
		List<Map<String, Object>> result = template.queryForList("show tables");
		template.execute("use information_schema");
		result.forEach(m -> {
			m.forEach((k, v) -> {
				if (v.toString().startsWith(prefix)) {
					addTable(table(pkg, template, k.replace("Tables_in_", ""), v.toString(), prefix));
				}
			});
		});
		return this;
	}

	DBTable table(String pkg, JdbcTemplate template, String schema, String name, String prefix) {
		final DBTable t = new DBTable(pkg + "." + StringUtil.columnToProperty(name.replaceFirst(prefix, "")).toLowerCase(),
				StringUtil.columnToProperty("_" + name.replaceFirst(prefix, "")), "");
		List<Map<String, Object>> result = template.queryForList("select * from columns where table_schema='" + schema + "' and table_name='" + name + "'");
		result.forEach(m -> {
			Class<?> clazz = TypeUtil.toJavaType(m.get("DATA_TYPE").toString());
			if (clazz == null) {
				log().error("Type[{}] of db not mapped by java type.", m.get("DATA_TYPE"));
				throw new SysException("Type mismatch!");
			}
			t.addField(new JavaField(clazz, StringUtil.columnToProperty(m.get("COLUMN_NAME").toString()), m.get("COLUMN_COMMENT").toString()));
		});
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenReq#codeType()
	 */
	@Override
	public CodeType codeType() {
		return CodeType.DAO;
	}

	public List<DBTable> getTables() {
		return tables;
	}

	public void addTable(DBTable table) {
		if (tables == null) {
			tables = new ArrayList<>();
		}
		tables.add(table);
	}

	public void setTables(List<DBTable> tables) {
		this.tables = tables;
	}

}
