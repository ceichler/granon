package transformations.operators;



import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElementDecl;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for "merging" any two relations of type pi1 and pi2 with x -pi1->y-pi2->z, with z related to O by o, x to S by s and y to I by i
 * Transforms it into x--pf-->z
 * Neither S nor O nor I can be the target of pi1 or pi2
 * @author ceichler
 *
 */
public class EdgeChord extends Operator {
	


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
	public EdgeChord(String x, String s,String S,String i, String I, String o, String O, String pi1, String pi2, String pf) {
		r = Grammar.edgeChord.getClone();
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

	/**
	 * transforming all paths pi1 pi2 with value related to O by o from a node x related to S by S with an intermediary node related to I by i
	 * to a of type pf having the same source and target.
	 * @param map contains all needed parameters
	 */
	
	public EdgeChord(HashMap<String,ArrayList<String>> mapTokens) {
		r = Grammar.edgeChord.getClone();
		map.put("x",mapTokens.get("S").get(0));
		map.put("s",mapTokens.get("S").get(1));
		map.put("S",mapTokens.get("S").get(2));
		map.put("o",mapTokens.get("O").get(1));
		map.put("O",mapTokens.get("O").get(2));
		map.put("i",mapTokens.get("I").get(1));
		map.put("I",mapTokens.get("I").get(2));
		map.put("pi1",mapTokens.get("pi1").get(0));
		map.put("pi2",mapTokens.get("pi2").get(0));
		map.put("pf",mapTokens.get("pf").get(0));
		System.out.println("\u001B[36m [In EdgeChord] mapTokens = "+mapTokens+"\u001B[0m");
		System.out.println("\u001B[36m [In EdgeChord] map = "+map+"\u001B[0m");
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
				
		
		//if both s and S are null no PAC for source

		handlePAC(map.get("s"), map.get("S"), "SourceSet");
		
		//if both o and O are null no object PAC
		handlePAC(map.get("o"), map.get("O"), "ObjectSet");
		
		//if both i and I are null no object PAC
		handlePAC(map.get("i"), map.get("I"), "IntermSet");
				

		
		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);
		
	}
	
}
