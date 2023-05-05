package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.exceptions.SyntaxException;
import transformations.operators.JoinSet;
import transformations.operators.NewNode;

public class ParseJoinSet extends ParseOperator{
	/**
	 * create parser for JoinSet
	 * @param command
	 */
	public ParseJoinSet(String command) {
		this.command = command;
	}

	
	/**
	 * Extract all argument from user command
	 * @return a HashMap contains all arguments
	 */
	public HashMap<String,ArrayList<String>> getTokensPosArg(){
		HashMap<String,ArrayList<String>> mapTokens = new HashMap<String,ArrayList<String>>();
		String regex = "JoinSet\\s*\\((.+?)\\)";      
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        
        // this is the code for creating first part of HashMap {JoinSet=[x,X]}
        if (matcher.find()) {	   
        	// System.out.println(matcher.group(1));
            String[] afterSplit = matcher.group(1).split(",");
            for (int i =0; i<afterSplit.length;i++) {
            	afterSplit[i] = afterSplit[i].replace("\"", "").trim();     
            }
            mapTokens.put("JoinSet", new ArrayList<String>(Arrays.asList(afterSplit)));
        }
        // this is the code to add the rest of HashMap: {where=[X1,X2,X3,...] where = [Y1,Y2,Y3,...]}
		regex = "where\\s*\\{(\\(.*?\\))\\}|except\\s*\\{(\\(.*?\\))\\}";      
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(command);
        
        while (matcher.find()) {
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		ArrayList<String> listCondition = new ArrayList<String>();
                if (matcher.group(i)==null) {continue;}
                // System.out.println("Group matcher " + matcher.group(i));
                String[] elements = matcher.group(i).split(",");
                for (int j=0;j<elements.length;j++) {
                	elements[j] = elements[j].replace("\"","").replace("(", "").replace(")", "");
                	listCondition.add(elements[j]);
                }
                // matcher.group(0).split("[^a-zA-Z]+")[0]) for getting the where or except keyword to make is the key of hashmap
                // listCondition is an ArrayList contains the PAC or NAC
                mapTokens.put(matcher.group(0).split("[^a-zA-Z]+")[0], listCondition);
        	}
        }
		
		return mapTokens;
	
	}
	
	/**
	 * Check syntax of the current JoinSet command
	 * @param mapTokens
	 * @throws SyntaxException
	 */
	public void checkSyntax(HashMap<String,ArrayList<String>> mapTokens) throws SyntaxException{
		HashMap<String,ArrayList<String>> localMapTokens = new HashMap<String,ArrayList<String>>();
		localMapTokens.putAll(mapTokens);
		
		for (String joinStr:localMapTokens.get("JoinSet")) {
			if (joinStr.equals("null") || joinStr.equals("*")) {
				throw new SyntaxException("[JoinSet] must be JoinSet(\"String\",\"String\")");
			}
		}
		
		String[] strs = new String[] {"where","except"};
		
		for (String str:strs) {
			for (int i = 0; i<localMapTokens.get(str).size();i+=3) {
				ArrayList<String> setForCheck = new ArrayList<String>(Arrays.asList(
								localMapTokens.get(str).get(i),
								localMapTokens.get(str).get(i+1),
								localMapTokens.get(str).get(i+2)));
				checkSet("Set",setForCheck);
			}
		}
	}
	@Override
	public void execute() throws SyntaxException {
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg();
		System.out.println();
		checkSyntax(mapTokens);
		(new JoinSet(mapTokens)).execute();
		
	}

}
