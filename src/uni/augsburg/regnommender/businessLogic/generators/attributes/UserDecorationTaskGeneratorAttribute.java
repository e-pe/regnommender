package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

/**
 * 
 *  user wants decorative garden?
 *
 */
public class UserDecorationTaskGeneratorAttribute extends TaskGeneratorAttribute{


	public UserDecorationTaskGeneratorAttribute(IThreshold threshold,
			ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
		
	}

	@Override
	public float collect() {
		
		double value = (Double)this.sourceType.getSource().getValue();
		
		return (float)value;
	}

}
