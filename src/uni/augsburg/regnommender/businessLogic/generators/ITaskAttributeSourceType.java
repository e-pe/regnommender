package uni.augsburg.regnommender.businessLogic.generators;

/**
 * This interface represents the functionality to get actual data (actual value of water consumption of a plant, actual weather conditions) for a specific task generator attribute.
 * 
 * @param <TType> is the type of the actual data to be returned.
 */
public interface ITaskAttributeSourceType<TType> {
	
	/**
	 * Gets the name of the task attribute, e.g season, water consumption.
	 *
	 * @return The name of the source type.
	 */
	public String getName();
	
	/**
	 * Gets the source of the task attribute required for generating tasks. 
	 *
	 * @return The source.
	 */
	public ITaskAttributeSource<TType> getSource();
}
