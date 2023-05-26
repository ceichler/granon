package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.exceptions.SyntaxException;
import transformations.procedures.Anatomization;

public class ParseAnatomization extends ParseOperatorOpt{
	
	public ParseAnatomization(String command) {
		super(command);
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
		if (matcher.find()) {ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","Str","Str"));
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
	
	/**
	 * verify the syntax of this command
	 * @param mapTokens HashMap that contains all need information extracted from user's command
	 * @throws SyntaxException
	 */
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
		HashMap<String,ArrayList<String>> mapTokens;
		
		if (!command.contains("=")) {
			mapTokens = this.getTokensPosArg();
		}else {
			ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("idn","qID","sens"));
			ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("Str","Str","Str"));
			mapTokens = this.getKeywordArgs(command,listArgKeywords,parameterRequiredForm);
			
			for (String key : mapTokens.keySet()) {
				for (int i=0;i<mapTokens.get(key).size();i++) {
					mapTokens.get(key).set(i,mapTokens.get(key).get(i).replace("{","").replace("}",""));		 
				}
			}
			
			System.out.println(mapTokens);
		}
		this.checkSyntax(mapTokens);
		
		System.out.println("\u001B[33m [Anatomization]  "+mapTokens+"\u001B[0m");
		
		
		// execute the operator
		(new Anatomization(mapTokens)).execute();
		
	}

}
