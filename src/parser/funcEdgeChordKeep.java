package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcEdgeChordKeep extends funcOperators {

	public funcEdgeChordKeep(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"pf":[pf]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeChordKeep\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", Parser.handleSet(matcher.group(1)));
    		    result.put("pi", Parser.handleSet(matcher.group(2)));
    		    result.put("M", Parser.handleSet(matcher.group(3)));
    		    result.put("po", Parser.handleSet(matcher.group(4)));
    		    result.put("O", Parser.handleSet(matcher.group(5)));
    		    result.put("pf", Parser.handleSet(matcher.group(6)));
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
    	
    	if (!result.get("O").get(0).equals("*")) {
    		System.err.println("must be (*,o,O_att) !");
    		return null;
    	}
    	if (result.get("pf").get(0).equals("*")) {
    		System.err.println("pf cannot be *");
    		return null;
    	}
    	return result;
	}

}
