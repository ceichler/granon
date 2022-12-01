package transformations.operators;


import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for "merging" any two relations of type pi1 and pi2 with x -pi1->y-pi2->z, with z related to O by o, x to S by s and y to I by i
 * Transforms it into x--pf-->z. Does not delete pi1 and pi2
 * Neither S nor O nor I can be the target of pi1 or pi2
 * @author ceichler
 *
 */
public class EdgeChordKeep extends Operator {
	

	/**
	 * transforming all paths pi1 pi2 with value related to O by o from a node x related to S by S with an intermediary node related to I by i
	 * to a of type pf having the same source and target.	 
	 * @param x source node
	 * @param s relation with the source set
	 * @param S source set
	 * @param o relation with the object set
	 * @param O object set
	 * @param i relation with the intermediary set
	 * @param I intermediary set
	 * @param pf final relation, cannot be *
	 * @param pi1 initial relation from x to interm
	 * @param pi2 initial relation from interm to the object
	 * If s & S are null the semantic is "any node source of a relation"
	 * If s & S are stars the semantic is "any such node source with at least another relation with another node"
	 * same for o and O; i and I
	 */
	public EdgeChordKeep(String x, String s,String S,String i, String I, String o, String O, String pi1, String pi2, String pf) {
		r = Grammar.edgeChordKeep.getClone();
		map.put("x",x);
		map.put("s",s);
		map.put("S",S);
		map.put("o",o);
		map.put("O",O);
		map.put("i",i);
		map.put("I",I);
		map.put("pi1",pi1);
		map.put("pi2",pi2);
		map.put("pf",pf);
	}

	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpEChord");
		//setting pi1, pi2 and pf
		setArcValue(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcValue(r.getTarget().getArcs(GraGraUtils.TEDGE));
		//setting x
		setNodeValue(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
		r.createNACDuetoRHS();	
	
		
		//if both s and S are null no PAC for source

		handlePAC(map.get("s"), map.get("S"), "SourceSet");
		
		//if both o and O are null no object PAC
		handlePAC(map.get("o"), map.get("O"), "ObjectSet");
		
		//if both i and I are null no object PAC
		handlePAC(map.get("i"), map.get("I"), "IntermSet");
				
		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	


}
