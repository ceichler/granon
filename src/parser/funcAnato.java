package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for analyzing the Anatomization command
 * @author khai
 *
 */

public class funcAnato extends funcOperators {
	
	/**
	 * create the object for analyzing Anatomization command with the provided function
	 * @param command
	 */
	public funcAnato(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * analyzing the {@link #command} then return the token
	 * @param rePattern custom regex for analyzing the command. (null by defaut)
	 * @return a HashMap contains all the tokens for executing the operator
	 * 			- return syntax: {"idn"=[e1,e2,e3,...], "sens"=[p1,p2,p3,...], "qID"=[q1,q2,q3,...]}
	 * 			- Example:	{ "idn"=["name"], "qID"=["knows" ], "sens"=["livesIn"] }
	 * 
	 */

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	String pattern;
    	/**
    	 * if there is no equal oil we are using "positional arguments"
    	 */
		if (!command.contains("=")) {
    		if (rePattern == null) { 
    			/**
    			 * This pattern is used to match commands of the form Anatomization ( {e1,e2,...,en}, {q1,q2,...,qm}, {p1,p2,...,pk} )
    			 */
        		pattern =  "[Anatomization|Anato]\\s*\\(\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*\\)";
        	}else {
        		pattern = rePattern;
        	}    	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);
    		if (matcher.find()) {
    			/**
    			 * create a HashMap result to contain the matched tokens
    			 */
    			result.put("idn", Parser.handleSet(matcher.group(1)));
    		    result.put("qID", Parser.handleSet(matcher.group(2)));
    		    result.put("sens", Parser.handleSet(matcher.group(3)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	else {
    		/**
    		 * remove existing curly braces in tokens
    		 */
    		command = command.replace("{", "").replace("}", "");
    		result = Parser.handleKeywordArgs(command);
    	}
    	return result;
    	
	}

}
