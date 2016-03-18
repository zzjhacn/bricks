package ${s.pkg};

import cn.codingwoo.base.commons.fsm.UnknownStatException;
import cn.codingwoo.base.commons.fsm.Order;

/**
 * @author Gray <b>long1795@gmail.com</b>
 */
public enum ${s.name}Status {

#foreach( ${f} in ${s.statNames} )
	$f(${s.name}.STATUS_$f, ${s.name}States.$f),

#end
	;

	public static ${s.name}Status byCode(int code) {
		for (${s.name}Status s : ${s.name}Status.values()) {
			if (s.getCode() == code) {
				return s;
			}
		}
		throw new UnknownStatException();
	}

	private int code;

	private ${s.name}State stat;

	${s.name}Status(int code) {
		this.code = code;
		this.stat = new ${s.name}State(code, this.toString());
	}

	${s.name}Status(int code, ${s.name}State stat) {
		this.code = code;
		this.stat = stat;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ${s.name}State getStat() {
		return stat;
	}

	public void setStat(${s.name}State stat) {
		this.stat = stat;
	}

}
