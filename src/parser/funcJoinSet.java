package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcJoinSet extends funcOperators {

	public funcJoinSet(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getToken(String rePattern) {
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
		HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
		hashMap.put(joinSetKey,  new ArrayList<>(joinSetValues));
		hashMap.put(whereKey, new ArrayList<>(whereValues));
		hashMap.put(exceptKey, new ArrayList<>(exceptValues));

		
//		System.out.println(hashMap);
//		
//		for (List<String> li : hashMap.values()) {
//			System.out.println(li);
//		}
//		
//		for (String st : hashMap.get("except")) {
//			System.out.println(st);
//		}
		
		return hashMap;
		
	}

}
