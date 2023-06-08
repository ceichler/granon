package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.exceptions.SyntaxException;
import transformations.operators.CloneSet;

public class ParseCloneSet extends ParseOperatorOpt{

	/**
	 * list of keywords that represent the required arguments in operator
	 */
	protected ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","c","C_att"));
	/**
	 * the syntactic constraints
	 */
	protected ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","Str","Str"));
	
	/**
	 * create parser for CloneSet
	 * @param command user's command
	 */
	
	public ParseCloneSet(String command) {
		super(command);
	}
	
	
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getArgs(listArgKeywords, parameterRequiredForm);
		
		// execute the operator
		(new CloneSet(mapTokens)).execute();
		
	}

}
