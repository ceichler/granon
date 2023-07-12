package parser.operators;

import java.util.*;
import java.util.regex.*;

import agg.xt_basis.*;
import executable.granonui.Tui;
import parser.exceptions.SyntaxException;


/**
 * This class contains methods to parse commands of the form positional arguments
 * @author khai
 *
 */


public abstract class ParseOperator {
	
	/**
	 * command entered by user. 
	 */
	protected String command;
	/**
	 * regex pattern for positional arguments to extract the arguments from user's command
	 */
	final String regex = "(\\([^\\\\)\\\\(]+\\))|(\\\"[^\\\"]+\\\")|(\\*)|(null)";
	
	/**
	 * create Parse class with the user's command
	 * @param command user's command
	 */
	public ParseOperator(String command) {
		this.command = command;
	}
	
	
	/**
	 * execute the user's command
	 * @throws SyntaxException
	 */
	public abstract void execute() throws SyntaxException;
	
	
	/**
	 * extract the token (the arguments) from the user's command
	 * @param listArgKeywords ArrayList contains keywords that represent parameters of user's commands <br> E.g: S,c,C_att in CloneSet (S,c,C_att)
	 * @return The HashMap includes the keyword of the parameters provided by listArgKeyword and the corresponding arguments extracted from the user command
	 * @throws SyntaxException 
	 */
	public HashMap<String,ArrayList<String>> getTokensPosArg(ArrayList<String> listArgKeywords) throws SyntaxException{
		
		HashMap<String,ArrayList<String>> mapTokens = new HashMap<String,ArrayList<String>>();

			
			// here is the script for Positional Arguments
			if (!command.contains("=")) {
				
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher(command);
		        
		        // because of the positional arguments form, count is used to match the keys with their value in command
		        int count = 0;
		        while (matcher.find()) {
		        	
		        	 // because count starts at 0
		        	 if (count >= listArgKeywords.size()) {
		        		 String errorStr = new String("Redundant arguments. [Syntax] " + 
		 						this.getClass().getSimpleName().replace("Parse", "") +
		 						"(");
		 				for (String keyword:listArgKeywords) {
		 					errorStr = errorStr + keyword + ",";
		 				}
		 				errorStr = errorStr.substring(0, errorStr.length()-1);
		 				errorStr = errorStr + ")";
		 				
		 				// throws exception:  Redundant arguments. [Syntax] <Operator>(key1,key2,key3,...)
		 				throw new SyntaxException(errorStr);
					}
		        	
		        	
		            String paramsStr = matcher.group(0);
		            // remove unnecessary characters (')' and '"')
		            paramsStr = paramsStr.replace("(", "").replace(")", "").replace("\"", "");
		            String[] afterSplit = paramsStr.split(","); 
		            // trim all elements in set (to remove extra white spaces from the beginning and end of the input string)
		            for (int compt = 0; compt<afterSplit.length;compt++) {
		            	afterSplit[compt] = afterSplit[compt].trim();
		            }
		            
		            // arraylist of all arguments after split and trim
		            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(afterSplit));
		            
		            // replace all String "null" by null 
		            for (int i = 0;i < temp.size();i++) {
		            	if (temp.get(i).equals("null")) {
		            		temp.set(i, null);
		            	}
		            }
		            
		            // put all arguments into map 
		            mapTokens.put(listArgKeywords.get(count), temp);
		            count++;
		        }
			}
			
			// check if there are some missing arguments exceptions
			if (mapTokens.size() != listArgKeywords.size()) {
				String errorStr = new String("Missing arguments. [Syntax] " + 
						this.getClass().getSimpleName().replace("Parse", "") +
						"(");
				for (String keyword:listArgKeywords) {
					errorStr = errorStr + keyword + ",";
				}
				errorStr = errorStr.substring(0, errorStr.length()-1);
				errorStr = errorStr + ")";
				throw new SyntaxException(errorStr);
			}
			
		return mapTokens;
	}

	
	/**
	 * Check if the provided Set follows the required syntax
	 * @param setFormCode required form of Set S=("String",null,null) or Set=("*",null,null)
	 * @param setVal the set for checking syntax
	 * @throws SyntaxException
	 */
	public void checkSet(String setFormCode, ArrayList<String> setVal) throws SyntaxException {
		
		// this is the method that will verify the syntax of Set. Set is the triplet (x,s,S) which represent a set of node
		
		// The number of elements in a Set is exactly 3, if the provided ArrayList size if not 3 ===> it's not a Set ==> exception
		if (setVal.size()!=3) {
			String error = new String("(");
			for (String ele:setVal) {error += "\""+ele+"\","; }
			// This statement is important, although it is not standardized but temporary nodes or edges are created as ".$+timestamp()" in granon (e.g. LDP)
			error = error.replaceAll(".$", ")");
			error +=  " is not a Set. The number of elements in the Set must be 3";
			throw new SyntaxException(error);
		}
		
		
		// We has two setFormCode, they are "S and "Set". "S" for set's form (x,s,S) and "Set" for set's form (*,o,O)
		ArrayList<String> setForm ;
		if (setFormCode.equals("S")) {
			// "*" accept * and Str
			setForm = new ArrayList<>(Arrays.asList("*",null,null));
		} else if (setFormCode.equals("Set")) {
			// "**" only accept "*" 
			setForm = new ArrayList<>(Arrays.asList("**",null,null));
		} else {
			// This exception occurs very rarely, if it does, check setFormCode or parameterRequiredFrom of Parse[Operator] respectively.
			// It's to say that all the Set in this language have form S=(*,null,null) or Set=(**,null,null)
			// if another Set structure is added, add a new  "if" condition for it
			throw new SyntaxException("Invalid setFormCode. " + setFormCode+ " ?");
		}
		
		// now we loop through this Set's form (that was generated in the code above (*,null,null) or (**,null,null))
		// if there are new Set's form is added into the language, please update this for loop
		for (int i =0; i<setForm.size();i++) {
			
			// if the actual form is null ===> It accepts both Str,* and null ===> continue 
			if(setForm.get(i)==null) {continue;}
			
			// if we have null in the command but the form is not null ==> invalid syntax. E.g Command: (*,null,*), form: (*,*,*) ==> invalid
			if (setVal.get(i) == null && setForm.get(i)!=null) {
				throw new SyntaxException(setVal.toString() + " " + setFormCode + "["+i+ "] null is not allowed");
			}
			
			// if we have * in the command but the form is "Str" ==> invalid syntax. E.g Command: (*,null,*), form: (Str,null,*) ==> invalid
			else if (setVal.get(i).equals("*") && setForm.get(i).equals("Str")) {
				throw new SyntaxException(setVal.toString() + " " + setFormCode + "["+i+ "]  * is not allowed");
			}
			
			// this is special case for "Set" setFormCode. e.g Command: ("id105",null,null), form: ("**",null,null) ==> invalid 
			else if (setForm.get(i).equals("**") && !setVal.get(i).equals("*")) {
				throw new SyntaxException(setVal.toString() + " " + setFormCode + "["+i+ "]  must be *");
			}
			
		}
		
	}
	
	/**
	 * Verify the user's command syntax using a HashMap containing all the tokens extracted from it
	 * @param mapTokens the HashMap contains all arguments extracted from user's command
	 * @param parameterRequiredForm required form of the command
	 * @param listArgKeywords ArrayList of all keywords that represent the parameters <br> E.g: S,c,C_att in CloneSet (S,c,C_att)
	 * @throws SyntaxException
	 */
	public void checkSyntax(HashMap<String,ArrayList<String>> mapTokens, ArrayList<String> parameterRequiredForm, ArrayList<String> listArgKeywords) throws SyntaxException{
		HashMap<String, ArrayList<String>> localMap = new HashMap<String, ArrayList<String>>();
		localMap.putAll(mapTokens);

			
		// setForm: example "S","*","Set","Str"
		ArrayList<String> setForm = parameterRequiredForm;
		// listKeys: list of param's names eg: [S,pi,O,pf]
		ArrayList<String> listKeys = listArgKeywords;
		
		// step through all the arguments in the map
		for (int i=0;i<listKeys.size();i++) {
			
			// if a, ArrayList of correspoinding key has 3 String inside ==> it must be a Set.
			// e.g: map = {S=[x,s,S], O=[*,o,O],...} map.get("S).size() = [x,s,S].size() = 3
			if (localMap.get(listKeys.get(i)).size() == 3) {
				if (setForm.get(i).equals("S")) {
					// if this form of set is S ==> checkSet for S
					checkSet("S",localMap.get(listKeys.get(i)));
				}else if (setForm.get(i).equals("Set")) {
					// if this form of set is Set ==> checkSet for Set
					checkSet("Set",localMap.get(listKeys.get(i)));
				}else {
					
					// a Set entered corresponds to a key that does not accept Set
					// e.g. in ModifyEdge(pf="a label for new edge"), the user try to enter ModifyEdge(pf=(*,*,*))
					// in this example pf only accept Str but the user entered a Set ===> throw this acception
					throw new SyntaxException(
							"Cannot identify this Set: " 
							+ localMap.get(listKeys.get(i))
					);
					
					
				}
			} 
			// else the args is not a Set ==> it has only one String inside the ArrayList
			else {
				
				// if params at a position can be null ==> it can be null,* or Str
				if(setForm.get(i)==null) {continue;}
				
				// Check the formule
				if (setForm.get(i).equals("S") || setForm.get(i).equals("Set")) {
					checkSet("S",localMap.get(listKeys.get(i)));
					throw new SyntaxException("Something wrong with "  + listKeys.get(i) + "'s form !");
				}
				
				// this is very special, use for LDP only
				if(setForm.get(i).equals("Num")) {
					try {
						// check if this args is a number or not
						Integer.parseInt(localMap.get(listKeys.get(i)).get(0));
					}catch (NumberFormatException e){
						throw new SyntaxException("k must be a number");
					}
				}
				// if the params cannot be null --> error for null args in this position
				if (localMap.get(listKeys.get(i)).get(0) == null && setForm.get(i)!=null) {
					throw new SyntaxException(listKeys.get(i) + " cannot be null");
				}

				else if (localMap.get(listKeys.get(i)).get(0) == null && setForm.get(i)==null){
					continue;
				}
				// if we have * at a positon that must be Str --> error
				else if(localMap.get(listKeys.get(i)).get(0).equals("*") && setForm.get(i).equals("Str")) {
					throw new SyntaxException(listKeys.get(i)+ " cannot be *");
				} 
			}
		}
	}
	
	
	/**
	 * 
	 * This method is used to get all destination's "att" of an prop in provided Graph
	 * @param graph current graph
	 * @param prop  the Edge's "prop" that we want to get the destination
	 */
	public ArrayList<String> getEdgeDst(Graph graph, String prop) {
		// ArrayList contains all label of the destination nodes of "String prop"
		ArrayList<String> dstsName = new ArrayList<String>();
		// loop through all the edges of the graph
		for (Arc a : graph.getArcsSet()) {
			// Get arc "prop"
			String arcProp = a.getAttribute().getValueAsString(1).replace("\"", "");
			if (arcProp.equals(prop)) {
				// Get source node and destination node
				Node dst = (Node) a.getTarget();
				// the label from getValueAsString is in the form "label" so we need to replace '"' by ''
				String dstName = dst.getAttribute().getValueAsString(1).replace("\"", ""); // att inside the graph appears as "att" ===> must remove the ""
				dstsName.add(dstName);
			}
		}
		return dstsName;
	}
	
	/**
	 * Check if any node's att in listDst appear in the list destination of elements in listEdge
	 * @param mapTokens the map that contains all the args after extracting user's command
	 * @param listEdge list all edges for checking destination
	 * @param listDst list node's att that cannot be destination of element in listEdge
	 * @throws SyntaxException 
	 */
	public void checkContrains(HashMap<String,ArrayList<String>> mapTokens, ArrayList<String> listEdge, ArrayList<String> listDst) throws SyntaxException {
		// get the current graph
		Graph graph = Tui.grammar.getHostGraph();
		
		for (String edge:listEdge) {
		
			if (mapTokens.get(edge) == null) {return;}
			
			// p is the prop of edge
			String p = mapTokens.get(edge).get(0);
			
			// get list of the Destination of the egde
			ArrayList<String> listEdgeDst = getEdgeDst(graph, p);
			
			for (String dst:listDst) {
			
				if (listEdgeDst.contains(mapTokens.get(dst).get(2))) {
					String warning = "[Warning] Edge \""+p + "\"'s source and destination are both \"" + mapTokens.get(dst).get(2)+ "\". Rules can't be matched ";
					System.err.println(warning);
				}
				
			}
		}
		
	}
	
}
