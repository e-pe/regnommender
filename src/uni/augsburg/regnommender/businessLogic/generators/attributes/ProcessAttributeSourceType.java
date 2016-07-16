package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;

/**
 *  attribute indicating if user wants to process garden products
 */
public class ProcessAttributeSourceType implements 
ITaskAttributeContextSourceType<UserData, Float> {
	
private UserData context;
private ITaskAttributeSource<Float> source;

/**
 * 
 * @param context
 */
public ProcessAttributeSourceType(Object context) {
	this.setContext((UserData)context);
	this.source = this.createSource();
}

/**
 * 
 */
public String getName() {
	return null;
}

/**
 * 
 */
public ITaskAttributeSource<Float> getSource() {
	return this.source;
}

/**
 * 
 */
public UserData getContext() {
	return this.context;
}

/**
 * 
 */
public void setContext(UserData value) {
	this.context = value;
}

/**
 * 
 * @return actual attribute value: 
 */
private ITaskAttributeSource<Float> createSource() {
	
	return new ITaskAttributeSource<Float>() {
		public Float getValue() {
			final float hopefullyTasksThreshold= 0.7f;
			if(context.getUseGardenProducts())return hopefullyTasksThreshold;
			else return 0.0f;
		}
	};
}

}
