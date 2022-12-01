package transformations.operators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import agg.xt_basis.Arc;
import agg.xt_basis.GraphObject;
import agg.xt_basis.Node;
import agg.xt_basis.OrdinaryMorphism;
import agg.xt_basis.Rule;
import agg.xt_basis.TypeException;
import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Pair;
import utils.Report;

/**
 * Creates a "clone" node for all nodes in a set (i.e. forall x s.t. x--s-->S, creates x---c--->C
 * @author ceichler
 *
 */
public class CloneSet extends Operator {

	/**
	 * Graph rewriting rule formalizing the operator
	 */
	private Rule r = Grammar.cloneSet.getClone();

	/**
	 * Property to match
	 */
	private String s;
	
	/**
	 * Range of the property s for matching
	 */
	private String S;
	
	/**
	 * Name of the node to clone (can be left unspecified to match all)
	 */
	private String src;
	
	/**
	 * Property relating the clone and the src
	 */
	private String c;

	/**
	 * Name of the clone
	 */
	private String C;
	
	
	/**
	 * Creates the operator creating a C for all src s.t. src--s-->S
	 * @param src name of the source
	 * @param s name of the property relating src and S 
	 * @param S name of the node related to src through s
	 * @param c property relating src and the clone
	 * @param C name of the clone
	 * @throws InvalidArguments
	 * s, S and S can be star. (s,S) can be (null,null).
	 */
	public CloneSet(String src, String s, String S, String c, String C) throws InvalidArguments {
		this.s=s;
		this.S = S;
		this.c = c;
		this.C = C;
		this.src = src;

	}

	@Override
	public void execute() {

		r.setName("tmpCS");
		//setting set name and propName

		//if both s and S are null no PAC for source
		if(s == null && S == null) r.removePAC(r.getPAC("SourceSet"));
		else {
			setArcVal(r.getPAC("SourceSet").getTarget().getArcs(GraGraUtils.TEDGE));
			setNodeVal(r.getPAC("SourceSet").getTarget().getNodes(GraGraUtils.TNODE));
		}
		
		
		//setting att values for R and L
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
		setNodeVal(r.getRight().getNodes(GraGraUtils.TNODE));
		setArcVal(r.getRight().getArcs(GraGraUtils.TEDGE));
		
		
		// recreating no duplicate NAC
		//easiest after having set the value of X

		r.removeNAC(r.getNAC("NoDuplicate"));
		r.createNACDuetoRHS();


		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);

	}
	
	/**
	 * Gives value to parameters within the rules
	 * @param nodes nodes whose parameter to set
	 */
	private void setNodeVal(List<Node> nodes) {
		for(Node n : nodes) {
			switch(n.getAttribute().getValueAsString(0)) {
			case "C":
				if(!C.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", C);
				break;
			case "src":
				if(!src.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", src);
				break;
			case "S":
				if(!S.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", S);
				break;
			default:
				break;
			}
			
		}
	}
	
	/**
	 * Gives value to parameter within the rule
	 * @param arcs arcs whose parameter to set
	 */
	private void setArcVal(Vector<Arc> arcs) {
		for(Arc a : arcs) {
			switch(a.getAttribute().getValueAsString(0)) {
			case "c":
				if(!c.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", c);
				break;
			case "s":
				if(!s.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(a, "prop", s);
				break;
				
			}
			
		}
	}

}
