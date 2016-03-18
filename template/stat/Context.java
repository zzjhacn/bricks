package ${s.pkg};

import cn.codingwoo.base.commons.fsm.FSMContext;
import cn.codingwoo.base.commons.fsm.UnknownStatException;

/**
 * @author Gray <b>long1795@gmail.com</b>
 */
public class ${s.name}Context extends FSMContext {
	private static final long serialVersionUID = 1L;

	/**
	 * @param initState
	 */
	protected ${s.name}Context(${s.name}State initState) {
		super(initState);
	}

	@Override
	public void enterStartState() {
		getState().entry(this);
	}

	public ${s.name}State getState() throws UnknownStatException {
		if (state == null) {
			throw new UnknownStatException();
		}
		return (${s.name}State) state;
	}

}
