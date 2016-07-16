package uni.augsburg.regnommender.businessLogic;

import java.util.*;

import uni.augsburg.regnommender.core.*;

/**
 * 
 *  This class provides the functionality to group specific user tasks which share the same task.
 *  
 */
public class UserTaskGroupData {
	private String id;
	private TaskData task;
	
	private ArrayList<UserTaskData> userTasks;
	private ILazyLoad<ArrayList<UserTaskData>> userTaskLoader;
	
	/**
	 * Constructor.
	 */
	public UserTaskGroupData() {
		this.task = new TaskData();
	}
	
	/**
	 * Gets the id of the group.
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of the group.
	 * @param value
	 */
	public void setId(String value) {
		this.id = value;
	}
	
	/**
	 * Gets the task.
	 * @return
	 */
	public TaskData getTask() {
		return this.task;
	}
	
	/**
	 * Loads user tasks on demand.
	 * @return An iterator with user tasks.
	 * @throws Exception 
	 */
	public Iterator<UserTaskData> getUserTasks() throws Exception {
		if(this.userTasks == null) {
			if(this.userTaskLoader == null)
				throw new Exception("No Loader is specified for loading task attributes.");
			
			this.userTasks = this.userTaskLoader.load();
		}
		
		return this.userTasks.iterator();
	}
	
	/**
	 * Sets the specific user task loader for retrieving the corresponding user tasks.  
	 * 
	 * @return The reference to the current group.
	 */
	public UserTaskGroupData setSettings(
			ILazyLoad<ArrayList<UserTaskData>> userTaskLoader) {
		
		this.userTaskLoader = userTaskLoader;
		
		return this;
	}
		
	/**
	 * Marks all user tasks within the group as done.
	 */
	public void markUserTasksAsDone() {
		Iterator<UserTaskData> itemIt;
		try {
			itemIt = this.getUserTasks();
			
			while(itemIt.hasNext()) {
				UserTaskData item = itemIt.next();
				item.setIsDone(true);
				item.setDoneOn(new Date());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Marks all user tasks within the group as undone.
	 */
	public void markUserTasksAsUnDone() {
		Iterator<UserTaskData> itemIt;
		try {
			itemIt = this.getUserTasks();
			
			while(itemIt.hasNext()) {
				UserTaskData item = itemIt.next();
				item.setIsDone(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
