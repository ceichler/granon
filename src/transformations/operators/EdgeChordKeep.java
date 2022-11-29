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
 * Operator for "merging" any two relations of type pi1 and pi2 with x -pi1->y-pi2->z, with z related to O by o, x to S by s and y to I by i
 * Transforms it into x--pf-->z. Does not delete pi1 and pi2
 * Neither S nor O nor I can be the target of pi1 or pi2
 * @author ceichler
 *
 */
public class EdgeChordKeep extends Operator {
	
	/**
	 * Argument of operator; attributes related to properties, sources and objects
	 */
	private String x, s, S, o, O, i, I, pf, pi1, pi2;

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
		this.x = x;
		this.s= s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.i =i;
		this.I = I;
		this.pi1 = pi1;
		this.pi2 = pi2;
		this.pf = pf;
		}

	
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpEChord");
		//setting pi1, pi2 and pf
		setArcVal(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcVal(r.getTarget().getArcs(GraGraUtils.TEDGE));
		//setting x
		setNodeVal(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
				
		
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
		//if both i and I are null no object PAC
		if(i == null && I == null) r.removePAC(r.getPAC("IntermSet"));
		else {
			setArcVal(r.getPAC("IntermSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("IntermSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		

		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	
	/**
	 * Gives value to parameters within the rules
	 * @param arcs arcs whose parameter to set
	 */
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "pi1":
				if(!pi1.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", pi1);
				break;
			case "pi2":
				if(!pi2.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", pi2);
				break;
			case "pf":
				GraGraUtils.setAtt(a, "prop", pf);
				break;
			case "s":
				if(!s.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", s);
				break;
			case "o":
				if(!o.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", o);
				break;
			case "i":
				if(!i.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", i);
				break;
				
			}
			
		}
	}
	

	/**
	 * Gives value to parameters within the rules
	 * @param nodes nodes whose parameter to set
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
			case "I":
				GraGraUtils.setAtt(n, "att", I);
				break;
			default:
				break;
			}
			
		}
	}

}
