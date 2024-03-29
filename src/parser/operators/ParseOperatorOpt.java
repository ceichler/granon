package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.exceptions.SyntaxException;

/**
 * This class contains methods to parse commands of the form keyword arguments
 * @author khai
 *
 */

public abstract class ParseOperatorOpt extends ParseOperator{
		
	/**
	 * the regex pattern for extracting tokens (arguments) from the keyword arguments of user's command
	 */
	final String regex = "(\\w+)\\s*=\\s*(.*?)\\s*(?:(?=\\w+\\s*=)|\\)$)";
	
	
	/**
	 * create the Parser for this operator with user's command
	 */
	public ParseOperatorOpt(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Extracts keyword arguments from the user's command and returns a HashMap containing the parameter key-value pairs. (required to pass all arguments)
	 * 
	 * @param comd a string containing keyword arguments in the form "key=value"
	 * @return a HashMap where the keys are the parameter names (keywords) extracted from the string and the values are ArrayLists of parameters associated with those keywords
	*/
	
	public HashMap<String,ArrayList<String>> handleKeywordArgs(String comd) {
		HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
		// comd = comd.replace(" ", "");
		/**
		 * match all strings of the form "keyword = value"
		 */
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(comd);


        // Store the parameter key-value pairs in a HashMap
        while (matcher.find()) {
            result.put(matcher.group(1),handleSet(matcher.group(2)));
        }

		return result;
	}
	
	
	/**
	 * Extract all the arguments from a Set - E.g: (a,b,c) --> [a,b,c] (ArrayList)
	 * @param paramsStr the String represent a Set
	 * @return an ArrayList represents a Set
	 */
	public ArrayList<String> handleSet(String paramsStr){
		
		paramsStr = paramsStr.replace("(", "").replace(")", "").replace("\"", "");
        String[] afterSplit = paramsStr.split(","); 
        // trim all elements in set
        for (int compt = 0; compt<afterSplit.length;compt++) {
        	afterSplit[compt] = afterSplit[compt].trim();
        }
        
        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(afterSplit));
        
        for (int i = 0;i < temp.size();i++) {
        	if (temp.get(i).equals("null")) {
        		temp.set(i, null);
        	}
        }
		return temp;
	}	

	
	
	/**
	 * Extracts keyword arguments from a string and returns a HashMap containing the parameter key-value pairs. (just pass the required arguments) 
	 * 
	 * @param comd a string containing keyword arguments in the form "key=value"
	 * @return a HashMap where the keys are the parameter names (keywords) extracted from the string and the values are ArrayLists of parameters associated with those keywords
	 * @throws SyntaxException 
	*/
	public HashMap<String,ArrayList<String>> getKeywordArgs(String comd, ArrayList<String> listArgKeywords, ArrayList<String> parameterRequiredForm) throws SyntaxException {
		HashMap<String,ArrayList<String>> result = handleKeywordArgs(comd);
		
		for (String keyword:result.keySet()) {
			if (!listArgKeywords.contains(keyword)) {
				String syntax = this.getClass().getSimpleName().replace("Parse", "")+"(";
				for (String key:listArgKeywords) {
					syntax = syntax + key + ",";
				}
				syntax = syntax.substring(0, syntax.length()-1);
				syntax = syntax + ")";
				throw new SyntaxException(keyword + " is not the keywork for this operator [Syntax] "+syntax);
			}
			if (result.get(keyword).get(0).equals("")) {
				throw new SyntaxException(keyword + " has no value");
			}
		}
		
		for (String keyword:listArgKeywords) {
			if (result.keySet().contains(keyword)) {continue;}
			
			String keyForm = parameterRequiredForm.get(listArgKeywords.indexOf(keyword));
			
			if (keyForm.equals("Str")) {
				throw new SyntaxException("["+keyword+"] required argument");
			} else if (keyForm.equals("*")) {
				result.put(keyword, new ArrayList<String>(Arrays.asList("*")));
			} else if (keyForm.equals("null")) {
				ArrayList<String> nullArg = new ArrayList<>();
				nullArg.add(null);
				result.put(keyword, nullArg);
			} else if (keyForm.equals("S") || keyForm.equals("Set")) {
				result.put(keyword, new ArrayList<String>(Arrays.asList("*",null,null)));
			}  else if (keyForm.equals("Num")) {
				throw new SyntaxException("["+keyword+"] required argument");
			} 
			
		}
		
		return result;
	}
	
	public HashMap<String,ArrayList<String>> getArgs(ArrayList<String> listArgKeywords, ArrayList<String> parameterRequiredForm) throws SyntaxException{
		HashMap<String,ArrayList<String>> mapTokens;
		
		if (!command.contains("=")) {
			mapTokens = this.getTokensPosArg(listArgKeywords);
		}else {
			mapTokens = this.getKeywordArgs(command,listArgKeywords,parameterRequiredForm);
		}
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		
		String op = this.getClass().getSimpleName().replace("Parse", "");
		System.out.println("\u001B[33m  ["+ op +"] "+mapTokens+"\u001B[0m");
		return mapTokens;
				
	}
}
