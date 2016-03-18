package ${s.pkg};

/**
 * @author Gray <b>long1795@gmail.com</b>
 */
public class Default${s.name}Context extends ${s.name}Context {
	private static final long serialVersionUID = 1L;

	/**
	 * @param initState
	 */
	public Default${s.name}Context(${s.name}State initState) {
		super(initState);
	}
#foreach( ${f} in ${s.actions} )

	public ${s.name}Context $f() {
		transition = "$f";
		getState().$f(this);
		transition = "";
		return this;
	}
#end
}
