package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;

/**
 *  attribute to indicate fruit or bloomtime of a plant
 *
 */
public class FruitBloomTimeAttributeSourceType  implements 
	ITaskAttributeContextSourceType<PlantData, Float> {
	
	private PlantData context;
	private ITaskAttributeSource<Float> source;
	
	/**
	 * 
	 * @param context
	 */
	public FruitBloomTimeAttributeSourceType(Object context) {
		this.setContext((PlantData)context);
		this.source = this.createSource();
	}
	
	/**
	 * 
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * 
	 */
	public ITaskAttributeSource<Float> getSource() {
		return this.source;
	}
	
	/**
	 * 
	 */
	public PlantData getContext() {
		return this.context;
	}
	
	/**
	 * 
	 */
	public void setContext(PlantData value) {
		this.context = value;
	}
	
	/**
	 * 
	 * @return actual attribute value: FruitTime BloomTime
	 */
	private ITaskAttributeSource<Float> createSource() {
		return new ITaskAttributeSource<Float>() {
			public Float getValue() {
				if (!context.getFruitTime().equals(""))
					return Float.valueOf(context.getFruitTime());
				else if(!context.getBloomTime().equals(""))
				{
					return Float.valueOf(context.getBloomTime());
				}
				else return null;
			}
		};
	}

}
