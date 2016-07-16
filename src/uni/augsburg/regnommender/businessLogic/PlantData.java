package uni.augsburg.regnommender.businessLogic;

/**
 * 
 * This class contains meta data information for a plant.
 *
 */
public class PlantData {
	private String id;
	private String name;
	
	private String description;
	private String plantingDescription;
	private String seedingDescription;
	private String cuttingDescription;
	private String fertilizingDescription;
	private String pouringDescription;
	private String usingGardenProductsDescription;
	private String scionDescription;

	private String imageUrl1;
	private String imageUrl2;
	private String imageUrl3;
	
	private double waterConsumption;
	private double sunlightRequirement;
	private double timeConsumption;
	private double fertilizer;
	private double price;
	private double cut;
	private boolean isToxic;
	
	private String category;
	
	private String plantTime;
	private String fruitTime;
	private String bloomTime;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the plantingDescription
	 */
	public String getPlantingDescription() {
		return plantingDescription;
	}
	/**
	 * @param plantingDescription the plantingDescription to set
	 */
	public void setPlantingDescription(String plantingDescription) {
		this.plantingDescription = plantingDescription;
	}
	/**
	 * @return the seedingDescription
	 */
	public String getSeedingDescription() {
		return seedingDescription;
	}
	/**
	 * @param seedingDescription the seedingDescription to set
	 */
	public void setSeedingDescription(String seedingDescription) {
		this.seedingDescription = seedingDescription;
	}
	/**
	 * @return the cuttingDescription
	 */
	public String getCuttingDescription() {
		return cuttingDescription;
	}
	/**
	 * @param cuttingDescription the cuttingDescription to set
	 */
	public void setCuttingDescription(String cuttingDescription) {
		this.cuttingDescription = cuttingDescription;
	}
	/**
	 * @return the fertilizingDescription
	 */
	public String getFertilizingDescription() {
		return fertilizingDescription;
	}
	/**
	 * @param fertilizingDescription the fertilizingDescription to set
	 */
	public void setFertilizingDescription(String fertilizingDescription) {
		this.fertilizingDescription = fertilizingDescription;
	}
	/**
	 * @return the pouringDescription
	 */
	public String getPouringDescription() {
		return pouringDescription;
	}
	/**
	 * @param pouringDescription the pouringDescription to set
	 */
	public void setPouringDescription(String pouringDescription) {
		this.pouringDescription = pouringDescription;
	}
	/**
	 * @return the usingGardenProductsDescription
	 */
	public String getUsingGardenProductsDescription() {
		return usingGardenProductsDescription;
	}
	/**
	 * @param usingGardenProductsDescription the usingGardenProductsDescription to set
	 */
	public void setUsingGardenProductsDescription(
			String usingGardenProductsDescription) {
		this.usingGardenProductsDescription = usingGardenProductsDescription;
	}
	/**
	 * @return the scionDescription
	 */
	public String getScionDescription() {
		return scionDescription;
	}
	/**
	 * @param scionDescription the scionDescription to set
	 */
	public void setScionDescription(String scionDescription) {
		this.scionDescription = scionDescription;
	}
	/**
	 * @return the imageUrl1
	 */
	public String getImageUrl1() {
		return imageUrl1;
	}
	/**
	 * @param imageUrl1 the imageUrl1 to set
	 */
	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}
	/**
	 * @return the imageUrl2
	 */
	public String getImageUrl2() {
		return imageUrl2;
	}
	/**
	 * @param imageUrl2 the imageUrl2 to set
	 */
	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}
	/**
	 * @return the imageUrl3
	 */
	public String getImageUrl3() {
		return imageUrl3;
	}
	/**
	 * @param imageUrl3 the imageUrl3 to set
	 */
	public void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}
	/**
	 * @return the waterConsumption
	 */
	public double getWaterConsumption() {
		return waterConsumption;
	}
	/**
	 * @param waterConsumption the waterConsumption to set
	 */
	public void setWaterConsumption(double waterConsumption) {
		this.waterConsumption = waterConsumption;
	}
	/**
	 * @return the sunlightRequirement
	 */
	public double getSunlightRequirement() {
		return sunlightRequirement;
	}
	/**
	 * @param sunlightRequirement the sunlightRequirement to set
	 */
	public void setSunlightRequirement(double sunlightRequirement) {
		this.sunlightRequirement = sunlightRequirement;
	}
	/**
	 * @return the timeConsumption
	 */
	public double getTimeConsumption() {
		return timeConsumption;
	}
	/**
	 * @param timeConsumption the timeConsumption to set
	 */
	public void setTimeConsumption(double timeConsumption) {
		this.timeConsumption = timeConsumption;
	}
	/**
	 * @return the fertilizer
	 */
	public double getFertilizer() {
		return fertilizer;
	}
	/**
	 * @param fertilizer the fertilizer to set
	 */
	public void setFertilizer(double fertilizer) {
		this.fertilizer = fertilizer;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the cut
	 */
	public double getCut() {
		return cut;
	}
	/**
	 * @param cut the cut to set
	 */
	public void setCut(double cut) {
		this.cut = cut;
	}
	/**
	 * @return the isToxic
	 */
	public boolean isToxic() {
		return isToxic;
	}
	/**
	 * @param isToxic the isToxic to set
	 */
	public void setToxic(boolean isToxic) {
		this.isToxic = isToxic;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the plantTime
	 */
	public String getPlantTime() {
		return plantTime;
	}
	/**
	 * @param plantTime the plantTime to set
	 */
	public void setPlantTime(String plantTime) {
		this.plantTime = plantTime;
	}
	/**
	 * @return the fruitTime
	 */
	public String getFruitTime() {
		return fruitTime;
	}
	/**
	 * @param fruitTime the fruitTime to set
	 */
	public void setFruitTime(String fruitTime) {
		this.fruitTime = fruitTime;
	}
	/**
	 * @return the bloomTime
	 */
	public String getBloomTime() {
		return bloomTime;
	}
	/**
	 * @param bloomTime the bloomTime to set
	 */
	public void setBloomTime(String bloomTime) {
		this.bloomTime = bloomTime;
	}
	
	/**
	 * 
	 * @param plant
	 */
	public void loadFrom(PlantData plant) {
		this.id = plant.getId();
		this.name = plant.getName();
	}
}
