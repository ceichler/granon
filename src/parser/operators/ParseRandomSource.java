package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.procedures.RandomSrc;

public class ParseRandomSource extends ParseOperatorOpt{

	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","p","O","T"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Set"));
	
	/**
	 * create parser for RandomSource
	 * @param command user's command
	 */
	
	public ParseRandomSource(String command) {
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
		
		
		System.out.println("\u001B[33m [RandomSource]  "+mapTokens+"\u001B[0m");
		
		// execute the operator
		(new RandomSrc(mapTokens)).execute();
		
	}

}
