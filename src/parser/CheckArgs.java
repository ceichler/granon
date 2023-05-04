package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public interface CheckArgs {
	/**
	 * 
	 * 
	 * parameterRequiredForm contains required  arguments in a specific syntax <br>
	 * all the Set "O,I,T,X" can have at least the form **,null,null. <br>
	 * <p>
	 * if we have null for a params, it's to say that it's value can be null,* or a String <br>
	 * if we have * for a params, it's to say that it's value can be * or a String and cannot be null. <br>
	 * if we have String for a params, it's to say that it's value cannot be * or null. <br>
	 * </p>
	 * a special case is **, this means that the value of this param must be * (e.g O=(**,o,O)) <br>
	 * 
	 * the order of the parameters in the ArrayList is exactly the same as them in the {@link OperatorsHandling#listKeys} of class {@link OperatorsHandling} <br>
	 * @see OperatorsHandling
	 * 
	 */
	public static final HashMap<String, ArrayList<String>> parameterRequiredForm = new HashMap<String, ArrayList<String>>(){{
		/**
		 *  String
		 */
		put("Str", new ArrayList<>(Arrays.asList("String"))); // for the params cannot  be both * and null
		
		/**
		 *  Set
		 */
		put("S", new ArrayList<>(Arrays.asList("*",null,null)));    // for S and X
		put("Set", new ArrayList<>(Arrays.asList("**",null,null))); // for O,I,T
		
		/**
		 * list Set
		 */
		put("ListSet", new ArrayList<>(Arrays.asList("where","except")));
		/**
		 * list Str
		 */
		put("ListStr", new ArrayList<>(Arrays.asList("JoinSet","idn","qID","sens")));
		
		/**
		 *  operateurs
		 */
		// DeleteNode(X)
		put("DeleteNode",			new ArrayList<>(Arrays.asList("S")));
		// NewNode(X_att)
		put("NewNode",				new ArrayList<>(Arrays.asList("Str")));
		// EdgeReverse(S,p,O)
		put("EdgeReverse",			new ArrayList<>(Arrays.asList("S","*","Set")));
		// EdgeCut(S,pi,O,pf1,interm,pf2)
		put("EdgeCut",				new ArrayList<>(Arrays.asList("S","*","Set","Str","Str","Str")));
		// EdgeCopy(S,pi,O,pf)
		put("EdgeCopy",				new ArrayList<>(Arrays.asList("S","*","Set","Str")));
		// EdgeChord(S,pi1,I,pi2,O,pf)
		put("EdgeChord",			new ArrayList<>(Arrays.asList("S","*","Set","*","Set","Str")));
		// EdgeChordKeep(S,pi1,I,pi2,O,pf)
		put("EdgeChordKeep",		new ArrayList<>(Arrays.asList("S","*","Set","*","Set","Str")));
		// ModifyEdge(S,O,pi,pf)
		put("ModifyEdge",			new ArrayList<>(Arrays.asList("S","Set","*","Str")));
		// RandomTransformSource(S,pi,O,T,pf)
		put("RandomTransformSource",new ArrayList<>(Arrays.asList("S","*","Set","Set","Str")));
		// RandomTransformTarget(S,pi,O,T,pf)
		put("RandomTransformTarget",new ArrayList<>(Arrays.asList("S","*","Set","Set","Str")));
		// CloneSet(S,c,C_att)
		put("CloneSet", 			new ArrayList<>(Arrays.asList("S","Str","Str")));
		// Need a specific definition
		// JoinSet([0],[1]) where [2] except [3]
		put("JoinSet",				new ArrayList<>(Arrays.asList("ListStr","ListSet","ListSet"))); 
		
		
		/**
		 *  Procedure
		 */
		// RandomSource(S,p,O,T)    // like RandomTransformSource(S,pi,O,T,pf) but pf was defined  
		put("RandomSource", 		new ArrayList<>(Arrays.asList("S","*","Set","Set")));
		// RandomTarget(S,p,O,T)    // like RandomTransformTarget(S,pi,O,T,pf) but pf was defined 
		put("RandomTarget", 		new ArrayList<>(Arrays.asList("S","*","Set","Set")));
		// LDP(S,p,O,k)				//p cannot be * because LDP use p as pf in EdgeChord
		// put("LDP",					new ArrayList<>(Arrays.asList("S","Str","Set","Str"))); // this is very special, k must be a number (in development)
		put("LDP",					new ArrayList<>(Arrays.asList("S","Str","Set","Num")));
		// Need a specific definition
		// {idn = ["Str","Str",...],sens = ["Str","Str",...],qID = ["Str","Str",...]}
		put("Anatomization", 		new ArrayList<>(Arrays.asList("ListStr","ListStr","ListStr")));
		
	}};

	
	/**
	 * Check if the actual Set is satisfied or not
	 * @param setFormCode form of set predefined in {@link #parameterRequiredForm}
	 * @param setVal the set to be checked
	 * @return true if the Set is satisfied ortherwise false
	 * @throws SyntaxException 
	 */
	public void checkSet(String setFormCode, ArrayList<String> setVal) throws SyntaxException;
	/**
	 * Check if the actuals args are satisfied or not
	 * @param map a HashMap of arguments and its value
	 * @return true if the args are satisfied ortherwise false
	 */
	public void checkArgs(HashMap<String,ArrayList<String>> map) throws SyntaxException;
	
	
}
