package transformations.operators;


import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Creates a "clone" node for all nodes in a set (i.e. forall x s.t. x--s-->S, creates x---c--->C
 * @author ceichler
 *
 */
public class CloneSet extends Operator {
	
	
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
		map.put("s", s);
		map.put("src", src);
		map.put("S", S);
		map.put("c", c);
		map.put("C", C);
		r = Grammar.cloneSet.getClone();

	}

	@Override
	public void execute() {

		r.setName("tmpCS");

		//if both s and S are null no PAC for source
		handlePAC(map.get("s"), map.get("S"), "SourceSet");
				
		//setting att values for R and L
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
		setNodeValue(r.getRight().getNodes(GraGraUtils.TNODE));
		setArcValue(r.getRight().getArcs(GraGraUtils.TEDGE));
		
		
		//recreating the nac
		r.removeNAC(r.getNAC("NoDuplicate"));
		r.createNACDuetoRHS();


		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);

	}
	
	

}
