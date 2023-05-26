package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.RandomTransformSrc;

public class ParseRandomTransformSource extends ParseOperatorOpt{

	
	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi","O","T","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Set","Str"));
	
	/**
	 * create parser for ParseRandomTransformSource
	 * @param command user's command
	 */
	
	public ParseRandomTransformSource(String command) {
		super(command);
	}
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens;
		
		if (!command.contains("=")) {
			mapTokens = this.getTokensPosArg(listArgKeywords);
		}else {
			mapTokens = this.getKeywordArgs(command,listArgKeywords,parameterRequiredForm);
		}
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		System.out.println("\u001B[33m [RandomTransformSource]  "+mapTokens+"\u001B[0m");
		
		// Neither S nor O can be the target of pi
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		checkContrains(mapTokens,listEdge,listDst);
		// execute the operator
		(new RandomTransformSrc(mapTokens)).execute();
		
	}

}
