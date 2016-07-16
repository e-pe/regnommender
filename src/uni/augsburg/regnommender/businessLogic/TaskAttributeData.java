package uni.augsburg.regnommender.businessLogic;

import uni.augsburg.regnommender.core.*;

/**
 * 
 * This class contains meta data information for describing a specific task attribute.
 *
 */
public class TaskAttributeData implements IThreshold {

	private String id;
	private String name;
	private float weight;
	private float lowThreshold;
	private float highThreshold;
	
	private String classPath;
	private String sourceClassPath;
	private boolean sourceContextSensitive;
	private String sourceContextName;
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setId(String value) {
		this.id = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassPath() {
		return this.classPath;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setClassPath(String value) {
		this.classPath = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSourceClassPath() {
		return this.sourceClassPath;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setSourceClassPath(String value) {
		this.sourceClassPath = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getSourceContextSensitive() {
		return this.sourceContextSensitive;
	}
	
	/**
	 * 
	 */
	public void setSourceContextSensitive(boolean value) {
		this.sourceContextSensitive = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSourceContextName() {
		return this.sourceContextName;
	}
	
	/**
	 * 
	 */
	public void setSourceContextName(String value) {
		this.sourceContextName = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String value) {
		this.name = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getWeight(){
		return this.weight;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setWeight(float value) {
		this.weight = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getLowThreshold() {
		return this.lowThreshold;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setLowThreshold(float value){
		this.lowThreshold = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getHighThreshold() {
		return this.highThreshold;
	}
	
	/**
	 * 
	 */
	public void setHighThreshold(float value) {
		this.highThreshold = value;
	}
}
