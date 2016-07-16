package uni.augsburg.regnommender.presentation;


import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.PlantStatus;
import android.graphics.Bitmap;

/**
 * This class is only used in the Inventory.
 */
public class Plant {

	private String name;
	private boolean selected;
	private Bitmap picture;
	private PlantStatus status;
	private PlantData plantData;
	private boolean isRemoved;
	
	
	public Plant(String name, Bitmap picture, PlantStatus status, PlantData plantData, boolean isRemoved) {
		super();
		this.name = name;
		this.selected = false;
		this.picture = picture;
		this.setStatus(status);
		this.setPlantData(plantData);
		this.setRemoved(isRemoved);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Bitmap getPicture() {
		return picture;
	}
	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public PlantStatus getStatus() {
		return status;
	}

	public void setStatus(PlantStatus status) {
		this.status = status;
	}

	public PlantData getPlantData() {
		return plantData;
	}

	public void setPlantData(PlantData plantData) {
		this.plantData = plantData;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	
}
