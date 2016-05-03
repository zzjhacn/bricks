package ${req.pkg};

import com.bricks.lang.BaseObject;
#foreach( ${f} in ${req.imports} )
import ${f};
#end
/**
 * ${req.comment}
 * 
 * @author Gray <b>long1795@gmail.com</b>
 */
public class ${req.clazzName} extends BaseObject {
	private static final long serialVersionUID = 1L;
#foreach( ${f} in ${req.staticFields} )

	/**
	 * ${f.comment}
	 */
	public static final ${f.type} ${f.name};
#end
#foreach( ${f} in ${req.fields} )

	/**
	 * ${f.comment}
	 */
	${f.toString()}//${f.comment}
#end
#foreach( ${f} in ${req.fields} )

	/**
	 * @return ${f.comment}
	 */
	public ${f.genericStr()} ${f.getterName()}() {
		return ${f.name};
	}

	/**
	 * 设置 ${f.comment}
	 * @param ${f.name}
	 *            ${f.comment}
	 */
	public void ${f.setterName()}(${f.genericStr()} ${f.name}) {
		this.${f.name} = ${f.name};
	}
#end
}
