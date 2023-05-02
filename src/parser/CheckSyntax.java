package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import agg.xt_basis.Arc;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import executable.granonui.Tui;

/**
 * 
 * 
 * class for checking the Syntax of the user's command
 * @author khai
 *
 */

public class CheckSyntax implements CheckArgs{

	/**
	 * this method will check if a Set has valid syntax 
	 * @param setFormCode the code for the Set's form. represented in {@link CheckArgs#parameterRequiredForm}
	 * @exception SyntaxException
	 */
	
	@Override
	public void checkSet(String setFormCode, ArrayList<String> setVal) throws SyntaxException {
		// The number of elements in a Set is extractly 3
		if (setVal.size()!=3) {
			throw new SyntaxException(7,"The number of elements in the Set must be 3");
		}
		// We has two setFormCode, they are "S and "Set". "S" for set's form (x,s,S) and "Set" for set's form (*,o,O)
		ArrayList<String> setForm = CheckArgs.parameterRequiredForm.get(setFormCode);
		
		
		for (int i =0; i<setForm.size();i++) {
			if(setForm.get(i)==null) {continue;}
			if (setVal.get(i) == null && setForm.get(i)!=null) {
				// if we have null in the command but the form is not null ==> invalid syntax. E.g Command: (*,null,*), form: (*,*,*) ==> invalid
				throw new SyntaxException(7,setFormCode + "["+i+ "] null is not allowed");
			}
			// if we have * in the command but the form is "Str" ==> invalid syntax. E.g Command: (*,null,*), form: (Str,null,*) ==> invalid
			else if (setVal.get(i).equals("*") && setForm.get(i).equals("Str")) {
				throw new SyntaxException(7,setFormCode + "["+i+ "]  * is not allowed");
			}
			// this is special case for "Set" setFormCode. e.g Command: ("id105",null,null), form: ("**",null,null) ==> invalid 
			else if (setForm.get(i).equals("**") && !setVal.get(i).equals("*")) {
				throw new SyntaxException(7,setFormCode + "["+i+ "]  must be *");
			}
		}
	}

	/**
	 * check the HashMap if it has valid syntax for execute the operator. If the syntax is failed, the method will throw a SyntaxException
	 * @param map the HashList contains all the information which is extracted from the command
	 * @exception SyntaxException
	 * 
	 */
	
	@Override
	public void checkArgs(final HashMap<String, ArrayList<String>> map) throws SyntaxException{
		HashMap<String, ArrayList<String>> localMap = new HashMap<String, ArrayList<String>>();
		localMap.putAll(map);
		String op = new String(localMap.get("operator").get(0));
		localMap.remove("operator");
		// Special check for JoinSet and Anatomization
		if (op.equals("JoinSet")) {
			ArrayList<String> setForm = CheckArgs.parameterRequiredForm.get(op);
			// check if JoinSet("Str","Str")
			// To test Exception: JoinSet (*,"QI") where {(*,"type","Person"),(*,"knows",*)} except {(*,"liveIn","Paris")}
			// JoinSet ("hasQI",*) where {(*,"type","Person"),(*,"knows",*)} except {(*,"liveIn","Paris")}
			if (localMap.get("JoinSet").get(0).equals("*") || localMap.get("JoinSet").get(1).equals("*") || localMap.get("JoinSet").get(0) == null || localMap.get("JoinSet").get(1) == null) {
				throw new SyntaxException(5," [JoinSet] must define the joined Node/Edge");
			}
			// to test this exception: JoinSet ("hasQI","QI") where {("what","type","Person"),(*,"knows",*)} except {(*,"liveIn","Paris")}
			for (int i = 0; i<localMap.get("where").size();i+=3) {
				ArrayList<String> whereSet = new ArrayList<String>(Arrays.asList(localMap.get("where").get(i),localMap.get("where").get(i+1),localMap.get("where").get(i+2)));
				checkSet("Set",whereSet);
			}
			// to test this exception: JoinSet ("hasQI","QI") where {(*,"type","Person"),(*,"knows",*)} except {("id150","liveIn","Paris")}
			for (int i = 0; i<localMap.get("except").size();i+=3) {
				ArrayList<String> whereSet = new ArrayList<String>(Arrays.asList(localMap.get("except").get(i),localMap.get("except").get(i+1),localMap.get("except").get(i+2)));
				checkSet("Set",whereSet);
			}
			// return when the JoinSet which passed the SyntaxException
			return;
			
		}
		else if (op.equals("Anatomization")) {
			
			for (String key:localMap.keySet()) {
				for (int i = 0; i<localMap.get(key).size();i++) {
					if (localMap.get(key).get(i).equals("*") || localMap.get(key).get(i) == null) {
						throw new SyntaxException(5,"an Edge must be defined by a String (neither * nor null)!");
					}
				}
			}
			
			// return when the Anatomization passed the SyntaxException
			return;
		}
		
		// setForm: example "S","*","Set","Str"
		ArrayList<String> setForm = CheckArgs.parameterRequiredForm.get(op);
		// listKeys: list of param's names eg: [S,pi,O,pf]
		String[] listKeys = OperatorsHandling.listKeys.get(op)[0].split(",");
		// check if the user has entered enough arguments for the operator in the command
		if (setForm.size()!=localMap.size()) {
			throw new SyntaxException(2);
		}
		
		// step through all the arguments in the map
		for (int i=0;i<listKeys.length;i++) {
			// if a, ArrayList of correspoinding key has 3 String inside ==> it must be a Set.
			// e.g: map = {S=[x,s,S], O=[*,o,O],...} map.get("S).size() = [x,s,S].size() = 3
			if (localMap.get(listKeys[i]).size() == 3) {
				if (listKeys[i].equals("S")) {
					checkSet("S",localMap.get(listKeys[i]));
				}else {
					checkSet("Set",localMap.get(listKeys[i]));
				}
			} 
			// else the args is not a Set ==> it has only one String inside the ArrayList
			else {
				// if params at a position can be null ==> it can be null,* or Str
				if(setForm.get(i)==null) {continue;}
				// this is very special, use for LDP only
				if(setForm.get(i).equals("Num")) {
					try {
						// check if this args is a number or not
						Integer.parseInt(localMap.get(listKeys[i]).get(0));
					}catch (NumberFormatException e){
						throw new SyntaxException("k must be a number");
					}
				}
				// if the params cannot be null --> error for null args in this position
				if (localMap.get(listKeys[i]).get(0) == null && setForm.get(i)!=null) {
					throw new SyntaxException(4,listKeys[i] + " cannot be null");
				}
				// maybe redundant (must be verified)
				else if (localMap.get(listKeys[i]).get(0) == null && setForm.get(i)==null){
					continue;
				}
				// if we have * at a positon that must be Str --> error
				else if(localMap.get(listKeys[i]).get(0).equals("*") && setForm.get(i).equals("Str")) {
					throw new SyntaxException(4,listKeys[i]+ " cannot be *");
				}
			}
			
		}
		this.checkEdgeContraints(map);
		
	}
	
		
	
	
	// In development
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
				String dstName = dst.getAttribute().getValueAsString(1).replace("\"", "");
				dstsName.add(dstName);
			}
		}
		return dstsName;
	}

	/**
	 * This method will check for all specific constraint and throw a SyntaxException if any constraint violated
	 * @param map the map which contains al the arguments from user's command
	 * @throws SyntaxException 
	 * 
	 */
	@Override
	public void checkEdgeContraints(HashMap<String, ArrayList<String>> map) throws SyntaxException {
		
		// get the current graph
		Graph graph = Tui.grammar.getHostGraph();
		
		
		//get the operator's name
		String op = new String(map.get("operator").get(0));
		// System.out.println("\u001B[32m"+op+"\u001B[0m");
		
		// if the current operator has no specific contrains ---> return  
		if (!CheckArgs.specificConstraints.containsKey(op)) {return;}

		// get the contrains list of the current operator
		ArrayList<String> listConstraints = CheckArgs.specificConstraints.get(op);
		
		
		System.out.println("[List Constraints]" + listConstraints.toString());
		
		
		// check for edge contrains, each 3 elements represented the contrains
		// 1st element = type of constrains
		// 2nd element = main object
		// 3rt element = related objects
		for (int i=0 ; i < listConstraints.size(); i+= 3) {

			if (listConstraints.get(i).equals("NotTarget")) {
				
				// get the edge's prop
				String edgeToCheck = map.get(listConstraints.get(i+1)).get(0);
				
				// Get the destination of the provied edge
				ArrayList<String> dstNames = getEdgeDst(graph, edgeToCheck);
				if (dstNames==null) {
					System.out.println(edgeToCheck);
					throw new SyntaxException("Something went wrong");
				}
				
				System.out.println("[Dst]" + dstNames.toString());
				
				// get the list of node's "att" that cannot be target of provided edge
				String[] listRelatedObjectKey = listConstraints.get(i+2).split(",");
				
				
				// map has the form {"O"=[*,o,O], "S"=[x,s,S], T=[*,t,T],....}
				for (String relatedObjectKey : listRelatedObjectKey) {
					
					String objectAtt = map.get(relatedObjectKey).get(2);
					if (dstNames.contains(objectAtt)) {
						String error = "The "+edgeToCheck+"'s target cannot be "+objectAtt;
						throw new SyntaxException(8, error);
					}
				
				
				}
				
			}
		}
		
		
	}

}
