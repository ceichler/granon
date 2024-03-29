package transformations.procedures;

import java.util.ArrayList;
import java.util.HashMap;

import transformations.operators.ModifyEdge;
import transformations.operators.RandomTransformTar;
import utils.GraGraUtils;

/**
 * Handles randomization:
 * Randomly provides a new value for all relations of type p with source node x related to S by S
 * and value related to O by O  to a random value related to T by t
 *
 * @author ceichler
 *
 */
public class RandomTar {
	


	
	/**
	 * Argument of the operator, 
	 * node x source of p
	 * node S target of edge s related to initial sources
	 * node T target of edge t, related to new sources
	 * edge p whose source to randomly change
	 * node O target of edge o, related to values of p
	 */
	private String x, s,S, t, T, o, O, p, pTmp;
	
	/**
	 * Constructing the class, init attribute
	 * @param x the source
	 * @param s relation of Sources
	 * @param S node the Sources are in relation with
	 * @param t relation of new sources
	 * @param T node the new sources are in relation with
	 * @param o relation of values
	 * @param O node the values are in relation with
	 * @param p the property whose sources to modify
	 */
	public RandomTar(String x, String s, String S, String t, String T, String o, String O, String p) {
		this.x = x;
		this.s = s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.t = t;
		this.T = T;
		this.p = p;
		this.pTmp = "$" + System.currentTimeMillis();
	}

	
	/**
	 * Constructing the class, init attribute
	 * @param s relation of Sources
	 * @param S node the Sources are in relation with
	 * @param t relation of new sources
	 * @param T node the new sources are in relation with
	 * @param o relation of values
	 * @param O node the values are in relation with
	 * @param p the property whose sources to modify
	 */
	public RandomTar(String s, String S, String t, String T, String o, String O, String p) {
		this(GraGraUtils.STAR,s,S,t,T,o,O,p);
	}
	
	/**
	 * Constructing class, init attribute
	 * @param map contains all needed arguments
	 */
	public RandomTar(HashMap<String,ArrayList<String>> mapTokens) {
		this(
				mapTokens.get("S").get(0),
				mapTokens.get("S").get(1),
				mapTokens.get("S").get(2),
				mapTokens.get("T").get(1),
				mapTokens.get("T").get(2),
				mapTokens.get("O").get(1),
				mapTokens.get("O").get(2),
				mapTokens.get("p").get(0)
			);
	}
	
	/**
	 * Execute the random transformation of sources, uses RandomTransform and ModifyEdge
	 */
	public void execute() {
		(new RandomTransformTar(x, s,S,t,T,o,O,p,pTmp)).execute();
		(new ModifyEdge(GraGraUtils.STAR, null,null,null,null,pTmp,p)).execute();
		
	}

}
