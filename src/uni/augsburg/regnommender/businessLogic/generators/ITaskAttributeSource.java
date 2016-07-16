package uni.augsburg.regnommender.businessLogic.generators;

/**
 * This interface 
 * 
 * @param <T> is the type of the value to be returned in order to be consumed by the specific task generator attribute.
 */
public interface ITaskAttributeSource<T> {
	
	/**
	 * Gets the value from the task attribute source for generating an user task.
	 * 
	 * @return
	 */
	public T getValue();
}
