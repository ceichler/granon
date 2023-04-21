package parser;


import java.util.regex.*;

import transformations.operators.*;
import transformations.procedures.*;
import utils.Pair;

import java.util.*;
import java.util.function.Function;


/**
 * This class defines a parser to analyze and execute the command
 *
 * @author cnguyen
 */

public class Parser {
	
	/**
	 * 
	 *  Represents a query command to be executed on a graph database.
	 *  The command follows the syntax "operator_name (list_of_arguments)"
	 * 
	 */
	public static String command;
	
	/**
	 * 
	 *  A mapping of operators to their corresponding executors.
	 * 
	 */
	
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
	


	/**
	 *
	 *	Extracts parameters from a string representing a set of the form "(a,b,c)" or "a,b,c"
	 *
	 *
	 *	@param input a string containing information about a set
	 *	@return an ArrayList of parameters in the set
	 *
	 */
	
	public static ArrayList<String> handleSet(String input){
		ArrayList<String> parameters = new ArrayList<String>();
	    input = input.trim(); // Remove any leading/trailing whitespace
	    if (input.endsWith(",")) {
	        input = input.substring(0, input.length() - 1);
	    }
	    if (input.startsWith("(") && input.endsWith(")")) { // Check if input has parentheses
	        input = input.substring(1, input.length()-1); // Remove parentheses
	        if (!input.isEmpty()) { // Check if input is not empty
	            String[] params = input.split(","); // Split input by comma
	            for (String param : params) {
	                parameters.add(param.trim().replaceAll("^\"|\"$", "")); // Remove any leading/trailing quotes and add parameter to ArrayList
	            }
	        }
	    } else { // If input doesn't have parentheses, split by comma and add parameters to ArrayList
	        String[] params = input.split(",");
	        for (String param : params) {
	            parameters.add(param.trim().replace("\"", ""));
	        }
	    }
	    return parameters;
	}
	
	/**
	 * Extracts keyword arguments from a string and returns a HashMap containing the parameter key-value pairs.
	 * 
	 * @param comd a string containing keyword arguments in the form "key=value"
	 * @return a HashMap where the keys are the parameter names (keywords) extracted from the string and the values are ArrayLists of parameters associated with those keywords
	*/
	
	public static HashMap<String,ArrayList<String>> handleKeywordArgs(String comd) {
		HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
		comd = comd.replace(" ", "");
		/**
		 * match all strings of the form "keyword = value"
		 */
		String pattern = "(\\w+)\\s*=\\s*(.*?)\\s*(?:(?=\\w+\\s*=)|\\)$)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(comd);


        // Store the parameter key-value pairs in a HashMap
        while (matcher.find()) {
            result.put(matcher.group(1),Parser.handleSet(matcher.group(2)));
        }

		return result;
	}
	
	
	
	/**
	 * Deletes a node from the graph based on the given {@link #command}. Command has the syntax DelteNode((S_att,p,O_att))
	 * 
     * 
	 * @param rePattern a regular expression pattern used to select the node to be deleted (null for defaut).It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default)
	 * @return 0 if the node was successfully deleted, -1 otherwise
	 * @see transformations.operators.DeleteNode
	 * @see parser.funcDeleteNode
	 */
    public static int DeleteNode(String rePattern) {    	
    	
    	HashMap<String,ArrayList<String>> result = (new funcDeleteNode(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	/**
    	 * result = {"S"=[x,s,S_att]}
    	 */
    	(new DeleteNode(result.get("S").get(0),result.get("S").get(1),result.get("S").get(2))).execute();
    	
    	
		return 0;
    			
    }
    
    /**
     * Creates a new node in the graph based on the given {@link #command}. Command has the syntax NewNode("new_node_name")
     * 
     * @param rePattern a regular expression pattern used to select the node where the new node will be created.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default)
     * @return 0 if the new node was successfully created, -1 if the node selection result was null
     * @see transformations.operators.NewNode
     * @see parser.funcNewNode
     * 
     */
        
    public static int NewNode(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcNewNode(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	/**
    	 * result = {"X"=[X_att]}
    	 */
    	(new NewNode(result.get("X").get(0))).execute();
		return 0;		
    	
    }
    
    /**
	 *	
	 *   Reverses the direction of an edge in the graph based on the attribute {@link #command}. Command has the syntax EdgeReverse((x,s,S_att),p,(*,o,O_att))
	 *   
	 *   @param rePattern a regular expression pattern used to select the edge to be reversed.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default)
	 *   @return 0 if the edge was successfully reversed, -1 if the edge selection result was null
	 *   @see transformations.operators.EdgeReverse
	 *   @see parser.funcEdgeReverse
     */

    public static int EdgeReverse(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcEdgeReverse(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	/**
    	 * result = {"S"=[x,s,S_att],"p"=[p],"O"=[*,o,O_att]}
    	 */
    	
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
    	// EdgeReverse((*,"type","Person"),"livesIn",(*,"inGroup","France"))
		return 0;
    			
    }
    
    /**
     * Executes a command to "cut" an edge in the graph database based on {@link #command}. 
     * Command has the syntax  EdgeCut((x,s,S_att),p,(*,o,O),pi,M_att,po)
     * 
     * @param rePattern a regular expression pattern used to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the command was executed successfully, -1 if there was an error
     * @see transformations.operators.EdgeCut
     * @see funcEdgeCut
     */
    public static int EdgeCut(String rePattern) {
    	// EdgeCut (("Paris",null,null),"type",(*,null,null),"pi","IntermNode","pf")
    	HashMap<String,ArrayList<String>> result = (new funcEdgeCut(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"pi":[pi],"M_att":[M_att],"po":[po]}
    	 */
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
    
    /**
     * Executes the EdgeCopy operation based on {@link #command}. command has the syntax EdgeCopy((x,s,S_att), pi, (*,o,O_att), pf)
     *  
     * @param rePattern a custom regular expression pattern to match and extract substrings from the command string.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the operation was successful, -1 if there was an error.
     * @see transformations.operators.EdgeCopy
     * @see funcEdgeCopy
     */
    public static int EdgeCopy(String rePattern) {
    	// EdgeCut (("Paris",null,null),"type",(*,null,null),"pi","IntermNode","pf")
    	HashMap<String,ArrayList<String>> result = (new funcEdgeCopy(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	/**
    	 * result = {"S":[x,s,S_att],"pi":[pi],"O":[*,o,O_att],"pf":[pf]}
    	 */
    	
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
    
    
    /**
     *  The EdgeChord function creates a new edge between two nodes, with an intermediate node, creating a chord that connects them then 
     *  remove two relation between source node - intermediate node nad intermediate node target node. 
     *  {@link #command} has the syntax EdgeChord(S, pi , M, po , O, p)
     *       
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     *  
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.operators.EdgeChord
     * @see funcEdgeChord
     */
    
    
    public static int EdgeChord(String rePattern) {
    	// result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"pf":[pf]} 
    	// EdgeChord ((*,"type","Person"),"livesIn",(*,null,null),"inGroup",(*,null,null),"livesIn")
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
		return 0;
    	
    }

    
    /**
     *  The EdgeChord function creates a new edge between two nodes, with an intermediate node, creating a chord that connects them 
     *  keep two relationships between source node - intermediate node and destination node intermediate node. 
     *  {@link #command} has the syntax EdgeChordKeep((x,s,S_att), pi , (*,m,M_att), po , (*,o,O), pf)
     *       
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.operators.EdgeChordKeep
     * @see funcEdgeChordKeep
     */
    public static int EdgeChordKeep(String rePattern) {
    	// EdgeChordKeep ((*,"type","Person"),"livesIn",(*,"type","city"),"inGroup",(*,null,null),"livesIn")
    	HashMap<String,ArrayList<String>> result = (new funcEdgeChordKeep(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"pi":[pi],"M":[*,m,M_att],"po":[po],"O":[*,o,O_att],"pf":[pf]} 
    	 * 
    	 */
    	
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
    
    /**
     * The ModifyEdge function transforming all relations indicated in the command. 
     * {@link #command} has the syntax ModifyEdge((x,s,S),(*,o,O),pi,pf)
     * 
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.operators.ModifyEdge
     * @see funcModifyEdge
     */
    public static int ModifyEdge(String rePattern) {
    	// ModifyEdge((*,"type","Person"),(*,"type","city"), "livesIn", "worksIn")
    	HashMap<String,ArrayList<String>> result = (new funcModifyEdge(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	if (result.equals(null)) {
    		return -1;
    	}
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"O":[*,o,O_att],"pi":[pi],"pf":[pf]}
    	 * 
    	 */
    	
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
    
    /**
     * The RandomSource randomly provides a new source for all relations indicated in command.
     * {@link #command} has the syntax RandomSource((x,s,S_att), p, (*,o,O_att), (*,t,T_att) )
     * 
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.procedures.RandomSrc
     * @see funcRandomSource
     */
    public static int RandomSource(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcRandomSource(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");   	
    	// RandomSource((*,"type","City"),"inGroup",(*,null,null),(*,"type","City"))
    	
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O_att],"T":[*,t,T_att]}
    	 * 
    	 */
    	
    	
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
    
    /**
     * The RandomSource randomly provides a new source for all relations indicated in command.
     * {@link #command} has the syntax RandomTarget((x,s,S_att), p, (*,o,O_att), (*,t,T_att) )
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command.It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.procedures.RandomTar
     * @see funcRandomTarget
     */
    public static int RandomTarget(String rePattern) {
    	// RandomTarget((*,"type","Person"),"name",(*,"isA","Name"),(*,"isA","Name"))
    	HashMap<String,ArrayList<String>> result = (new funcRandomTarget(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"p":[pi],"O":[*,o,O_att],"T":[*,t,T_att]} 
    	 * 
    	 */
    	
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
    
    
    
    /**
     * The JoinSet create an edge to a node indicated in the command. 
     * {@link #command} has the syntax JoinSet (p,O_att)  where {(*,x1,X1_att),(*,x2,X2_att),(*,x3,X3_att),...} except {(*,y1,Y1_att),(*,y2,Y2_att),..}
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command. It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.operators.JoinSet
     * @see funcJoinSet
     */
    public static int JoinSet(String rePattern) {
    	// result = {JoinSet=[hasQI, QI], where=[Y1,Y2,Y3,...], except=[X1,X2,X3,...]}
    	// JoinSet ("hasQI","QI") where {(*,*,"Stuart")} except {(*,"knows",*)}
    	HashMap<String,ArrayList<String>> result = (new funcJoinSet(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	/**
    	 *  excepts = {Pair<String>(X1_x,X1_att),Pair<String>(X2_x,X2_att),...}
    	 */
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
		
		/**
		 *  wheres = {Pair<String>(Y1_x,Y1_att),Pair<String>(Y2_x,Y2_att),...}
		 */
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
		
		/**
		 *  result = {"JoinSet"=["hasQI", "QI"], "where"=[*,Y1_y,Y1_att,*,Y2_y,Y2_att,...], "except"=[*,X1_x,X1_att,*,X2_x,X2_att,...]}
		 *  excepts = {Pair<>(X1,X2,..)}
		 *  wheres = {Pair<>{Y1,Y2,..}}
		 */
		
    	
		(new JoinSet(
				excepts,
				wheres,
				result.get("JoinSet").get(1),
				result.get("JoinSet").get(0)
				
		)).execute();
		return 0;    	
		
    }
    
    
    /**
     * The CloneSet creates a "clone" node for all nodes in a set indicated by command. 
     * {@link #command} has the syntax CloneSet ((x,s,S_att),c,C_att)
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command. It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.operators.CloneSet
     * @see funcCloneSet
     */
    public static int CloneSet(String rePattern) {
    	// CloneSet(("*", "type", "city"), "clone", "Clone")
    	HashMap<String,ArrayList<String>> result = (new funcCloneSet(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	/**
    	 * 
    	 * result = {"S":[x,s,S_att],"c":[c],"C_att":[C_att]} 
    	 * 
    	 */
    	(new CloneSet(
    			result.get("S").get(0),
    			result.get("S").get(1),
    			result.get("S").get(2),
    			result.get("c").get(0),
    			result.get("C_att").get(0)
    	)).execute();
    	
		return 0;
    	
    }
    
    /**
     * The LDP handles LDP
     * {@link #command} has the syntax LDP((x,s,S_att),p,(*,o,O_att),k)
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command. It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.procedures.LDP
     * @see funcLDP
     */
    public static int LDP(String rePattern) {
    	// LDP((*,"type","Person"),"name",(*,"isA","Name"),3)
    	HashMap<String,ArrayList<String>> result = (new funcLDP(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	/**
    	 *  result = {"S":[x,s,S_att],"p":[p],"O":[*,o,O],"k":[k]} 
    	 */
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
    
    
    /**
     * The Anatomization handles anato.
     * {@link #command} has the syntax Anatomization( {e1_prop,e2_prop,...}, {q1_prop,q2_prop,... }, {p1_prop,p2_prop,...} )
     * 
     * @param rePattern a regular expression pattern to match and extract substrings from the command. It is recommended to use the default regular expression pattern unless the user has a specific
     *  need to customize it. (null for default) 
     * @return 0 if the function is executed successfully, -1 otherwise
     * @see transformations.procedures.Anatomization
     * @see funcAnato
     */
    public static int Anatomization(String rePattern) {
    	HashMap<String,ArrayList<String>> result = (new funcAnato(command)).getToken(rePattern);
    	System.out.println("[in Parser] \u001B[33m"+result+"\u001B[0m");
    	
    	/**
    	 * result = {"idn"=[e1,e2,e3,...], "sens"=[p1,p2,p3,...], "qID"=[q1,q2,q3,...]}
    	 */
    	
    	(new Anatomization(
    			result.get("idn"),
    			result.get("qID"),
    			result.get("sens")
    			
    	)).execute();
    	return 0;
    	
    }
    
    /**
     * execute the {@link #command} provided
     */
    public static int execute() {
    	
    	command = command.trim();
    	String[] operator;
    	
    	/**
    	 * extract operator name in command
    	 */
    	int index = command.indexOf("(");
        if (index >= 0) {        	
            operator = new String[1];
            operator[0] = command.substring(0, index).trim() ;
        } else {
        	operator = command.split("\\s+");
        }
    	 	
        if (operator.length > 0) {
            System.out.println("[in Parser] Call_func = " + operator[0]);
            Object[] functionArgs = {null};
            try {
            	/**
            	 * call the corresponding execute function
            	 */
            	operators.get(operator[0]).apply(functionArgs);
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
