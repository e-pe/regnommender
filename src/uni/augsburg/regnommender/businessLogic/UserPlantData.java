package uni.augsburg.regnommender.businessLogic;

import java.util.Date;

/**
 * 
 * This class contains information about a specific plant existing in the garden of the user.
 *
 */
public class UserPlantData {
	private PlantData plant;
	private PlantStatus plantStatus;
	private String id;
	private Date addedOn;
	private Date removedOn;
	private boolean isRemoved;
	
	/**
	 * Constructor
	 */
	public UserPlantData() {
		this.plant = new PlantData();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public PlantData getPlant() {
		return this.plant;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getAddedOn(){
		return this.addedOn;
	}
	
	/**
	 * 
	 * @param addedOn
	 */
	public void setAddedOn(Date addedOn){
		this.addedOn = addedOn;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getRemovedOn() {
		return this.removedOn;
	}
	
	/**
	 * 
	 * @param removedOn
	 */
	public void setRemovedOn(Date removedOn) {
		this.removedOn = removedOn;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsRemoved(){
		return this.isRemoved;
	}
	
	/**
	 * 
	 * @return
	 */
	public void setIsRemoved(boolean isRemoved){
		this.isRemoved = isRemoved;
	}

	public PlantStatus getPlantStatus() {
		return plantStatus;
	}

	public void setPlantStatus(PlantStatus plantStatus) {
		this.plantStatus = plantStatus;
	}
	
}
