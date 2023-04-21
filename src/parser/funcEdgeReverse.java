package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for analyzing the EdgeReverse command
 * 
 * @author 
 *
 */


public class funcEdgeReverse extends funcOperators{
	
	
	/**
	 * create the object with specific command to parse
	 * @param command 
	 */
	
	public funcEdgeReverse(String command) {
		super(command);
	}
	
	/**
	 * analyzing the {@link #command} then return the tokens
	 * @param rePattern custom regex for analyzing the command. (null by defaut)
	 * @return a HashMap contains all the tokens for executing the operator
	 * 			- return syntax: {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att]} 
	 * 			- Example:	
	 */

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att]} 
    	// prepare for necessary upgrades when there is an equal sign character in request
  	
    	// Version with equals sign  character
    	// e.g EdgeReverse((*,"type","Person"),"livesIn",(*,"inGroup","France"))
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeReverse\\s*\\(\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*\\((.*)\\)\\s*\\)";
        		// \\s* to ignore spaces
        		// without \\s*  "EdgeReverse\\(\\((.*)\\),(.*),\\((.*)\\)\)"
        		// (.*) to match the necessary elements (this this case we have 3 elements S,p and O)
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {   
    		    result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("p", Parser.handleSet(matcher.group(2)));
    		    result.put("O", Parser.handleSet(matcher.group(3)));
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g DeleteNode(X = ("id105",*,*))
    	// The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    	else {
    		result = Parser.handleKeywordArgs(command);
    	}
    	
    	// built-in function contains only o and O_att so the required syntax is of the form (*,o,O)
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("must be (*,o,O_att) !");
    		return null;
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	return result;
	}

}
