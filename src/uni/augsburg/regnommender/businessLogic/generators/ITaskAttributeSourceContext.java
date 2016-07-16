package uni.augsburg.regnommender.businessLogic.generators;

/**
 * This interface is used to specify a specific context object e.g. Plant, User, UserTask to evaluate a task generator attribute in the right way.  
 */
public interface ITaskAttributeSourceContext {
	/**
	 * Gets the context object.
	 */
	public Object getContext();
}
