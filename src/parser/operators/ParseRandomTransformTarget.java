package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.RandomTransformTar;

public class ParseRandomTransformTarget extends ParseOperatorOpt{
	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi","O","T","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Set","Str"));
	
	/**
	 * create parser for ParseRandomTransformTarget
	 * @param command user's command
	 */
	
	public ParseRandomTransformTarget(String command) {
		super(command);
	}

	
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getArgs(listArgKeywords, parameterRequiredForm);
		
		// Neither S nor O can be the target of pi
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		// execute the operator
		checkContrains(mapTokens,listEdge,listDst);
		(new RandomTransformTar(mapTokens)).execute();
		
	}
}
