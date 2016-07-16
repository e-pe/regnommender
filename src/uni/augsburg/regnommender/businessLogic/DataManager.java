package uni.augsburg.regnommender.businessLogic;

import java.util.*;

import uni.augsburg.regnommender.core.*;
import uni.augsburg.regnommender.dataAccess.*;
import uni.augsburg.regnommender.presentation.*;

import android.content.*;
import android.database.*;
import android.net.*;
import android.provider.*;
import android.util.*;

/**
 * 
 * This class contains the functionality to provide business logic objects fetched from the database. 
 *
 */
public class DataManager {
	private ContentResolver resolver; 
	private DataQueryManager queryManager;
	
	/**
	 * The constructor requires the android content resolver class to perform requests to the specific ContentProvider.
	 * @param resolver for performing database requests. 
	 */
	public DataManager(ContentResolver resolver) {
		this.resolver = resolver;
		this.queryManager = new DataQueryManager(this, resolver);
	}
	
	/**
	 * Gets the query manager object for performing additional requests to the database.
	 * @return
	 */
	public DataQueryManager getQueryManager() {
		return this.queryManager;
	}
	
	/**
	 * Gets a list all plants stored in the database.
	 * @return A list with all available plants.
	 * @throws Exception whether the fetching process could not be finished successfully. 
	 */
	public ArrayList<PlantData> getAllPlants() throws Exception {
		Cursor c = null;
		ArrayList<PlantData> plants = new ArrayList<PlantData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.PlantTableMetaData.CONTENT_URI;
				
			c = resolver.query(uri,
				null, //projection
				null, //selection string
				null, //selection args array of strings
				"ASC"); //sort order
			
			int iId = c.getColumnIndex(
					BaseColumns._ID);
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				PlantData plant = new PlantData();				
				plant.setId(c.getString(iId));
				
				this.fillPlant(plant, c);
				
				plants.add(plant);
			}		
		
		} catch (Exception ex) {
			Log.d("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return plants;
		
	}
	
	/**
	 * Fills a plant object with the specified data retrieved from the database.
	 * @return A plant object with the corresponding data.
	 * @throws Exception
	 */
	private void fillPlant(PlantData plant, Cursor cursor) throws Exception {
		
		
		int iName = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_NAME);
	
		int iDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC);
		
		int iPlantingDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_PLANT);
		
		int iSeedingDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_SEED);
		
		int iScionDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_CUTTING);
		
		int iCutDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_CUT);
		
		int iFertilizingDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_FERTILIZE);
		
		int iPouringDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_POUR);
		
		int iGardenProductsDesc = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_PROCESS);
		
		int iImageUrl1 = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL1);
		
		int iImageUrl2 = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL2);

		int iImageUrl3 = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL3);		
		
		int iWaterConsumption = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_WATER_CONSUMPTION);
		
		int iSunlightRequirement = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_SUNLIGHT_REQUIREMENT);
		
		int iTimeConsumption = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_TIME_CONSUMPTION);
		
		int iFertilizer = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_FERTILIZER);
		
		int iPrice = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_COST);
		
		int iCut = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_CUT);
		
		int iToxic = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_TOXIC);
		
		int iCategory = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_CATEGORY);
		
		int iPlantTime = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_PLANT_TIME);
		
		int iFruitTime = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_FRUIT_TIME);
		
		int iBloomTime = cursor.getColumnIndex(
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_BLOOM_TIME);
		
		
		plant.setName(cursor.getString(iName));
		
		plant.setDescription(cursor.getString(iDesc));
		plant.setPlantingDescription(cursor.getString(iPlantingDesc));
		plant.setSeedingDescription(cursor.getString(iSeedingDesc));
		plant.setScionDescription(cursor.getString(iScionDesc));
		plant.setCuttingDescription(cursor.getString(iCutDesc));
		plant.setFertilizingDescription(cursor.getString(iFertilizingDesc));
		plant.setPouringDescription(cursor.getString(iPouringDesc));
		plant.setUsingGardenProductsDescription(cursor.getString(iGardenProductsDesc));
		plant.setImageUrl1(cursor.getString(iImageUrl1));
		plant.setImageUrl2(cursor.getString(iImageUrl2));
		plant.setImageUrl3(cursor.getString(iImageUrl3));
		
		
		plant.setWaterConsumption(cursor.getDouble(iWaterConsumption));
		plant.setSunlightRequirement(cursor.getDouble(iSunlightRequirement));
		plant.setTimeConsumption(cursor.getDouble(iTimeConsumption));
		plant.setFertilizer(cursor.getDouble(iFertilizer));
		plant.setPrice(cursor.getDouble(iPrice));
		plant.setCut(cursor.getDouble(iCut));
		plant.setToxic((cursor.getInt(iToxic)== 1)?true:false);
		plant.setCategory(cursor.getString(iCategory));
		plant.setPlantTime(cursor.getString(iPlantTime));
		plant.setBloomTime(cursor.getString(iBloomTime));
		plant.setFruitTime(cursor.getString(iFruitTime));
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<UserPlantData> getUserPlants() throws Exception {
		Cursor c = null;
		ArrayList<UserPlantData> userPlants = new ArrayList<UserPlantData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserPlantTableMetaData.CONTENT_URI;
				
			c = resolver.query(uri,
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
			
			//columns from the userplant table
			int iUserPlantId = c.getColumnIndex(
					BaseColumns._ID);
			
			int iPlantId = c.getColumnIndex(
					GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID);
			
			int iPlantAddedOn = c.getColumnIndex(
					GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ADDED_ON);
		
			int iPlantRemovedOn = c.getColumnIndex(
					GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_REMOVED_ON);
			
			int iPlantIsRemoved = c.getColumnIndex(
					GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_IS_REMOVED);
			
			int iPlantStatus = c.getColumnIndex(
					GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_STATUS);
											
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				UserPlantData userPlant = new UserPlantData();
				PlantData plant = userPlant.getPlant();
				
				userPlant.setId(c.getString(iUserPlantId));
				
				
				Date dateAddedOn = new Date();
				dateAddedOn.setTime(c.getLong(iPlantAddedOn)*1000);
				
				userPlant.setAddedOn(dateAddedOn);
				
				Date dateRemovedOn = new Date();
				dateRemovedOn.setTime(c.getLong(iPlantRemovedOn)*1000);
				
				userPlant.setRemovedOn(dateRemovedOn);
				userPlant.setIsRemoved((c.getInt(iPlantIsRemoved)== 1)?true:false);
				userPlant.setPlantStatus(PlantStatus.values()[c.getInt(iPlantStatus)]);
				
				plant.setId(c.getString(iPlantId));
				
				this.fillPlant(plant, c);
								
				userPlants.add(userPlant);
			}		
		
		} catch (Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return userPlants;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public UserData getUserSettings() throws Exception {
		Cursor c = null;
		UserData userData = new UserData();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTableMetaData.CONTENT_URI;
			
			c = resolver.query(uri,
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			
			//columns from the user table
			int iId = c.getColumnIndex(
					BaseColumns._ID);
			
			int iUserName = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_NAME);
			
			int iStrainSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_STRAIN_SETTING);
			
			int iTimeSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_TIME_SETTING);
		
			int iFinanceSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_FINANCE_SETTING);
			
			int iGardenLocation = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_LOCATION);
			
			int iGardenGPSLongitude = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_GPS_LONGITUDE);
		
			int iGardenGPSLatitude = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_GPS_LATITUDE);
			
			int iHasChildren = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_HAS_CHILDREN);
			
			int iUseGardenProducts = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_USE_GARDEN_PRODUCTS);
			
			int iReminderSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_REMINDER_SETTING);
			
			int iGardenPreference = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_PREFERENCE);
			
			int iGardenCurrent = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_CURRENT);
			
			int iCropSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_CROP_SETTING);
			
			int iDecoSetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_DECO_SETTING);
			
			int iTidySetting = c.getColumnIndex(
					GardenRepositoryMetaData.UserTableMetaData.USER_TIDY_SETTING);
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				userData.setId(c.getString(iId));
				userData.setName(c.getString(iUserName));
				userData.setFinanceSetting(c.getDouble(iFinanceSetting));
				userData.setTimeSetting(c.getDouble(iTimeSetting));
				userData.setStrainSetting(c.getDouble(iStrainSetting));
				userData.setReminderSetting(c.getDouble(iReminderSetting));
				userData.setGardenGPSLatitude(c.getDouble(iGardenGPSLatitude));
				userData.setGardenGPSLongitude(c.getDouble(iGardenGPSLongitude));
				userData.setGardenLocation(c.getString(iGardenLocation));
				userData.setHasChildren(c.getInt(iHasChildren) == 0 ? false : true);
				userData.setUseGardenProducts(c.getInt(iUseGardenProducts) == 0 ? false : true);
				userData.setCropSetting(c.getDouble(iCropSetting));
				userData.setDecoSetting(c.getDouble(iDecoSetting));
				userData.setTidySetting(c.getDouble(iTidySetting));
				
				String gardenCurrent = c.getString(iGardenCurrent);
				
				if(gardenCurrent != null && !gardenCurrent.equals("")) {
					String[] gardenCurrentValues = gardenCurrent.split(",");
					for (String value : gardenCurrentValues) {
						userData.getGardenCurrent().add(GardenCategory.valueOf(value));
					}
				}
				
				String gardenPreference = c.getString(iGardenPreference);
				
				if(gardenPreference != null && !gardenPreference.equals("")) {
					String[] gardenPreferenceValues = gardenPreference.split(",");
					for (String value : gardenPreferenceValues) {
						userData.getGardenPreferences().add(GardenCategory.valueOf(value));
					}
				}
				
			}
			
			
		
		} catch (Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return userData;
	}
	
	public UserData createUserSettings () throws Exception {
		Uri uri = GardenRepositoryMetaData.UserTableMetaData.CONTENT_URI;
		
		ContentValues values = new ContentValues();
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_NAME, "");
		
		this.resolver.insert(uri, values);
		
		return this.getUserSettings();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void updateUserSettings(UserData user) throws Exception {
		Uri uri = GardenRepositoryMetaData.UserTableMetaData.CONTENT_URI;
		
		ContentValues values = new ContentValues();
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_NAME, user.getName());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_LOCATION, user.getGardenLocation());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_GPS_LATITUDE, user.getGardenGPSLatitude());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_GPS_LONGITUDE, user.getGardenGPSLongitude());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_USE_GARDEN_PRODUCTS, user.getUseGardenProducts());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_HAS_CHILDREN, user.getHasChildren());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_FINANCE_SETTING, user.getFinanceSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_REMINDER_SETTING, user.getReminderSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_STRAIN_SETTING, user.getStrainSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_TIME_SETTING, user.getTimeSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_CROP_SETTING, user.getCropSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_DECO_SETTING, user.getDecoSetting());
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_TIDY_SETTING, user.getTidySetting());
		
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_PREFERENCE, 
				DataManagerHelper.joinCollection(user.getGardenPreferences()));
		
		values.put(GardenRepositoryMetaData.UserTableMetaData.USER_GARDEN_CURRENT, 
				DataManagerHelper.joinCollection(user.getGardenCurrent()));
		
		
		this.resolver.update(uri, values, null, null);
	}
	
	/**
	 * 
	 * @param userPlant
	 */
	public void addUserPlant(UserPlantData userPlant) throws Exception {
		Uri uri = GardenRepositoryMetaData.UserPlantTableMetaData.CONTENT_URI;;

		ContentValues values = new ContentValues();
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID, userPlant.getPlant().getId());
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_STATUS, userPlant.getPlantStatus().ordinal());
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ADDED_ON, (long)(userPlant.getAddedOn().getTime()/1000));
		
		if (userPlant.getRemovedOn() == null)
			values.putNull(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_REMOVED_ON);
		else
			values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_REMOVED_ON, (long)(userPlant.getRemovedOn().getTime()/1000));
		
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_IS_REMOVED, userPlant.getIsRemoved());
		
		this.resolver.insert(uri, values);
	}
	
	
	/**
	 * 
	 * @param userPlant
	 */
	public void updateUserPlant(UserPlantData userPlant) throws Exception {
		Uri uri = GardenRepositoryMetaData.UserPlantTableMetaData.CONTENT_URI;
		
		ContentValues values = new ContentValues();
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID, userPlant.getPlant().getId());
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_STATUS, userPlant.getPlantStatus().ordinal());
		
		if (userPlant.getAddedOn() != null)
			values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ADDED_ON, (long)(userPlant.getAddedOn().getTime()/1000));
		
		if (userPlant.getRemovedOn() != null)
			values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_REMOVED_ON, (long)(userPlant.getRemovedOn().getTime()/1000));
		values.put(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_IS_REMOVED, userPlant.getIsRemoved());
		
		this.resolver.update(uri, values, null, null);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<TaskData> getAllTasks() throws Exception {
		Cursor c = null;
		ArrayList<TaskData> tasks = new ArrayList<TaskData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.TaskTableMetaData.CONTENT_URI;
			
			c = resolver.query(uri,
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			
			int iId = c.getColumnIndex(
					BaseColumns._ID);
			
			int iName = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME);
			
			int iDescription = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION);
			
			int iThreshold = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_THRESHOLD);
			
			int iCategoryPictureIcon = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON);
			
			int iCategoryPictureButton = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON);
			
			int iIsPlantTask = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK);
			
			int iDuration = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_DURATION);
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				final TaskData task = new TaskData();
				
				task.setId(c.getString(iId));
				task.setName(c.getString(iName));
				task.setDescription(c.getString(iDescription));
				task.setThreshold(c.getFloat(iThreshold));
				task.setCategoryPictureButton(c.getString(iCategoryPictureButton));
				task.setCategoryPictureIcon(c.getString(iCategoryPictureIcon));
				task.setIsPlantTask(c.getInt(iIsPlantTask) == 1? true:false);
				task.setDuration(c.getInt(iDuration));
				
				//initializes lazy loading for fetching task attributes for the specified task
				task.setSettings(new ILazyLoad<ArrayList<TaskAttributeData>>(){
					public ArrayList<TaskAttributeData> load() {
						try {
							return getTaskAttributes(task.getId());
						
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
					
				});
				
				tasks.add(task);
			}
						
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return tasks;
	}
	
	/**
	 * 
	 * @return arraylist of UserTasks set done
	 * @throws Exception
	 */
	public ArrayList<UserTaskGroupData> getDoneUserTaskGroups() throws Exception {
		Cursor c = null;
		ArrayList<UserTaskGroupData> userTaskGroups = new ArrayList<UserTaskGroupData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			//gets all user tasks which are done 
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + "=" + "1";
			
			c = resolver.query(uri,
					new String[] { "userTaskGroups" }, //queryType
					selection, //selection string
					null, //selection args array of strings
					"DESC"); //sort order
			
			int iGroupId = c.getColumnIndex(
					GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID);
			
			int iTaskId = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData._ID);
			
			int iTaskName = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME);
			
			int iTaskDescription = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION);
			
			int iCategoryPictureIcon = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON);

			int iCategoryPictureButton = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON);
			
			int iPlantTask = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK);
			
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				final UserTaskGroupData userTaskGroup = new UserTaskGroupData();
				TaskData task = userTaskGroup.getTask();			
				userTaskGroup.setId(c.getString(iGroupId));
				
				task.setId(c.getString(iTaskId));
				task.setName(c.getString(iTaskName));
				task.setDescription(c.getString(iTaskDescription));
				task.setCategoryPictureButton(c.getString(iCategoryPictureButton));
				task.setCategoryPictureIcon(c.getString(iCategoryPictureIcon));
				task.setIsPlantTask(c.getInt(iPlantTask) == 1 ? true : false);
				
				// loads user tasks on demand
				userTaskGroup.setSettings(new ILazyLoad<ArrayList<UserTaskData>>() {
					public ArrayList<UserTaskData> load() {
						try {
							return getUserTasksByGroup(userTaskGroup.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
					
				});
				
				userTaskGroups.add(userTaskGroup);
			}
			
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}		
		
		
		return userTaskGroups;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<UserTaskGroupData> getPendingUserTaskGroups() throws Exception {
		Cursor c = null;
		ArrayList<UserTaskGroupData> userTaskGroups = new ArrayList<UserTaskGroupData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			//gets all user tasks which a pending
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + "=" + "0";
			
			c = resolver.query(uri,
					new String[] { "userTaskGroups" }, //queryType
					selection, //selection string
					null, //selection args array of strings
					"DESC"); //sort order
			
			int iGroupId = c.getColumnIndex(
					GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID);
			
			int iTaskId = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData._ID);
			
			int iTaskName = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME);
			
			int iTaskDescription = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION);
			
			int iCategoryPictureIcon = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON);

			int iCategoryPictureButton = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON);
			
			int iPlantTask = c.getColumnIndex(
					GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK);
			
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				final UserTaskGroupData userTaskGroup = new UserTaskGroupData();
				TaskData task = userTaskGroup.getTask();			
				userTaskGroup.setId(c.getString(iGroupId));
				
				task.setId(c.getString(iTaskId));
				task.setName(c.getString(iTaskName));
				task.setDescription(c.getString(iTaskDescription));
				task.setCategoryPictureButton(c.getString(iCategoryPictureButton));
				task.setCategoryPictureIcon(c.getString(iCategoryPictureIcon));
				task.setIsPlantTask(c.getInt(iPlantTask) == 1 ? true : false);
				
				// loads user tasks on demand
				userTaskGroup.setSettings(new ILazyLoad<ArrayList<UserTaskData>>() {
					public ArrayList<UserTaskData> load() {
						try {
							return getUserTasksByGroup(userTaskGroup.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
					
				});
				
				userTaskGroups.add(userTaskGroup);
			}
			
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}		
		
		
		return userTaskGroups;
	}
	
	/**
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void addUserTask(UserTaskData userTask) throws Exception {
		Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
		
		ContentValues cv = new ContentValues();
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID, userTask.getGroupId());
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_PRIORITY, userTask.getPriority());
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID, userTask.getTask().getId());
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.PLANT_ID, userTask.getPlant().getId());
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE, userTask.getIsDone()?1:0);
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_AUTO_REMOVED, userTask.getIsAutoRemoved()?1:0);
		cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ADDED_ON, (long)(userTask.getAddedOn().getTime()/1000));
		
		
		if (userTask.getDoneOn() == null)
			cv.putNull(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON);
		else
			cv.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON, (long)(userTask.getDoneOn().getTime()/1000));
		
		this.resolver.insert(uri, cv);
	}
	
	/**
	 * 
	 * @param userTasks
	 * @throws Exception
	 */
	public void addUserTasks(Iterator<UserTaskData> userTasks) throws Exception {
		while(userTasks.hasNext()) {
			UserTaskData userTask = userTasks.next();
			
			this.addUserTask(userTask);
		}
	}
	 
	/**
	 * 
	 * @throws Exception
	 */
	public void updateUserTask(UserTaskData userTask) throws Exception {
		Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
		
		ContentValues values = new ContentValues();
		values.put(GardenRepositoryMetaData.UserTaskTableMetaData._ID, userTask.getId());
		values.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON, (long)(userTask.getDoneOn().getTime()/1000));
		values.put(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE, userTask.getIsDone());
		
		this.resolver.update(uri, values, null, null);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void updateUserTaskGroup(UserTaskGroupData uTaskG) throws Exception {
		Iterator<UserTaskData> item = uTaskG.getUserTasks();
		
		while(item.hasNext()) {
			updateUserTask(item.next());
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<UserTaskData> getPendingUserTasks() throws Exception {
		Cursor c = null;
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + "=" + "0";
			
			c = resolver.query(uri,
					new String[] { "userTasks" }, //queryType
					selection, //selection string
					null, //selection args array of strings
					null); //sort order
			
			
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				UserTaskData userTask = new UserTaskData();
				
				this.fillUserTask(userTask, c);
				
				userTasks.add(userTask);
			}
				
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return userTasks;
	}
		
	/**
	 * 
	 * @param taskGroupId
	 * @return
	 * @throws Exception
	 */
	private ArrayList<UserTaskData> getUserTasksByGroup(String taskGroupId) throws Exception {
		Cursor c = null;
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
			
			String selection = GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID + "=\"" + taskGroupId+"\"";
			
			c = resolver.query(uri,
					new String[] { "userTasksByGroup" }, //queryType
					selection, //selection string
					null, //selection args array of strings
					null); //sort order
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				UserTaskData userTask = new UserTaskData();
								
				this.fillUserTask(userTask, c);
				
				userTasks.add(userTask);
			}
				
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return userTasks;
	}
	
	/**
	 * 
	 * @param userTask
	 * @param cursor
	 * @throws Exception 
	 */
	void fillUserTask(UserTaskData userTask, Cursor cursor) throws Exception {
		int iId = cursor.getColumnIndex(BaseColumns._ID);
		
		int iGroupId = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID);
		
		int iPriority = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_PRIORITY);
		
		int iIsDone = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE);
		
		int iIsPlantTask = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK);
		
		int iDoneOn = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON);
		
		int iTaskId = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID);
		
		int iTaskName = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME);
		
		int iTaskDescription = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION);
		
		int iTaskTreshold = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_THRESHOLD);
		
		int iDuration = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_DURATION);
		
		int iCategoryPictureIcon = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON);

		int iCategoryPictureButton = cursor.getColumnIndex(
				GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON);	
						
		int iAddedOn = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ADDED_ON);
		
		int iAutoRemoved = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_AUTO_REMOVED);
		
		int iPlantId = cursor.getColumnIndex(
				GardenRepositoryMetaData.UserTaskTableMetaData.PLANT_ID);
		
		TaskData task = userTask.getTask();
		PlantData plant = userTask.getPlant();
		
		userTask.setId(cursor.getString(iId));
		userTask.setGroupId(cursor.getString(iGroupId));
		userTask.setPriority(cursor.getFloat(iPriority));
		userTask.setIsDone(cursor.getInt(iIsDone) == 0 ? false : true);
		userTask.setIsAutoRemoved(cursor.getInt(iAutoRemoved) == 0 ? false : true);
		
		Date dateDoneOn = new Date();
		dateDoneOn.setTime(cursor.getLong(iDoneOn)*1000);
		userTask.setDoneOn(dateDoneOn);
		
		Date dateAddedOn = new Date();
		dateAddedOn.setTime(cursor.getLong(iAddedOn)*1000);
		userTask.setAddedOn(dateAddedOn);

		task.setId(cursor.getString(iTaskId));
		task.setThreshold(cursor.getFloat(iTaskTreshold));
		task.setName(cursor.getString(iTaskName));
		task.setDescription(cursor.getString(iTaskDescription));
		task.setCategoryPictureButton(cursor.getString(iCategoryPictureButton));
		task.setCategoryPictureIcon(cursor.getString(iCategoryPictureIcon));
		task.setIsPlantTask(cursor.getInt(iIsPlantTask) == 0 ? false : true);
		task.setDuration(cursor.getInt(iDuration));
		
		
		if (task.getIsPlantTask()) {
			plant.setId(cursor.getString(iPlantId));
			this.fillPlant(plant, cursor);
		}
	}
	
	/**
	 * Gets tasks attributes by task id.
	 * @return
	 * @throws Exception
	 */
	private ArrayList<TaskAttributeData> getTaskAttributes(String taskId) throws Exception {
		Cursor c = null;
		ArrayList<TaskAttributeData> taskAttributes = new ArrayList<TaskAttributeData>();
		
		try {
			Uri uri = GardenRepositoryMetaData.TaskAttributeTableMetaData.CONTENT_URI;
			
			String selection = GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ID + "=" + taskId;
			
			c = resolver.query(uri,
					null, //projection
					selection, //selection string
					null, //selection args array of strings
					null); //sort order
			
			int iId = c.getColumnIndex(
					BaseColumns._ID);
			
			int iName = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_NAME);
			
			int iWeight = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_WEIGHT);
			
			int iLowThreshold = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_LOW_THRESHOLD);
			
			int iHighThreshold = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_HIGH_THRESHOLD);
			
			int iClassPath = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_CLASS_PATH);
			
			int iSourceClassPath = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_SOURCE_CLASS_PATH);
			
			int iSourceContextSensitive = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_SOURCE_CONTEXT_SENSITIVE);
			
			int iSourceContextName = c.getColumnIndex(
					GardenRepositoryMetaData.TaskAttributeTableMetaData.TASK_ATTRIBUTE_SOURCE_CONTEXT_NAME);
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				TaskAttributeData taskAttribute = new TaskAttributeData();
				
				taskAttribute.setId(c.getString(iId));
				taskAttribute.setName(c.getString(iName));
				taskAttribute.setWeight(c.getFloat(iWeight));
				taskAttribute.setClassPath(c.getString(iClassPath));
				taskAttribute.setLowThreshold(c.getFloat(iLowThreshold));
				taskAttribute.setHighThreshold(c.getFloat(iHighThreshold));
				taskAttribute.setSourceClassPath(c.getString(iSourceClassPath));
				taskAttribute.setSourceContextName(c.getString(iSourceContextName));
				taskAttribute.setSourceContextSensitive(c.getInt(iSourceContextSensitive) == 0 ? false : true);
				
				taskAttributes.add(taskAttribute);
			}
						
		} catch(Exception ex) {
			Log.e("ERROR", ex.getMessage());
			
			throw ex;
		} finally {
			c.close();
		}
		
		return taskAttributes;
	}
}
