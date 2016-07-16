package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;

/**
 * 
 *  Cut attribute returns plants value indicating cut frequence
 *
 */
public class CutAttributeSourceType implements 
ITaskAttributeContextSourceType<PlantData, Double> {
	
private PlantData context;
private ITaskAttributeSource<Double> source;


public CutAttributeSourceType(Object context) {
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
 * 
 * @return actual attribute value: waterConsumption
 */
private ITaskAttributeSource<Double> createSource() {
	return new ITaskAttributeSource<Double>() {
		public Double getValue() {
			return context.getCut();
		}
	};
}

}