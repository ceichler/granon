package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcDeleteNode extends funcOperators{

	public funcDeleteNode(String command) {
		super(command);
	}
	
	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att]}
    	String temp;
    	
    	
    	// Version without equals sign  character
    	// e.g DeleteNode(("id105",*,*))
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "DeleteNode\\s*\\((.*)\\)";
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    temp = matcher.group(1);
    		    result.put("S", Parser.handleSet(temp));
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g DeleteNode(X = ("id105",*,*))
    	else {
    		result = Parser.handleKeywordArgs(command);
    	}
    	
    	for (int i=0;i<3;i++) {
    		if (result.get("S").get(i).equals("null")) {
    			result.get("S").set(i, null);
    		}
    	}
    	return result;
	}

}
