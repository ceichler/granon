package parser;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * abstract class for command analyzing
 * @author khai
 *
 */


public abstract class OperatorsHandling{
	/**
	 * the provided command
	 */
	String command;
	/**
	 * 
	 * the HashMap contain all key for identifying the command's arguments 
	 * 
	 */
	
	public static final HashMap<String,String[]> listKeys = new HashMap<String, String[]>(){{
	// operateurs
	put("DeleteNode",			new String[]{"X"});
	put("NewNode",				new String[]{"X_att"});
	put("EdgeReverse",			new String[]{"S","p","O"});
	put("EdgeCut",				new String[]{"S","pi","O","pf1","interm","pf2"});
	put("EdgeCopy",				new String[]{"S","pi","O","pf"});
	put("EdgeChord",			new String[]{"S","pi1","I","pi2","O","pf"});
	put("EdgeChordKeep",		new String[]{"S","pi1","I","pi2","O","pf"});
	put("ModifyEdge",			new String[]{"S","O","pi","pf"});
	put("RandomTransformSource",new String[]{"S","pi","O","T","pf"});
	put("RandomTransformTarget",new String[]{"S","pi","O","T","pf"});
	put("CloneSet", 			new String[]{"S","c","C_att"});	
	put("JoinSet",				new String[]{"JoinSet","where","except"});	// for description only, no need this thing in the code
	
	
	// Procedures
	put("RandomSource", 		new String[]{"S","p","O","T"});
	put("RandomTarget", 		new String[]{"S","p","O","T"});
	put("LDP",					new String[]{"S","p","O","k"});
	put("Anatomization", 		new String[]{"idn","qID","sens"}); // for description only, no need this thing in the code
	
	
	// Sets
	put("S",					new String[]{"x","s","S"});
	put("X",					new String[]{"X","s","S"});
	put("O",					new String[]{"*","o","O"});
	put("T",					new String[]{"*","t","T"});
	put("I",					new String[]{"*","i","I"});
	
	
	//Specific _att
	put("X_att",				new String[] {"X"});
	put("C_att",				new String[] {"C"});
}};

	
	/**
	 * create the command analyzer
	 * @param command
	 */
	public OperatorsHandling(String command) {
		this.command = command;
	}
	/**
	 * function that parses the command and returns the tokens after analyzing the command
	 * @return HashMap contains all token inside the command
	 * @throws SyntaxException 
	 */
	public abstract HashMap<String,ArrayList<String>> getToken() throws SyntaxException;

	/**
	 * function that create the Params for execute the opreators
	 * @param tokens contains all token after analyzing the command
	 * @return HashMap contains the information for executable
	 */
	
	public abstract HashMap<String, String> generateExecutableMap(HashMap<String, ArrayList<String>> tokens);
}
