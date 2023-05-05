package transformations.operators;


import java.util.ArrayList;
import java.util.HashMap;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for node deletion, deletes all nodes attributed "arg" s.t. arg--s-->S
 * arg, s, and S can be star
 * (s,S) can be (null,null)
 * @author ceichler
 *
 */
public class DeleteNode extends Operator {
	

	/**
	 * Initializing the operator
	 * @param arg the attribute of the node(s) to be deleted
	 */
	public DeleteNode(String arg) {
		this(arg, null, null);
		
	}
	
	/**
	 * Initializing the operator
	 * @param arg the attribute of the node(s) to be deleted
	 * @param S node in relation with the node(s) to be deleted
	 * @param s relation between arg and S
	 */
	public DeleteNode(String arg, String s, String S) {
		r = Grammar.deleteNode.getClone();
		map.put("X", arg);
		map.put("s", s);
		map.put("S", S);
	}
	
	/**
	 * Initializing the operator
	 * @param map contains all needed parameters
	 */
	public DeleteNode(HashMap<String,ArrayList<String>> mapTokens) {
		r = Grammar.deleteNode.getClone();
		map.put("X", mapTokens.get("X").get(0));
		map.put("s", mapTokens.get("X").get(1));
		map.put("S", mapTokens.get("X").get(2));
	}

	@Override
	public void execute() {
		//cloning and seting setName
		this.r.setName("tmpDelNode");
		//set X
		setNodeValue(r.getLeft().getNodes(GraGraUtils.TNODE));
		//if both s and S are null no PAC for source
		
		handlePAC(map.get("s"), map.get("S"), "Set");
		//transforming

		GraGraUtils.transformAll(new Report(), r, Tui.grammar);	
	}

}
