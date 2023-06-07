package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.EdgeChord;

public class ParseEdgeChord extends ParseOperatorOpt {

	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi1","I","pi2","O","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","*","Set","Str"));
	
	/**
	 * create parser for EdgeChord
	 * @param command user's command
	 */
	
	public ParseEdgeChord(String command) {
		super(command);
	}
	
	
	
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getArgs(listArgKeywords, parameterRequiredForm);
		
		// Neither S nor O nor I can be the target of pi1 or pi2
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi1","pi2"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O","I"));
		
		// execute the operator
		checkContrains(mapTokens,listEdge,listDst);
		(new EdgeChord(mapTokens)).execute();

	}

}
