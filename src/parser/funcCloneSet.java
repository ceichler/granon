package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * class to parse the command operator CloneSet
 * @author khai
 *
 */

public class funcCloneSet extends funcOperators {

	/**
	 * create the object with specific command to parse
	 * @param command
	 */
	
	public funcCloneSet(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * analyzing the {@link #command} then return the token
	 * @param rePattern custom regex for analyzing the command. (null by defaut)
	 * @return a HashMap contains all the tokens for executing the operator
	 * 			- return syntax: {"S"=[x,s,S_att],"c"=[c],"C_att"=[C_att]}
	 * 			- Example:	{"S"=["*", "type", "city"], "c"=["clone"], "C_att"=["Clone"]}
	 */
	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();

    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "CloneSet\\s*\\(\\s*\\((.*)\\)\\s*,(.*),(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    			result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("c", Parser.handleSet(matcher.group(2)));
    		    result.put("C_att", Parser.handleSet(matcher.group(3)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    		result = Parser.handleKeywordArgs(command);
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	
    	return result;
	}

}
