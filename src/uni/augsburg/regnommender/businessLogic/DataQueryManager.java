package uni.augsburg.regnommender.businessLogic;

import uni.augsburg.regnommender.dataAccess.*;

import android.content.*;
import android.database.*;
import android.net.*;
import android.util.*;

/**
 * 
 * The class provides the functionality to retrieve additional data from the database. 
 *
 */
public class DataQueryManager {
	private DataManager dataManager;
	private ContentResolver resolver;
	
	/**
	 * Constructor
	 */
	public DataQueryManager(DataManager dataManager, ContentResolver resolver) {
		this.resolver = resolver;
		this.dataManager = dataManager;
	}
	
	/**
	 * Finds the last user task which was done by user.
	 * @return The specific user task.
	 */
	public UserTaskData findLastDoneUserTask(String taskId, String plantId) {
		Cursor c = null;		
		UserTaskData userTask = null;
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + "=" + "1" + " and "
					+ GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID + "=" + taskId + " and "
					+ GardenRepositoryMetaData.UserTaskTableMetaData.PLANT_ID + (plantId != null ? "=" + plantId : " IS NULL");
			
			String sortOrder = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON + " DESC";
			
			c = resolver.query(uri,
				new String[] { "userTasksByDate" }, //queryType
				selection, //selection string
				null, //selection args array of strings
				sortOrder); //sort order
								
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				userTask = new UserTaskData();			
				this.dataManager.fillUserTask(userTask, c);
				
				//gets only the first user task
				break;
			}		
		
		} catch (Exception ex) {
			Log.d("ERROR", ex.getMessage());
			
		} finally {
			c.close();
		}
		
		return userTask;
	}
	
	/**
	 * Finds the last user task which was done by user.
	 * 
	 * @param taskId of the specific task.
	 * @return The specific user task.
	 */
	public UserTaskData findLastDoneUserTask(String taskId) {
		Cursor c = null;		
		UserTaskData userTask = null;
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + "=" + "1" + " and "
					+ GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID + "=" + taskId ;
			
			String sortOrder = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON + " DESC";
			
			c = resolver.query(uri,
				new String[] { "userTasksByDate" }, //queryType
				selection, //selection string
				null, //selection args array of strings
				sortOrder); //sort order
								
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				userTask = new UserTaskData();			
				this.dataManager.fillUserTask(userTask, c);
				
				//gets only the first user task
				break;
			}		
		
		} catch (Exception ex) {
			Log.d("ERROR", ex.getMessage());
			
		} finally {
			c.close();
		}
		
		return userTask;
	}
	
}
