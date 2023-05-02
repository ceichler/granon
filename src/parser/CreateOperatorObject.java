package parser;

import java.lang.reflect.Constructor;
import java.util.*;

import transformations.operators.*;
import transformations.procedures.*;


public class CreateOperatorObject {
	public static Object createObject(OperatorsHandling tokenHd,HashMap<String,ArrayList<String>> result) throws SyntaxException {
		
		Object returnedObject;
		
    	HashMap <String,String> execMap = null;
    	if (!result.get("operator").get(0).equals("JoinSet") && !result.get("operator").get(0).equals("Anatomization")) {
    		execMap = tokenHd.generateExecutableMap(result);
    	}   	

    	System.out.println("\u001B[36m [execMap] " + execMap + "\u001B[0m");
    	
    	
    	
    	switch (result.get("operator").get(0)) {
    	
    	
    		case "NewNode":
    			System.out.println("NewNode");
    			returnedObject = (new NewNode(execMap));
    			break;
	
    		case "DeleteNode":
    			System.out.println("DeleteNode");
    			returnedObject = (new DeleteNode(execMap));
    			
    			
    			
    		case "EdgeReverse":
    			// Neither S nor O can be the target of pi
    			System.out.println("EdgeReverse");
    			returnedObject = (new EdgeReverse(execMap));
    			break;
    			
    			
    			
    		case "EdgeCut":
    			// Neither S nor O can be the target of pi
    			System.out.println("EdgeCut");
    			returnedObject = (new EdgeCut(execMap));
    			break;
    			
    			
    			
    		case "EdgeCopy":
    			// Neither S nor O can be target of pi
    			System.out.println("EdgeCopy");
    			returnedObject = (new EdgeCopy(execMap));
    			break;
    			
    			
    		case "EdgeChord":
    			// Neither S nor O nor I can be the target of pi1 or pi2
    			System.out.println("EdgeChord");
    			returnedObject = (new EdgeChord(execMap));
    			break;
    			
    			
    		case "EdgeChordKeep":
    			// Neither S nor O nor I can be the target of pi1 or pi2
    			System.out.println("EdgeChordKeep");
    			returnedObject = (new EdgeChordKeep(execMap));
    			break;
    			
    			
    		case "ModifyEdge":
    			// Neither S nor O can be target of pi
    			System.out.println("ModifyEdge");
    			returnedObject = (new ModifyEdge(execMap));
    			break;
    			
    			
    		case "RandomTransformSource":
    			// S, O and T cannot be the target of pi
    			System.out.println("RandomSource");
    			returnedObject = (new RandomTransformSrc(execMap));
    			break;
    			
    			
    		case "RandomTransformTarget":
    			// Neither S, O or T can be the target of pi
    			System.out.println("RandomTarget");
    			returnedObject = (new RandomTransformTar(execMap));
    			break;	
    			
    			
    		case "RandomSource":
    			System.out.println("RandomSource");
    			returnedObject = (new RandomSrc(execMap));
    			break;
    			
    			
    		case "RandomTarget":
    			System.out.println("RandomTarget");
    			returnedObject = (new RandomTar(execMap));
    			break;
    			
    			
    		case "CloneSet":
    			returnedObject = (new CloneSet(execMap));
    			System.out.println("CloneSet");
    			break;
    			
    			
    		case "LDP":
    			System.out.println("LDP");
    			returnedObject = (new LDP(execMap));
    			break;
    			
    			
    		case "Anatomization":
    			System.out.println("Anatomization");
    			returnedObject = (new Anatomization(result.get("idn"),result.get("qID"),result.get("sens")));
    			break;
    			
    			
    		case "JoinSet":
    			// (new NewNode(result.get("JoinSet").get(1)));
    			// {JoinSet=[hasQI, QI], where=[*, *, Stuart], except=[*, knows, *], operator=[JoinSet]}
    			returnedObject = (new JoinSet(result));
    			break;
    			
    			
    		default:
    			System.err.println("undefined operator");
    			throw new SyntaxException(1);			  			
    	}
		return returnedObject;
	}
	
	public static <T> T createObject(String className, HashMap<String, Class<?>> classMap, Object... args) throws Exception {
	    Class<?> clazz = classMap.get(className);
	    if (clazz == null) {
	        throw new IllegalArgumentException("Class not found for name: " + className);
	    }
	    Constructor<?> constructor = clazz.getConstructor(getParameterTypes(args));
	    return (T) constructor.newInstance(args);
	}

	private static Class<?>[] getParameterTypes(Object... args) {
	    Class<?>[] parameterTypes = new Class[args.length];
	    for (int i = 0; i < args.length; i++) {
	        parameterTypes[i] = args[i].getClass();
	    }
	    return parameterTypes;
	}

	
	public static void main(String[] args) throws Exception {
		HashMap<String, Class<?>> classMap = new HashMap<>();
		classMap.put("MyClass", NewNode.class);
		NewNode instance = createObject("MyClass", classMap, "ce");

	}
	
}
