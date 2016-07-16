package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 * 
 *  how much time does the user spend doing garden work
 *
 */
public class UserTimeConsumptionAttributeSourceType implements 
	ITaskAttributeContextSourceType<UserData, Double> {

	private UserData context;
	private ITaskAttributeSource<Double> source;
	
	public UserTimeConsumptionAttributeSourceType(Object context) {
		this.setContext((UserData)context);
		this.source = this.createSource();
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ITaskAttributeSource<Double> getSource() {
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

	private ITaskAttributeSource<Double> createSource() {
		return new ITaskAttributeSource<Double>() {
			public Double getValue() {
				//get time user wants to spend on garden work
				return context.getTimeSetting();
			}
		};
	}
	
}

