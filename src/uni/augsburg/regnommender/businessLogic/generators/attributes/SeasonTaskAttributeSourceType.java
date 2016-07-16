package uni.augsburg.regnommender.businessLogic.generators.attributes;

import java.util.Calendar;

import uni.augsburg.regnommender.businessLogic.generators.*;

/**
 *  attribute indicating if we are within season
 */
public class SeasonTaskAttributeSourceType implements ITaskAttributeSourceType<Integer> {
	private ITaskAttributeSource<Integer> source;
	

	public SeasonTaskAttributeSourceType(){
		this.source = this.createSource();
	}
	

	public String getName() {
		return null;
	}


	public ITaskAttributeSource<Integer> getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return day of year
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
