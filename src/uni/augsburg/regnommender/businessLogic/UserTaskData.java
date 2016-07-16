package uni.augsburg.regnommender.businessLogic;

import java.util.Date;

import android.util.Log;

/**
 * 
 * This class contains information about a specific task suggested to the user.
 *
 */
public class UserTaskData {
	private TaskData task;
	private PlantData plant;
	
	private String id;
	private String groupId;
	private Date doneOn;
	private boolean isDone;
	private float priority;
	private Date addedOn;
	private boolean autoRemoved;
	
	/**
	 * Constructor
	 */
	public UserTaskData() {
		this.task = new TaskData();
		this.plant = new PlantData();
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
	public void setId(String value) {
		this.id = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGroupId(){
		return this.groupId;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setGroupId(String value) {
		this.groupId = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public TaskData getTask() {
		return task;
	}
	
	/**
	 * 
	 * @return
	 */
	public PlantData getPlant() {
		return plant;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getPriority() {
		return this.priority;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setPriority(float value) {
		this.priority = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getDoneOn() {
		return doneOn;
	}
	
	/**
	 * 
	 * @param doneOn
	 */
	public void setDoneOn(Date doneOn) {
		this.doneOn = doneOn;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsDone() {
		return this.isDone;
	}
	
	/**
	 * 
	 */
	public void setIsDone(boolean value) {
		this.isDone = value;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public boolean getIsAutoRemoved() {
		return autoRemoved;
	}

	public void setIsAutoRemoved(boolean autoRemoved) {
		this.autoRemoved = autoRemoved;
	}
	
	
	/**
	 * 
	 * @param userTask
	 * @return
	 */
	public boolean equals(UserTaskData userTask) {
		if(!this.getId().equals(userTask.getId()))
			return false;
		
		return true;
	}
	
	/**
	 * 
	 * @param userTask
	 * @return
	 */
	public boolean similarTo(UserTaskData userTask) {
		TaskData task1 = this.getTask();
		TaskData task2 = userTask.getTask();
		
		PlantData plant1 = this.getPlant();
		PlantData plant2 = userTask.getPlant();
		
		//Log.d("user task data similarTo", plant1.getId());
		//Log.d("user task data similarTo", plant2.getId());
		
		if(task1.getId() == null || task1.getId().equals(""))
			return false;
		
		if(task2.getId() == null || task2.getId().equals(""))
			return false;
			
		if(task1.getId().equals(task2.getId())) {
			
			//Log.d("user task data similarTo was here", "0");
			
			if(!task1.getIsPlantTask() && !task2.getIsPlantTask())
				return true;
			
			else if (task1.getIsPlantTask() && task2.getIsPlantTask()) {
				
				//Log.d("user task data similarTo was here", "1");
				
				if(plant1.getId() == null || plant1.getId().equals(""))
					return false;
				
				//Log.d("user task data similarTo was here", "2");
				
				if(plant2.getId() == null || plant2.getId().equals(""))
					return false;
				
				//Log.d("user task data similarTo was here", "3");
				
				if(plant1.getId().equalsIgnoreCase(plant2.getId()))		 
					return true;
			}
		}
			
		return false;	
	}
}
