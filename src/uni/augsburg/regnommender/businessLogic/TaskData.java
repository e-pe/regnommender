package uni.augsburg.regnommender.businessLogic;

import java.util.*;

import uni.augsburg.regnommender.core.*;

/**
 * 
 * This class contains meta data information for a task.
 *
 */
public class TaskData {
	private String id;
	private String name;
	private float threshold;
	private String description;
	private String categoryPictureIcon;
	private String categoryPictureButton;
	private ArrayList<TaskAttributeData> attributes;
	private boolean isPlantTask;
	private int duration;
	
	private ILazyLoad<ArrayList<TaskAttributeData>> attributeLoader;
	private boolean done;
	
	/**
	 * Constructor
	 */
	public TaskData() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param username
	 * @param description
	 */
	public TaskData(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/**
	 * 
	 * @return
	 */
	public TaskData setSettings(
			ILazyLoad<ArrayList<TaskAttributeData>> attributeLoader) {
		
		this.attributeLoader = attributeLoader;
		
		return this;
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
	 */
	public void setId(String value) {
		this.id = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getThreshold() {
		return this.threshold;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setThreshold(Float value) {
		this.threshold = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setName(String value) {
		this.name = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setDescription(String value) {
		this.description = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterator<TaskAttributeData> getAttributes() throws Exception {
		if(this.attributes == null) {
			if(this.attributeLoader == null)
				throw new Exception("No Loader is specified for loading task attributes.");
			
			this.attributes = this.attributeLoader.load();
		}
		
		return this.attributes.iterator();
	}
	
	/**
	 * 
	 * @param task
	 */
	public void loadFrom(TaskData task) {
		this.id = task.getId();
		this.name = task.getName();
		this.threshold = task.getThreshold();
		this.description = task.getDescription();
		this.isPlantTask = task.getIsPlantTask();
	}

	public String getCategoryPictureIcon() {
		return categoryPictureIcon;
	}

	public void setCategoryPictureIcon(String categoryPictureIcon) {
		this.categoryPictureIcon = categoryPictureIcon;
	}

	public String getCategoryPictureButton() {
		return categoryPictureButton;
	}

	public void setCategoryPictureButton(String categoryPictureButton) {
		this.categoryPictureButton = categoryPictureButton;
	}

	public boolean getIsPlantTask() {
		return isPlantTask;
	}

	public void setIsPlantTask(boolean isPlantTask) {
		this.isPlantTask = isPlantTask;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}

