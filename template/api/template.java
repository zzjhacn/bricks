package ${a.pkg};

import com.landaojia.base.utils.BaseObject;
#foreach( ${import} in ${a.imports} )
import ${import};
#end

/**
 * ${a.classComment}
 * 
 * @author Gray <b>long1795@gmail.com</b>
 */
public class ${a.className} extends BaseObject {
	private static final long serialVersionUID = 1L;

#foreach( ${f} in ${a.staticFields} )
	/**
	 * ${f.comment}
	 */
	public static final ${f.type} ${f.name};
	
#end
#foreach( ${f} in ${a.fields} )

	/**
	 * ${f.comment}
	 */
	private ${f.type} ${f.name};//${f.comment}
#end

#foreach( ${f} in ${a.fields} )
	/**
	 * @return ${f.comment}
	 */
	public ${f.type} get${f.getterAndSetterName}() {
		return ${f.name};
	}

	/**
	 * 设置 ${f.comment}
	 * @param ${f.name}
	 *            ${f.comment}
	 */
	public void set${f.getterAndSetterName}(${f.type} ${f.name}) {
		this.${f.name} = ${f.name};
	}
#end
}
