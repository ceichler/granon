package transformations.operators;


import java.util.HashMap;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
* Operator for transforming all relations of type pi with value related to O by o from a node related to S by S
* to a relation of type pf with same the target and a random source related to T by t
* From  S<-s-x-pi->y-o->O z-t->T to T<-t-z-pf->y-o->O x-s->S 
* S, O and T cannot be the target of pi
* @author ceichler
*
*/
public class RandomTransformSrc extends Operator {
	

	/**
	 * transforming all relations of type pi with value related to O by o from a node x related to S by S
	 * to a relation of type pf with the same value and a random source related to T by t
	 * @param x source node
	 * @param s relation with the source set
	 * @param S source set
	 * @param t relation with the target set
	 * @param T target set
	 * @param o relation with the object set
	 * @param O object set
	 * @param pi intial relation
	 * @param pf final relation, cannot be *
	 * If s & S are null the semantic is "any node source of a relation pi with value related to O by o"
	 * If s & S are stars the semantic is "any such node source with at least another relation with another node"
	 * same for o, O and t, T
	 */
	public RandomTransformSrc(String x, String s,String S,String t,String T,String o, String O, String pi, String pf) {
		r = Grammar.randomTransformSrc.getClone();
		map.put("x",x);
		map.put("s",s);
		map.put("S",S);
		map.put("o",o);
		map.put("O",O);
		map.put("pi",pi);
		map.put("pf",pf);
		map.put("t",t);
		map.put("T",T);
		if(pi.contentEquals(pf)) throw new InvalidArguments("pi and pf must be distinct");
	}

	/**
	 * transforming all relations of type pi with value related to O by o from a node x related to S by S
	 * to a relation of type pf with the same value and a random source related to T by t
	 * @param s relation with the source set
	 * @param S source set
	 * @param t relation with the target set
	 * @param T target set
	 * @param o relation with the object set
	 * @param O object set
	 * @param pi intial relation
	 * @param pf final relation, cannot be *
	 * If s & S are null the semantic is "any node source of a relation pi with value related to O by o"
	 * If s & S are stars the semantic is "any such node source with at least another relation with another node"
	 * same for o, O and t, T
	 */
	public RandomTransformSrc(String s,String S,String t,String T,String o, String O, String pi, String pf) {
		this(GraGraUtils.STAR, s, S, t, T,o,O,pi,pf);
	}
	
	/**
	 * transforming all relations of type pre-determined
	 * @param map contains all needed arguments
	 */
	
	public RandomTransformSrc(HashMap<String,String> map) {
		r = Grammar.randomTransformSrc.getClone();
		this.map =map;
	}
	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpRnd");
		setArcValue(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcValue(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//if both s and S are null no PAC for source
		handlePAC(map.get("s"),map.get("S"), "SourceSet");
		
		//if both o and O are null no object PAC
		handlePAC(map.get("o"),map.get("O"), "ObjectSet");
		
		
		//if both t and T are null no target PAC
		handlePAC(map.get("t"),map.get("T"), "TargetSet");
		
		

		//setting x
		setNodeValue(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
		

		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	
}
