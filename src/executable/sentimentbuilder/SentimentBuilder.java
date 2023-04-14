package executable.sentimentbuilder;



import java.io.File;  
// Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Vector;

import agg.attribute.AttrInstance;
import agg.attribute.impl.ValueMember;
import agg.attribute.impl.ValueTuple;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.TypeException;
import transformations.operators.NewNode;
import utils.GraGraUtils;
import utils.Grammar;


/**
 * Parser for the sentiment140 dataset; produce a graph
 * @author ceichler
 *
 */
public class SentimentBuilder {
	/**
	 * File to load.
	 * Expected format: 
	 * target: the polarity of the tweet (0 = negative, 2 = neutral, 4 = positive)
	 * ids: The id of the tweet ( 2087)
	 * date: the date of the tweet (Sat May 16 23:58:44 UTC 2009)
	 * flag: The query (lyx). If there is no query, then this value is NO_QUERY. This will be ignored.
	 * user: the user that tweeted (robotickilldozr)
	 * text: the text of the tweet (Lyx is cool). Will extract referenced username from the text
	 */
	private static String fileName= //"testdata.manual.2009.06.14.csv";
	"training.1600000.processed.noemoticon.csv"; // full dataset

	// Using sentiment140 data set
	public static String[] atts = {"emotion", "id", "date", "query", "user", "text"};

	private static Graph g = Grammar.graphGrammar.getGraph();
	//Easy access to nodes through their attribute
	HashMap<String, Node> nodesByID = new HashMap<String, Node>();
	HashMap<String, Node> usersByName = new HashMap<String, Node>();
	
	Node tweet, user;
	Node ref=null;
	Node person = null;
	Node emotion = null;
	Node neg = null;
	Node neutral = null;
	Node pos = null;
	Node tweetType = null;
	Node time = null;
	
	public SentimentBuilder() {
		
		Node n;
		//constructing starting graph
		
		n = createNode("personType", nodesByID);
		person = n;
		n = createNode("tweetType", nodesByID);
		tweetType = n;
		n = createNode("negative", nodesByID);
		neg = n;
		n = createNode("neutral", nodesByID);
		neutral = n;
		n = createNode("positive", nodesByID);
		pos = n;
		n = createNode("emotionType", nodesByID);
		emotion = n;
		
		try {
			GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, pos, emotion), "prop", "rdf:Type");
			GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, neutral, emotion), "prop", "rdf:Type");
			GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, neg, emotion), "prop", "rdf:Type");
		} catch (TypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	/**
	 * 
 	* Parse filename and add its content to the host graph of the grammar
 	* do not construct groups
	 * @param maxTweet nb of line to parse
	 * @param prevNbTweet nb of line already parsed
	 * @throws TypeException
	 */
	public void parse(int maxTweet, int prevNbTweet) throws TypeException {
		parse(maxTweet, prevNbTweet, false);
	}
	
	/**
	 * 
 	* Parse filename and add its content to the host graph of the grammar
	 * @param maxTweet nb of line to parse
	 * @param prevNbTweet nb of line already parsed
	 * @param anato whether groups should be constructed (timewindow of 1 min)
	 * @throws TypeException
	 */
	public void parse(int maxTweet, int prevNbTweet, boolean anato) throws TypeException {
			

		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			int currentLine = 0;
			while(myReader.hasNextLine() && currentLine < prevNbTweet) {
				currentLine++;
				myReader.nextLine();
			}
			while (myReader.hasNextLine() && currentLine<maxTweet) {
				currentLine++;
				// Reading line and handling data
				String line = myReader.nextLine();
				String[] values = line.split("\",\"");
				values[0] = values[0].substring(1);
				values[values.length-1] = values[values.length-1].substring(0, values[values.length-1].length()-1) ;
				//Creating the node corresponding to the tweet
				tweet = createNode(values[1], nodesByID);
				//give it a type
				GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, tweet, tweetType), "prop", "rdf:Type");
				
				//Relates the tweet to its target emotion
				switch (values[0]) {
					case "0":
						emotion = neg;
						break;
					case "2":
						emotion = neutral;
						break;
					case "4":
						emotion = pos;
						break;
				}
				GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, tweet, emotion), "prop", "hasEmotion"); 
				
				//creates date and relates it to the tweet
				time = createNode(values[2], nodesByID);
				GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, tweet, time), "prop", "timestamp"); 
				//if user does not exist
				if(!usersByName.containsKey(values[4])) {
					//create user and name
					user = createNode("user"+currentLine,nodesByID);
					usersByName.put(new String(values[4]),user);

					//creates name and relates user to it
					GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, user, createNode(values[4], nodesByID)), "prop", "hasName");
					//gives the user its type
					GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, user, person), "prop", "rdf:Type");
					//relates the user to its tweet
					GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, user, tweet), "prop", "authored");
				} //if user exists it already has a name

				
				//creates text and put it in relation with the tweet

				GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, tweet, createNode(values[5], nodesByID)), "prop", "hasText"); 
				
				//GraGraUtils.print(g);
				
				
				// Création des références
				
				String[] referenceString = values[values.length-1].split("@");
				for(int i=1;i<referenceString.length;i++) {
					try {
						String handle = referenceString[i].split("\\s")[0]; // split on anything that is not a letter || or space (otherwise space is left ...)

						try{
							handle = handle.split("[^a-zA-Z0-9]")[0];
							//creates the referenced node and put it in relation with the tweet
							ref= createNode(handle,nodesByID);
							GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, tweet, ref), "prop", "references");
							//System.out.print(handle+" / ");
						}
						catch(Exception e) {
							System.err.println("error");
						}
					}
					catch(Exception e) {
						// if error here, it means the @ was part of an emoticon or something, and has nothing behind it. So we do nothing
					}
				}
				
				//Creating groups
				if(anato) {
					GraGraUtils.setAtt(g.createArc(GraGraUtils.TEDGE, time, 
						createNode(values[2].substring(0,16), nodesByID)), "prop", "inGroup"); 
				}
				
				
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}	
	
	}
	
	private static Node createNode(String att, HashMap<String, Node> nodesByID) {
		Node n = null;
		if(!nodesByID.containsKey(att) || att == null) {
			try {
				n = Grammar.graphGrammar.getGraph().createNode(GraGraUtils.TNODE);
			} catch (TypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(att != null) {
				GraGraUtils.setAtt(n, "att", att);
				nodesByID.put(att, n);
			}
		}
		else n = nodesByID.get(att);
		return n;
	}

}

