package executable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import executable.granonui.Tui;
import transformations.operators.*;
import transformations.procedures.*;
import utils.GraGraUtils;
import utils.Pair;

public class HardCodedTests {

	public HardCodedTests() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void testPostPreprocessing() {
		System.out.println("Not yet implemented hardcoding preprocessing");
		HashSet<Pair<String>> excepts = new HashSet<Pair<String>>();
		
		//excepts.add(new Pair("*", "city"));
		
		//excepts.add(new Pair("name", "Stuart"));
		excepts.add(new Pair<String>("knows", "*"));
		HashSet<Pair<String>> where = new HashSet<Pair<String>>();
		//where.add(new Pair("*","*"));
		//where.add(new Pair("livesInA", "*"));
		where.add(new Pair<String>("*", "Stuart"));
		Prepostprocessing pp = new Prepostprocessing(excepts, where);
		try {
			pp.doPreprocessing();
		} catch (InvalidArguments e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraGraUtils.print(Tui.grammar.getHostGraph());
		pp.doPostprocessing();
		GraGraUtils.print(Tui.grammar.getHostGraph());
	}
	
	
	public static void testDelStar() {
		System.out.println("Not yet implemented hardcoding Del(*)");
		GraGraUtils.print(Tui.grammar.getHostGraph());
		(new DeleteNode("*")).execute();
		GraGraUtils.print(Tui.grammar.getHostGraph());
	}
	
	public static void testDelMultiple() {
		System.out.println("Not yet implemented hardcoding Del(*)");
		GraGraUtils.print(Tui.grammar.getHostGraph());
		(new DeleteNode("*","type","Person")).execute();
		GraGraUtils.print(Tui.grammar.getHostGraph());
	}
	
	public static void testRandTransform() {
		(new RandomTransformSrc("type","*", "type", "city", "isA","*", "name", "newName")).execute();
	}
	
	public static void testRandTransformWithX() {
		(new RandomTransformSrc("Paris", null,null, "type", "Person", null, null, "*", "isWorseThan")).execute();
	}
	
	
	public static void testModifyEdge() {
		(new ModifyEdge("type", "*", "*", "Name", "name", "newName")).execute();
	}
	
	public static void testModifyEdgeWithX() {
		(new ModifyEdge("Paris", null, null, null, null, "*", "notType")).execute();
	}
	
	public static void testCutEdge() {
		(new EdgeCut("Paris", null, null, null, null, "type", "pi", "IntermNode", "pf")).execute();
	}
	
	public static void testEdgeReverse() {
		(new EdgeReverse("Paris", null, null, null, null, "type")).execute();
	}
	
	public static void testEdgeChord2() {
		(new EdgeChord("Paris", null,null, null, null, null, null, "pi", "pf", "type")).execute();
	}
	
	public static void testEdgeChord() {
		(new EdgeChord("*", "type","Person", null, null, null, null, "name", "isA", "hasA")).execute();
	}
	
	public static void testModifyEdgeStar() {
		(new ModifyEdge(null, null,"*","*", "type", "truc")).execute();
	}
	
	public static void testRandom() {
		(new RandomSrc("type","*", "type", "city", null,null, "name")).execute();
	}
	//(x, String s, String S, String t, String T, String o, String O, String p)
	public static void testRandomTar() {
		(new RandomTar("*","type", "Person", "isA", "Name", "isA", "Name", "name")).execute();
	}
	

	public static void testLDP() {
		(new LDP("type", "Person", "isA", "Name", "name", 3)).execute();
	}
	
	public static void testCloneSet() {
		(new CloneSet("*", "type", "city", "clone", "Clone")).execute();
	}
	
	
	public static void testAnato() {
		List<String> identifiers = new ArrayList<String>();
		List<String> qIDs  = new ArrayList<String>();
		List<String> sensitives  = new ArrayList<String>();
		identifiers.add("name");
		qIDs.add("knows");
		sensitives.add("livesIn");
		(new Anatomization(identifiers, qIDs, sensitives)).execute();
	}
}

