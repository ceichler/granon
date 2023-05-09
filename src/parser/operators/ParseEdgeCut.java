package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.EdgeCut;

public class ParseEdgeCut extends ParseOperator {

	
	/**
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi","O","pf1","interm","pf2"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Str","Str","Str"));
	
	/**
	 * create parser for EdgeCut
	 * @param command
	 */
	
	public ParseEdgeCut(String command) {
		this.command = command;
	}
	
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		System.out.println("\u001B[33m [EdgeCut]  "+mapTokens+"\u001B[0m");
		
		// Neither S nor O can be the target of pi
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		// execute the operator
		checkContrains(mapTokens,listEdge,listDst);
		(new EdgeCut(mapTokens)).execute();

	}

}
