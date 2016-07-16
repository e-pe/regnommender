package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

/**
 *  attribute to indicate fertilizeruse of plant
 *
 */
public class FertilizerConsumptionTaskGeneratorAttribute extends TaskGeneratorAttribute{
	
	/**
	 * 
	 * @param task
	 * @param attribute
	 */
	public FertilizerConsumptionTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	@Override
	public float collect()
	{		
		//db request
		double value = (Double)this.sourceType.getSource().getValue();
		
		return (float)value;
	}
}
