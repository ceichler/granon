package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcLDP extends funcOperators {

	public funcLDP(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O],"k":[k]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "LDP\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		
    		if (matcher.find()) {
    			result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("p", Parser.handleSet(matcher.group(2)));
    		    result.put("O", Parser.handleSet(matcher.group(3)));
    		    result.put("k", Parser.handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// PVP,PVB
    	// version with equal sign character
    	else {
    		result = Parser.handleKeywordArgs(command);
    	}
    	// Replace all String "null" by null
    	for (String i : result.keySet()) {
    		result.get(i).replaceAll(s -> s.equals("null") ? null : s);
    	}
    	
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("must be (*,o,O_att) !");
    		return null;
    	}
    	return result;
	}

}
