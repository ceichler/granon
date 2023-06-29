package graphVisu;

import java.util.ArrayList;
import java.util.HashSet;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class MyGraph {
	
	private Graph graph;
	


	private static int graphID = 0;
	/**
	 * Style sheet for graphic rendering
	 */
	public static String styleSheet =
			"node {" +
					"	fill-color: black;" +
					//"	text-mode: normal;"+
					//	        "   size: dyn-size;" +
					"}" +
					"node.marked {" +
					"   fill-mode : dyn-plain;" +
					"	fill-color:  yellow, red ;" +
					"}"+
					"node.markedBis {" +
						"   fill-mode : dyn-plain;" +
						"	fill-color:  green ;" +
						"}"+
					"node.nothing {" +
					"	fill-color: black;" +
					"}" +
					"node.markedTer {" +
					"	fill-color: green;" +
					"}";
	
	public MyGraph(HashSet<String> nodes, HashSet<ArrayList<String>> edges) {
		graph = new SingleGraph("Graph "+MyGraph.graphID);
		graph.addAttribute("ui.stylesheet", styleSheet);
		MyGraph.graphID++;
		for (String nodeEle:nodes) {
			graph.addNode(nodeEle);
		}
		for (ArrayList<String> edgeEle: edges) {
			graph.addEdge(edgeEle.get(0), edgeEle.get(1), edgeEle.get(2),true);
		}
		setNodeLabel();
		setEdgeLabel();
		
	}
	
	public void setNodeLabel() {
		for (Node node:graph) {
			node.setAttribute("ui.label", node.getId());
		}
	}
	
	public void setEdgeLabel() {
		for (Edge edge:graph.getEdgeSet()) {
			String edgeId = edge.getId();
			String label = edgeId.substring(0,edgeId.indexOf("$"));
			edge.setAttribute("ui.label", label);
		}
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
		
}
