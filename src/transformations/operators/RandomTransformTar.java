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
 * Operator for transforming all relations of type pi with value related to O by o from a node related to S by S
 * to a relation of type pf with same the source and a random value related to T by t
 * From  S<-s-x-pi->y-o->O z-t->T to S<-s-x-pf->z-t->T y-o->O 
 * Neither S, O or T can be the target of pi
 * @author ceichler
 *
 */
public class RandomTransformTar extends Operator {
	
	/**
	 * Argument of operator; attributes related to properties, sources, targets and objects
	 */
	private String x, s, S, t, T, o, O, pi, pf;

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
	public RandomTransformTar(String x, String s,String S,String t,String T,String o, String O, String pi, String pf) {
		r = Grammar.randomTransformTarget.getClone();
		this.x = x;
		this.s= s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.t = t;
		this.T = T;
		this.pi = pi;
		this.pf = pf;
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
	public RandomTransformTar(String s,String S,String t,String T,String o, String O, String pi, String pf) {
		this(GraGraUtils.STAR, s, S, t, T,o,O,pi,pf);
	}
	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpRnd");
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
		
		//if both t and T are null no target PAC
		if(t == null && T == null) r.removePAC(r.getPAC("TargetSet"));
		else {
			setArcVal(r.getPAC("TargetSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("TargetSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		

		//setting x
		setNodeVal(r.getRight().getNodes(GraGraUtils.TNODE));
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
		
		//transforming
		GraGraUtils.transformAllRand(new Report(), r, Tui.grammar);
		
	}
	
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "pi":
				if(!pi.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", pi);
				break;
			case "pf":
				//removing this pf = star
				//if(!pf.contentEquals(GraGraUtils.STAR)) 
				GraGraUtils.setAtt(a, "prop", pf);
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
