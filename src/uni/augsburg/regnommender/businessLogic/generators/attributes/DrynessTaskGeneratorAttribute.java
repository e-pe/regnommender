package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

/**
 *  Dryness attribute uses weather to calculate dryness factor
 *
 */
public class DrynessTaskGeneratorAttribute extends TaskGeneratorAttribute {
	

	public DrynessTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}
	
	//normalized and wighted value
	@Override
	public float getNormalizedValue(){
		return (Float)this.sourceType.getSource().getValue();
	}

	@Override
	/**
	 * Will be not invoked.
	 */
	public float collect() {
		return 0;
	}

}
