package executable;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import agg.xt_basis.TypeException;
import executable.granonui.Tui;
import executable.sentimentbuilder.SentimentBuilder;
import transformations.procedures.LDP;
import utils.GraGraUtils;
import utils.Grammar;

public class BenchSentimentLDP {
	
	//private static List<Integer> nbTweets = Arrays.asList(100, 200, 500, 750, 1000);//, 10000, 100000, 1000000, 2000000);
	public static Grammar grammar = new Grammar("anonOperator.ggx");//("sentimentGraGra.ggx");
	
	
	/**
	 * Start the bench
	 * @param args nb of line to parse to construct the graph, whether the output file needs to be init
	 * @throws TypeException
	 */
	public static void main(String[] args) throws TypeException {
		
		//Clearinr host graph to get an empty one
		grammar.getHostGraph().clear();
		//Bad design relying on tui's grammar. Making sure it's the one used
		Tui.grammar = grammar;
		
		//number of iteration for the XP with fixed parameters
		int nbIter = 5;
		//Start and end date of time of XP
		long start, end;
		//File to write XP measurements
		File outputFile = null;
		
		
		//Building our sentiment graph
		SentimentBuilder sb = new SentimentBuilder();
		
		
		int nbT = Integer.parseInt(args[0]);
		boolean first = Boolean.parseBoolean(args[1]);
			
		sb.parse(nbT, 0);
		//GraGraUtils.print(grammar.getHostGraph());
			
			outputFile =new File("benchLDP_sentiment_"+nbT);
			System.out.println("output file = " + outputFile);
			if(first) {
				prepareFile(nbT, grammar.getHostGraph().getNodesCount(), grammar.getHostGraph().getArcsCount(), outputFile);
			}
			
			for(int i=0;i<nbIter;i++) {
				start=System.currentTimeMillis();
				(new LDP("rdf:Type", "tweetType", "rdf:Type", "emotionType", "hasEmotion", 2)).execute();
				end=System.currentTimeMillis();
				try{
					FileWriter fileWriter=new FileWriter(outputFile,true);
					fileWriter.write(end-start + "\t");
					//System.out.println(end-start);
					fileWriter.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				System.out.println("one run done");
				//GraGraUtils.print(grammar.getHostGraph());
			}
			//prevNbTweets = nbT;
		//}
	}

	private static void prepareFile(int nbTweet, int nbN, int nbE, File output) {
		//Preparing files		
		try{
			FileWriter fileWriter=new FileWriter(output);
			fileWriter.write("LDP epsilon ln(2) \n"+ "nbTweet nbNodes nb Edges \n" + nbTweet + " " + nbN + " " + nbE + "\n");
			fileWriter.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	

}
