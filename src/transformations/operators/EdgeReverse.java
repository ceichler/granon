package transformations.operators;


import java.util.HashMap;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for reversing all relations of type p with value related to O by o from a node x related to S by s
 * Noting x--p-->y such a relation, transforms it into y--p-->y
 * Neither S nor O can be the target of pi
 * @author ceichler
 *
 */
public class EdgeReverse extends Operator {
	

	/**
	 * Reverse all relations of type p with value related to O by o from a node x related to S by S
	 * @param x source node
	 * @param x source
	 * @param s relation with the source set
	 * @param S source set
	 * @param o relation with the object set
	 * @param O object set
	 * @param p relation
	 * If s & S are null the semantic is "any node source of a relation p with value related to O by o"
	 * If s & S are stars the semantic is "any such node source with at least another relation with another node"
	 * same for o and O
	 */
	public EdgeReverse(String x, String s,String S,String o, String O, String p) {
		r = Grammar.edgeReverse.getClone();
		map.put("x",x);
		map.put("s",s);
		map.put("S",S);
		map.put("o",o);
		map.put("O",O);
		map.put("p",p);
	}
	
	/**
	 * reverse all relation pre-determined
	 * @param map contains all needed arguments
	 */

	public EdgeReverse(HashMap<String,String> map) {
		r = Grammar.edgeReverse.getClone();
		this.map = map;
	}
	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpER");
		setArcValue(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcValue(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//if both s and S are null no PAC for source

		handlePAC(map.get("s"), map.get("S"), "SourceSet");
		
		//if both o and O are null no object PAC
		handlePAC(map.get("o"), map.get("O"), "ObjectSet");
		
		

		//setting x
		setNodeValue(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
	


		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);
		
	}
	
}
