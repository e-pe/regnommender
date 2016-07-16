package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.Calendar;

import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSourceType;

/**
 * 
 *  attribute for user situation 0 user is not available 1 tasks can be presentet to user
 *
 */
public class GPSTaskAttributeSourceType implements ITaskAttributeSourceType<Integer> {
	private ITaskAttributeSource<Integer> source;
	
	/**
	 * 
	 */
	public GPSTaskAttributeSourceType(){
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
			
				return cal.get(Calendar.DAY_OF_YEAR);
			}
			
		};
	}
}
