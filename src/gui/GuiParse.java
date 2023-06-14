package gui;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;



/**
 * This class will contains all the information of the parser, to generate the suitable componants
 * @author khai
 *
 */
public class GuiParse {
	private String operator;
	private ArrayList<String> args;
	private ArrayList<String> requiredForm;
	
	public ArrayList<String> getArgs() {
		return args;
	}
	
	public ArrayList<String> getRequiredForm() {
		return requiredForm;
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
		setArgs();
	}
	
	public void setArgs() {
		// create the className of the operator
		String className = "parser.operators.Parse"+operator;
        String attributeName1 = "listArgKeywords";
        String attributeName2 = "parameterRequiredForm";
        
        if (!className.equals("parser.operators.ParseAnatomization") && !className.equals("parser.operators.ParseJoinSet")) {
	        try {
	        	// create the operator
			    Class<?> clazz = Class.forName(className);
			    Constructor<?> constructor = clazz.getConstructor(String.class);
			    Object object = constructor.newInstance(operator);
		
		        // Use reflection to get the value of the attribute
		        Field field1 = clazz.getDeclaredField(attributeName1);
		        field1.setAccessible(true);
		        args = (ArrayList<String>) field1.get(object);
		        
		        Field field2 = clazz.getDeclaredField(attributeName2);
		        field2.setAccessible(true);
		        requiredForm = (ArrayList<String>) field2.get(object);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
        }else if (className.equals("parser.operators.ParseAnatomization")) {
        	args = new ArrayList<String> (Arrays.asList("idn","qID","sens"));
        	requiredForm = new ArrayList<String> (Arrays.asList("Str","Str","Str"));
        }else if (className.equals("parser.operators.ParseJoinSet")) {
        	args = new ArrayList<String> (Arrays.asList("x","X_att","Where","Except"));
        	requiredForm = new ArrayList<String> (Arrays.asList("Str","Str","S","S"));
        }
	}
	
	public int getNumArg(){
		return args.size();
	}
		
	public String getSyntax(){
		String syntax = new String(operator);
		
		syntax += "(";
		for (String key: this.args) {
			syntax += key + ",";
		}
		syntax = syntax.substring(0, syntax.length()-1);
		syntax += ")";
		return syntax;
	}
	
}
