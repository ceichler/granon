package utils;


import agg.attribute.AttrInstance;
import agg.attribute.impl.ValueMember;
import agg.attribute.impl.ValueTuple;
import agg.xt_basis.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class implements some useful methods for graphs manipulation: printing, saving...
 *
 * @author ceichler
 */
public final class GraGraUtils {

	/* CONSTANTS */

	public static final String STAR = "*";
	public static final Type TNODE = Grammar.graphGrammar.getTypeSet().getTypeByName("node");
	public static final Type TEDGE = Grammar.graphGrammar.getTypeSet().getTypeByName("relation");


	/**
	 * Private constructor as all method are static
	 */
	private GraGraUtils() {
		throw new AssertionError();
	}

	/* UTILS */

	/**
	 * Save the current gra gra in fileName
	 *
	 * @param fileName where to save
	 */
	public static void save(String fileName, GraGra graphGrammar) {
		graphGrammar.save(fileName);
		System.out.println(String.format("Grammar %s saved in %s.", graphGrammar.getName(), fileName));
	}


	/**
	 * Setting an attribute attName of an element with a value
	 *
	 * @param element attributed element with an attribute to set
	 * @param attName name of the attribute to set
	 * @param value   value to set the attribute to
	 */
	public static void setAtt(GraphObject element, String attName, Object value) {
		//To handle attributes
		AttrInstance attrInstance = element.getAttribute();

		// Set values of attributes
		ValueTuple vt = (ValueTuple) attrInstance;
		ValueMember vm = (ValueMember) vt.getMemberAt(attName);
		vm.setExprAsObject(value);
		vm.checkValidity();
	}



	/**
	 * Print a graph
	 *
	 * @param graph the graph to print
	 */
	public static void print(Graph graph) {
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

		System.out.println(display);
	}


	/**
	 * Looks for and returns a GraphObject with a type and an attribute fixed with some value in a graph
	 *
	 * @param type    the type of the searched GraphObject (e.g., Prop, Class...)
	 * @param attName the name of the attribute of interest
	 * @param att     the value the attribute attName should have
	 * @param graph   the graph in which to look for the object
	 * @return a GraphObject from graph whose attributes attName equal att
	 */
	public static GraphObject getTypedObjectWithAtt(Type type, Object att, String attName, Graph graph) {

		for (GraphObject obj : graph.getElementsOfTypeAsVector(type)) {
			if (att.equals(obj.getAttribute().getValueAt(attName))) return obj;
		}

		return null;
	}



	
	/**
	 * Apply some rule to the host graph of a RDFSgrammar
	 * No idea why this works better than transform.
	 * This applies all the matches found by the match creation through nextCompletion
	 * @param rule       the rule to apply
	 * @param rdfsGraGra to what it should be apply
	 * @return whether a transformation has been conducted
	 */
	public static boolean transformAll(Report report, Rule rule, Grammar gram) {
		

		//adding the rule to the grammar
		Grammar.graphGrammar.addRule(rule);
		rule.setEnabled(true);
		boolean ok = false;
		//creating matches with a strategy
		Match match = Grammar.graphGrammar.createMatch(rule);
		match.setCompletionStrategy(Grammar.graphGrammar.getMorphismCompletionStrategy());

		
		

		// Applying 
		
		while (match.nextCompletion()) {
			// Checking NACs & pacs & valdity
			if(match.isValid()) {

				//If NACs ok, transforming
				ok = true;
				try {

					StaticStep.execute(match);
				} catch (TypeException e) {
					ok = false;
					System.err.println(match.getLastErrorMsg());
					e.printStackTrace();
				}
			}
		}

		
		// System.out.println("End completion");
		if(!ok)  report.addMessage(match.getLastErrorMsg());
		match.dispose();
		Grammar.graphGrammar.removeRule(rule);
		
		return ok;
	}


	/**
	 * Apply some rule to the host graph of a RDFSgrammar
	 * Apply a single completion per match created
	 * However, recreates match as long as a match with at least one completion exists
	 * @param rule       the rule to apply
	 * @param rdfsGraGra to what it should be apply
	 * @return whether a transformation has been conducted
	 */
	public static boolean transformAllRand(Report report, Rule rule, Grammar gram) {
		

		//adding the rule to the grammar
		Grammar.graphGrammar.addRule(rule);
		rule.setEnabled(true);
		boolean ok = false;
		//creating matches with a strategy
		Match match = Grammar.graphGrammar.createMatch(rule);
		MorphCompletionStrategy cs = Grammar.graphGrammar.getMorphismCompletionStrategy();
		cs.setRandomisedDomain(true);
		cs.enableParallelSearch(false);
		match.setCompletionStrategy(cs);
		


		// Applying
		while (match.nextCompletion()) {

			// Checking NACs & pacs & valdity
			if(match.isValid()) {
				//If NACs ok, transforming
				ok = true;
				try {
					StaticStep.execute(match);
				} catch (TypeException e) {
					ok = false;
					System.err.println(match.getLastErrorMsg());
					e.printStackTrace();
				}
				
				match.dispose();
				match = Grammar.graphGrammar.createMatch(rule);	
				match.setCompletionStrategy(Grammar.graphGrammar.getMorphismCompletionStrategy());
				
				//GraGraUtils.print(Grammar.graphGrammar.getGraph());
				
			}
			

		}

		if(!ok)  report.addMessage(match.getLastErrorMsg());
		match.dispose();
		
		Grammar.graphGrammar.removeRule(rule);
		return ok;
	}
}