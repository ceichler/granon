package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcCloneSet extends funcOperators {

	public funcCloneSet(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"c":[c],"C_att":[C_att]} 
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
    	// PVP,PVB
    	// version with equal sign character
    	else {
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("O must be (*,o,O_att) !");
    		return null;
    	}
    	return result;
	}

}
