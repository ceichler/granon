package transformations.operators;


import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import agg.xt_basis.Arc;
import agg.xt_basis.Node;
import agg.xt_basis.Rule;
import utils.GraGraUtils;

/**
 * Abstract class representing an atomic operator (a graph rewriting rule)
 * @author ceichler
 *
 */
public abstract class Operator {
	
	/**
	 * corresponding rewriting rule
	 */
	
	protected Rule r;
	
	/**
	 * Couples (name, value) of attributes appearing in the rule
	 */
	protected HashMap<String,String> map = new HashMap<String,String>();
	
	/**
	 * Execution of the operator
	 */
	public abstract void execute();
	
	/**
	 * Gives value to parameters/attributes within the rules
	 * @param nodes nodes whose parameter to set
	 */
	protected void setNodeValue(List<Node> nodes) {
		String name;
		for(Node n : nodes) {
			name = n.getAttribute().getValueAsString(0);
			if(map.containsKey(name) && !map.get(name).contentEquals(GraGraUtils.STAR))GraGraUtils.setAtt(n, "att", map.get(name));
			
		}
	}
	
	/**
	 * Gives value to parameters/attributes within the rule
	 * @param arcs arcs whose parameter to set
	 */
	protected void setArcValue(Vector<Arc> arcs) {
		String name;
		for(Arc a : arcs) {
			name = a.getAttribute().getValueAsString(0);
			if(map.containsKey(name) && !map.get(name).contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", map.get(name));
		}
	}
	
	/**
	 * Disable PAC if (x,X) = (null,null), else set their attributes value
	 * @param x qualifying property
	 * @param X qualifying node
	 * @param name name of the PAC
	 */
	protected void handlePAC(String x, String X, String name) {
		if(x == null && X == null) r.removePAC(r.getPAC(name));
		else {
			setArcValue(r.getPAC(name).getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeValue(r.getPAC(name).getTarget().getNodes(GraGraUtils.TNODE));
		}
	}

}
