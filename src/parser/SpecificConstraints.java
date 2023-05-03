package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Arc;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import executable.granonui.Tui;

public class SpecificConstraints {
	
	/**
	 * 
	 * mapForCheck is the map obtained after parsing the user's command
	 * 
	 * E.g
	 * {S=[id107, *, *], pf=[cousin], pi=[knows], operator=[EdgeCopy], O=[*, isA, Name]}
	 * mapForCheck = {S=[id107, *, *], pf=[cousin], pi=[knows], operator=[EdgeCopy], O=[*, isA, Name]}
	 * 
	 */
	HashMap<String,ArrayList<String>> mapForCheck;
	
	
	
	/**
	 *  retrictedTargetNode lists nodes that cannot be the target of some particular edge
	 */
	public static final ArrayList<String> retrictedTargetNode = new ArrayList<String>(Arrays.asList("S","O","I","T"));
	
	
	
	/**
	 * retrictedEde lists nodes with some specific target restrictions
	 */
	public static final ArrayList<String> retrictedEdge = new ArrayList<String>(Arrays.asList("p","pi","pi1","pi2"));
	
	
	
	/**
	 * create object with the map extracted form user's command
	 * @param mapForCheck
	 */
	public SpecificConstraints(HashMap<String,ArrayList<String>> mapForCheck) {
		this.mapForCheck = mapForCheck;
	}
	
	
	
	/**
	 * 
	 * This method is used to get all destination's "att" of an prop in provided Graph
	 * @param graph current graph
	 * @param prop  the Edge's "prop" 
	 */
	public ArrayList<String> getEdgeDst(Graph graph, String prop) {
		ArrayList<String> dstsName = new ArrayList<String>();
		for (Arc a : graph.getArcsSet()) {
			// Get arc "prop"
			String arcProp = a.getAttribute().getValueAsString(1).replace("\"", "");
			if (arcProp.equals(prop)) {
				// Get source node and destination node
				Node dst = (Node) a.getTarget();
				String dstName = dst.getAttribute().getValueAsString(1).replace("\"", ""); // att inside the graph appears as "att" ===> must remove the ""
				dstsName.add(dstName);
			}
		}
		return dstsName;
	}
	
	
		
	/**
	 * 
	 * check if the operator can execute with actual map without SyntaxException
	 * @throws SyntaxException 
	 * 
	 */
	public void checkTarget() throws SyntaxException {
		// get the current graph
		Graph graph = Tui.grammar.getHostGraph();
		
		
		for (String actualEdge:retrictedEdge ) {
			if (!mapForCheck.containsKey(actualEdge)) {continue;}
		
			// actualEdge is just the key in the formular, to check this retriction we must define the real one in the graph
			String realEdge = mapForCheck.get(actualEdge).get(0);
			
			
			for (String cannotBeTarNode:retrictedTargetNode) {
				if (!mapForCheck.containsKey(cannotBeTarNode)) {continue;}
				
				// cannotBeTarNode is just the key in the formular, to check this retriction we must define the real one in the graph
				String realTarNode = mapForCheck.get(cannotBeTarNode).get(2);
				
				
				ArrayList<String> listEdgeDst = getEdgeDst(graph, realEdge);

				if (listEdgeDst.contains(realTarNode)) {
					String error = "The "+actualEdge+"'s target cannot be "+realTarNode;
					throw new SyntaxException(8, error);
				}
				
				
			}
		}

	}
	
	
}
