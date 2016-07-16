package uni.augsburg.regnommender.businessLogic.generators;

/**
 * 
 * This interface derives from the ITaskAttributeSourceType and represents an extension for getting actual data in scope of a context (e.g. plant, user, last done user task).
 *
 * @param <TContext> is the type of the context object (e.g PlantData, UserData, UserTaskData)
 * @param <TType> is the type of the value that will be returned and can be consumed by the specific task generator attribute.
 */
public interface ITaskAttributeContextSourceType<TContext, TType> 
	extends ITaskAttributeSourceType<TType>  {
	
	/**
	 * Gets the context which provides actual data for the specific task generator attribute.
	 */
	public TContext getContext();
	
	/**
	 * Sets the context which provides actual data for the specific task generator attribute.
	 */
	public void setContext(TContext value);
}
