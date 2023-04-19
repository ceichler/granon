package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcEdgeCut extends funcOperators {

	public funcEdgeCut(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"pi":[pi],"M_att":[M_att],"po":[po]} 
    	// prepare for necessary upgrades when there is an equal sign character in request

    	// Version with equals sign  character
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeCut\\s*\\(\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)" ;
        		// \\s* to ignore spaces
        		// without \\s*  ""
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
    		    result.put("pi", Parser.handleSet(matcher.group(4)));
    		    result.put("M_att", Parser.handleSet(matcher.group(5)));
    		    result.put("po", Parser.handleSet(matcher.group(6)));
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g EdgeCut (S=("id110",*,*),p="livesIn",O=(*,"type","City"),pi="inIntem",M_att="intem",po="outIntem")
    	// The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
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
    	return result;
	}

}
