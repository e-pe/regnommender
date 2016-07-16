package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.IThreshold;

/**
 *  attribute indicating plants water consumption
 *
 */
public class WaterConsumptionTaskGeneratorAttribute extends TaskGeneratorAttribute{
	

	public WaterConsumptionTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	@Override
	public float collect()
	{
		
		//db request
		double waterConsumption=(Double)this.sourceType.getSource().getValue();
		
		return (float)waterConsumption;
	}

}