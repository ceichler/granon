package parser;


import java.util.regex.*;

import java.util.*;
import java.util.function.Function;


public class Parser {
	
	// In the syntax of the operators that I define
	// I denote that X,Y,S,O is a Set (set of 3 elements to identify a node, e.g ("id105","type","Person"))  
	// and X_att,Y_att,S_att,O_att is the att of a node (e.g  "Person")
	
	public static String command;
	public static Map<String, Function<Object[], Object>> operators = new HashMap<String, Function<Object[], Object>>(){
		private static final long serialVersionUID = 24721833899330550L;
	{
		put("DeleteNode", (Object[] argsArray) -> DeleteNode((String)argsArray[0]));
		put("NewNode", (Object[] argsArray) -> NewNode((String)argsArray[0]));
		put("EdgeReverse", (Object[] argsArray) -> EdgeReverse((String)argsArray[0]));
        put("EdgeCut", (Object[] argsArray) -> EdgeCut((String)argsArray[0]));
        put("EdgeCopy", (Object[] argsArray) -> EdgeCopy((String)argsArray[0]));
        put("EdgeChord", (Object[] argsArray) -> EdgeChord((String)argsArray[0]));
        put("EdgeChordKeep", (Object[] argsArray) -> EdgeChordKeep((String)argsArray[0]));
        put("ModifyEdge", (Object[] argsArray) -> ModifyEdge((String)argsArray[0]));
        put("RandomSource", (Object[] argsArray) -> RandomSource((String)argsArray[0]));
        put("RandomTarget", (Object[] argsArray) -> RandomTarget((String)argsArray[0]));
        put("JoinSet", (Object[] argsArray) -> JoinSet((String)argsArray[0]));
        put("CloneSet", (Object[] argsArray) -> CloneSet((String)argsArray[0]));
        put("LDP", (Object[] argsArray) -> LDP((String)argsArray[0]));
        put("Anatomization", (Object[] argsArray) -> Anatomization((String)argsArray[0]));
	}};
	

	// "(\"id105\",*,*)" ---> "id105","*","*"
	public static ArrayList<String> handleSet(String str) {
		String input = str.trim();
		ArrayList<String> result =new ArrayList<String>();
		if (input.startsWith("(") && input.endsWith(")")) {
		    input = input.substring(1, input.length() - 1);
		}
		// String input = str.substring(1, str.length() - 1);
		String[] output = input.split(",");
		for (int i = 0; i < output.length; i++) {
		    output[i] = output[i].replaceAll("\"", "");
		    output[i] = output[i].trim();
		    result.add(output[i]);
		}
		return result;
	}
	
	
	
	
	// This function is designed to assist in obtaining the necessary parameters for creating a DeleteNode object
	// Syntax: DeleteNode((S_att,p,O_att)) | DeleteNode (S_att,p,O_att)
	// E.g DeleteNode(("id105",*,*)) , DeleteNode("id105",*,*) 
	// The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command. 
    public static int DeleteNode(String rePattern) {    	
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att]}
    	String temp;
    	
    	
    	// Version with equals sign  character
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
    		    result.put("S", handleSet(temp));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g DeleteNode(X = ("id105",*,*))
    	else {
    	}
		return 0;
    			
    }
    
    
    // Syntax: 	NewNode(S_att) | NewNode S_att
    // e.g 		NewNode("id108") | NewNode "id108"
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int NewNode(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	
    	// Version with equals sign  character
    	if (!command.contains("=")) { 
   
	    	if (rePattern == null) {
	        	if (command.contains("(") || command.contains(")")) {
	        		pattern =  "NewNode\\s*\\(\"(.*)\"\\)";
	        		
	        	}else {
	        		pattern =  "NewNode\\s*\"(.*)\"";
	        	}
	    	}else {
	    		pattern = rePattern;
	    	}    	
	    	
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(command);
			
			if (matcher.find()) {
			    // result[0] = matcher.group(1).trim();
			    result.put("X", handleSet(matcher.group(1).trim()));
			    System.out.println(result);
			} else {
				System.err.println("Syntax error!");
			}
		
    	}
    	// version with equals sign
    	else {

    	}
		return 0;		
    	
    }
    
    
    // Syntax:  EdgeReverse((x,s,S_att),p,(*,o,O_att))
    // e.g 		EdgeReverse(("id110","name","Johnson"),"livesIn",(*,"type","City"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeReverse(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att]} 
    	// prepare for necessary upgrades when there is an equal sign character in request
  	
    	// Version with equals sign  character
    	// e.g EdgeReverse(("id110","name","Johnson"),"livesIn",(*,"type","City"))
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeReverse\\s*\\(\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*\\((.*)\\)\\s*\\)";
        		// \\s* to ignore spaces
        		// without \\s*  "EdgeReverse\\(\\((.*)\\),(.*),\\((.*)\\)\)"
        		// (.*) to match the necessary elements (this this case we have 3 elements S,p and O)
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {   
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g DeleteNode(X = ("id105",*,*))
    	// The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    	else {
    	}
		return 0;
    			
    }
    
	// Syntax:  EdgeCut(S,p,O,pi,M_att,po)
	// e.g  	EdgeCut (("id110",*,*),"livesIn",(*,"type","City"),"inIntem","intem","outIntem")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeCut(String rePattern) {
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
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    result.put("pi", handleSet(matcher.group(4)));
    		    result.put("M_att", handleSet(matcher.group(5)));
    		    result.put("po", handleSet(matcher.group(6)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	// e.g EdgeCut (S=("id110",*,*),p="livesIn",O=(*,"type","City"),pi="inIntem",M_att="intem",po="outIntem")
    	// The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    	else {
    	}
		return 0;
    }
    
    // Syntax:	EdgeCopy(S, p, O, p')
    // e.g 		EdgeCopy (("id555",*,*),"knows",(*,"type","person"),"cousin")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeCopy(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"p_n":[p_n]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeCopy\\s*\\(\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*,\\s*\\((.*)\\)\\s*,\\s*(.*)\\s*\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    result.put("p_n", handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;   	
    }
    
    
    // Syntax: 	 EdgeChord(S, pi , M, po , O, p)
    // e.g		 EdgeChord ((*,"type","Person"),"livesIn",(*,"type","City"),"inGroup",(*,null,null),"livesIn")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeChord(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"p":[p]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeChord\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("pi", handleSet(matcher.group(2)));
    		    result.put("M", handleSet(matcher.group(3)));
    		    result.put("po", handleSet(matcher.group(4)));
    		    result.put("O", handleSet(matcher.group(5)));
    		    result.put("p", handleSet(matcher.group(6)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }

    
    // Syntax: 	 EdgeChordKeep(S, pI , M, pO , O, p)
    // e.g		 EdgeChordKeep ((*,"type","Person"),"livesIn",(*,"type","City"),"inGroup",(*,null,null),"livesIn")    
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeChordKeep(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"p":[p]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "EdgeChordKeep\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("pi", handleSet(matcher.group(2)));
    		    result.put("M", handleSet(matcher.group(3)));
    		    result.put("po", handleSet(matcher.group(4)));
    		    result.put("O", handleSet(matcher.group(5)));
    		    result.put("p", handleSet(matcher.group(6)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }
    
    // Syntax: 	ModifyEdge(S,O,pi,pf)
    // e.g		ModifyEdge((*,"type","Person"),(*,"type","City"), "livesIn", "worksIn")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int ModifyEdge(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"O":[*,o,O_att],"pi":[pi],"pf":[pf]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "ModifyEdge\\s*\\(\\s*\\((.*)\\)\\s*,\\s*\\((.*)\\)\\s*,(.*),(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("O", handleSet(matcher.group(2)));
    		    result.put("pi", handleSet(matcher.group(3)));
    		    result.put("pf", handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }
    
    // Syntax:  RandomSource(S, p, O, T )
    // e.g		RandomSource((*,"type","City"),"inGroup",(*,null,null),(*,"type","City"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int RandomSource(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[pi],"O":[*,o,O_att],"T":[*,t,T_att]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "RandomSource\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,\\s*\\((.*)\\)\\s*\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    		    result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    result.put("T", handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }
    
    // Syntax:	RandomTarget(S, p, O, T)
    // e.g		RandomTarget((*,"type","Person"),"name",(*,"isA","Name"),(*,"isA","Name"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int RandomTarget(String rePattern) {
    	// call function RandomTar
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[pi],"O":[*,o,O_att],"T":[*,t,T_att]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "RandomTarget\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,\\s*\\((.*)\\)\\s*\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    			result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    result.put("T", handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    	
    }
    // Syntax:  JoinSet (p,O_att)  where {X1,X2,X3,...} except {Y1,Y2,Y3,..}
    // e.g 		JoinSet ("hasQI","QI") where {(*,"type","Person"),(*,"livesIn","Paris")} except {(*,"knows","Johnson")}
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int JoinSet(String rePattern) {
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
		HashMap<String, List<String>> hashMap = new HashMap<>();
		hashMap.put(joinSetKey, joinSetValues);
		hashMap.put(whereKey, whereValues);
		hashMap.put(exceptKey, exceptValues);

		
		System.out.println(hashMap);
		
//		for (List<String> li : hashMap.values()) {
//			System.out.println(li);
//		}
//		
//		for (String st : hashMap.get("except")) {
//			System.out.println(st);
//		}	
		return 0;    	
		
    }
    
    
    // Syntax:  CloneSet (S,c,C_att)
    // e.g 		CloneSet(("*", "type", "city"), "clone", "Clone")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int CloneSet(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"c":[c],"C_att":[C_att]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "CloneSet\\s*\\(\\s*\\((.*)\\)\\s*,(.*),(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
        	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		

    		if (matcher.find()) {
    			result.put("S", handleSet(matcher.group(1)));
    		    result.put("c", handleSet(matcher.group(2)));
    		    result.put("C_att", handleSet(matcher.group(3)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// PVP,PVB
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }
    
    // Syntax:	LDP(S,p,O,k)
    //e.g		LDP((*,"type","Person"),"name",(*,"isA","Name"),3)
    // LDP("type", "Person", "isA", "Name", "name", 3)
    public static int LDP(String rePattern) {
    	String pattern;
    	HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O],"k":[k]} 
    	if (!command.contains("=")) {
    		if (rePattern == null) { 
        		pattern =  "LDP\\s*\\(\\s*\\((.*)\\)\\s*,(.*),\\s*\\((.*)\\)\\s*,(.*)\\)" ;
        	}else {
        		pattern = rePattern;
        	}    	
    		Pattern regex = Pattern.compile(pattern);
    		Matcher matcher = regex.matcher(command);		
    		if (matcher.find()) {
    			result.put("S", handleSet(matcher.group(1)));
    		    result.put("p", handleSet(matcher.group(2)));
    		    result.put("O", handleSet(matcher.group(3)));
    		    result.put("k", handleSet(matcher.group(4)));
    		    System.out.println(result);
    		} else {
    			System.err.println("Syntax error!");
    		}
    	}
    	// PVP,PVB
    	// version with equal sign character
    	else {
    	}
		return 0;
    	
    }
    	

    
    public static int PrePostprocessing(String rePattern) {
    	System.err.println("this operator is under development");
    	return 0;
    }
    
    
    // Syntax:
    // e.g
    // 
    public static int Anatomization(String rePattern) {
    	System.err.println("this operator is under development");
    	return 0;
    	
    }
    
    
    
    
    
    
    public static int execute() {
    	
    	command = command.trim();
    	String call_func;
    	String[] words;
    	
    	int index = command.indexOf("(");
        if (index >= 0) {        	
            words = new String[1];
            words[0] = command.substring(0, index).trim() ;
        } else {
        	words = command.split("\\s+");
        }
    	 	
        if (words.length > 0) {
            call_func = words[0];
            System.out.println("Call_func = " + call_func);
            Object[] functionArgs = {null};
            try {
            operators.get(call_func).apply(functionArgs);
            }
            catch(Exception e){
            	System.err.println("operator does not exist");
            }
        } else {
        	System.err.println("Empty command !");
        }
		return 0;
    }	
}
