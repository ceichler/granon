package parser;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * abstract class for command analyzing
 * @author khai
 *
 */


public abstract class funcOperators {
	/**
	 * the provided command
	 */
	public String command;
	/**
	 * create the command analyzer
	 * @param command
	 */
	public funcOperators(String command) {
		this.command = command;
	}
	/**
	 * function that parses the command and returns the tokens for running the program
	 * @param rePattern regex pattern for command string processing
	 * @return HashMap contains all token for executing operator
	 */
	public abstract HashMap<String,ArrayList<String>> getToken(String rePattern);
}
