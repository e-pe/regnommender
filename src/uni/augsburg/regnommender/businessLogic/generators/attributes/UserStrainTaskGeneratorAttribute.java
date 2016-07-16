package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

/**
 * 
 * how straining work can be done by user?
 *
 */
public class UserStrainTaskGeneratorAttribute extends TaskGeneratorAttribute{

	/**
	 * 
	 * @param threshold
	 * @param sourceType
	 */
	public UserStrainTaskGeneratorAttribute(IThreshold threshold,
			ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
		
	}

	@Override
	public float collect() {
		
		double value = (Double)this.sourceType.getSource().getValue();
		
		return (float)value;
	}

}
