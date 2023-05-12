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
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi","O","T","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Set","Str"));
	
	/**
	 * create parser for ParseRandomTransformTarget
	 * @param command
	 */
	
	public ParseRandomTransformTarget(String command) {
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
		
		
		System.out.println("\u001B[33m [RandomTransformTarget]  "+mapTokens+"\u001B[0m");
		
		// Neither S nor O can be the target of pi
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		// execute the operator
		checkContrains(mapTokens,listEdge,listDst);
		(new RandomTransformTar(mapTokens)).execute();
		
	}
}
