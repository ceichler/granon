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

public class BenchSentiment {
	
	private static List<Integer> nbTweets = Arrays.asList(100, 1000, 10000, 100000, 1000000, 2000000);
	public static Grammar grammar = new Grammar("sentimentGraGra.ggx");
	
	
	
	public static void main(String[] args) throws TypeException {
		int nbIter = 100;
		long start, end;
		File outputFile = null;

		for(Integer nbT : nbTweets) {
			SentimentBuilder.parse(600);
			//GraGraUtils.print(grammar.getHostGraph());
			Tui.grammar = grammar;
			outputFile =new File("benchLDP_sentiment_"+nbT);
			prepareFile(nbT, grammar.getHostGraph().getNodesCount(), grammar.getHostGraph().getArcsCount(), outputFile);
			System.out.println("done loading");
			for(int i=0;i<nbIter;i++) {
				start=System.currentTimeMillis();
				(new LDP("rdf:Type", "tweetType", "rdf:Type", "emotionType", "hasEmotion", 2)).execute();
				end=System.currentTimeMillis();
				try{
					FileWriter fileWriter=new FileWriter(outputFile,true);
					fileWriter.write(end-start + "\t");
					fileWriter.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				//GraGraUtils.print(grammar.getHostGraph());
			}
		}
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
