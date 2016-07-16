package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.PlantStatus;
import uni.augsburg.regnommender.businessLogic.UserPlantData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;

/**
 *  attribute indicating if plant was planted, compare seed, cutting
 */
public class PlantAttributeSourceType implements 
	ITaskAttributeContextSourceType<UserPlantData, Float> {
		
	private UserPlantData context;
	private ITaskAttributeSource<Float> source;
	

	public PlantAttributeSourceType(Object context) {
		this.setContext((UserPlantData)context);
		this.source = this.createSource();
	}
	

	public String getName() {
		return null;
	}
	

	public ITaskAttributeSource<Float> getSource() {
		return this.source;
	}
	

	public UserPlantData getContext() {
		return this.context;
	}
	

	public void setContext(UserPlantData value) {
		this.context = value;
	}
	
	/**
	 * @return actual attribute value: 1.0f if plant is planted
	 */
	private ITaskAttributeSource<Float> createSource() {
		return new ITaskAttributeSource<Float>() {
			public Float getValue() {
				if(context.getPlantStatus().equals(PlantStatus.exists))
				{   String tmp = (context.getPlant().getPlantTime());
					return Float.valueOf(tmp);
				}
				else return 0.0f;
			}
		};
	}

}