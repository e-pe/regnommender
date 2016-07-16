package uni.augsburg.regnommender.presentation;

import java.util.*;
import uni.augsburg.regnommender.core.*;
import uni.augsburg.regnommender.businessLogic.*;

import android.content.*;
import android.util.*;

/**
 * 
 * This class is used in the presentation layer to retrieve data.
 *
 */
public class GardenContext {
	private Context context;
	
	/**
	 * Constructor
	 * 
	 */
	public GardenContext(Context context) {
		this.context = context;
	}
	
	
	/**
	 * Gets all plants.
	 * 
	 */
	public void getAllPlants(
			ISuccess<Iterator<PlantData>> onSuccess, 
			IFailure<Exception> onFailure){
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(
						recommender.getAllPlants().iterator());
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Gets plants selected by user.
	 */
	public void getPlantsByUser(
			ISuccess<Iterator<UserPlantData>> onSuccess, 
			IFailure<Exception> onFailure){
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null) 
				onSuccess.invoke(
						recommender.getUserPlants().iterator());
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Gets user settings.
	 */
	public void getUserSettings(
			ISuccess<UserData> onSuccess, 
			IFailure<Exception> onFailure){
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(recommender.getUserSettings());
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Creates user settings.
	 */
	public void createUserSettings(
			ISuccess<UserData> onSuccess, 
			IFailure<Exception> onFailure){
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(recommender.createUserSettings());
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Updates user settings.
	 */
	public void setUserSettings(
			UserData userSettings,
			ISuccess<UserData> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			recommender.updateUserSettings(userSettings);
			
			if(onSuccess != null)
				onSuccess.invoke(null);
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Gets all tasks.
	 */
	public void getAllTasks(
			ISuccess<Iterator<TaskData>> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(
						recommender.getAllTasks().iterator());
		
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Gets pending user tasks.
	 */
	public void getPendingTasksByUser(
			ISuccess<Iterator<UserTaskGroupData>> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(
						recommender.getPendingUserTaskGroups().iterator());
		
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Gets done user tasks.
	 */
	public void getDoneTasksByUser(
			ISuccess<Iterator<UserTaskGroupData>> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			if(onSuccess != null)
				onSuccess.invoke(
						recommender.getDoneUserTaskGroups().iterator());
		
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	/**
	 * Adds a new plant.
	 */
	public void addUserPlant(
			UserPlantData userPlantData,
			ISuccess<UserPlantData> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			recommender.addUserPlant(userPlantData);
			
			if(onSuccess != null)
				onSuccess.invoke(null);
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}
	
	
	/**
	 * Updates a plant.
	 */
	public void updateUserPlant(
			UserPlantData userPlantData,
			ISuccess<UserPlantData> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			recommender.updateUserPlant(userPlantData);
			
			if(onSuccess != null)
				onSuccess.invoke(null);
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}	
	
	
	/**
	 * Updates a user task generated for the specific user.
	 */
	public void updateUserTask(
			UserTaskData userTaskData,
			ISuccess<UserTaskData> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			recommender.updateUserTask(userTaskData);
			
			if(onSuccess != null)
				onSuccess.invoke(null);
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}	
	
	
	/**
	 * Updates the specific user task group containing user tasks which are of the same task.
	 */
	public void updateUserTaskGroup(
			UserTaskGroupData userTaskGroupData,
			ISuccess<Object> onSuccess, 
			IFailure<Exception> onFailure) {
		
		try {
			DataManager recommender = new DataManager(
					this.context.getContentResolver());
			
			recommender.updateUserTaskGroup(userTaskGroupData);
			
			if(onSuccess != null)
				onSuccess.invoke(null);
			
		} catch (Exception ex) {
			if(onFailure != null) {
				Log.e("GardenContext", ex.getMessage());
				onFailure.invoke(ex);
			}
		}
	}	
	
}
