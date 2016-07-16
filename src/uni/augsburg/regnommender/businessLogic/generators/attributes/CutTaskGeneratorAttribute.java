package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;

/**
 * 
 *  Cut attribute returns plants value indicating cut frequence
 *
 */
public class CutTaskGeneratorAttribute extends TaskGeneratorAttribute{
	

	public CutTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
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
