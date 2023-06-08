package transformations.operators;

import java.util.ArrayList;
import java.util.HashMap;

import executable.granonui.Tui;
import utils.GraGraUtils;
import utils.Grammar;
import utils.Report;

/**
 * Operator for node creation
 * @author ceichler
 *
 */
public class NewNode extends Operator {
	
	/**
	 * Argument of operator; attribute of the node to be created
	 */
	private String arg;

	/**
	 * Initializing a new node 
	 * @param arg the attribute of the node being created
	 */
	public NewNode(String arg) {
		r = Grammar.newNode.getClone();
		 this.arg = arg;
		
	}
	
	public NewNode(HashMap<String,String> map) {
		this(map.get("X"));
	}

	@Override
	public void execute() {
		//cloning and seting setName
		this.r.setName("tmpAddNode");
		//set X 
		GraGraUtils.setAtt(r.getElementsToCreate().get(0), "att", arg);
		
		//transforming
		GraGraUtils.transformAll(new Report(), r, Tui.grammar);
		
	}

}
