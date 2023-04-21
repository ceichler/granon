package progFileReader;

import java.util.*;
import parser.Parser;

public class ProgRunner {
	public static void execProg() {
		ProgReader reader = new ProgReader();
		List<String> listCommand = reader.getFileContent();
		for (String command : listCommand) {
			Parser.command = command;
			Parser.execute();
		}
	}
	public static void execProg(String pathToFile) {
		ProgReader reader = new ProgReader(pathToFile);
		List<String> listCommand = reader.getFileContent();
		for (String command : listCommand) {
			Parser.command = command;
			Parser.execute();
		}
	}
}
