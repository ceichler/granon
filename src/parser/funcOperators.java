package parser;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class funcOperators {
	public String command;
	public funcOperators(String command) {
		this.command = command;
	}
	public abstract HashMap<String,ArrayList<String>> getToken(String rePattern);
}
