package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.procedures.RandomTar;

public class ParseRandomTarget extends ParseOperator{

	/**
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","p","O","T"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Set"));
	
	/**
	 * create parser for RandomTarget
	 * @param command
	 */
	
	public ParseRandomTarget(String command) {
		this.command = command;
	}
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		System.out.println("\u001B[33m [RandomTarget]  "+mapTokens+"\u001B[0m");
		
		
		// execute the operator
		(new RandomTar(mapTokens)).execute();
		
	}
}
