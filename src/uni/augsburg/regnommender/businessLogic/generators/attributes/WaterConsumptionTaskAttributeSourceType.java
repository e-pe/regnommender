package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 *  attribute indicating plants water consumption
 *
 */
public class WaterConsumptionTaskAttributeSourceType implements 
	ITaskAttributeContextSourceType<PlantData, Double> {

	private PlantData context;
	private ITaskAttributeSource<Double> source;
	

	public WaterConsumptionTaskAttributeSourceType(Object context) {
		this.setContext((PlantData)context);
		this.source = this.createSource();
	}
	

	public String getName() {
		return null;
	}


	public ITaskAttributeSource<Double> getSource() {
		return this.source;
	}


	public PlantData getContext() {
		return this.context;
	}


	public void setContext(PlantData value) {
		this.context = value;
	}
	
	/** 
	 * @return actual attribute value: waterConsumption
	 */
	private ITaskAttributeSource<Double> createSource() {
		return new ITaskAttributeSource<Double>() {
			public Double getValue() {
				return context.getWaterConsumption();
			}
		};
	}
}
