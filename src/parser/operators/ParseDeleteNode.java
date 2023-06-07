package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.operators.DeleteNode;
import transformations.operators.NewNode;

public class ParseDeleteNode extends ParseOperatorOpt{
	/**
	 * list of keywords that represent the required arguments in operator
	 */
	private ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("X"));
	/**
	 * the syntactic constraints
	 */
	private ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S"));
	
	/**
	 * create parser for DeleteNode
	 * @param command user's command
	 */
	public ParseDeleteNode(String command) {
		super(command);
	}
	
	
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getArgs(listArgKeywords, parameterRequiredForm);
		
		// execute the operator
		(new DeleteNode(mapTokens)).execute();
		
	}

}
