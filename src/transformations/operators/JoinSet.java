package transformations.operators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * Adds a 
 * @author ceichler
 *
 */
public class JoinSet extends Operator {
	
	/**
	 * Graph rewriting rule formalizing the operator
	 */
	private Rule r = Grammar.joinSet.getClone();
	/**
	 * Pairs of excepts <x,X> for building NACs
	 */
	private Set<Pair<String>> excepts = new HashSet<Pair<String>>();
	/**
	 * Pairs of wheres <y,Y> for building PACs
	 */
	private Set<Pair<String>> wheres = new HashSet<Pair<String>>();
	
	/**
	 * Name of the node to put nodes satisfying wheres and excepts in relation with
	 */
	private String setName;
	
	/**
	 * Name of the property to create between the set and the node satisfying the wheres and excepts
	 */
	private String propName;
	
	/**
	 * Name of the node to put in relation with setName through propName (can be left unspecified to match all)
	 */
	private String src;
	
	/**
	 * Creates the operator 
	 * @para src name of the node to put in relation with setName through propName (can be star)
	 * @param excepts set of except pairs <x,X>, ignore nodes related to X by x for any <x,X> in except
	 * @param wheres set of wheres paris <y,Y>, only target nodes related to Y by y for all <y,Y> in wheres
	 * @param setName name of the set to put nodes in
	 * @param propName name of the relation to create between nodes satisfying conditions and the set
	 * @throws InvalidArguments
	 */
	public JoinSet(String src, Set<Pair<String>> excepts, Set<Pair<String>> wheres, String setName, String propName) throws InvalidArguments {
		this.setName=setName;
		this.propName = propName;
		this.excepts=excepts;
		this.wheres = wheres;
		this.src = src;
		
	}
	
	/**
	 * Creates the operator with x unspecified (star)
	 * @param excepts set of except pairs <x,X>, ignore nodes related to X by x for any <x,X> in except
	 * @param wheres set of wheres paris <y,Y>, only target nodes related to Y by y for all <y,Y> in wheres
	 * @param setName name of the set to put nodes in
	 * @param propName name of the relation to create between nodes satisfying conditions and the set
	 * @throws InvalidArguments
	 */
	public JoinSet(Set<Pair<String>> excepts, Set<Pair<String>> wheres, String setName, String propName) throws InvalidArguments {
		this(GraGraUtils.STAR, excepts, wheres, setName, propName);
		
	}
	

	/**
	 * Creates the operator with default propName = js and x unspecified (star)
	 * @param excepts set of except pairs <x,X>, ignore nodes related to X by x for any <x,X> in except
	 * @param wheres set of wheres paris <y,Y>, only target nodes related to Y by y for all <y,Y> in wheres
	 * @param setName name of the set to put nodes in
	 * @param propName name of the relation to create between nodes satisfying conditions and the set
	 * @throws InvalidArguments
	 */
	public JoinSet(Set<Pair<String>> excepts, Set<Pair<String>> wheres, String setName) throws InvalidArguments {
		this(GraGraUtils.STAR,excepts, wheres, setName, "js");
	}
	
	/**
	 * Creates the operator without where and except clauses (will create an edge propName from any
	 * node src to setName)
	 * @param src name of the nodes to put in the set
	 * @param setName name of the set to put nodes in
	 * @param propName name of the relation to create between nodes satisfying conditions and the set
	 * @throws InvalidArguments
	 */
	public JoinSet(String src, String setName, String propName) throws InvalidArguments {
		this(src,new HashSet<Pair<String>>(), new HashSet<Pair<String>>(), setName, propName);
	}

	@Override
	public void execute() {
		
		r.setName("tmpJS");
		//setting set name and propName
		
		
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
		setNodeVal(r.getRight().getNodes(GraGraUtils.TNODE));
		/**
		List<Node> ls = r.getLeft().getNodes(GraGraUtils.TNODE);
		System.out.println(r.getLeft().getNodesList().get(0).getType());
		System.out.println(GraGraUtils.TNODE);
		setNodeVal(r.getLeft().getNodes(GraGraUtils.TNODE));
		GraGraUtils.setAtt(ls.get(1), "att", setName);
		if(!src.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(ls.get(0), "att", src);
		ls = r.getRight().getNodes(GraGraUtils.TNODE);
		GraGraUtils.setAtt(ls.get(1), "att", setName);
		if(!src.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(ls.get(0), "att", src);
		*/
		
		GraGraUtils.setAtt(r.getRight().getArcs(GraGraUtils.TEDGE).get(0), "prop", propName);
		
		// recreating no duplicate NAC
		//easiest after having set the value of X
		
		r.removeNAC(r.getNAC("NoDuplicate"));
		r.createNACDuetoRHS();
		
		/**
		 * Building excepts
		 */
		for(Pair<String> p : excepts) {
			//creating the NAC as No Duplicate and trashing the morphism from L to NAC
			OrdinaryMorphism nac = r.createNAC();
			nac.setName("NAC_" +p.getFirst() + "_"+p.getSecond());
			try {
				//creating nodes
				nac.getTarget().createNode(GraGraUtils.TNODE);
				nac.getTarget().createNode(GraGraUtils.TNODE);
				//creating edge
				nac.getTarget().createArc(GraGraUtils.TEDGE, nac.getTarget().getNodes(GraGraUtils.TNODE).get(0), nac.getTarget().getNodes(GraGraUtils.TNODE).get(1));
				
				
			} catch (TypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//setting xi if not *
			if(!p.getFirst().contentEquals(GraGraUtils.STAR)) 
				GraGraUtils.setAtt(nac.getTarget().getArcsSet().iterator().next(), "prop", p.getFirst());
			for(GraphObject obj : nac.getTarget().getNodesSet()) {				
				//setting map from L to the node with an outArc in NAC
				if(obj.getNumberOfIncomingArcs()==0) nac.addMapping(r.getLeft().getNodes(GraGraUtils.TNODE).get(0), obj);
				//setting XI if not *
				else if(!p.getSecond().contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(obj, "att", p.getSecond());
				else GraGraUtils.setAtt(obj, "att", null);
					
			}
			System.out.println("except " + p.getFirst() + " " + p.getSecond());
			
			
		}
		//removing the pattern NAC
		r.removeNAC(r.getNAC("NJS"));
		
		/**
		 * Building wheres
		 */
		r.removePAC(r.getPAC("PJS"));
		for(Pair<String> p : wheres) {
			//creating the PAC
			OrdinaryMorphism pac = r.createPAC();
			pac.setName("PAC_" +p.getFirst() + "_"+p.getSecond());
		
			try {
				//creating nodes
				pac.getTarget().createNode(GraGraUtils.TNODE);
				pac.getTarget().createNode(GraGraUtils.TNODE);
				//creating edge
				pac.getTarget().createArc(GraGraUtils.TEDGE, pac.getTarget().getNodes(GraGraUtils.TNODE).get(0), pac.getTarget().getNodes(GraGraUtils.TNODE).get(1));
				
				
			} catch (TypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			//setting yi if not *
			if(!p.getFirst().contentEquals(GraGraUtils.STAR))
				GraGraUtils.setAtt(pac.getTarget().getArcs(GraGraUtils.TEDGE).get(0), "prop", p.getFirst());
				
			for(GraphObject obj : pac.getTarget().getNodesSet()) {				
				//setting map from L to the node with an outArc in PAC
				if(obj.getNumberOfIncomingArcs()==0) pac.addMapping(r.getLeft().getNodes(GraGraUtils.TNODE).get(0), obj);
				//setting YI if not *
				else if(!p.getSecond().contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(obj, "att", p.getSecond());
			}

			System.out.println("where " + p.getFirst() + " " + p.getSecond());
			
		}

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
			case "setName":
				if(!setName.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", setName);
				break;
			case "src":
				if(!src.contentEquals(GraGraUtils.STAR)) GraGraUtils.setAtt(n, "att", src);
				break;
			default:
				break;
			}
			
		}
	}

}
