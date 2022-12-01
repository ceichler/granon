package transformations.procedures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import transformations.operators.DeleteNode;
import transformations.operators.EdgeChordKeep;
import transformations.operators.EdgeCopy;
import transformations.operators.EdgeCut;
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
	
	public void execute() {
		
		
		for(String ei : e) {			
			(new EdgeCut(GraGraUtils.STAR, null,null,null,null, ei, "pf1", "ToBeDeleted", "pf2")).execute();
			(new DeleteNode("ToBeDeleted")).execute();
		}
		
		
		(new NewNode("QI")).execute();
		
		for(String qj : q) {
			Set<Pair<String>> where = new HashSet<Pair<String>>();
			where.add(new Pair<String>(qj,GraGraUtils.STAR));
			(new JoinSet(null, where, "QI", "hasQI")).execute();;
		}
		for(String pi : p) {
			(new EdgeChordKeep(GraGraUtils.STAR, "hasQI","QI", null, null, null, null, pi, "inGroup", "$"+pi)).execute();	
			(new EdgeCut(GraGraUtils.STAR, "hasQI","QI",null,null, pi, "pf1", "ToBeDeleted", "pf2")).execute();
			(new DeleteNode("ToBeDeleted")).execute();
			
		}

		for(String pi :p) {
			(new EdgeCopy(null, null, null, null, "$"+pi, pi)).execute();
			(new EdgeCut(GraGraUtils.STAR,  null,null,null,null, "$"+pi, "pf1", "ToBeDeleted", "pf2")).execute();
			(new DeleteNode("ToBeDeleted")).execute();
		}
		
		(new DeleteNode("QI")).execute();

		
	}

}
