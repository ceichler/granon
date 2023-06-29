package graphVisu;

import java.util.ArrayList;
import java.util.HashSet;

import utils.Grammar; 

public class TestGraph {
	public static void main(String[] args) {

		HashSet<String> nodes = new HashSet<String>();
		HashSet<ArrayList<String>> edges = new HashSet<ArrayList<String>>();
		
		Grammar grammar = new Grammar();
		
		for (agg.xt_basis.Node node:grammar.getHostGraph().getNodesSet()) {
			String att = node.getAttribute().getValueAsString(1).replace("\"", "");
			nodes.add(att);
		}
		
		int count_edge = 0;
		for (agg.xt_basis.Arc edge:grammar.getHostGraph().getArcsSet()) {
			ArrayList<String> edgeStr = new ArrayList<String>();
			
			String prop = edge.getAttribute().getValueAsString(1).replace("\"", "")+"$_"+count_edge;
			count_edge++;
			// Arc and Nodes share a common superclass GraphObject
			agg.xt_basis.Node src = (agg.xt_basis.Node) edge.getSource();
			agg.xt_basis.Node dst = (agg.xt_basis.Node) edge.getTarget();

			// Get node values
			String srcName = src.getAttribute().getValueAsString(1).replace("\"", "");
			String dstName = dst.getAttribute().getValueAsString(1).replace("\"", "");
			
			edgeStr.add(prop);
			edgeStr.add(srcName);
			edgeStr.add(dstName);
			edges.add(edgeStr);
		}
		
		MyGraph mygraph = new MyGraph(nodes,edges);
		mygraph.getGraph().display();
		
	}
}
