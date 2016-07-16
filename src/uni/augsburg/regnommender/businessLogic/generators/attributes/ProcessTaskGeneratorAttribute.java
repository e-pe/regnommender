package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;
/**
 *  attribute indicating if user wants to process garden products
 */
public class ProcessTaskGeneratorAttribute extends TaskGeneratorAttribute{
	

	public ProcessTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	@Override
	public float collect()
	{
		
		//db request
		float value = (Float)this.sourceType.getSource().getValue();
		
		return value;
	}
}	
