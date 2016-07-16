package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;

/**
 * 
 *  user has lawn?
 *
 */
public class UserLawnTaskGeneratorAttribute extends TaskGeneratorAttribute{

	public UserLawnTaskGeneratorAttribute(IThreshold threshold,
			ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
		
	}

	@Override
	public float collect() {
		
		float value = (Float)this.sourceType.getSource().getValue();
		
		return value;
	}

}
