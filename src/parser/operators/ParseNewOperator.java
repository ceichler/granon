package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;

import parser.exceptions.SyntaxException;

public class ParseNewOperator extends ParseOperatorOpt{

	/**
	 * list of keywords that represent the required arguments in operator
	 */
	protected ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","p1","I","p2","O"));
	/**
	 * the syntactic constraints
	 */
	protected ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Str","Set"));
	
	
	public ParseNewOperator(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws SyntaxException {
		// TODO Auto-generated method stub
		
	}

}
