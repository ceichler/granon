package gui;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.SwingUtilities;

import org.graphstream.ui.view.Viewer;

import agg.xt_basis.Arc;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import executable.granonui.Tui;
import graphVisu.MyGraph;

public class DisplayGraph {
	
	
	/**
	 * get all the att of every nodes in the provided graph
	 * @param graph graph that we want to extract all att
	 * @return ArrayList of all att of a graph
	 */
	public static ArrayList<String> getAtt(Graph graph){
		ArrayList<String> listAtt = new ArrayList<String> ();
		for (Node n : graph.getNodesSet()) {
			// Get node name
			String nodeName = n.getAttribute().getValueAsString(1);
			nodeName = nodeName.replace("\"", "");
			if (listAtt.contains(nodeName)) {
				continue;
			}
			listAtt.add(nodeName);
		}
		// sort the arraylist in alphabetical order
    	listAtt.sort(String::compareToIgnoreCase);
    	listAtt.add(0,"*");
		listAtt.add(0,"null");
		return listAtt;
	}
	
	/**
	 *  get all prop of edges of privided graph
	 * @param graph  the graph that we want to extract all prop
	 * @return ArrayList of all Prop of provided graph
	 */
	public static ArrayList<String> getProp(Graph graph){
		ArrayList<String> listProp = new ArrayList<String> ();
		for (Arc a : graph.getArcsSet()) {

			// Get arc name
			String arcName = a.getAttribute().getValueAsString(1);
			arcName = arcName.replace("\"", "");
			if (listProp.contains(arcName)) {
				continue;
			}
			listProp.add(arcName);
		}
		listProp.sort(String::compareToIgnoreCase);
    	listProp.add(0,"*");
		listProp.add(0,"null");
		return listProp;
	}
	
	/**
	 *  Return a string that represent a graph
	 * @param graph graph that we want to display
	 * @return String graph
	 */
	public static String graphToString(Graph graph) {
		StringBuilder display = new StringBuilder();

		// Display graph infos
		display.append(String.format(
				"[Graph]%nname: %s%nnumber of nodes: %d%nnumber of edges: %d%n",
				graph.getName(),
				graph.getSizeOfNodes(),
				graph.getSizeOfArcs()
		));

		// Display nodes infos
		display.append("\n[Nodes]\n");
		for (Node n : graph.getNodesSet()) {
			// Get node type and name
			String nodeType = n.getType().getStringRepr();
			String nodeName = n.getAttribute().getValueAsString(1);

			// Display node
			display.append(String.format("(%-7s) %s%n", nodeType, nodeName));
		}

		// Display edges infos
		display.append("\n[Edges]\n");
		for (Arc a : graph.getArcsSet()) {
			// Arc and Nodes share a common superclass GraphObject
			Node src = (Node) a.getSource();
			Node dst = (Node) a.getTarget();

			// Get node values
			String srcName = src.getAttribute().getValueAsString(1);
			String dstName = dst.getAttribute().getValueAsString(1);

			// Get arc name
			String arcName = a.getAttribute().getValueAsString(1);

			// Display arc
			display.append(String.format("%10s -- %-8s --> %s%n", srcName, arcName, dstName));
		}

		return display.toString();
	}
	
	
	/**
	 *  Display the TUI graph on a new pop up windows in String form
	 */
	public static void drawTextGraph() {
		String text = graphToString(Tui.grammar.getHostGraph());
		SwingUtilities.invokeLater(() -> {
            new ScrollableTextPane(text).setVisible(true);
        });
	}
	
	/**
	 * Display the graph using GraphStream
	 */
	public static void drawGraph() {
		// HashSet of all nodes
		HashSet<String> nodes = new HashSet<String>();
		// HashSet of all edges
		HashSet<ArrayList<String>> edges = new HashSet<ArrayList<String>>();
				
		
		// get all node's att from provided graph
		for (agg.xt_basis.Node node:Tui.grammar.getHostGraph().getNodesSet()) {
			String att = node.getAttribute().getValueAsString(1).replace("\"", "");
			nodes.add(att);
		}
		
		int count_edge = 0;
		
		// get all edges and puts it into HashSet
		for (agg.xt_basis.Arc edge:Tui.grammar.getHostGraph().getArcsSet()) {
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
		Viewer viewer = mygraph.display(true);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		
	}
}
