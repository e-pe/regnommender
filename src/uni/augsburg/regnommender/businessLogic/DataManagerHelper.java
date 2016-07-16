package uni.augsburg.regnommender.businessLogic;

import java.util.*;

import android.util.*;
import uni.augsburg.regnommender.presentation.*;

/**
 * 
 * This class provides additional functionality used by DataManager class.
 *
 */
public class DataManagerHelper {

	/**
	 * This method creates a comma separated string from a collection of type GardenCategory.
	 * 
	 * @param in A collection with garden categories.
	 * @return A string with the pattern "category1, category2, category3".
	 */
	public static String joinCollection(Collection<GardenCategory> in) {
		String output = "";
		
		if(in.size() == 0)
			return output;
			
		Iterator<GardenCategory> iterator = in.iterator(); 				
		while(iterator.hasNext()) {
			output += iterator.next() + ",";
		}
		
		return output.substring(0, output.lastIndexOf(","));
	}
	
	/**
	 * This method calculates the set difference (C = A \ B).
	 * 
	 * @param userTasks1 the first collection.
	 * @param userTasks2 the second collection.
	 * @return A new collection containing userTasks1 \ userTasks2.
	 */
	public static ArrayList<UserTaskData> differenceSimilar(
			ArrayList<UserTaskData> userTasks1, 
			ArrayList<UserTaskData> userTasks2){
						
		 
		ArrayList<UserTaskData> setDifference = new ArrayList<UserTaskData>();
				   
		for(int i = 0; i < userTasks1.size(); i++) {
			UserTaskData userTask1 = userTasks1.get(i);
			
			boolean exists = false;
			for(int j = 0; j < userTasks2.size(); j++) {
				UserTaskData userTask2 = userTasks2.get(j);
				
				
				if(userTask2.similarTo(userTask1)){
					exists = true;
					
					break;
				}
			}
			
			if(!exists) {
				setDifference.add(userTask1);
			}
		}
		return setDifference;
		
		
	}
}
