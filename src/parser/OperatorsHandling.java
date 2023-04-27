package parser;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * abstract class for command analyzing
 * @author khai
 *
 */


public abstract class OperatorsHandling implements CheckArgs{
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
	put("DeleteNode",			new String[]{"X",						"X=X,s,S"});
	put("NewNode",				new String[]{"X_att",					"X_att=X"});
	put("EdgeReverse",			new String[]{"S,p,O",					"S=x,s,S","O=o_x,o,O"});
	put("EdgeCut",				new String[]{"S,pi,O,pf1,interm,pf2",	"S=x,s,S","O=o_x,o,O"});
	put("EdgeCopy",				new String[]{"S,pi,O,pf",				"S=x,s,S","O=o_x,o,O"});
	put("EdgeChord",			new String[]{"S,pi1,I,pi2,O,pf",		"S=x,s,S","I=i_x,i,I","O=o_x,o,O"});
	put("EdgeChordKeep",		new String[]{"S,pi1,I,pi2,O,pf",		"S=x,s,S","I=i_x,i,I","O=o_x,o,O"});
	put("ModifyEdge",			new String[]{"S,O,pi,pf",				"S=x,s,S","O=o_x,o,O"});
	put("RandomTransformSource",new String[]{"S,pi,O,T,pf",				"S=x,s,S","O=o_x,o,O","T=t_x,x,T"});
	put("RandomTransformTarget",new String[]{"S,pi,O,T,pf",				"S=x,s,S","O=o_x,o,O","T=t_x,t,T"});
	put("CloneSet", 			new String[]{"S,c,C_att",				"S=src,s,S","C_att=C"});	
	put("JoinSet",				new String[]{"JoinSet,where,except",						""});	// for description only, no need this thing in the code
	
	
	// Procedure
	put("RandomSource", 		new String[]{"S,p,O,T",					"S=x,s,S","O=o_x,o,O","T=t_x,t,T"});
	put("RandomTarget", 		new String[]{"S,p,O,T",					"S=x,s,S","O=o_x,o,O","T=t_x,t,T"});
	put("LDP",					new String[]{"S,p,O,k",					"S=x,s,S","O=o_x,o,O"});
	put("Anatomization", 		new String[]{"idn,qID,sens",			""}); // for description only, no need this thing in the code
	
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
