package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.exceptions.SyntaxException;
import transformations.procedures.Anatomization;

public class ParseAnatomization extends ParseOperator{
	
	public ParseAnatomization(String command) {
		this.command = command;
	}

	
	/**
	 * extract the token from the user's command
	 * @return HashMap contains parameter and corresponding arguments of user's command
	 */
	public HashMap<String,ArrayList<String>> getTokensPosArg(){
		HashMap<String,ArrayList<String>> mapTokens = new HashMap<String,ArrayList<String>>();
		
		String pattern =  "Anatomization\\s*\\(\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*,\\s*\\{(.*)\\}\\s*\\)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(command);
		if (matcher.find()) {
			/**
			 * create a HashMap result to contain the matched tokens
			 */
			String[] keys = {"idn","qID","sens"};
			int count = 0;
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String[] after_split = matcher.group(i).split(",");
				for (int j=0;j<after_split.length;j++) {
					after_split[j] = after_split[j].replace(" ", "").replace("\"", "").replace("{","").replace("}", "");
				}
				ArrayList<String> element = new ArrayList<String>(Arrays.asList(after_split));
				mapTokens.put(keys[count], element);
				count++;
			}
		} 
		return mapTokens;
	}
	
	public void checkSyntax(HashMap<String,ArrayList<String>> mapTokens) throws SyntaxException{
		HashMap<String, ArrayList<String>> localMap = new HashMap<String, ArrayList<String>>();
		localMap.putAll(mapTokens);
		
		for(String key:localMap.keySet()) {
			for (String val:localMap.get(key)) {
				if (val.equals("*") || val.equals("null")) {
					throw new SyntaxException(key + " cannot contain neither * nor null");
				}
			}
		}
		
	}
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg();
		this.checkSyntax(mapTokens);
		
		// execute the operator
		(new Anatomization(mapTokens)).execute();
		
	}

}
