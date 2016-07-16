package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.*;

import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 *  DayTimeAttribute returns how far the day has progressed
 */
public class DayTimeAttributeSourceType implements ITaskAttributeSourceType<Integer> {
	private ITaskAttributeSource<Integer> source;
	
	/**
	 * 
	 */
	public DayTimeAttributeSourceType(){
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
	public ITaskAttributeSource<Integer> getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return
	 */
	public ITaskAttributeSource<Integer> createSource() {
		return new ITaskAttributeSource<Integer>() {

			public Integer getValue() {
				Calendar cal = Calendar.getInstance();
			
				return cal.get(Calendar.HOUR_OF_DAY);
			}
			
		};
	}

}