package uni.augsburg.regnommender.businessLogic;

import java.util.TreeSet;

import uni.augsburg.regnommender.presentation.GardenCategory;

/**
 * 
 * This class contains information about user settings, user preferences and user configuration.
 *
 */
public class UserData {
	
	// Unkraut, wild, strukturiert, viele Bäume, Schatten, Brachland, 
	private TreeSet<GardenCategory> gardenCurrent;
	
	// Rasen, Obstgarten, Ziergarten, Blumenbeet, Gemüsegarten, Gewässer, 
	private TreeSet<GardenCategory> gardenPreferences;
	
	private String id;
	private String name;
	private double strainSetting;
	private double timeSetting;
	private double financeSetting;
	private String gardenLocation;
	private double gardenGPSLongitude;
	private double gardenGPSLatitude;	
	private boolean hasChildren; // toxic Alert
	private boolean useGardenProducts = true;	
	private double reminderSetting = 2.0;
	private double cropSetting;
	private double decoSetting;
	private double tidySetting;
	
	
	/**
	 * Constructor
	 */
	public UserData() {
		gardenCurrent = new TreeSet<GardenCategory>();
		gardenPreferences = new TreeSet<GardenCategory>();	
	}
	
	
	public double getCropSetting() {
		return cropSetting;
	}


	public void setCropSetting(double cropSetting) {
		this.cropSetting = cropSetting;
	}


	public double getDecoSetting() {
		return decoSetting;
	}


	public void setDecoSetting(double decoSetting) {
		this.decoSetting = decoSetting;
	}


	public double getTidySetting() {
		return tidySetting;
	}


	public void setTidySetting(double tidySetting) {
		this.tidySetting = tidySetting;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setId(String value){
		this.id = value;
	}

	/**
	 * 
	 * @return
	 */
	public double getStrainSetting() {
		return strainSetting;
	}

	/**
	 * 
	 * @param strainSetting
	 */
	public void setStrainSetting(double strainSetting) {
		this.strainSetting = strainSetting;
	}

	/**
	 * 
	 * @return
	 */
	public double getTimeSetting() {
		return timeSetting;
	}
	
	/**
	 * 
	 * @param timeSetting
	 */
	public void setTimeSetting(double timeSetting) {
		this.timeSetting = timeSetting;
	}

	/**
	 * 
	 * @return
	 */
	public double getFinanceSetting() {
		return financeSetting;
	}

	/**
	 * 
	 * @param financeSetting
	 */
	public void setFinanceSetting(double financeSetting) {
		this.financeSetting = financeSetting;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public double getGardenGPSLongitude() {
		return gardenGPSLongitude;
	}

	/**
	 * 
	 * @param gardenGPSLongitude
	 */
	public void setGardenGPSLongitude(double gardenGPSLongitude) {
		this.gardenGPSLongitude = gardenGPSLongitude;
	}

	/**
	 * 
	 * @return
	 */
	public double getGardenGPSLatitude() {
		return gardenGPSLatitude;
	}

	/**
	 * 
	 * @param gardenGPSLatitude
	 */
	public void setGardenGPSLatitude(double gardenGPSLatitude) {
		this.gardenGPSLatitude = gardenGPSLatitude;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getHasChildren() {
		return hasChildren;
	}

	/**
	 * 
	 * @param hasChildren
	 */
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getUseGardenProducts() {
		return useGardenProducts;
	}

	/**
	 * 
	 * @param useGardenProducts
	 */
	public void setUseGardenProducts(boolean useGardenProducts) {
		this.useGardenProducts = useGardenProducts;
	}

	/**
	 * 
	 * @return
	 */
	public double getReminderSetting() {
		return reminderSetting;
	}

	/**
	 * 
	 * @param reminderSetting
	 */
	public void setReminderSetting(double reminderSetting) {
		this.reminderSetting = reminderSetting;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGardenLocation() {
		return gardenLocation;
	}

	/**
	 * 
	 * @param userLocation
	 */
	public void setGardenLocation(String userLocation) {
		this.gardenLocation = userLocation;
	}

	public TreeSet<GardenCategory> getGardenPreferences() {
		return gardenPreferences;
	}

	public TreeSet<GardenCategory> getGardenCurrent() {
		return gardenCurrent;
	}
	
	public void setGardenCurrent(TreeSet<GardenCategory> gardenUpdate) {
		gardenCurrent = gardenUpdate;
	}

}
