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

		
		
		// setForm: example "S","*","Set","Str"
		ArrayList<String> setForm = CheckArgs.parameterRequiredForm.get(op);
		// listKeys: list of param's names eg: [S,pi,O,pf]
		String[] listKeys = OperatorsHandling.listKeys.get(op);
		
		// check if the user has entered enough arguments for the operator in the command
		// example: RandomSource((*,"type","City"),"inGroup",(*,null,null),(*,"type","City")) ==> size = 4
		// setForm <of RandomSource> = (S,p,O,T) ===> size = 4
		// except JoinSet
		if (setForm.size()!=localMap.size() && !op.equals("JoinSet")) {
			throw new SyntaxException(2);
		}
		
		// step through all the arguments in the map
		for (int i=0;i<listKeys.length;i++) {
			//check for special form like "ListSet", "ListStr"
			if(setForm.get(i).equals("ListSet")) {
				// localMap in this case has the form: key=[*,s1,S1,*,s2,S2,...]
				// e.g where=[*, *, Stuart, *, livesIn, Paris], except=[*, knows, *]
				for ( int count =0;count<localMap.get(listKeys[i]).size();count+=3){
					
					// aSet is a Set to check
					//e.g: where {aSet,aSet,aSet}
					// so aSet is an ArrayList trying to get a tripler who represent a valid Set
					ArrayList<String> aSet = new ArrayList<String>(Arrays.asList(localMap.get(listKeys[i]).get(count),
							localMap.get(listKeys[i]).get(count+1),
							localMap.get(listKeys[i]).get(count+2)));
					checkSet("Set",aSet);				
				}
			}else if (setForm.get(i).equals("ListStr")) {
				for (String aStr:localMap.get(listKeys[i])) {
					if (aStr == null) {
						throw new SyntaxException(4,listKeys[i]+" cannot contain null");
					}else if (aStr.equals("*")) {
						throw new SyntaxException(4,listKeys[i]+" cannot contain *");
					}
				}
			}
			
			// check the rest neither "ListStr" not "ListSet"
			else {
			
				// if a, ArrayList of correspoinding key has 3 String inside ==> it must be a Set.
				// e.g: map = {S=[x,s,S], O=[*,o,O],...} map.get("S).size() = [x,s,S].size() = 3
				if (localMap.get(listKeys[i]).size() == 3) {
					if (setForm.get(i).equals("S")) {
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
		}
		(new SpecificConstraints(map)).checkTarget();
		
	}
	


}
