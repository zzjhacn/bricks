package ${s.pkg};

/**
 * @author Gray <b>long1795@gmail.com</b>
 */
public class ${s.name}States {
	
#foreach( ${f} in ${s.stats} )
	static final ${s.name}State ${f.name} = new ${s.name}State(${s.name}.STATUS_${f.name},"${f.name}") {
		private static final long serialVersionUID = 1L;
#foreach( ${m} in ${f.actions.entrySet()} )

		protected void ${m.key}(${s.name}Context ctx) {
			ctx.setState(${m.value});
		}
#end
	};

#end
}
