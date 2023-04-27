package parser;


import transformations.operators.*;
import transformations.procedures.*;
import java.util.*;


/**
 * This class defines a parser to analyze and execute the command
 *
 * @author cnguyen
 */

public class Parser {
	
	/**
	 * 
	 * the command to execute
	 */
	public static String command;
	
	
	/**
	 * execute the command
	 * @return 0 if success, -1 otherwise
	 */
    public static int execute() {
    	
    	command = command.trim();
    	OperatorsHandling tokenHd = new TokenExtraction(command);
    	HashMap<String, ArrayList<String>> result = null;
		try {
			result = tokenHd.getToken();
			tokenHd.checkArgs(result);
		} catch (SyntaxException e) {
			e.printStackTrace();
			return -1;
		}
    	System.out.println("\u001B[36m [result] " + result +"\u001B[0m");
    	HashMap <String,String> execMap = null;
    	if (!result.get("operator").get(0).equals("JoinSet") && !result.get("operator").get(0).equals("Anatomization")) {
    		execMap = tokenHd.generateExecutableMap(result);
    	}   	
    	System.out.println("\u001B[36m [execMap] " + execMap + "\u001B[0m");
    	switch (result.get("operator").get(0)) {
    		case "NewNode":
    			System.out.println("NewNode");
    			(new NewNode(result.get("S_att").get(0))).execute();
    			break;
    		case "DeleteNode":
    			System.out.println("DeleteNode");
    			(new DeleteNode(execMap)).execute();
    		case "EdgeReverse":
    			System.out.println("EdgeReverse");
    			(new EdgeReverse(execMap)).execute();
    			break;
    		case "EdgeCut":
    			System.out.println("EdgeCut");
    			(new EdgeCut(execMap)).execute();
    			break;
    		case "EdgeCopy":
    			System.out.println("EdgeCopy");
    			(new EdgeCopy(execMap)).execute();
    			break;
    		case "EdgeChord":
    			System.out.println("EdgeChord");
    			 (new EdgeChord(execMap)).execute();
    			break;
    		case "EdgeChordKeep":
    			System.out.println("EdgeChordKeep");
    			(new EdgeChordKeep(execMap)).execute();
    			break;
    		case "ModifyEdge":
    			System.out.println("ModifyEdge");
    			(new ModifyEdge(execMap)).execute();
    			break;
    		case "RandomSource":
    			System.out.println("RandomSource");
    			(new RandomSrc(execMap)).execute();
    			break;
    		case "RandomTarget":
    			System.out.println("RandomTarget");
    			(new RandomTar(execMap)).execute();
    			break;
    		case "CloneSet":
    			(new CloneSet(execMap)).execute();
    			System.out.println("CloneSet");
    			break;
    		case "LDP":
    			System.out.println("LDP");
    			(new LDP(execMap)).execute();
    			break;
    		case "Anatomization":
    			System.out.println("Anatomization");
    			(new Anatomization(result.get("idn"),result.get("qID"),result.get("sens"))).execute();
    			break;
    		case "JoinSet":
    			// (new NewNode(result.get("JoinSet").get(1))).execute();
    			// {JoinSet=[hasQI, QI], where=[*, *, Stuart], except=[*, knows, *], operator=[JoinSet]}
    			(new JoinSet(result)).execute();
    			break;
    		default:
    			System.err.println("undefined operator");
    			break;
    	}
    	return 0;
    }
}
