package parser.operators;

import java.util.*;

import parser.exceptions.SyntaxException;
import transformations.operators.NewNode;

public class ParseNewNode extends ParseOperator{
	
	/**
	 * list of keywords that represent the required arguments in operator
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("X"));
	
	/**
	 * the syntactic constraints
	 * 
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("Str"));
	
	/**
	 * create the parser for NewNode
	 * @param command user's command
	 */
	public ParseNewNode(String command) {
		super(command);
	}

	
	
	@Override
	public void execute() throws SyntaxException {
		
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		

		String newNodeAtt = mapTokens.get("X").get(0);

		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);

		System.out.println("\u001B[33m [NewNode]  "+mapTokens+"\u001B[0m");
		
		// execute the operator
		(new NewNode(newNodeAtt)).execute();
		
	}

}
