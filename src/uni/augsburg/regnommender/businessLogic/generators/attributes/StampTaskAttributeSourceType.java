package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.*;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 * 
 *  last execution of task is sufficiantly long ago
 *
 */
public class StampTaskAttributeSourceType implements 
	ITaskAttributeContextSourceType<UserTaskData, Date> {
	
	private UserTaskData context;
	private ITaskAttributeSource<Date> source;
	
	/**
	 * 
	 */
	public StampTaskAttributeSourceType(Object context) {
		this.setContext((UserTaskData)context);
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
	public ITaskAttributeSource<Date> getSource() {
		return this.source;
	}

	/**
	 * 
	 */
	public UserTaskData getContext() {
		return this.context;
	}

	/**
	 * 
	 */
	public void setContext(UserTaskData value) {
		this.context = value;
	}
	
	/**
	 * 
	 * @return
	 */
	private ITaskAttributeSource<Date> createSource() {
		return new ITaskAttributeSource<Date>() {
			public Date getValue() {
				if(context != null)
					return context.getDoneOn();
				
				return null;
			}
		};
	}
}
