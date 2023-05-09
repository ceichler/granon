package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.ModifyEdge;

public class ParseModifyEdge extends ParseOperator{

	
	/**
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","O","pi","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","Set","*","Str"));
	
	/**
	 * create parser for ModifyEdge
	 * @param command
	 */
	
	public ParseModifyEdge(String command) {
		this.command = command;
	}
	
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		
		System.out.println("\u001B[33m [ModifyEdge]  "+mapTokens+"\u001B[0m");
		
		
		ArrayList<String> listEdge = new ArrayList<String>(Arrays.asList("pi"));
		ArrayList<String> listDst = new ArrayList<String>(Arrays.asList("S","O"));
		
		// Neither S nor O can be the target of pi
		checkContrains(mapTokens,listEdge,listDst);
		
		// execute the operator
		(new ModifyEdge(mapTokens)).execute();
		
	}

}
