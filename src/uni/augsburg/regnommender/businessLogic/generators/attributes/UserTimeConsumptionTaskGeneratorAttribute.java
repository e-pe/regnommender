package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;


/**
 *  get time user wants to spend on gardenwork 
 */

public class UserTimeConsumptionTaskGeneratorAttribute extends TaskGeneratorAttribute{

	public UserTimeConsumptionTaskGeneratorAttribute(IThreshold threshold,
			ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
		
	}

	@Override
	public float collect() {
		
		double value = (Double)this.sourceType.getSource().getValue();
		
		return (float)value;
	}

}
