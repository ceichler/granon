package transformations.operators;


import java.util.List;
import java.util.Vector;
import agg.xt_basis.Arc;
import agg.xt_basis.Node;
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
	 * Argument of operator; attributes related to properties, sources and objects
	 */
	private String x, s, S, o, O, p;

	/**
	 * transforming all relations of type pi with value related to O by o from a node x related to S by S
	 * to two relation of type pf1 and pf2 having the same source and target, respectively, and their target and source being interm.	 * @param x source node
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
		this.x = x;
		this.s= s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.p = p;
	}

	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpER");
		setArcVal(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcVal(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//if both s and S are null no PAC for source
		if(s == null && S == null) r.removePAC(r.getPAC("SourceSet"));
		else {
			setArcVal(r.getPAC("SourceSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("SourceSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		
		//if both o and O are null no object PAC
		if(o == null && O == null) r.removePAC(r.getPAC("ObjectSet"));
		else {
			setArcVal(r.getPAC("ObjectSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("ObjectSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		

		//setting x and interm
		setNodeVal(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	
	/**
	 * Gives value to parameter within the rule
	 * @param arcs arcs whose parameter to set
	 */
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "p":
				if(!p.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", p);
				break;
			case "s":
				if(!s.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", s);
				break;
			case "o":
				if(!o.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", o);
				break;
				
			}
			
		}
	}
	
	/**
	 * give value to parameter within the rule
	 * @param nodes nodes whose parameters to set
	 */
	private void setNodeVal(List<Node> nodes) {
		for(Node n : nodes) {
			switch(n.getAttribute().getValueAsString(0)) {
			case "S":
				if(!S.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", S);
				break;
			case "O":
				if(!O.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", O);
				break;
			case "x":
				if(!x.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", x);
				break;
			default:
				break;
			}
			
		}
	}

}
