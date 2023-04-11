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
	
	private static List<Integer> nbTweets = Arrays.asList(100, 200, 500, 750, 1000);//, 10000, 100000, 1000000, 2000000);
	public static Grammar grammar = new Grammar("sentimentGraGra.ggx");
	
	
	
	public static void main(String[] args) throws TypeException {
		Tui.grammar = grammar;
		int nbIter = 10;
		long start, end;
		File outputFile = null;
		int prevNbTweets = 0;
		SentimentBuilder sb = new SentimentBuilder();
		
			int nbT = 0;//Integer.parseInt(args[0]);
			boolean first = true;// = Boolean.parseBoolean(args[1]);
			
		//for(Integer nbT : nbTweets) {
			sb.parse(nbT, 0);
			GraGraUtils.print(grammar.getHostGraph());
			
			outputFile =new File("benchLDP_sentiment_"+nbT);
			if(first) {
				prepareFile(nbT, grammar.getHostGraph().getNodesCount(), grammar.getHostGraph().getArcsCount(), outputFile);
			}
			
			//for(int i=0;i<nbIter;i++) {
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
				//GraGraUtils.print(grammar.getHostGraph());
			//}
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
