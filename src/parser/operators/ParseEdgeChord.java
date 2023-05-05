package parser.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Graph;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;
import transformations.operators.EdgeChord;

public class ParseEdgeChord extends ParseOperator {

	/**
	 * list of keys for parsing
	 */
	ArrayList<String> listArgKeywords = new ArrayList<String>(Arrays.asList("S","pi1","I","pi2","O","pf"));
	/**
	 * the syntactic constraints
	 */
	ArrayList<String> parameterRequiredForm = new ArrayList<String> (Arrays.asList("S","*","Set","*","Set","Str"));
	
	/**
	 * create parser for EdgeChord
	 * @param command
	 */
	
	public ParseEdgeChord(String command) {
		this.command = command;
	}
	

	/**
	 * Neither S nor O nor I can be the target of pi1 or pi2
	 * @param mapTokens
	 * @throws SyntaxException 
	 */
	private void checkContrains(HashMap<String,ArrayList<String>> mapTokens) throws SyntaxException {
		// get the current graph
		Graph graph = Tui.grammar.getHostGraph();
		
		if (mapTokens.get("pi") == null) {return;}
		
		String pi1 = mapTokens.get("pi1").get(0);
		String pi2 = mapTokens.get("pi2").get(0);
		
		ArrayList<String> listEdgeDst1 = getEdgeDst(graph, pi1);
		ArrayList<String> listEdgeDst2 = getEdgeDst(graph, pi1);
		
		if (listEdgeDst1.contains(mapTokens.get("S").get(2))) {
			throw new SyntaxException(pi1 + "'s destinaton cannot be " + mapTokens.get("S").get(2));
		}
		
		if (listEdgeDst2.contains(mapTokens.get("S").get(2))) {
			throw new SyntaxException(pi2 + "'s destinaton cannot be " + mapTokens.get("S").get(2));
		}
		
		if ( listEdgeDst1.contains(mapTokens.get("O").get(2))) {
			throw new SyntaxException(pi1 + "'s destinaton cannot be " + mapTokens.get("O").get(2));
		}
		
		if (listEdgeDst2.contains(mapTokens.get("S").get(2))) {
			throw new SyntaxException(pi2 + "'s destinaton cannot be " + mapTokens.get("S").get(2));
		}
		
	}
	
	
	
	@Override
	public void execute() throws SyntaxException {
		// listTokens = {X=["new node's att"]}
		HashMap<String,ArrayList<String>> mapTokens = this.getTokensPosArg(listArgKeywords);
		this.checkSyntax(mapTokens, parameterRequiredForm, listArgKeywords);
		
		// execute the operator
		checkContrains(mapTokens);
		(new EdgeChord(mapTokens)).execute();

	}

}
