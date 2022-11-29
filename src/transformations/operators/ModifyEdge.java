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
 * to a relation pf with same source and value
 * Neither S nor O can be target of pi
 * @author ceichler
 *
 */
public class ModifyEdge extends Operator {
	
	/**
	 * Argument of operator; attributes related to the properties, sources and object
	 */
	private String s, S, o, O, pi, pf,x;

	
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
		this.s= s;
		this.S = S;
		this.o = o;
		this.O = O;
		this.pi = pi;
		this.pf = pf;
		this.x = x;
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

	@Override
	public void execute() {
		//setting attribute values
		this.r.setName("tmpModify");

		//if both s and S are *, no PAC
		if(s == null && S == null) r.removePAC(r.getPAC("SourceSet"));
		else {
			setArcVal(r.getPAC("SourceSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("SourceSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		if(o == null && O == null) r.removePAC(r.getPAC("ObjectSet"));
		else {
			setArcVal(r.getPAC("ObjectSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("ObjectSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		
		//setting x

		setNodeVal(r.getSource().getNodes(GraGraUtils.TNODE));
		
		//setting pi & pf
		setArcVal(r.getSource().getArcs(GraGraUtils.TEDGE));
		setArcVal(r.getTarget().getArcs(GraGraUtils.TEDGE));
		
		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);
		
	}
	
	/**
	 * Set all the attributes of the input arcs to the value of the class attribute with the same name
	 * @param arcs the arcs whose attribute to set
	 */
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "pi":
				if(!pi.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", pi);
				break;
			case "pf":
				//this is pretty bad, should be forbidden
				//if(!pf.contentEquals(GraGraUtils.STAR)) 
				
				GraGraUtils.setAtt(a, "prop", pf);
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
	 * 
	 * Set all the attributes of the input nodes to the value of the class attribute with the same name
	 * @param nodes the nodes whose attributes to set
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
			}
			
		}
	}

}
