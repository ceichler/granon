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
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi1","I","pi2","O","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","*","Set","Str"));
	
	/**
	 * create parser for EdgeChord
	 * @param command
	 */
	
	public ParseEdgeChord(String command) {
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
		
		
		System.out.println("\u001B[33m [EdgeChord]  "+mapTokens+"\u001B[0m");
		
		// Neither S nor O nor I can be the target of pi1 or pi2
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi1","pi2"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O","I"));
		
		// execute the operator
		checkContrains(mapTokens,listEdge,listDst);
		(new EdgeChord(mapTokens)).execute();

	}

}
