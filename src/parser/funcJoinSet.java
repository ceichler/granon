package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for analyzing the JoinSet command
 * 
 * @author 
 *
 */

public class funcJoinSet extends funcOperators {
	
	/**
	 * create the object with specific command to parse
	 * @param command 
	 */
	
	
	public funcJoinSet(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * analyzing the {@link #command} then return the tokens
	 * @param rePattern custom regex for analyzing the command. (null by defaut)
	 * @return a HashMap contains all the tokens for executing the operator
	 * 			- return syntax: {"JoinSet"=["hasQI", "QI"], "where"=["Y1","Y2","Y3",...], "except"=["X1","X2","X3",...]}
	 * 			- Example:	
	 */

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
		HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
		if (!command.contains("=")) {
		
			Pattern joinSetPattern = Pattern.compile("(JoinSet)\\s*\\((.*?)\\)");
			Matcher joinSetMatcher = joinSetPattern.matcher(command);
	
			String joinSetKey = "";
			List<String> joinSetValues = new ArrayList<>();
			if (joinSetMatcher.find()) {
			    joinSetKey = joinSetMatcher.group(1);
			    String joinSetValuesString = joinSetMatcher.group(2).replace("\"", "");
			    String[] joinSetValuesArray = joinSetValuesString.split(",");
			    joinSetValues = Arrays.asList(joinSetValuesArray);
			}
	
			// Extract where and its values
			Pattern wherePattern = Pattern.compile("(where)\\s*\\{(.*?)\\}");
			Matcher whereMatcher = wherePattern.matcher(command);
	
			String whereKey = "";
			List<String> whereValues = new ArrayList<>();
			if (whereMatcher.find()) {
			    whereKey = whereMatcher.group(1);
			    String whereValuesString = whereMatcher.group(2).replace("(", "").replace(")", "").replace("\"", ""); 
			    whereValues = Arrays.asList(whereValuesString.split(","));
			}
	
			// Extract except and its values
			Pattern exceptPattern = Pattern.compile("(except)\\s*\\{(.*?)\\}");
			Matcher exceptMatcher = exceptPattern.matcher(command);
	
			String exceptKey = "";
			List<String> exceptValues = new ArrayList<>();
			if (exceptMatcher.find()) {
			    exceptKey = exceptMatcher.group(1);
			    String exceptValuesString = exceptMatcher.group(2).replace("(", "").replace(")", "").replace("\"", "");
			    exceptValues = Arrays.asList(exceptValuesString.split(","));
			}
	
			// Create the HashMap
			hashMap.put(joinSetKey,  new ArrayList<>(joinSetValues));
			hashMap.put(whereKey, new ArrayList<>(whereValues));
			hashMap.put(exceptKey, new ArrayList<>(exceptValues));

		}
		
		return hashMap;
		
	}

}
