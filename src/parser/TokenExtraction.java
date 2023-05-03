package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is created for analysing the command and extracting useful token to execute the command
 * @author khai
 *
 */


public class TokenExtraction extends OperatorsHandling{

	/**
	 * create object contains the command for analysis
	 * @param command
	 */
	
	public TokenExtraction(String command) {
		super(command);
	}
	
	/**
	 *  This function is used to determine the operator included in the command
	 * @param input the command with the opertor
	 * @return the operator's name
	 * @throws SyntaxException 
	 */
	
	private String getOperator(String input) throws SyntaxException {
		String[] operator;
        int index = input.indexOf("(");
        if (index >= 0) {        	
            operator = new String[1];
            operator[0] = input.substring(0, index).trim() ;
        } else {
        	operator = input.split("\\s+");
        }
    	 	
        if (operator.length > 0) {
        	operator[0] = operator[0].trim();
        	if (!OperatorsHandling.listKeys.containsKey(operator[0])) {
        		throw new SyntaxException(1,operator[0]);
        	}
        	return operator[0];
        }
        throw new SyntaxException(0,"\n");
	}

	
	
	/**
	 *  this function analyze the command and return the HashMap contain all useful token for executing command
	 *  @return a HashMap with useful Token <br>
	 *  	- the HashMap contain  the operator's name and it's arguments <br>
	 *  	- Example: {p=[livesIn], S=[*, type, Person], operator=[EdgeReverse], O=[*, inGroup, France]} <br>
	 * @throws SyntaxException 
	 */	
	@Override
	public HashMap<String, ArrayList<String>> getToken() throws SyntaxException {
		HashMap<String, ArrayList<String>>  result = new HashMap<String, ArrayList<String>> ();
				
		// Get operator's name
		String op = getOperator(command);	// if !op.equals("JoinSet"), at the moment it doesn't work with JoinSet
		
		if (!op.equals("JoinSet") && !op.equals("Anatomization")){
			
			// Get the keys list of the operator
			String[] list_keys = OperatorsHandling.listKeys.get(op);
			
			// put the string containing operator's name into HashMap
			// we will use it to call the "real" function for rewriting on the graph database
			result.put(
					"operator", new ArrayList<String>() {{add(op);}}
					);
			
			// here is the script for Positional Arguments
			if (!command.contains("=")) {
				
				// We use this regex for matching all Parameters inside the command
				String regex = "(\\([^\\\\)\\\\(]+\\))|(\\\"[^\\\"]+\\\")|(\\*)|(null)";
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher(command);
		        
		        // because of the positional arguments form, count is used to match the keys with their value in command
		        int count = 0;
		        while (matcher.find()) {
		            String paramsStr = matcher.group(0);
		            // remove unnecessary characters
		            paramsStr = paramsStr.replace("(", "").replace(")", "").replace("\"", "");
		            // System.out.println("\u001B[36m"+paramsStr+"\u001B[0m");
		            String[] afterSplit = paramsStr.split(",");         
		            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(afterSplit));
		            for (int i = 0;i < temp.size();i++) {
		            	if (temp.get(i).equals("null")) {
		            		temp.set(i, null);
		            	}
		            }
		            result.put(list_keys[count], temp);
		            count++;
		        }
			}
			// we will have the script for Keywords Arguments here
			else {
				
			}
		}
		/**
		 * 
		 * for analyzing Joinset syntax <br>
		 * e.g. <br>
		 * JoinSet ("hasQI","QI") where {(*,"type","Person"),(*,"knows",*)} except {(*,"liveIn","Paris")} <br>
		 * result = {JoinSet=[hasQI, QI], where=[*, type, Person, *, knows, *], except=[*, liveIn, Paris], operator=[JoinSet]} <br>
		 * 
		 */
		else if (op.equals("JoinSet")) {
			result.put(
					"operator", new ArrayList<String>() {{add(op);}}
			);
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
	            result.put(op, new ArrayList<String>(Arrays.asList(afterSplit)));
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
	                result.put(matcher.group(0).split("[^a-zA-Z]+")[0], listCondition);
	        	}
	        }
	        
	        
	        
	        
		}
		/**
		 * for analyzing Anatomization syntax <br>
		 * e.g. <br>
		 * Command: Anatomization ( {"name"}, {"knows","type" }, {"livesIn"} ) <br>
		 * result = {idn=[name], sens=[livesIn], qID=[knows, type], operator=[Anatomization]} <br>
		 */
		else if(op.equals("Anatomization")) {
			result.put(
					"operator", new ArrayList<String>() {{add(op);}}
			);
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
    				result.put(keys[count], element);
    				count++;
    			}
    		} 
		}
        
        return result;
    }
	
	
	/**
	 * genrate le HashMap for executing the operator
	 * @param tokens after analyzing the command
	 * @return map of params can be passed directly to the constructor of the operators' class
	 */
	@Override
	public HashMap<String,String> generateExecutableMap(final HashMap<String,ArrayList<String>> result) {
		
		HashMap<String,ArrayList<String>> tokens = new HashMap<String,ArrayList<String>>();
		tokens.putAll(result);
		
		HashMap<String,String> execMap = new HashMap<String,String>();
		
		
		
		// loop through the map (which is obtained after analyzing user's command)
		// e.g: tokens = {p=[livesIn], S=[*, type, Person], operator=[EdgeReverse], O=[*, null, null]}
		for (String key:tokens.keySet()) {
			// check if listKeys contain the actual key or not (if yes => we must transform it into executable map)
			if (listKeys.containsKey(key)) {
				
				// getting the Set convert parameters
				String[] split_element = listKeys.get(key);

				
				if (key.contains("_att")) {
					execMap.put(split_element[0],tokens.get(key).get(0));
					continue;
				}
					
				// if the Set has type "*",o,O we don't put the first arg into execMap
				if (split_element[0].equals("*")) {
					execMap.put(split_element[1], tokens.get(key).get(1));
					execMap.put(split_element[2], tokens.get(key).get(2));
				}
				// if the Set has type x,s,S we put all into the execMap
				else {
					execMap.put(split_element[0], tokens.get(key).get(0));
					execMap.put(split_element[1], tokens.get(key).get(1));
					execMap.put(split_element[2], tokens.get(key).get(2));
				}
				
				
			}
			// if not we can put it directly into the exeuctable map
			else {
				// example tokens.get("p").get(0) = livesIn
				execMap.put(key,tokens.get(key).get(0));
			}
		}

		
		// we dont need the operator's name in the executable map
		execMap.remove("operator");
		return execMap;
		
	}


	
}
