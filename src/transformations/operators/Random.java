package transformations.operators;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import agg.xt_basis.Arc;
import agg.xt_basis.GraphObject;
import agg.xt_basis.Node;
import granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for randomizing all relations of type p with value related to O by o from a node related to S by S
 * to a random value related to T by t
 * @author ceichler
 *
 */
public class Random extends Operator {
	
	/**
	 * Argument of operator; attribute of the node to be created
	 */
	private String s, S, t, T, o, O, p;

	/**
	 * Initializing a new node 
	 * @param arg the attribute of the node being created
	 */
	public Random(String s,String S,String t,String T,String o, String O, String p) {
		r = Grammar.random.getClone();
		this.s= s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.t = t;
		this.T = T;
		this.p = p;
	}

	@Override
	public void execute() {
		//cloning and seting setName
		this.r.setName("tmpRnd");
		setArcVal(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcVal(r.getTarget().getArcs(GraGraUtils.TEDGE));
		setNodeVal(r.getSource().getNodes(GraGraUtils.TNODE));;
		setNodeVal(r.getTarget().getNodes(GraGraUtils.TNODE));
		
		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "p":
				if(!p.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", p);
				break;
			case "s":
				if(!s.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", s);
				break;
			case "t":
				if(!t.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", t);
				break;
			case "o":
				if(!o.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", o);
				break;
				
			}
			
		}
	}
	
	private void setNodeVal(List<Node> nodes) {
		for(Node n : nodes) {
			switch(n.getAttribute().getValueAsString(0)) {
			case "S":
				if(!S.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", S);
				break;
			case "T":
				if(!T.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", T);
				break;
			case "O":
				if(!S.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", O);
				break;
			}
			
		}
	}

}
