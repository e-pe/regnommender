package uni.augsburg.regnommender.core;

/**
 * This interface provides the functionality to specify a query for retrieving data from the database.
 *
 * @param <TParameter> is the type of the specific parameter required to perform a query.
 * @param <TResult> is the type of the result returned by a query. 
 */
public interface IQuery<TParameter, TResult> {
	/**
	 * Run the specific query.
	 * @param parameter is the specific parameter required to perform the query.
	 * @return The result.
	 */
	public TResult runQuery(TParameter parameter);
}

