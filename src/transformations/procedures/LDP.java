package transformations.procedures;

import transformations.operators.DeleteNode;
import transformations.operators.EdgeChord;
import transformations.operators.EdgeCopy;
import transformations.operators.EdgeCut;
import transformations.operators.JoinSet;
import transformations.operators.ModifyEdge;
import transformations.operators.NewNode;
import transformations.operators.Operator;
import transformations.operators.RandomTransformSrc;
import utils.GraGraUtils;

/**
 * Handles LDP
 * Randomly provides a new value for all relations of type p with source node (x) related to S by S
 * from its value related to O by o  to a random value related to O by o
 * with a bias K in favor of the real value (staircase density with the most probable value being K times more probable than the others)
 *
 * @author ceichler
 *
 */
public class LDP {
	


	
	/**
	 * Argument of the operator, 
	 * node x source of p
	 * node S target of edge s related to initial sources
	 * edge p whose object to randomly change
	 * node O target of edge o, related to values of p
	 */
	private String x, s,S, o, O, p, internalName = "$" + System.currentTimeMillis();
	/**
	 * the bias
	 */
	private int k;
	
	/**
	 * Constructing the class, init attribute
	 * @param x the source
	 * @param s relation of Sources
	 * @param S node the Sources are in relation with
	 * @param o relation of values
	 * @param O node the values are in relation with
	 * @param p the property whose value to modify
	 * @param k the bias
	 */
	public LDP(String x, String s, String S, String o, String O, String p, int k) {
		this.x = x;
		this.s = s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.p = p;
		this.k = k;
	}

	
	/**
	 * Constructing the class, init attribute, whatever x
	 * @param s relation of Sources
	 * @param S node the Sources are in relation with
	 *  @param o relation of values
	 * @param O node the values are in relation with
	 * @param p the property whose value to modify
	 * @param k the bias
	 */
	public LDP(String s, String S, String o, String O, String p, int k)  {
		this(GraGraUtils.STAR,s,S,o,O,p,k);
	}

	/**
	 * Execute the random transformation of sources, uses RandomTransform and ModifyEdge
	 */
	public void execute() {
		String clone = "clone " + internalName;
		Operator newNode = new NewNode(clone);
		//creating clones for bias
		for(int i = 0; i<k; i++) newNode.execute();
		//putting them in O through o
		(new JoinSet(clone, O, o)).execute();
		
		//Edge cut to keep track of the initial object
		String p1 = "p1 " + internalName;
		String pNew = "pNew " + internalName;
		String pOld = "pOld " + internalName;
		String interm = "interm " + internalName;
		(new EdgeCut(GraGraUtils.STAR, s,S,o, O, p, p1, interm, pNew)).execute();
		
		//Creating a copy pOld of pNew
		(new EdgeCopy(interm, null,null,null,null, pNew, pOld)).execute();
		
		//randomization
		(new RandomTar(interm, null,null,o,O,o,O,pNew)).execute();
		
		//if clone back to old (i.e. interm is related to clone by pNew
		(new EdgeChord(GraGraUtils.STAR, null,null,pNew,clone,null, null, p1, pOld, p)).execute();
		//else send p to pNew
		(new EdgeChord(GraGraUtils.STAR, null,null,null,null,null, null, p1, pNew, p)).execute();
		
		//cleaning
		(new DeleteNode(clone, null, null)).execute();
		(new DeleteNode(interm, null, null)).execute();
	}

}
