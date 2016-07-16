package uni.augsburg.regnommender.businessLogic.generators;

import uni.augsburg.regnommender.core.*;

/**
 * This class represents the functionality for analyzing actual data (ITaskAttributeSourceType) and generating a value from the interval [0, 1] to contribute to the priority of the whole task.
 */
public abstract class TaskGeneratorAttribute {
	protected IThreshold threshold;
	protected ITaskAttributeSourceType<?> sourceType;
	
	/**
	 *  
	 * @param Taskrecipes thresholds (high, low, weight)
	 * @param sourceType generalized attributes current value
	 */
	public TaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		this.threshold = threshold;
		this.sourceType = sourceType;
	}
	
	/**
	 * attributes weight are needed for correct mean calculation
	 * @return attributes weight
	 */
	public float getWeight()
	{
		return (float) this.threshold.getWeight();
	}
	
	/**
	 * this is a standard approach to attribute normalization using lower threshold and higher threshold
	 * @return normalized attribute value ready to be used by mean generator
	 */
	public float getNormalizedValue() {
		float value = this.collect();
		float numerator = value - this.threshold.getLowThreshold();
		float denominator = this.threshold.getHighThreshold() - this.threshold.getLowThreshold();
		float weight = this.threshold.getWeight();
		
		return (numerator / denominator);		
	}
	
	
	/**
	 * get access to Generators attribute value 
	 * @return unnormalized and unweighted values as read from db or other source
	 */
	public abstract float collect();
}
