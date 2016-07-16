package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.businessLogic.generators.simulators.*;
import uni.augsburg.regnommender.core.*;

/**
 * 
 *  attribute for user situation 0 user is not available 1 tasks can be presentet to user
 *
 */
public class GPSTaskGeneratorAttribute extends TaskGeneratorAttribute{
	

	public GPSTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}
	
	
	@Override
	public float getNormalizedValue() 
	{
		if(UserAvailabilitySimulator.getAtHome() && UserAvailabilitySimulator.getUserAviable() )return 1.0f;
		else if(UserAvailabilitySimulator.getAtHome() || UserAvailabilitySimulator.getUserAviable() ) return 0.5f;
		else return 0.0f;
	}

	@Override
	public float collect()
	{		
		if(UserAvailabilitySimulator.getAtHome() && UserAvailabilitySimulator.getUserAviable() )return 1.0f;
		else if(UserAvailabilitySimulator.getAtHome() || UserAvailabilitySimulator.getUserAviable() ) return 0.5f;
		else return 0.0f;
		
	}

}
