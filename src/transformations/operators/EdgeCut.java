package transformations.operators;


import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for "cutting" all relations of type pi with value related to O by o from a node x related to S by s
 * Noting x--pi-->y such a relation, transforms it into x--pf1-->interm--pf2--->y
 * Neither S nor O can be the target of pi
 * @author ceichler
 *
 */
public class EdgeCut extends Operator {
	

	/**
	 * transforming all relations of type pi with value related to O by o from a node x related to S by S
	 * to two relation of type pf1 and pf2 having the same source and target, respectively, and their target and source being interm.	 * @param x source node
	 * @param x source
	 * @param s relation with the source set
	 * @param S source set
	 * @param o relation with the object set
	 * @param O object set
	 * @param pi intial relation
	 * @param pf1 final relation from x to interm, cannot be *
	 * @param pf2 final relation from interm to the object, cannot be *
	 * @param interm the intermediary node
	 * If s & S are null the semantic is "any node source of a relation pi with value related to O by o"
	 * If s & S are stars the semantic is "any such node source with at least another relation with another node"
	 * same for o and O T
	 */
	public EdgeCut(String x, String s,String S,String o, String O, String pi, String pf1, String interm, String pf2) {
		r = Grammar.edgeCut.getClone();
		map.put("x",x);
		map.put("s",s);
		map.put("S",S);
		map.put("o",o);
		map.put("O",O);
		map.put("pi",pi);
		map.put("pf1",pf1);
		map.put("pf2",pf2);
		map.put("interm", interm);
	}

	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpECut");
		setArcValue(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcValue(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//if both s and S are null no PAC for source

		handlePAC(map.get("s"), map.get("S"), "SourceSet");
		
		//if both o and O are null no object PAC
		handlePAC(map.get("o"), map.get("O"), "ObjectSet");
		
				

		//setting x and interm
		setNodeValue(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}


}
