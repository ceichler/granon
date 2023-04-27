package parser;

import java.util.HashMap;

public class SyntaxException extends Exception {
	
	private static HashMap<Integer, String> listExceptions = new HashMap<Integer,String>(){{
		put(0,"Cannot get operator from command");
		put(1,"Operator not found");
		put(2,"insufficient number of arguments");
		put(3,"Missing required parameter");
		put(4,"Invalid parameter");
		put(5,"Invalid parameter value");
		put(6,"Null params map");
		put(7,"Invalid Set form");
	}};
	
	public SyntaxException(int exception_code,String msg) {
		super(listExceptions.get(exception_code) + " [!]Message: " + msg);
	}
	
	public SyntaxException(int exception_code) {
		super(listExceptions.get(exception_code));
	}
	
	public SyntaxException() {
		super();
	}

	public SyntaxException(String message) {
		super(message);
	}
	
}
