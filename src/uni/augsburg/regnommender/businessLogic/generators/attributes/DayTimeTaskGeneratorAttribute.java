package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;

/**
 *  DayTimeAttribute returns how far the day has progressed
 */
public class DayTimeTaskGeneratorAttribute extends TaskGeneratorAttribute {

	public DayTimeTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	@Override
	public float getNormalizedValue() 
	{
		float outerMinHour= this.threshold.getLowThreshold(); // actual db value	
		float outerMaxHour= this.threshold.getHighThreshold(); //actual db value
		
		//gets the current value from the specified source type
		Integer currentHour = (Integer)this.sourceType.getSource().getValue();
		
		/** dayTime ist definiert als
		 * Hauptsaison = zwischen innerMinHour und innerMaxHour
		 * Nebensaison = zwischen outerMinHour und innerMinHour bzw. innerMaxHour und outerMaxHour
		 * ausserhalb dayTime = Rest
		 */
		float innerMinHour=0, innerMaxHour=0, dayTime=0;
				
		//interpolated inner values of Trapezoid to get increase and decrease to actual value
		//might be a spline in future
		
		innerMinHour = (float) (outerMinHour + ((outerMaxHour - outerMinHour) * 0.1));
		innerMaxHour = (float) (outerMaxHour - ((outerMaxHour - outerMinHour) * 0.1));
		
		// Falls aktueller Tag in ansteigender Nebensaison
		if (currentHour <= innerMinHour && currentHour >= outerMinHour) {
			dayTime =  ((currentHour - outerMinHour) / (innerMinHour - outerMinHour));
		}
		
		// Falls aktueller Tag in fallender Nebensaison
		if (currentHour >= innerMaxHour && currentHour <= outerMaxHour) {
			dayTime = ((currentHour - innerMaxHour ) / (outerMaxHour - innerMaxHour));
		}
		
		// Falls aktueller Tag au�erhalb Saison
		if (currentHour < outerMinHour || currentHour > outerMaxHour) {
			dayTime = 0;
		}
		
		// Falls aktueller Tag innerhalb Hauptsaison
		if (currentHour > innerMinHour && currentHour < innerMaxHour) {
			dayTime = 1;
		}
		
		return dayTime;
	}
	
	@Override
	/**
	 * Will be not invoked. 
	 */
	public float collect()
	{	
		return 0f;
	}
}
