package uni.augsburg.regnommender.core;

/**
 * @brief
 * formally known as Taskrecipes
 * contains data necessary to generate actual Task Data
 */
public interface IThreshold {
	/**
	 * 
	 * @return weight for attribute importance
	 */
	public float getWeight();
	
	/**
	 * 
	 * @return lower bound for normalization
	 */
	public float getLowThreshold();
	/**
	 * 
	 * @return higher bound for normalization
	 */
	public float getHighThreshold();
}
