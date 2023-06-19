package parser;

import java.lang.reflect.Constructor;
import java.util.*;

import parser.exceptions.SyntaxException;
import parser.operators.*;

/**
 * This program offers a parser capable of reading, parsing, and executing user commands on a graph database. <br>
 * To input a user command, use the syntax Parser.command = [user command], and execute it by invoking the command Parser.execute()
 * @author khai
 *
 */

public class Parser {

	/*
	 * the command that user want to execute on the database
	 */
	public static String command;
	
	/**
	 * list of operators provided by the language
	 */
	public static final ArrayList<String> listOperators = new ArrayList<String>(Arrays.asList(
			
				// operators
				"NewNode",
				"DeleteNode",
				"EdgeReverse",
				"EdgeCut",
				"EdgeCopy",
				"EdgeChord",
				"EdgeChordKeep",
				"ModifyEdge",
				"RandomTransformSource",
				"RandomTransformTarget",
				"CloneSet",
				"JoinSet",
				
				
				//procedures
				"RandomSource",
				"RandomTarget",
				"LDP",
				"Anatomization"
			
			));

	/**
	 * get the operator name from the user's command
	 * @return operator's name
	 * @throws SyntaxException
	 */
	public static String getOperator() throws SyntaxException {
		String[] operator;
        int index = command.indexOf("(");
        if (index >= 0) {        	
            operator = new String[1];
            operator[0] = command.substring(0, index).trim() ;
        } else {
        	operator = command.split("\\s+");
        }
    	 	
        if (operator.length > 0) {
        	operator[0] = operator[0].trim();
        	if (!listOperators.contains(operator[0])) {
        		throw new SyntaxException(operator[0] + " does not exist");
        	}
        	return operator[0];
        }
        throw new SyntaxException("Cannot get operator from command");
	}
	
	/**
	 * execute the user's command on the database
	 * 
	 */
	public static void execute(){
		
		String operator = null;
		try {
			operator = getOperator();
		} catch (SyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		

		// create the className of the operator
		String className = "parser.operators.Parse"+operator;
		
		
		try {
			// create the operator
		    Class<?> myClass = Class.forName(className);
		    Constructor<?> constructor = myClass.getConstructor(String.class);
		    Object myObj = constructor.newInstance(command);
		    // Use the object
		    if (myObj instanceof ParseOperator) {
		    	// execute the operator
		        ((ParseOperator) myObj).execute();
		    }
		} catch (Exception e) {
		    // Handle any exceptions
		    e.printStackTrace();
		}


		
	}
	
	public static void executeGui() throws Exception{
		
		String operator = getOperator();


		// create the className of the operator
		String className = "parser.operators.Parse"+operator;
		
		// create the operator
	    Class<?> myClass = Class.forName(className);
	    Constructor<?> constructor = myClass.getConstructor(String.class);
	    Object myObj = constructor.newInstance(command);
	    // Use the object
	    if (myObj instanceof ParseOperator) {
	    	// execute the operator
	        ((ParseOperator) myObj).execute();
	    }
	}
	
}
