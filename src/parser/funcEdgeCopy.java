package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcEdgeCopy extends funcOperators {

	public funcEdgeCopy(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"pi":[pi],"O":[*,o,O_att],"pf":[pf]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeCopy\\s*\\(\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("pi", Parser.handleSet(matcher.group(2)));
    		    result.put("O", Parser.handleSet(matcher.group(3)));
    		    result.put("pf", Parser.handleSet(matcher.group(4)));
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	// object must have the form (*,o,O)
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("must be (*,o,O_att) !");
    		return null;
    	}
    	// pf copy, cannot be *
    	if (result.get("pf").get(0).equals("*")) {
    		System.err.println("pf cannot be *");
    		return null;
    	}
    	return result;
	}

}
