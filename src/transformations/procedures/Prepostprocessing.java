package transformations.procedures;

import java.util.HashSet;
import java.util.Set;

import executable.granonui.Tui;
import transformations.operators.DeleteNode;
import transformations.operators.InvalidArguments;
import transformations.operators.JoinSet;
import transformations.operators.NewNode;

import utils.Grammar;
import utils.Pair;

/**
 * Handles pre and post processing related to except and where clauses
 * Pre processing consists in creating a relation between all nodes respecting the clauses and a newly created node (a set)
 * Post processing deletes the node (the set)
 * @author ceichler
 *
 */
public class Prepostprocessing {
	/**
	 * Negative clauses
	 * set of (xi,Xi), attributes of a relation and a node respectively
	 */
	Set<Pair<String>> excepts = new HashSet<Pair<String>>();
	/**
	 * Positive clauses
	 *  set of (yi,Yi), attributes a relation and a node respectively
	 */
	Set<Pair<String>> where = new HashSet<Pair<String>>();
	
	/**
	 * Name of the created set
	 */
	String setName = "$" + System.currentTimeMillis();
	
	Grammar grammar = Tui.grammar;

	/**
	 * Constructing the class with the excepts and wheres clauses to handle
	 * @param excepts set of (xi,Xi), attributes of a relation and a node respectively
	 * @param where set of (yi,Yi), attributes a relation and a node respectively
	 */
	public Prepostprocessing(Set<Pair<String>> excepts, Set<Pair<String>> where) {
		this.where = where;
		this.excepts = excepts;
		
	}
	
	/**
	 * Creates new set and put nodes satisfying wheres and excepts in the set
	 * @throws InvalidArguments 
	 */
	public void doPreprocessing() throws InvalidArguments {
		
		/** Creating a new set with setName **/
		(new NewNode(setName)).execute();
		
		/** Identifying items within set **/
		(new JoinSet(excepts, where, setName)).execute();
		
	}
	
	/**
	 * delete the set created during pre processing
	 */
	public void doPostprocessing(){
		(new DeleteNode(setName)).execute();
	}

}


