package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * class for analyzing the RandomSource command
 * 
 * @author 
 *
 */


public class funcRandomSource extends funcOperators {
	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"T":[*,t,T_att]}
	// Syntax:  RandomSource(S, p, O, T )
    // e.g		RandomSource((*,"type","City"),"inGroup",(*,null,null),(*,"type","City"))
	
	/**
	 * create the object for analyzing RandomSource command with specific command
	 * @param command
	 */
	public funcRandomSource(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	/**
	 * extracts the information in the command of the form "RandomSource(S, p, O, T )" 
	 * 
	 * @param rePattern a regular expression pattern to match and extract substrings from the command
	 * @returns a HashMap of the form 
	 * 			- syntax: {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"T":[*,t,T_att]}
	 * 			- Example: 
	 *
	 */
	
	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"T":[*,t,T_att]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "RandomSource\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,\\s*\\((.*)\\)\\s*\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("p", Parser.handleSet(matcher.group(2)));
    		    result.put("O", Parser.handleSet(matcher.group(3)));
    		    result.put("T", Parser.handleSet(matcher.group(4)));
    		    // System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    		result = Parser.handleKeywordArgs(command);
    	}
    	// built-in function contains only o and O_att so the required syntax is of the form (*,o,O)
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("O must be (*,o,O_att) !");
    		return null;
    	}
    	if (!result.get("T").get(0).equals("*")) {
    		System.err.println("T must be (*,t,T_att) !");
    		return null;
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	return result;
	}

}
