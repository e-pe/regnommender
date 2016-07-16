package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.Calendar;
import java.util.Date;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;
import uni.augsburg.regnommender.businessLogic.generators.TaskGeneratorAttribute;
import uni.augsburg.regnommender.core.IThreshold;

/**
 * 
 *  last execution of task is sufficiantly long ago
 *
 */
public class StampTaskGeneratorAttribute extends TaskGeneratorAttribute { 
	// usertasks as context, request done usertasks concerning current plant and task, sort according to date, choose latest
	
	/**
	 * 
	 * @param task
	 * @param attribute
	 */
	public StampTaskGeneratorAttribute(IThreshold threshold, ITaskAttributeSourceType<?> sourceType) {
		super(threshold, sourceType);
	}
	

	@Override
	/**
	 * Will be not invoked.
	 */
	public float collect() {
		Calendar cal = Calendar.getInstance();
		Object lastDone = this.sourceType.getSource().getValue();
		
		if(lastDone != null) {
			Date lastDoneDate = (Date)lastDone;
			long msDiff=(cal.getTimeInMillis() - lastDoneDate.getTime());
			float value = (float)(msDiff/1000/3600/24);//in days
			return value; 
		}
		
		return this.threshold.getHighThreshold();
	}
}
