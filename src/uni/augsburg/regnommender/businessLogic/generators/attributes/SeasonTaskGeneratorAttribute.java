package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

/**
 *  attribute indicating if we are within season
 */
public class SeasonTaskGeneratorAttribute extends TaskGeneratorAttribute {

	public SeasonTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	//normalized and wighted value
	@Override
	public float getNormalizedValue() 
	{
		float outerMinDay= this.threshold.getLowThreshold(); // actual db value		
		float outerMaxDay= this.threshold.getHighThreshold(); //actual db value
		
		//gets the current value from the specified source type
		Integer currentDay = (Integer)this.sourceType.getSource().getValue();
		
		/** Saison ist definiert als
		 * Hauptsaison = zwischen innerMinDay und innerMaxDay
		 * Nebensaison = zwischen outerMinDay und innerMin Day bzw. innerMaxDay und outerMaxDay
		 * ausserhalb Saison = Rest
		 */
		float innerMinDay=0, innerMaxDay=0, season=0;
		
		//interpolated inner values of Trapezoid to get increase and decrease to actual value
		//might be a spline in future
		
		innerMinDay = (float) (outerMinDay + ((outerMaxDay - outerMinDay) * 0.1));
		innerMaxDay = (float) (outerMaxDay - ((outerMaxDay - outerMinDay) * 0.1));
		
		// Falls aktueller Tag in ansteigender Nebensaison
		if (currentDay <= innerMinDay && currentDay >= outerMinDay) {
			season =  ((currentDay - outerMinDay) / (innerMinDay - outerMinDay));
		}
		
		// Falls aktueller Tag in fallender Nebensaison
		if (currentDay >= innerMaxDay && currentDay <= outerMaxDay) {
			season = ((currentDay - innerMaxDay  ) / (outerMaxDay - innerMaxDay));
		}
		
		// Falls aktueller Tag außerhalb Saison
		if (currentDay < outerMinDay || currentDay > outerMaxDay) {
			season = 0;
		}
		
		// Falls aktueller Tag innerhalb Hauptsaison
		if (currentDay > innerMinDay && currentDay < innerMaxDay) {
			season = 1;
		}
		
		return season;
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
