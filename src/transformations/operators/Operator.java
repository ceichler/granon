package transformations.operators;


import agg.xt_basis.Rule;

/**
 * Abstract class representing an atomic operator (a graph rewriting rule)
 * @author ceichler
 *
 */
public abstract class Operator {
	
	/**
	 * corresponding rewriting rule
	 */
	
	protected Rule r;
	
	
	
	/**
	 * Execution of the operator
	 */
	public abstract void execute();

}
