package parser;


import java.util.regex.*;

import transformations.operators.*;
import transformations.procedures.*;
import utils.Pair;

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
		put("Anato", (Object[] argsArray) -> Anatomization((String)argsArray[0]));
		put("PrePostprocessing", (Object[] argsArray)->PrePostprocessing((String)argsArray[0]));
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
    	
    	HashMap<String,ArrayList<String>> result = (new funcDeleteNode(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new DeleteNode(result.get("S").get(0),result.get("S").get(1),result.get("S").get(2))).execute();
    	
    	
		return 0;
    			
    }
        
    // Syntax: 	NewNode(S_att) | NewNode S_att
    // e.g 		NewNode("id108") | NewNode "id108"
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int NewNode(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcNewNode(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new NewNode(result.get("X").get(0))).execute();
		return 0;		
    	
    }
    
    
    // Syntax:  EdgeReverse((x,s,S_att),p,(*,o,O_att))
    // e.g 		EdgeReverse((*,"type","Person"),"livesIn",(*,"inGroup","France"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeReverse(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcEdgeReverse(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	(new EdgeReverse(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("p").get(0)
    			)
    	).execute();
    	// (new EdgeReverse("*","type","Person","inGroup","France","livesIn")).execute();
    	
		return 0;
    			
    }
    
	// Syntax:  EdgeCut(S,p,O,pi,M_att,po)
	// e.g  	EdgeCut (("Paris",null,null),"type",(*,null,null),"pi","IntermNode","pf")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeCut(String rePattern) {
    	
    	HashMap<String,ArrayList<String>> result = (new funcEdgeCut(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	// EdgeCut (("Paris",null,null),"type",(*,null,null),"pi","IntermNode","pf")
    	// (new EdgeCut("Paris", null, null, null, null, "type", "pi", "IntermNode", "pf")).execute();
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"pi":[pi],"M_att":[M_att],"po":[po]}
    	// EdgeCut(String x, String s,String S,String o, String O, String pi, String pf1, String interm, String pf2)
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new EdgeCut(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("p").get(0),
    			result.get("pi").get(0),
    			result.get("M_att").get(0),
    			result.get("po").get(0)
    			)
    	).execute();
    	
    	
		return 0;
    }
    
    // Syntax:	EdgeCopy(S, p, O, p')
    // e.g 		EdgeCopy (("id555",*,*),"knows",(*,"type","person"),"cousin")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeCopy(String rePattern) {
    	// result = {"S":[x,s,S_att],"pi":[p],"O":[*,o,O_att],"pf":[pf]}
    	// pf copy, cannot be *
    	HashMap<String,ArrayList<String>> result = (new funcEdgeCopy(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	// EdgeCopy(String x, String s,String S,String o, String O, String pi, String pf)
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new EdgeCopy(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("pi").get(0),
    			result.get("pf").get(0)
    	)).execute();
		return 0;   	
    }
    
    
    // Syntax: 	 EdgeChord(S, pi , M, po , O, p)
    // e.g		 EdgeChord ((*,"type","Person"),"livesIn",(*,null,null),"inGroup",(*,null,null),"livesIn")
    // (new EdgeChord("*", "type","Person", null, null, null, null, "livesIn", "inGroup", "livesIn")).execute();
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeChord(String rePattern) {
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"pf":[pf]} 
    	HashMap<String,ArrayList<String>> result = (new funcEdgeChord(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new EdgeChord(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("M").get(1),
    			result.get("M").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("pi").get(0),
    			result.get("po").get(0),
    			result.get("pf").get(0)
    	)).execute();
    	// EdgeChord((*,"type","Person"),"name",(*,null,null),"isA",(*,null,null),hasA)
    	// (new EdgeChord("*", "type","Person", null, null, null, null, "name", "isA", "hasA")).execute();
		return 0;
    	
    }

    
    // Syntax: 	 EdgeChordKeep(S, pI , M, pO , O, p) // p est pf dans le code
    // e.g		 EdgeChordKeep ((*,"type","Person"),"livesIn",(*,"type","city"),"inGroup",(*,null,null),"livesIn")    
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int EdgeChordKeep(String rePattern) {
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"pf":[pf]} 
    	HashMap<String,ArrayList<String>> result = (new funcEdgeChordKeep(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	(new EdgeChordKeep(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("M").get(1),
    			result.get("M").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("pi").get(0),
    			result.get("po").get(0),
    			result.get("pf").get(0)
    	)).execute();
		return 0;
    	
    }
    
    // Syntax: 	ModifyEdge(S,O,pi,pf)
    // e.g		ModifyEdge((*,"type","Person"),(*,"type","city"), "livesIn", "worksIn")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int ModifyEdge(String rePattern) {
    	// result = {"S":[x,s,S_att],"O":[*,o,O_att],"pi":[pi],"pf":[pf]}
    	HashMap<String,ArrayList<String>> result = (new funcModifyEdge(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	// ModifyEdge(String x, String s,String S,String o, String O, String pi, String pf)
    	(new ModifyEdge(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("pi").get(0),
    			result.get("pf").get(0)    			
    	)).execute();
		return 0;
    	
    }
    
    // Syntax:  RandomSource(S, p, O, T )
    // e.g		RandomSource((*,"type","City"),"inGroup",(*,null,null),(*,"type","City"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int RandomSource(String rePattern) {
    	// call function RandomSrc
    	HashMap<String,ArrayList<String>> result = (new funcRandomSource(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");   	
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"T":[*,t,T_att]}
    	// RandomSrc(String x, String s, String S, String t, String T, String o, String O, String p)
    	(new RandomSrc(
    	
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("T").get(1),
    			result.get("T").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("p").get(0)
    			
    	)).execute();
    	
		return 0;
    	
    }
    
    // Syntax:	RandomTarget(S, p, O, T)
    // e.g		RandomTarget((*,"type","Person"),"name",(*,"isA","Name"),(*,"isA","Name"))
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int RandomTarget(String rePattern) {
    	// call function RandomTar
    	// RandomTar(String x, String s, String S, String t, String T, String o, String O, String p)
    	// // result = {"S":[x,s,S_att],"p":[pi],"O":[*,o,O_att],"T":[*,t,T_att]} 
    	HashMap<String,ArrayList<String>> result = (new funcRandomTarget(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	(new RandomTar(
    			
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("T").get(1),
    			result.get("T").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("p").get(0)
    			
    	)).execute();
    	
		return 0;
    	
    	
    }
    // Syntax:  JoinSet (p,O_att)  where {X1,X2,X3,...} except {Y1,Y2,Y3,..}
    // e.g 		JoinSet ("hasQI","QI") where {(*,"type","Person"),(*,"livesIn","Paris")} except {(*,"knows","Johnson")}
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int JoinSet(String rePattern) {
    	// result = {JoinSet=[hasQI, QI], where=[Y1,Y2,Y3,...], except=[X1,X2,X3,...]}
    	// {JoinSet=[hasQI, QI], where=[*, type, Person, *, livesIn, Paris], except=[*, knows, Johnson]}
    	HashMap<String,ArrayList<String>> result = (new funcJoinSet(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	
		HashSet<Pair<String>> excepts = new HashSet<Pair<String>>();
		if (result.get("except").size() >= 3) {
			for (int i = 0; i < result.get("except").size();i+=3 ) {
				if (!result.get("except").get(i).equals("*")) {
					System.err.println(" Node in except must be represented by (*,x,X_att)");
					return -1;
				}
				excepts.add(new Pair<String>(result.get("except").get(i+1), result.get("except").get(i+2)));
			}
		}
		
		HashSet<Pair<String>> wheres = new HashSet<Pair<String>>();
		if (result.get("where").size() >= 3) {
			for (int i = 0; i < result.get("where").size();i+=3 ) {
				if (!result.get("where").get(i).equals("*")) {
					System.err.println(" Node in where must be represented by (*,x,X_att)");
					return -1;
				}
				wheres.add(new Pair<String>(result.get("where").get(i+1), result.get("where").get(i+2)));
			}
		}
    	
//    	for (Pair<String> a: wheres) {
//    		System.out.println(a.getFirst());
//
//    		System.out.println(a.getSecond());
//    	}
//    	for (Pair<String> a: excepts) {
//    		System.out.println(a.getFirst());
//
//    		System.out.println(a.getSecond());
//    	}
    	
    	// JoinSet(String src, Set<Pair<String>> excepts, Set<Pair<String>> wheres, String setName, String propName)
    	// JoinSet ("hasQI","QI") where {(*,"type","Person"),(*,"livesIn","Paris")} except {(*,"knows","Johnson")}
		// JoinSet(Set<Pair<String>> excepts, Set<Pair<String>> wheres, String setName, String propName)
		/** Creating a new set with setName **/
		(new NewNode(result.get("JoinSet").get(1))).execute();
		(new JoinSet(
				excepts,
				wheres,
				result.get("JoinSet").get(1),
				result.get("JoinSet").get(0)
				
		)).execute();
    	// JoinSet ("hasQI","QI") where {(*,*,"Stuart")} except {(*,"knows",*)}
		return 0;    	
		
    }
    
    
    // Syntax:  CloneSet (S,c,C_att)
    // e.g 		CloneSet(("*", "type", "city"), "clone", "Clone")
    // The rePattern parameter allows us to use our own regular expression pattern to match and extract substrings from the command.
    public static int CloneSet(String rePattern) {
    	// result = {"S":[x,s,S_att],"c":[c],"C_att":[C_att]} 
    	HashMap<String,ArrayList<String>> result = (new funcCloneSet(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	// CloneSet(String src, String s, String S, String c, String C)
    	(new CloneSet(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("c").get(0),
    			result.get("C_att").get(0)
    	)).execute();
    	
		return 0;
    	
    }
    
    // Syntax:	LDP(S,p,O,k)
    //e.g		LDP((*,"type","Person"),"name",(*,"isA","Name"),3)
    // LDP("type", "Person", "isA", "Name", "name", 3)
    public static int LDP(String rePattern) {
    	// result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O],"k":[k]} 
    	HashMap<String,ArrayList<String>> result = (new funcLDP(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	// LDP(String x, String s, String S, String o, String O, String p, int k)
    	(new LDP(
    			
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("O").get(1),
    			result.get("O").get(2),
    			result.get("p").get(0),
    			Integer.parseInt(result.get("k").get(0))
    			
    			
    	)).execute();
    	
		return 0;
    	
    }
    	

    
    public static int PrePostprocessing(String rePattern) {
    	System.err.println("this operator is under development");
    	return 0;
    }
    
    
    // Syntax:
    // e.g		Anatomization ( {"name"}, {"knows" }, {"livesIn"} )
    // 
    public static int Anatomization(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcAnato(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	// public Anatomization(List<String> identifiers, List<String> qID, List<String> sensitives)
    	// {idn=[], sens=[], qID=[]}
    	(new Anatomization(
    			result.get("idn"),
    			result.get("qID"),
    			result.get("sens")
    			
    	)).execute();
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
            System.out.println("[in Parser] Call_func = " + call_func);
            Object[] functionArgs = {null};
            try {
            	operators.get(call_func).apply(functionArgs);
            }
            catch(Exception e){
            	System.err.println("operator does not exist");
            	e.printStackTrace();
            }
        } else {
        	System.err.println("Empty command !");
        }
		return 0;
    }	
}
