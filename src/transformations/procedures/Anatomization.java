package transformations.procedures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import executable.granonui.Tui;
import transformations.operators.DeleteNode;
import transformations.operators.EdgeChord;
import transformations.operators.EdgeChordKeep;
import transformations.operators.EdgeCopy;
import transformations.operators.EdgeCut;
import transformations.operators.EdgeReverse;
import transformations.operators.JoinSet;
import transformations.operators.NewNode;
import utils.GraGraUtils;
import utils.Pair;

public class Anatomization {

	private List<String> e, q, p;
	public Anatomization(List<String> identifiers, List<String> qID, List<String> sensitives) {
		this.e = identifiers;
		this.q = qID;
		this.p = sensitives;
	}
	
	public Anatomization(HashMap<String,ArrayList<String>> mapTokens) {
		this.e = mapTokens.get("idn");
		this.q = mapTokens.get("qID");
		this.p = mapTokens.get("sens");
	}
	
	public void execute() {
		
		
		/**Delete identifiers
		 * 
		 */
		
		for(String ei : e) {			
			(new EdgeCut(GraGraUtils.STAR, null,null,null,null, ei, "pf1", "ToBeDeleted", "pf2")).execute();
			(new DeleteNode("ToBeDeleted")).execute();
		}
		
		
		/**
		 * Tag nodes that have QID
		 */
		(new NewNode("QI")).execute();
		
		for(String qj : q) {
			Set<Pair<String>> where = new HashSet<Pair<String>>();
			where.add(new Pair<String>(qj,GraGraUtils.STAR));
			(new JoinSet(null, where, "QI", "hasQI")).execute();;
		}
		
		/**
		 * Link node to the generalization of their sensitive attribut if they have a QID with $+pi and delete pi
		 */
		for(String pi : p) {
			(new EdgeChordKeep(GraGraUtils.STAR, "hasQI","QI", null, null, null, null, pi, "inGroup", "$"+pi)).execute();	
			
			//DO STUFFS HERE
			
			//If no multiple sensitive value
			/*
			//Copy $pi into $$pi
			//(new EdgeCopy(null, null, null, null, "$"+pi, "$$"+pi)).execute();
			// Reverse $$pi
			//(new EdgeReverse(GraGraUtils.STAR, null,null,null, null,"$$"+pi)).execute();
			//Chord replacing the link from the group to the individual and to the sensitive value by a ling from grp to sensitive value
			/(new EdgeChord(GraGraUtils.STAR, null, null, null, null, null,  null,  "$$"+pi, pi, "hasOne")).execute();
			*/
			
			//Cut SIO to identify the sensitive values and isolate them
			(new EdgeCut(GraGraUtils.STAR, "hasQI","QI",null, null, pi, "SI"+pi, "I"+pi, "IO"+pi)).execute();
			

			
			//Chord IOG, linking I to G directly
			(new EdgeChordKeep("I"+pi, null, null, null, null, null,  null,  "IO"+pi, "inGroup", "IG"+pi)).execute();
			

			
			//Reverse IG
			(new EdgeReverse(GraGraUtils.STAR, null,null,null, null,"IG"+pi)).execute();
			
			
			//Chord GIO
			(new EdgeChord(GraGraUtils.STAR, null, null, null, null, null,  null,  "IG"+pi, "IO"+pi, "hasOne")).execute();

			
			//Delete I
			(new DeleteNode("I"+pi)).execute();
		}

		/**
		 * Copy $pi in pi and delete $pi
		 */
		for(String pi :p) {
			(new EdgeCopy(null, null, null, null, "$"+pi, pi)).execute();
			(new EdgeCut(GraGraUtils.STAR,  null,null,null,null, "$"+pi, "pf1", "ToBeDeleted", "pf2")).execute();
			(new DeleteNode("ToBeDeleted")).execute();
		}
		
		(new DeleteNode("QI")).execute();

		
	}

}
