package com.bricks.helper.code;

import java.util.ArrayList;
import java.util.List;

import com.bricks.lang.BaseObject;
import com.bricks.utils.CollectionUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class JavaClass extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String pkg;

	private String clazzName;

	private String comment;

	private List<JavaField> fields;

	private List<JavaMethod> methods;

	private boolean isInterface;

	private List<String> imports = new ArrayList<>();

	public JavaClass() {}

	/**
	 * @param pkg
	 * @param clazzName
	 * @param comment
	 */
	public JavaClass(String pkg, String clazzName, String comment) {
		super();
		this.pkg = pkg;
		this.clazzName = clazzName;
		this.comment = comment;
	}

	public List<String> imports() {
		fields.forEach(f -> {
			if (needImp(f.getValType())) {
				imports.add(f.getValType().getName());
			}
			if (needImp(f.getKeyType())) {
				imports.add(f.getKeyType().getName());
			}
			if (f.getGenericType() != null && !GenericType.Non.equals(f.getGenericType()) && needImp(f.getGenericType().getClazzName())) {
				imports.add(f.getGenericType().getClazzName());
			}
		});

		return CollectionUtil.removeDup(imports);
	}

	boolean needImp(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		return needImp(clazz.getName());
	}

	boolean needImp(String clazzName) {
		if (clazzName == null) {
			return false;
		}
		if (clazzName.endsWith("Short")
				|| clazzName.endsWith("Integer")
				|| clazzName.endsWith("Long")
				|| clazzName.endsWith("Double")
				|| clazzName.endsWith("Float")
				|| clazzName.endsWith("String")
				|| clazzName.endsWith("Byte")
				|| clazzName.endsWith("Boolean")
				|| clazzName.endsWith("Character")) {
			return false;
		}
		return true;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<JavaField> getFields() {
		return fields;
	}

	public void addField(JavaField field) {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		fields.add(field);
	}

	public void setFields(List<JavaField> fields) {
		this.fields = fields;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public List<JavaMethod> getMethods() {
		return methods;
	}

	public void addMethod(JavaMethod method) {
		if (methods == null) {
			methods = new ArrayList<>();
		}
		methods.add(method);
	}

	public void setMethods(List<JavaMethod> methods) {
		this.methods = methods;
	}

	public void addImport(Class<?> imp) {
		addImport(imp.getName());
	}

	public void addImport(String imp) {
		if (imports == null) {
			imports = new ArrayList<>();
		}
		imports.add(imp);
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}
}
