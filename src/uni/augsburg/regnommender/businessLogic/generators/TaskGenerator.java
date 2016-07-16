package uni.augsburg.regnommender.businessLogic.generators;

import java.lang.reflect.*;
import java.util.*;

import android.util.Log;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.core.*;

/**
 * This class represents the functionality for instantiating task generator attributes with corresponding task attribute source types.
 */
public abstract class TaskGenerator implements ITaskGenerator {
	protected UserData user;
	protected ArrayList<TaskData> tasks;
	protected ArrayList<PlantData> plants;
	protected ArrayList<UserPlantData> userPlants;
	
	//this field defines a query for selecting the specified data from the external source e.g. database
	protected IQuery2<TaskData, PlantData, UserTaskData> getUserTask;
	
	/**
	 * Constructor
	 */
	public TaskGenerator() {
	}
	
	/**
	 * Sets required meta data information to enable user tasks generation.
	 */
	public void setDataSettings(
			UserData user,
			ArrayList<TaskData> tasks, 
			ArrayList<PlantData> plants, 
			ArrayList<UserPlantData> userPlants,
			IQuery2<TaskData, PlantData, UserTaskData> getUserTask){
		
		
		this.user = user;
		this.tasks = tasks;
		this.plants = plants;
		this.userPlants = userPlants;
		this.getUserTask = getUserTask;
	}
	
	/**
	 *  Creates the specific task attribute source type.
	 */
	private ITaskAttributeSourceType<?> createTaskAttributeSourceType(
			String sourceClassPath, 
			Boolean sourceContextSensitive, 
			String sourceContextName,
			HashMap<String, ITaskAttributeSourceContext> sourceContextMap) {
		
		try {
			Constructor<?> constructor = null;
			Object[] parameters = null; 
			Class<?> clazz = Class.forName(sourceClassPath);
			
			//detacts if a class has a constructor with the parameter of type Object
			if(sourceContextSensitive) {
				ITaskAttributeSourceContext sourceContext = sourceContextMap.get(sourceContextName);
				
				constructor = clazz.getConstructor(Object.class);
				parameters = new Object[] { sourceContext.getContext() };
				
			} else {
				constructor = clazz.getConstructor();
				parameters = new Object[] {};
			}
			
			ITaskAttributeSourceType<?> source = 
					(ITaskAttributeSourceType<?>) constructor.newInstance(parameters);
			
			return source;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates the specific task generator attribute.
	 */
	private TaskGeneratorAttribute createTaskGeneratorAttribute(
			String classPath,
			IThreshold treshold,
			ITaskAttributeSourceType<?> sourceType) throws Exception {
		
		if(sourceType == null)
			throw new Exception("A class of type TaskGenereratorAttribute cannot be instantiated " +
					"with the constructor parameter sourceType of type null.");
		
		try {
			Class<?> clazz = Class.forName(classPath);
			
			if (clazz == null)
				Log.e("TaskGeneratorAttribute:createTaskGeneratorAttribute","classPath invalid");
			
			//Log.d("TaskGeneratorAttribute",classPath);
			
			Constructor<?> ctor = clazz.getConstructor(IThreshold.class, ITaskAttributeSourceType.class);
			
			TaskGeneratorAttribute attribute = 
					(TaskGeneratorAttribute)ctor.newInstance(new Object[] { treshold, sourceType});
					
			
			return attribute;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates the specific task generator attribute.
	 */
	protected TaskGeneratorAttribute createTaskGeneratorAttribute(
			String classPath, 
			IThreshold treshold,
			String sourceClassPath, 
			Boolean sourceContextSensitive, 
			String sourceContextName,
			HashMap<String, ITaskAttributeSourceContext> sourceContext) {		
		
		try {
			return this.createTaskGeneratorAttribute(classPath, treshold,
					this.createTaskAttributeSourceType(
							sourceClassPath, 
							sourceContextSensitive, 
							sourceContextName, 
							sourceContext));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates the map with different source contexts.
	 */
	protected HashMap<String, ITaskAttributeSourceContext> createSourceContextMap(
			ITaskAttributeSourceContext onGetUserContext, 
			ITaskAttributeSourceContext onGetPlantContext,
			ITaskAttributeSourceContext onGetUserPlantContext,
			ITaskAttributeSourceContext onGetLastDoneUserTaskContext){
		
		HashMap<String, ITaskAttributeSourceContext> map = 
				new HashMap<String, ITaskAttributeSourceContext>();
		
		map.put("User", onGetUserContext);
		map.put("Plant", onGetPlantContext);
		map.put("UserPlant", onGetUserPlantContext);
		map.put("LastDoneUserTask", onGetLastDoneUserTaskContext);
		
		return map;
	}
		
	/**
	 * Generates user tasks in the derived task generator classes.
	 */
	public abstract ArrayList<UserTaskData> generateUserTasks();
}
