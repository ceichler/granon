package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.operators.CloneSet;

public class ParseCloneSet extends ParseOperatorOpt{

	/**
	 * list of keys for parsing
	 */
	protected ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","c","C_att"));
	/**
	 * the syntactic constraints
	 */
	protected ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","Str","Str"));
	
	/**
	 * create parser for CloneSet
	 * @param command
	 */
	
	public ParseCloneSet(String command) {
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
		
		System.out.println("\u001B[33m [CloneSet]  "+mapTokens+"\u001B[0m");
		
		// execute the operator
		(new CloneSet(mapTokens)).execute();
		
	}

}
