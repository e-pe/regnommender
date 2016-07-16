package uni.augsburg.regnommender.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.PlantStatus;
import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.UserPlantData;
import uni.augsburg.regnommender.businessLogic.UserTaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskGroupData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.WebView;


public class Helpers {
	
	//Inventory stuff ------------------------------------------------------------------------------------------------
	
	/**
	 * Returns a Plant-Object from the plants-List with a specific name.
	 * @param name name of the preferred plant
	 * @param plants list of the plants
	 * @return Plant-Object with a specific name
	 */
	public static Plant getPlantByName(String name, ArrayList<Plant> plants) {
		for (int i = 0; i < plants.size(); i++)
			if (plants.get(i).getName() == name)
				return plants.get(i);
		
		return null;
	}
	
	/**
	 * Determine if at least one plant is selected.
	 * @param plants list of the plants
	 * @return true if one or more plants are selected, false if not
	 */
	public static boolean isOnePlantSelected(ArrayList<Plant> plants) {
		for (int i = 0; i < plants.size(); i++)

			if (plants.get(i).isSelected())
				return true;
		
		return false;
	}
	
	/**
	 * Determine if selected plants are already marked as added = checked.
	 * @param plants list of the plants
	 * @return true if all selected plants are already added, false if not
	 */
	public static boolean areSelectedPlantsChecked(ArrayList<Plant> plants) {
		for (int i = 0; i < plants.size(); i++)
			if (plants.get(i).isSelected() && (plants.get(i).getStatus() == PlantStatus.not_existing || plants.get(i).isRemoved() == true))
				return false;
		
		return true;
	}
	
	
	/**
	 * Mark all selected plants as not existing in inventory.
	 * @param plants list of the plants
	 */
	public static void removeSelected(ArrayList<Plant> plants, Context context) {
		for (int i = 0; i < plants.size(); i++)
			if (plants.get(i).isSelected()) {
				//plants.get(i).setStatus(PlantStatus.not_existing);
				plants.get(i).setSelected(false);
				plants.get(i).setRemoved(true);
				
		    	UserPlantData userPlantData = new UserPlantData();
		    	Date dateNow = new Date();
		    	userPlantData.setRemovedOn(dateNow);
		    	userPlantData.setIsRemoved(true);
		    	userPlantData.setPlantStatus(plants.get(i).getStatus());
		    	userPlantData.getPlant().setId(plants.get(i).getPlantData().getId());
				
		    	GardenContext gardenCtx = new GardenContext(context);
		    	gardenCtx.updateUserPlant(userPlantData, new ISuccess<UserPlantData>() {
	    			
					public void invoke(UserPlantData data) {
						//Log.d(TAG, "Save Settings...");
					}
					
				}, new IFailure<Exception>() {

					public void invoke(Exception exception) {
						
					}
					
				});
	    		
			}
	}
	
	
	/**
	 * Returns all selected plants
	 */
	public static ArrayList<Plant> getSelectedPlants(ArrayList<Plant> plants) {
		ArrayList<Plant> selectedPlants = new ArrayList<Plant>();
		for (int i = 0; i < plants.size(); i++) {
			Plant selectedPlant = plants.get(i);
			if(plants.get(i).isSelected()) {
				selectedPlants.add(selectedPlant);
			}
		}
		return selectedPlants;
	}
	
	
	/**
	 * Set selected plants as added with a specific PlantStatus. 
	 * @param plants list of the plants
	 * @param status PlantStatus to that all selected plants are set
	 */
	public static void addSelectedWithStatus(ArrayList<Plant> plants, PlantStatus status, Context context) {
		for (int i = 0; i < plants.size(); i++)
			if (plants.get(i).isSelected()) {
				PlantStatus oldStatus = plants.get(i).getStatus();
				plants.get(i).setStatus(status);
				plants.get(i).setRemoved(false);
				plants.get(i).setSelected(false);
				

		    	UserPlantData userPlantData = new UserPlantData();
		    	Date dateNow = new Date();
		    	userPlantData.setAddedOn(dateNow);
		    	userPlantData.setPlantStatus(status);
		    	userPlantData.getPlant().setId(plants.get(i).getPlantData().getId());
		    	
		    	GardenContext gardenCtx = new GardenContext(context);
		    	
		    	if (oldStatus == PlantStatus.not_existing) {								// never existed in UserPlants -> insert
			    	gardenCtx.addUserPlant(userPlantData, new ISuccess<UserPlantData>() {
	
						public void invoke(UserPlantData data) {
							//Log.d(TAG, "Save Settings...");
						}
						
					}, new IFailure<Exception>() {
	
						public void invoke(Exception exception) {
							
						}
						
					});
				}
		    	else {																		// existed -> update
		    		gardenCtx.updateUserPlant(userPlantData, new ISuccess<UserPlantData>() {
		    			
						public void invoke(UserPlantData data) {
							//Log.d(TAG, "Save Settings...");
						}
						
					}, new IFailure<Exception>() {
	
						public void invoke(Exception exception) {
							
						}
						
					});
		    		
		    		
		    	}	    	
		    	
			}
			
	}
	
	
	/**
	 * Deselect all plants.
	 * @param plants list of the plants
	 */
	public static void deselectAllPlants(ArrayList<Plant> plants) {
		for (int i = 0; i < plants.size(); i++)
			plants.get(i).setSelected(false);
	}
	
	
	/**
	 * Get PlantData by id.
	 * @param plants list of the plants
	 * @param id id of the plant
	 * @return PlantData object
	 */
	public static PlantData getPlantDataFromId(ArrayList<Plant> plants, String id) {
		for (int i = 0; i < plants.size(); i++) 
			if (plants.get(i).getPlantData().getId().equals(id)) return plants.get(i).getPlantData();
		
		return null;
	}
	
	
	/**
	 * Get Plant by id.
	 * @param plants list of the plants
	 * @param id id of the plant
	 * @return Plant object
	 */
	public static Plant getPlantFromId(ArrayList<Plant> plants, String id) {
		for (int i = 0; i < plants.size(); i++) 
			if (plants.get(i).getPlantData().getId().equals(id)) return plants.get(i);
		
		return null;
	}
	
	
	
	
	//Task stuff ------------------------------------------------------------------------------------------------
	
	/**
	 * Get the correct section name of the website from the German task name
	 * @param taskname German task name
	 * @param context context
	 * @return name of the website section
	 */
	public static String getWebsiteSectionNameByTaskname(String taskname, Context context) {

	    if (context.getString(R.string.taskname_pour).equals(taskname)) 		return "Giessen";
	    if (context.getString(R.string.taskname_fertilize).equals(taskname)) 	return "Duengen";
	    if (context.getString(R.string.taskname_plant).equals(taskname)) 		return "Anpflanzen";
	    if (context.getString(R.string.taskname_seed).equals(taskname)) 		return "Aussaat";
	    if (context.getString(R.string.taskname_dig).equals(taskname)) 			return "";
	    if (context.getString(R.string.taskname_process).equals(taskname)) 		return "Verarbeitung";
	    if (context.getString(R.string.taskname_cutting).equals(taskname)) 		return "Steckling";
		if (context.getString(R.string.taskname_remove).equals(taskname)) 		return "";
		if (context.getString(R.string.taskname_cut).equals(taskname)) 			return "Zuschneiden";
		if (context.getString(R.string.taskname_mowLawn).equals(taskname)) 		return "";
		
		return "";
	}
	
	
	
	/**
	 * Get the plant description by taskname. The values in strings.xml have to be the same as in the database.
	 * @param task name of the task (see strings.xml)
	 * @param context app context
	 * @param plantData PlantData object
	 * @return description according to the task name
	 */
	public static String getPlantDescriptionByTaskname(String taskname, Context context, PlantData plantData) {

	    if (context.getString(R.string.taskname_pour).equals(taskname)) 		return plantData.getPouringDescription();
	    if (context.getString(R.string.taskname_fertilize).equals(taskname)) 	return plantData.getFertilizingDescription();
	    if (context.getString(R.string.taskname_plant).equals(taskname)) 		return plantData.getPlantingDescription();
	    if (context.getString(R.string.taskname_seed).equals(taskname)) 		return plantData.getSeedingDescription();
	    if (context.getString(R.string.taskname_dig).equals(taskname)) 			return "";
	    if (context.getString(R.string.taskname_process).equals(taskname)) 		return plantData.getUsingGardenProductsDescription();
	    if (context.getString(R.string.taskname_cutting).equals(taskname)) 		return plantData.getScionDescription();
		if (context.getString(R.string.taskname_remove).equals(taskname)) 		return "";
		if (context.getString(R.string.taskname_cut).equals(taskname)) 			return plantData.getCuttingDescription();
		if (context.getString(R.string.taskname_mowLawn).equals(taskname)) 		return "";
		
		return "";
	}
	
	
	// Website stuff  ------------------------------------------------------------------------------------------------
	
	/**
	 * This function splits the descriptions from the database into 256 character long Strings and sends the parts to the 
	 * javascript function that appends the strings on the website. Newline characters are replaced with the HTML equivalent.
	 * @param view the webview object
	 * @param descName the id of the description in the html code 
	 * @param desc the description itself
	 */
	public static void fillDescription(WebView view, String descName, String desc) {		
		desc = desc.replaceAll("\n", "</br>");	// newline char irritates the javascript function!
		String descPart;
		
		for (int i = 0; i < (int)(desc.length()/256); i++) {
			descPart = desc.substring(i*255, (i*255)+255);
			view.loadUrl("javascript:setDescriptions('"+descName+"'"+",'"+descPart+"')");		
		}
		
		descPart = desc.substring(((int)(desc.length()/256))*255);
		view.loadUrl("javascript:setDescriptions('"+descName+"'"+",'"+descPart+"')");
	}
	
	
	public static void fillDescription(WebView view, boolean isCommonDesc, String desc) {		
		desc = desc.replaceAll("\n", "</br>");	// newline char irritates the javascript function!
		String descPart;
		String javascriptFuncName = "setTaskDesc";
		
		if (!isCommonDesc) javascriptFuncName = "setTaskTabDesc";
		
		for (int i = 0; i < (int)(desc.length()/256); i++) {
			descPart = desc.substring(i*255, (i*255)+255);
			view.loadUrl("javascript:"+javascriptFuncName+"('"+descPart+"')");		
		}
		
		descPart = desc.substring(((int)(desc.length()/256))*255);
		view.loadUrl("javascript:"+javascriptFuncName+"('"+descPart+"')");	
	}
	
	
	/**
	 * Get the score of a UserTaskGroup.
	 * @param uTask UserTaskData object
	 * @param group UserTaskGroupData object
	 * @return the calculated score
	 */
	private static int scoreOfUserTaskGroup(UserTaskData uTask, UserTaskGroupData group) {
		int totalScoreOfGroup = 0;
		
		try {
			Iterator<UserTaskData> itUTasks = group.getUserTasks();
			while(itUTasks.hasNext()) {
				UserTaskData groupUTask = itUTasks.next();
				long value = (int)(uTask.getPriority()*10);
				
				long delta = (new Date()).getTime() - groupUTask.getAddedOn().getTime();			//actual value for task view
				if (groupUTask.getIsDone())
					delta = groupUTask.getDoneOn().getTime() - groupUTask.getAddedOn().getTime();  // value for history
				
				delta = delta/1000/3600/24;
				long daysRemain = groupUTask.getTask().getDuration() - delta;
				
				if (daysRemain > 0) {	//if autoremoved or task duration equal 0, ignore the remaining days
					float valuef = (((float)value)/((float)groupUTask.getTask().getDuration()))*(float)(daysRemain);
					value = (long)valuef;
				}
				totalScoreOfGroup += value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (uTask.getIsAutoRemoved())totalScoreOfGroup*=-1;		
		return totalScoreOfGroup;
	}
	
	/**
	 * Get the score of a UserTaskGroup as String with + sign.
	 * @param uTask UserTaskData object
	 * @param group UserTaskGroupData object
	 * @return the score as String with + sign
	 */
	public static String getScoreOfUserTaskGroup(UserTaskData uTask, UserTaskGroupData group) {
		long totalScoreOfGroup = scoreOfUserTaskGroup(uTask,group);
		String plusSign = (totalScoreOfGroup>0)?"+":"";
		
		return plusSign+String.valueOf(totalScoreOfGroup);
	}
	
	/**
	 * Calculates the score of all groups.
	 * @param groups List of the groups
	 * @return the calculated total score
	 */
	public static long getTotalScoreOfAllGroups(ArrayList<UserTaskGroupData> groups) {
		long totalScore = 0;

		for (int i = 0; i < groups.size(); i++) {
			Iterator<UserTaskData> itUserTasks;
			try {
				itUserTasks = groups.get(i).getUserTasks();
				totalScore+=scoreOfUserTaskGroup(itUserTasks.next(),groups.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalScore;
	}
	
	
	/**
	 * Detect if a plant should be preselected by the pictures in the settings: "mein garten bisher".
	 * @param currentPlant the plant to check
	 * @param userSettings the user settings
	 * @return "true" if it should be preselected "false" if not
	 */
	public static boolean is_preselected(Plant currentPlant, UserData userSettings)
	{
	
		if(userSettings.getGardenCurrent().contains(GardenCategory.vegetables)
				&& currentPlant.getPlantData().getCategory().equals("Gem\u00FCsegarten")
				&& userSettings.getFinanceSetting() >= currentPlant.getPlantData().getPrice()
				&& userSettings.getTimeSetting() >= currentPlant.getPlantData().getTimeConsumption()
		  )
		{
			return true;
		}
		else if(userSettings.getGardenCurrent().contains(GardenCategory.flowerbed) && currentPlant.getPlantData().getCategory().equals("Blumengarten")
				&& userSettings.getFinanceSetting() >= currentPlant.getPlantData().getPrice()
				&& userSettings.getTimeSetting() >= currentPlant.getPlantData().getTimeConsumption()
		  )
		{
			return true;
		}
		else if(userSettings.getGardenCurrent().contains(GardenCategory.orchard) && currentPlant.getPlantData().getCategory().equals("Obstgarten")
				&& userSettings.getFinanceSetting() >= currentPlant.getPlantData().getPrice()
				&& userSettings.getTimeSetting() >= currentPlant.getPlantData().getTimeConsumption()
		  )
		{
			return true;
		}
		else if(userSettings.getGardenCurrent().contains(GardenCategory.trees) && currentPlant.getPlantData().getCategory().equals("Geh\u00F6lz")
				&& userSettings.getFinanceSetting() >= currentPlant.getPlantData().getPrice()
				&& userSettings.getTimeSetting() >= currentPlant.getPlantData().getTimeConsumption()
		  )
		{
			return true;
		}
		else if(userSettings.getGardenCurrent().contains(GardenCategory.herbs)
			&& currentPlant.getPlantData().getCategory().equals("Kr\u00E4utergarten")
			&& userSettings.getFinanceSetting() >= currentPlant.getPlantData().getPrice()
			&& userSettings.getTimeSetting() >= currentPlant.getPlantData().getTimeConsumption()
		  )
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	
	
	
	/**
	 * @see http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
	 */
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        if (width > height) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        } else {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
    }
    return inSampleSize;
}
	
	/**
	 * @see http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
	 * adapted version for assets
	 */
	public static Bitmap decodeSampledBitmap(int reqWidth, int reqHeight, Context context, String assetPath) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    try {
			BitmapFactory.decodeStream(context.getAssets().open(assetPath), null, options);
		} catch (IOException e) {
			e.printStackTrace();
		}

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    
	    try {
	    	return BitmapFactory.decodeStream(context.getAssets().open(assetPath), null, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return null;
	}

}
