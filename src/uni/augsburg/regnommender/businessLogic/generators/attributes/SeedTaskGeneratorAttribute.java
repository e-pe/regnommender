package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.Calendar;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.core.*;

public class SeedTaskGeneratorAttribute extends TaskGeneratorAttribute{
	
	/**
	 * 
	 *  plant was grown from seed
	 *
	 */
	public SeedTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}

	@Override
	public float getNormalizedValue() 
	{
		float outerMinMonth = (int)this.collect() / 100; // actual db value		
		float outerMaxMonth = ((int)this.collect()) % 100; //actual db value
		
		//gets the current value from the specified source type
		Calendar cal = Calendar.getInstance();
		int currentMonth = (int)cal.get(Calendar.MONTH) + 1;
		
		/** Saison ist definiert als
		 * Hauptsaison = zwischen innerMinDay und innerMaxDay
		 * Nebensaison = zwischen outerMinDay und innerMin Day bzw. innerMaxDay und outerMaxDay
		 * ausserhalb Saison = Rest
		 */
		float innerMinMonth=0, innerMaxMonth=0, season=0;
		
		//interpolated inner values of Trapezoid to get increase and decrease to actual value
		//might be a spline in future
		
		innerMinMonth = (float) (outerMinMonth + ((outerMaxMonth - outerMinMonth) * 0.1));
		innerMaxMonth = (float) (outerMaxMonth - ((outerMaxMonth - outerMinMonth) * 0.1));
		
		// Falls aktueller Tag in ansteigender Nebensaison
		if (currentMonth <= innerMinMonth && currentMonth >= outerMinMonth) {
			season = ((currentMonth - outerMinMonth) / (innerMinMonth - outerMinMonth));
		}
		// Falls aktueller Tag in fallender Nebensaison
		if (currentMonth >= innerMaxMonth && currentMonth <= outerMaxMonth) {
			season = (( currentMonth - innerMaxMonth) / (outerMaxMonth - innerMaxMonth));
		}
		// Falls aktueller Tag außerhalb Saison
		if (currentMonth < outerMinMonth || currentMonth > outerMaxMonth) {
			season = 0;
		}
		// Falls aktueller Tag innerhalb Hauptsaison
		if (currentMonth > innerMinMonth && currentMonth < innerMaxMonth) {
			season = 1;
		}
		
		return season;
	}
	
	@Override
	public float collect()
	{		
		//db request
		float value=(Float)this.sourceType.getSource().getValue();
		
		return value;
	}
}	
