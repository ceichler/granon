package transformations.operators;


import agg.xt_basis.Rule;
import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for node deletion, deletes all node with given attribute value
 * @author ceichler
 *
 */
public class DeleteNode extends Operator {
	
	/**
	 * Argument of operator; attribute of the node to be deleted
	 * relation s to the node S (for multiple deletion)
	 */
	private String arg, s, S;

	/**
	 * Initializing the operator
	 * @param arg the attribute of the node to be deleted
	 */
	public DeleteNode(String arg) {
		this(arg, null, null);
		
	}
	
	/**
	 * Initializing the operator
	 * @param arg the attribute of the node to be deleted
	 */
	public DeleteNode(String arg, String s, String S) {
		r = Grammar.deleteNode.getClone();
		this.arg = arg;
		this.s = s;
		this.S = S;
	}

	@Override
	public void execute() {
		//cloning and seting setName
		this.r.setName("tmpDelNode");
		//set X 
		if(!arg.equals(GraGraUtils.STAR)) GraGraUtils.setAtt(r.getElementsToDelete().get(0), "att", arg);
		//if both s and S are null no PAC for source
		if(s == null && S == null) r.removePAC(r.getPAC("SourceSet"));
		else {
			if(!S.equals(GraGraUtils.STAR)) GraGraUtils.setAtt(r.getPAC("Set").getTarget().getNodes(GraGraUtils.TNODE).get(1), "att", S);
			if(!s.equals(GraGraUtils.STAR)) GraGraUtils.setAtt(r.getPAC("Set").getTarget().getArcs(GraGraUtils.TEDGE).get(0), "prop", s);
		}
		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);	
	}

}
