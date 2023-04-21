package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * class to parse the command operator NewNode
 * @author khai
 *
 */

public class funcNewNode extends funcOperators{
	
	/**
	 * create the object with specific command to parse
	 * @param command
	 */
	public funcNewNode(String command) {
		super(command);
	}
	
	/**
	 * analyzing the {@link #command} then return the token
	 * @param rePattern custom regex for analyzing the command. (null by defaut)
	 * @return a HashMap contains all the tokens for executing the operator
	 * 			- return syntax: {"X"=[att_of_new_node]}
	 * 			- Example:  {"X"=["NewNode"]}
	 */
	@Override
	public HashMap<String,ArrayList<String>> getToken(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	
    	// Version with equals sign  character
    	if (!command.contains("=")) { 
   
	    	if (rePattern == null) {
	        	if (command.contains("(") || command.contains(")")) {
	        		pattern =  "NewNode\\s*\\(\"(.*)\"\\)";
	        		
	        	}else {
	        		pattern =  "NewNode\\s*\"(.*)\"";
	        	}
	    	}else {
	    		pattern = rePattern;
	    	}    	
	    	
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(command);
			
			if (matcher.find()) {
			    // result[0] = matcher.group(1).trim();
			    result.put("X", Parser.handleSet(matcher.group(1).trim()));
			} else {
				System.err.println("Syntax error!");
			}
		
    	}
    	// version with equals sign
    	else {
    		result = Parser.handleKeywordArgs(command);

    	}
    	
    	if (result.get("X").get(0).equals("null")) {
    		System.err.println("Invalid command\n Node's attr cannot be \"null\"");
    		return null;
    	}
    	
    	return result;
	
    	
    }
}
