package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcAnato extends funcOperators {

	public funcAnato(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	String pattern;
		
		// String input = "Anatomization ( {"name"}, {"knows" }, {"livesIn"} )" ;
		// input = scan.nextLine();
		// String pattern = "(Anatomization|Anato)\\s*(\\s*{(.*)}\\s*,\\s*{(.*)}\\s*,\\s*{(.*)}\\s*)" ;
		// String pattern = "[Anatomization|Anato]\\s*\\(\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*\\)";
		if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "[Anatomization|Anato]\\s*\\(\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*\\)";
        	}else {
        		pattern = rePattern;
        	}    	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);
    		if (matcher.find()) {
    			result.put("idn", Parser.handleSet(matcher.group(1)));
    		    result.put("qID", Parser.handleSet(matcher.group(2)));
    		    result.put("sens", Parser.handleSet(matcher.group(3)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// PVP,PVB
    	// version with equal sign character
    	else {
    	}
    	return result;
    	
	}

}
