package transformations.operators;


import java.util.ArrayList;
import java.util.HashMap;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for transforming all relations of type pi with value related to O by o from a node related to S by S
 * to a relation pf with same source and value
 * Neither S nor O can be target of pi
 * @author ceichler
 *
 */
public class ModifyEdge extends Operator {
	

	
	/**
	 * Creating the operator transforming all relations of type pi with value related to O by o from a node x related to S by S
	 * to a relation pf with same source and value.
	 * If s and S are stars, the semantic is "for any node source of a relation pi and source of at least one other relation"
	 * If s and S are null, the semantic is "for any node source of a relation pi"
	 * Same goes for o and O
	 * @param x the source node
	 * @param s relation with the source set
	 * @param S source set
	 * @param o relation with the object set
	 * @param O object set
	 * @param pi initial relation to be modified
	 * @param pf final relation, cannot be *
	 */
	public ModifyEdge(String x, String s,String S,String o, String O, String pi, String pf) {
		r = Grammar.modifyEdge.getClone();
		map.put("x",x);
		map.put("s",s);
		map.put("S",S);
		map.put("o",o);
		map.put("O",O);
		map.put("pi",pi);
		map.put("pf",pf);
	}
	
	/**
	 * Creating the operator transforming all relations of type pi with value related to O by o from a node related to S by S
	 * to a relation pf with same source and value.
	 * If s and S are stars, the semantic is "for any node source of a relation pi and source of at least one other relation"
	 * If s and S are null, the semantic is "for any node source of a relation pi"
	 * Same goes for o and O
	 * @param s relation with the source set
	 * @param S source set
	 * @param o relation with the object set
	 * @param O object set
	 * @param pi initial relation to be modified
	 * @param pf final relation, cannot be *
	 */
	public ModifyEdge(String s,String S,String o, String O, String pi, String pf) {
		this(GraGraUtils.STAR, s, S, o, O, pi, pf);
	}
	
	
	/**
	 * Create operator for tranforming all relations pre-determined
	 * @param map contains all needed arguments
	 */
	public ModifyEdge(HashMap<String,ArrayList<String>> mapTokens) {
		r = Grammar.modifyEdge.getClone();
		map.put("x",mapTokens.get("S").get(0));
		map.put("s",mapTokens.get("S").get(1));
		map.put("S",mapTokens.get("S").get(2));
		map.put("o",mapTokens.get("O").get(1));
		map.put("O",mapTokens.get("O").get(2));
		map.put("pi",mapTokens.get("pi").get(0));
		map.put("pf",mapTokens.get("pf").get(0));
		System.out.println("\u001B[36m [In EdgeChord] mapTokens = "+mapTokens+"\u001B[0m");
		System.out.println("\u001B[36m [In EdgeChord] map = "+map+"\u001B[0m");
	}

	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpModify");

		//if both s and S are null, no PAC
		handlePAC(map.get("s"), map.get("S"), "SourceSet");
		//if both o and O are *, no PAC
		handlePAC(map.get("o"), map.get("O"), "ObjectSet");

		
		//setting x

		setNodeValue(r.getSource().getNodes(GraGraUtils.TNODE));
		
		//setting pi & pf
		setArcValue(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcValue(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);
		
	}

}
