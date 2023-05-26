package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.operators.DeleteNode;
import transformations.operators.NewNode;

public class ParseDeleteNode extends ParseOperatorOpt{
	/**
	 * list of keys for parsing
	 */
	private ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("X"));
	/**
	 * the syntactic constraints
	 */
	private ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S"));
	
	/**
	 * create parser for DeleteNode
	 * @param command
	 */
	public ParseDeleteNode(String command) {
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
		
		

		System.out.println("\u001B[33m  [DeleteNode] "+mapTokens+"\u001B[0m");
		
		// execute the operator
		(new DeleteNode(mapTokens)).execute();
		
	}

}