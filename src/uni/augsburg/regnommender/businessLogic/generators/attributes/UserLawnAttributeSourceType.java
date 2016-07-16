package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;
import uni.augsburg.regnommender.presentation.GardenCategory;

/**
 * 
 *  user has lawn?
 *
 */
public class UserLawnAttributeSourceType implements 
ITaskAttributeContextSourceType<UserData, Float> {

private UserData context;
private ITaskAttributeSource<Float> source;

public UserLawnAttributeSourceType(Object context) {
	this.setContext((UserData)context);
	this.source = this.createSource();
}

public String getName() {
	// TODO Auto-generated method stub
	return null;
}

public ITaskAttributeSource<Float> getSource() {
	// TODO Auto-generated method stub
	return this.source;
}

public UserData getContext() {
	// TODO Auto-generated method stub
	return this.context;
}

public void setContext(UserData value) {
	this.context=value;
	
}

private ITaskAttributeSource<Float> createSource() {
	return new ITaskAttributeSource<Float>() {
		public Float getValue() {
			//get time user wants to spend on garden work
			if(context.getGardenCurrent().contains(GardenCategory.lawn)
					||context.getGardenPreferences().contains(GardenCategory.lawn))
				return 1.0f;
			else return 0.0f;
					
		}
	};
}

}