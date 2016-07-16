package uni.augsburg.regnommender.businessLogic.generators;

import java.util.*;

import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.simulators.*;
import uni.augsburg.regnommender.core.*;
import android.util.*;

/**
 * This class represents a simple method for generating user tasks on the basis of arithmetic mean value.
 */
public class MeanBasedTaskGenerator extends TaskGenerator {

	
	/*
	 * (non-Javadoc)
	 * give access to generated userdata
	 * @see uni.augsburg.regnommender.businessLogic.generators.TaskGenerator#generateUserTasks()
	 */
	@Override
	public ArrayList<UserTaskData> generateUserTasks() {
		
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		userTasks.addAll(this.getUserTasksByUserPlants());
		userTasks.addAll(this.getUserTasksByUser());
		
		return userTasks;
	}
	
	/**
	 * Generate user tasks for specific user plants.
	 */
	private ArrayList<UserTaskData> getUserTasksByUserPlants() {
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		Iterator<TaskData> taskIterator = this.tasks.iterator();
		
		while(taskIterator.hasNext()) {
			TaskData task = taskIterator.next();
			
			if(task.getIsPlantTask()) {
				//creates an id for identifying user tasks belonging to the same task 
				String taskGroupId = UUID.randomUUID().toString().substring(0, 8);
				Iterator<UserPlantData> userPlantIterator = this.userPlants.iterator();
			
				while(userPlantIterator.hasNext()) {
					UserPlantData userPlant = userPlantIterator.next();
				
					if (!userPlant.getIsRemoved()) {			
						UserTaskData userTask = this.getUserTaskByUserPlant(task, userPlant, taskGroupId);
					
						if(userTask != null) 
							userTasks.add(userTask);
					}
				}
			}
		}
		
		return userTasks;
	}
	
	/**
	 * 
	 * @return 
	 */
	@SuppressWarnings("unused")
	private ArrayList<UserTaskData> getUserTasksByPlants() {
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		Iterator<TaskData> taskIterator = this.tasks.iterator();
		
		while (taskIterator.hasNext()) {
			TaskData task = taskIterator.next();
				
			//creates an id for identifying user tasks belonging to the same task 
			String taskGroupId = UUID.randomUUID().toString().substring(0, 8);
			Iterator<PlantData> plantIterator = this.plants.iterator();		
			
			while(plantIterator.hasNext()) {
				PlantData plant = plantIterator.next();
				
				UserTaskData userTask = this.getUserTaskByPlant(task, plant, taskGroupId);
				
				if(userTask != null) {
					userTasks.add(userTask);
				}
			}
		}
		
		return userTasks;
	}
		
	
	/**
	 * Generates a user task for the specific plant.
	 */
	private UserTaskData getUserTaskByPlant(TaskData task, PlantData plant, String taskGroupId) {
		try {
			UserTaskData userTask = null;
			Iterator<TaskAttributeData> taskAttributes = task.getAttributes();		
			
			Iterator<TaskGeneratorAttribute> attributes = this.getGeneratorAttributes(taskAttributes, 
					this.getSourceContextMap(this.user, task, plant, null));
			
			//calculates the priority of the current task 
			float calculatedPriority = this.calculatePriority(attributes);
			
			if(calculatedPriority >= task.getThreshold()) {
				userTask = new UserTaskData();
				
				userTask.setIsDone(false);
				userTask.setIsAutoRemoved(false);
				userTask.setGroupId(taskGroupId);
				userTask.setPriority(calculatedPriority);
				userTask.setAddedOn(new Date());
				
				userTask.getTask().loadFrom(task);
				userTask.getPlant().loadFrom(plant);
			}
			
			return userTask;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			Log.e("getUserTaskPlant", "getUserTaskPlant error occured during the calculation of the priority of task");
		}
		
		return null;
	}
	
	
	/**
	 * Generates a user task for the specific user plant.
	 */
	private UserTaskData getUserTaskByUserPlant(TaskData task, UserPlantData userPlant, String taskGroupId) {
		try {
			UserTaskData userTask = null;
			Iterator<TaskAttributeData> taskAttributes = task.getAttributes();		
			
			Iterator<TaskGeneratorAttribute> attributes = this.getGeneratorAttributes(taskAttributes, 
					this.getSourceContextMap(this.user, task, userPlant.getPlant(), userPlant));
			
			//calculates the priority of the current task 
			float calculatedPriority = this.calculatePriority(attributes);
			
			if(calculatedPriority >= task.getThreshold()) {
				userTask = new UserTaskData();
				
				userTask.setIsDone(false);
				userTask.setIsAutoRemoved(false);
				userTask.setGroupId(taskGroupId);
				userTask.setPriority(calculatedPriority);
				userTask.setAddedOn(new Date());
				
				userTask.getTask().loadFrom(task);
				userTask.getPlant().loadFrom(userPlant.getPlant());
			}
			
			return userTask;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			Log.e("getUserTaskPlant", "getUserTaskPlant error occured during the calculation of the priority of task");
		}
		
		return null;
	}
   
	
	/**
	 * Generates user tasks for the current user.
	 * @return newly generated usertask
	 */
	private ArrayList<UserTaskData> getUserTasksByUser() {
		ArrayList<UserTaskData> userTasks = new ArrayList<UserTaskData>();
		Iterator<TaskData> taskIterator = this.tasks.iterator();
		
		while (taskIterator.hasNext()) {
			TaskData task = taskIterator.next();
			
			//creates an id for identifying user tasks belonging to the same task 
			String taskGroupId = UUID.randomUUID().toString().substring(0, 8);
			
			if(!task.getIsPlantTask()) {
				UserTaskData userTask = this.getUserTaskByUser(task, this.user, taskGroupId);
				
				if(userTask != null) {
					userTasks.add(userTask);
				}
			}
		}
		
		return userTasks;
	}

	/**
	 * Generates user task for the current user.
	 * @param task
	 * @param plant
	 * @return
	 */
	private UserTaskData getUserTaskByUser(TaskData task, UserData user, String taskGroupId) {
		try {
			UserTaskData userTask = null;
			Iterator<TaskAttributeData> taskAttributes = task.getAttributes();
			
			Iterator<TaskGeneratorAttribute> attributes = this.getGeneratorAttributes(taskAttributes, 
					this.getSourceContextMap(this.user, task, null, null));
			
			//calculates the priority of the current task 
			float calculatedPriority = this.calculatePriority(attributes);
			
			if(calculatedPriority >= task.getThreshold()) {
				userTask = new UserTaskData();
				
				userTask.setIsDone(false);
				userTask.setIsAutoRemoved(false);
				userTask.setGroupId(taskGroupId);
				userTask.setPriority(calculatedPriority);
				userTask.setAddedOn(new Date());
				
				userTask.getTask().loadFrom(task);
			}
			
			return userTask;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			Log.e("getUserTaskPlant", "getUserTaskPlant error occured during the calculation of the priority of task");
		}
		
		return null;
	}
	
	/**
	 * Creates task generator attributes from task attribute meta data and initializes each with the specific task attribute source type.
	 * @param taskAttributes the meta data from the database.
	 * @param sourceContextMap contains objects to be included into task attribute source types which use context mechanism.
	 * @return The collection of instantiated task generator atttributes.
	 */
	private Iterator<TaskGeneratorAttribute> getGeneratorAttributes(
			Iterator<TaskAttributeData> taskAttributes, 
			HashMap<String, ITaskAttributeSourceContext> sourceContextMap) {
		
		//creates an array list for storing intermediate generator attributes for calculating the priority of a task
		ArrayList<TaskGeneratorAttribute> attributes = new ArrayList<TaskGeneratorAttribute>();
		
		while(taskAttributes.hasNext()) {
			TaskAttributeData taskAttribute = taskAttributes.next();
			
			TaskGeneratorAttribute generatorAttribute = this.createTaskGeneratorAttribute(
					taskAttribute.getClassPath(), 
					(IThreshold)taskAttribute, 
					taskAttribute.getSourceClassPath(), 
					taskAttribute.getSourceContextSensitive(), 
					taskAttribute.getSourceContextName(),
					sourceContextMap);
			
			if(generatorAttribute != null)
				attributes.add(generatorAttribute);
		}
		
		return attributes.iterator();
	}
	
	/**
	 * calculates a tasks priority using tasks attributes value and weight using mean
	 * @param iterator over tasks attributes 
	 * @return tasks new priority
	 */
	private float calculatePriority(Iterator<TaskGeneratorAttribute> attributes){
		float count = 0;
		float priority = 0f;
		
		while(attributes.hasNext()) {
			TaskGeneratorAttribute attribute = attributes.next();
			//Log.d("meanTask@calculate", attribute.getClass().getName()+" value: "+attribute.getNormalizedValue()+ " weight: " +attribute.getWeight());
			priority += attribute.getNormalizedValue()*attribute.getWeight();
			
			count += attribute.getWeight();
		}
		
		priority /= count;
		//Log.d("meanTask@calculate", "" + priority);
		if(JustGenerateTaskSimulator.On())
			return 1.337f;
		
		if(priority < 0) priority = 0;
			
		return priority;
	}
	
	/**
	 * Creates a map for storing all required context objects (user, task, plant, user plant).
	 */
	private HashMap<String, ITaskAttributeSourceContext> getSourceContextMap(
			final UserData user, 
			final TaskData task, 
			final PlantData plant,
			final UserPlantData userPlant) {
		
		return this.createSourceContextMap(
				//defines the source context for getting the user object
				new ITaskAttributeSourceContext() {
					public Object getContext() {
						return user;
					}
				},  
				//defines the source context for getting the plant object
				new ITaskAttributeSourceContext() {
					public Object getContext() {
						return plant;
					}
				},
				//defines the source context for getting the user plant object
				new ITaskAttributeSourceContext() {
					public Object getContext() {
						return userPlant;
					}
				},
				//defines the source context for getting the last done plant user task object
				new ITaskAttributeSourceContext(){
					public Object getContext() {
						return getUserTask.runQuery(task, plant);
					}
				});
	}
}