package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.EdgeCopy;

public class ParseEdgeCopy extends ParseOperator{

	
	/**
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi","O","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","Str"));
	
	/**
	 * create parser for EdgeCopy
	 * @param command
	 */
	
	public ParseEdgeCopy(String command) {
		this.command = command;
	}
	
	
	/**
	 * Neither S nor O can be the target of pi
	 * @param mapTokens
	 * @throws SyntaxException 
	 */
	private void checkContrains(HashMap<String,ArrayList<String>> mapTokens) throws SyntaxException {
		// get the current graph
		Graph graph = Tui.grammar.getHostGraph();
		
		if (mapTokens.get("pi") == null) {return;}
		
		String pi = mapTokens.get("pi").get(0);
		
		ArrayList<String> listEdgeDst = getEdgeDst(graph, pi);
		
		if (listEdgeDst.contains(mapTokens.get("S").get(2))) {
			throw new SyntaxException(pi + "'s destinaton cannot be " + mapTokens.get("S").get(2));
		}
		
		if ( listEdgeDst.contains(mapTokens.get("O").get(2))) {
			throw new SyntaxException(pi + "'s destinaton cannot be " + mapTokens.get("O").get(2));
		}
		
	}
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		
		// execute the operator
		checkContrains(mapTokens);
		(new EdgeCopy(mapTokens)).execute();
		
	}

}
