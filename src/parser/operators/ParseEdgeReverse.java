package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.EdgeReverse;

public class ParseEdgeReverse extends ParseOperatorOpt{

	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","p","O"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set"));
	
	/**
	 * create parser for EdgeReverse
	 * @param command user's command
	 */
	public ParseEdgeReverse(String command) {
		super(command);
	}
	
	
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getArgs(listArgKeywords, parameterRequiredForm);
		
		
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("p"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		// Neither S nor O can be the target of pi
		checkContrains(mapTokens,listEdge,listDst);
		
		// execute the operator
		(new EdgeReverse(mapTokens)).execute();
		
		
	}


}
