package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 * 
 *  user wants decorative garden?
 *
 */
public class UserDecorationAttributeSourceType implements 
	ITaskAttributeContextSourceType<UserData, Double> {
	
	private UserData context;
	private ITaskAttributeSource<Double> source;
	
	public UserDecorationAttributeSourceType(Object context) {
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
				return context.getDecoSetting();
			}
		};
	}	
}

