package executable.granonui;



import java.util.Scanner;
import utils.GraGraUtils;
import utils.Grammar;
import executable.HardCodedTests;
import parser.Parser;
import progFileReader.ProgRunner;

/**
 * @author ceichler
 */
public class Tui {

	public static Grammar grammar;// = new Grammar();
	private static Scanner scan = new Scanner(System.in);

	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		grammar = new Grammar();
		home();
	}

	private static void home() {
		boolean quit = false;
		String input;

		while (!quit) {
			switch (homeMenu()) {
				case 0:
					quit = true;
					break;
				case 1:
					System.out.println("Enter your command");
					scan.nextLine(); // read the "\n" of last nextInt()
					Parser.command = scan.nextLine();
					Parser.execute();
					break;
				case 2:
					GraGraUtils.print(grammar.getHostGraph());
					break;
				case 3:
					System.out.println("Saving...please enter a filename \n");
					input = scan.next();
					GraGraUtils.save(input, Grammar.graphGrammar);
					break;
				case 4:
					System.out.println("Loading...please enter a filename \n");
					input = scan.next();
					grammar = new Grammar(input);
					break;
				case 5:
					grammar = new Grammar();

					break;
				case 6:
					System.out.println("please enter a filename\n");
				case 7:
					// System.out.println("Running prog");
					System.out.println("Loading...please enter a filepath | Enter for default \n");
					scan.nextLine();
					input = scan.nextLine();
					if (input.isEmpty()) {ProgRunner.execProg();}
					else {ProgRunner.execProg(input);}
					break;
				default:
					System.out.println("Unknown command, please enter an integer between 0 and 9");
			}
		}
	}

	private static int homeMenu() {
      System.out.println("Main Menu. What to do?\n" + "0 : exit \n" + "1 : execute an operator \n" + 
				"2 : print current graph \n" +
				"3 : save current graph \n" +
				"4 : load a graph grammar \n" +
				"5 : reset graph \n" +
				"6 : load a graph NON FONCTIONNEL \n"+
				"7 : execute a  program file"
		);
		return scan.nextInt();
	}

}