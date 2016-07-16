package uni.augsburg.regnommender.core;

/**
 * 
 * This interface provides the functionality to specify a query for retrieving data from the database.
 *
 * @param <TParameter1> is the type of the first parameter required to perform a query.
 * @param <TParameter2> is the type of the second parameter required to perform a query.
 * @param <TResult> is the type of the result returned by a query. 
 */
public interface IQuery2<TParameter1, TParameter2, TResult> {
	/**
	 * Run the specific query.
	 * 
	 * @param parameter1 is the first parameter required to perform the query.
	 * @param parameter2 is the second parameter required to perform the query.
	 * @return The result.
	 */
	public TResult runQuery(TParameter1 parameter1, TParameter2 parameter2);
}
