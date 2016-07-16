package uni.augsburg.regnommender.core;

//http://developer.android.com/reference/android/app/Service.html#LocalServiceSample
//http://thedevelopersinfo.wordpress.com/2009/10/19/using-handler-for-long-time-operations-in-android/

import java.util.*;
import java.util.concurrent.TimeUnit;

import uni.augsburg.regnommender.*;
import uni.augsburg.regnommender.businessLogic.*;
import uni.augsburg.regnommender.businessLogic.generators.*;
import uni.augsburg.regnommender.businessLogic.generators.simulators.UserAvailabilitySimulator;
import uni.augsburg.regnommender.infrastructure.*;
import uni.augsburg.regnommender.infrastructure.weather.*;
import uni.augsburg.regnommender.presentation.*;
import uni.augsburg.regnommender.presentation.fragments.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;

/**
 * 
 * @brief RemindTask triggers notifications to the user and taskgenerator
 *
 */
class RemindTask implements Runnable {
	
	private BackgroundService service;
	public static long lastReminderNotification = Calendar.getInstance().getTimeInMillis();
	public static long lastUpdateNotification = Calendar.getInstance().getTimeInMillis();
	private UserData userSettings = new UserData();
	final ArrayList<UserTaskGroupData> allTaskGroups = new  ArrayList<UserTaskGroupData>();
	
	
	/**
	 * 
	 * @param service
	 */
	public RemindTask(BackgroundService service) {
		this.service = service;
		
	}
	
	
	/**
	 * Creates a string that contains a list of unique task names to display in notifications
	 * @param tasksToDisplay A list of UserTasks that fulfills priority and timestamp restrictions
	 * @return
	 */
	String uniqueTaskNames(ArrayList<UserTaskData> tasksToDisplay) {
		ArrayList<UserTaskGroupData> groupsToDisplay = createTaskGroups(tasksToDisplay);
		
		String taskNames = "";
		LinkedHashSet<String> taskNameList = new LinkedHashSet<String>();
		
		for (UserTaskGroupData groupToDisplay : groupsToDisplay) {
			taskNameList.add( groupToDisplay.getTask().getName());
		}
		taskNames = taskNameList.toString();
		taskNames = taskNames.substring(1, taskNames.length()-1);
		
		return taskNames;	
	}
	
	/**
	 * Builds a notification and notifies the user
	 * @param type The notification type (1 = in the near future, 2 = soon 3 = very soon, 4 = important)
	 * @param title The notification title
	 * @param text The notification text
	 */
	void sendNotification(int type, String title, String text) {
        int icons[] = {R.drawable.regnomender_icon_new, R.drawable.regnomender_icon, R.drawable.regnomender_icon_attention1, R.drawable.regnomender_icon_attention2, R.drawable.regnomender_icon_attention3};
		
		Intent intent = new Intent(service, MainActivity.class);
        
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(service, 0,
                intent, 0);
		
		
		Notification.Builder builder = new Notification.Builder(service.getApplicationContext());
		builder
		 .setSmallIcon(icons[type])
		 .setContentTitle(title)
		 .setContentText(text)
		 .setAutoCancel(true)
		 .setContentIntent(contentIntent);
		  
		Notification notific = builder.getNotification();
		  
		service.mNM.notify(icons[type], notific);	//a new notification is created for each item - if its still existing it will be overwritten
	}
	
    

	/**
	 * The run method doing actual work generating tasks, calling notifications
	 */
    public void run() {
    	
    	if(GardenApplication.getInstance().isUpdateAllowed())
	    	{
	    		ArrayList<UserTaskData> newUserTasks = this.generateTasks();
	    	
	        /**
			 * Get UserData - has to be done AFTER generating Tasks
			 */
			
			GardenContext ctx = new GardenContext(this.service.getApplicationContext());
			ctx.getUserSettings(new ISuccess<UserData>() {
	
				public void invoke(UserData data) {
					userSettings = data;
				}	
			}, new IFailure<Exception>(){
				public void invoke(Exception exception) {	
				}
			});	
			
			ctx.getPendingTasksByUser(new ISuccess<Iterator <UserTaskGroupData>>(){
				
				public void invoke(Iterator <UserTaskGroupData> groups){
					
					//get usertasks				
					while(groups.hasNext()) {				
						allTaskGroups.add(groups.next());
					}							
			}},
			new IFailure<Exception>(){
	
				public void invoke(Exception exception) {
					exception.printStackTrace();
					Log.e("ReminderTask update",exception.getMessage());
				}
			});
	        
	        
	        if (newUserTasks != null) {
	               
		        //list of all tasks in taskList
		        //ArrayList<TaskData> allTasks = new ArrayList<TaskData>();
		        
		        //Most urgent task in taskList
		        TaskData urgentUserTask = new TaskData();
		        
		        //list of new generated task groups
		        ArrayList<UserTaskGroupData> newTaskGroups = new ArrayList<UserTaskGroupData>();
		        
		        //fill newTaskGroups
		        newTaskGroups = createTaskGroups(newUserTasks);
		                
		        
		        // get all user tasks of all task groups
		        ArrayList<UserTaskData> allUserTasks = new ArrayList<UserTaskData>();
		        Iterator<UserTaskGroupData> taskGroupIterator = allTaskGroups.iterator();
		        while(taskGroupIterator.hasNext()) {
		        	try {
						Iterator<UserTaskData> userTaskIterator = taskGroupIterator.next().getUserTasks();
						while (userTaskIterator.hasNext()) {
							allUserTasks.add((UserTaskData) userTaskIterator.next());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
		        }
		
		        
		        int reminderSetting = (int)userSettings.getReminderSetting();
		        
		        boolean pushNotification = false;
		        
		        ArrayList<UserTaskData> tasksToDisplay;
		        ArrayList<UserTaskGroupData> groupsToDisplay;
		        long timeDiff;
		        
		        // Kriterien: Dringlichkeit der Tasks, Timestamp letzter Reminder, Timestamp letzte Ausführung, @home
		        switch (reminderSetting) {
		        
		        case 0:
		        	// REMINDER + UPDATER
		        	// nie: keine Benachrichtigungen
		        	break;
		        	
		        case 1:
		        	// REMINDER
		        	// selten: wenn Task besonders wichtig (>veryhigh) und letzte Notification > interval_long her
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : allUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_veryhigh) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	timeDiff = (GregorianCalendar.getInstance().getTimeInMillis() - lastReminderNotification);
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (TimeUnit.MILLISECONDS.toSeconds(timeDiff) > MiscValues.interval_long)	&&
		        		 (!tasksToDisplay.isEmpty()) ) {        		
		        		
		        		sendNotification(4,service.getString(R.string.notification_text_type4), uniqueTaskNames(tasksToDisplay));
	
		                lastReminderNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	
		        	// UPDATER: neue Tasks, falls extrem wichtig
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : newUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_veryhigh) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (!tasksToDisplay.isEmpty()) ) {
		        		
		        		sendNotification(0,service.getString(R.string.notification_text_new_tasks), uniqueTaskNames(tasksToDisplay));
		        		
		                lastUpdateNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	break;
		        	
		        case 2:
		        	// REMINDER: gelegentlich: wenn Task relativ wichtig(>high) und letzte Notification > interval_middle her
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : allUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_high) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	timeDiff = (GregorianCalendar.getInstance().getTimeInMillis() - lastReminderNotification);
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (TimeUnit.MILLISECONDS.toSeconds(timeDiff) > MiscValues.interval_middle)	&&
		        		 (!tasksToDisplay.isEmpty()) ) {
		
		        		sendNotification(3,service.getString(R.string.notification_text_type3), uniqueTaskNames(tasksToDisplay));
		        		
		                lastReminderNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	// UPDATER: neue Tasks, falls sehr wichtig
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : newUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_high) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (!tasksToDisplay.isEmpty()) ) {
		        		
		        		sendNotification(0,service.getString(R.string.notification_text_new_tasks), uniqueTaskNames(tasksToDisplay));
		        		
		                lastUpdateNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	break;
		        case 3:
		        	// REMINDER: häufig: Task relativ wichtig (>moderate) und mind. interval_short her.
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : allUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_moderate) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	timeDiff = (GregorianCalendar.getInstance().getTimeInMillis() - lastReminderNotification);
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (TimeUnit.MILLISECONDS.toSeconds(timeDiff) > MiscValues.interval_short)	&&
		        		 (!tasksToDisplay.isEmpty()) ) {
		        		
		        		sendNotification(2,service.getString(R.string.notification_text_type2), uniqueTaskNames(tasksToDisplay));
	
		                lastReminderNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	
		        	// UPDATER: neue Tasks, falls relativ wichtig
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : newUserTasks) {
		        		if (userTask.getPriority() > MiscValues.priority_moderate) {
		        			tasksToDisplay.add(userTask);
		        		}
		        	}
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (!tasksToDisplay.isEmpty()) ) {
		        		
		        		sendNotification(0,service.getString(R.string.notification_text_new_tasks), uniqueTaskNames(tasksToDisplay));
		        		
		                lastUpdateNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	break;
		        	
		        case 4:
		        	// REMINDER: immer, wenn neue Tasks generiert werden (ganze Liste)
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : allUserTasks) {
		        			tasksToDisplay.add(userTask);
		        	}
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		           		 (!tasksToDisplay.isEmpty()) ) {
	
		        		pushNotification = true;
		        		
		        		sendNotification(1,service.getString(R.string.notification_text_type1), uniqueTaskNames(tasksToDisplay));
		        		
		        		lastReminderNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	
		            // UPDATER: neue Tasks, falls relativ wichtig
		        	tasksToDisplay = new ArrayList<UserTaskData>();
		        	for (UserTaskData userTask : newUserTasks) {
		        		tasksToDisplay.add(userTask);
		        	}
		        	if ( (UserAvailabilitySimulator.getAtHome()) &&
		        		 (!tasksToDisplay.isEmpty()) ) {
		        		
		        		sendNotification(0,service.getString(R.string.notification_text_new_tasks), uniqueTaskNames(tasksToDisplay));
		        		
		                lastUpdateNotification = GregorianCalendar.getInstance().getTimeInMillis();
		        	}
		        	break;
		        }
	        }
	    }
        service.remindTask = new RemindTask(service);
        
        service.handlerTaskGen.postDelayed(service.remindTask, service.timeIntervalTaskGen);
	    
    }
    
    /**
     * generates UserTaskGroupData from USerTaskData
     * 
     * @param userTasks
     * @return
     */

	public ArrayList<UserTaskGroupData> createTaskGroups(ArrayList<UserTaskData> userTasks) {
		
		ArrayList<UserTaskGroupData> taskGroups = new ArrayList<UserTaskGroupData>();
		Iterator<UserTaskData> newUserTasksIterator = userTasks.iterator();
		
		while(newUserTasksIterator.hasNext()) {
	    	UserTaskData newUserTask = newUserTasksIterator.next();
	    	Iterator<UserTaskGroupData> taskGroupIterator = allTaskGroups.iterator();
	    	while (taskGroupIterator.hasNext()) {
	    		
	    		UserTaskGroupData taskGroupInList = taskGroupIterator.next();
	    		TaskData taskInList = taskGroupInList.getTask();
	    		
	    		if(taskInList.getId().equals(newUserTask.getTask().getId())) {
	    			if(!taskGroups.contains(taskGroupInList)) {
	    				taskGroups.add(taskGroupInList);
	    			}
	    		}
	    	}
	    }
		return taskGroups;
	}
    
   /**
    * compares Tasks, similar tasks are joined for userdisplay
    * @param task0 for comparison
    * @param task1 for comparison
    * @return true if task0 and task1 are similar
    */
   /*
   public boolean userTasksSimilar(UserTaskData task0, UserTaskData task1)
   {
   	String name0 =task0.getTask().getName();
   	String name1 =task1.getTask().getName();
   	if(task0.getTask().getName().equals(task1.getTask().getName()))
   	{
    	if(task0.getPlant()!=null&&task1.getPlant()!=null)
    	{
    		String plant0=task0.getPlant().getName();
    		String plant1= task1.getPlant().getName();
    		if(task0.getPlant().getName().equals(task1.getPlant().getName()))	
    																	return true;
    		else return false;
    	}
   	 else return true; //not plant task should compare something else here..
   	}
   	else
   		return false;
   }*/
    
    /**
     * Generates new tasks
     */
    public ArrayList<UserTaskData> generateTasks() {	
    	try {
    		//gets the data manager to read and write data to the database
    		final DataManager dataManager = new DataManager(
    			this.service.getApplicationContext().getContentResolver());
    	
    		//gets the specified task generator
    		TaskGenerator taskGenerator = this.service.getTaskGenerator();
    		
    		taskGenerator.setDataSettings(
    			//gets the user data required for launching the task generator 
    			dataManager.getUserSettings(), 
    			//gets the list of all available tasks required for launching the task generator 
    			dataManager.getAllTasks(), 
    			//gets the list of all available plants required for launching the task generator
    			dataManager.getAllPlants(), 
    			//gets the list of all available user plants required for launching the task generator
    			dataManager.getUserPlants(),
    			//defines a query for getting the specified user task for evaluating the specified task attribute
    			new IQuery2<TaskData, PlantData, UserTaskData>(){
    				public UserTaskData runQuery(TaskData task, PlantData plant){
    					if(task.getIsPlantTask())
    					{
    						String plantId = plant != null ? plant.getId() : null;
    						
    						return dataManager.getQueryManager().findLastDoneUserTask(task.getId(), plantId);
    					}
    					else
    					{
    						return dataManager.getQueryManager().findLastDoneUserTask(task.getId());
    					}
    					
    					//String plantId = plant != null ? plant.getId() : null;
    					
    					
    				}
    			});
    	
    		//generates user tasks and persists them to the database
    		ArrayList<UserTaskData> userTasks = taskGenerator.generateUserTasks();
    		//gets the set difference for persisting only new user tasks to the database
    		ArrayList<UserTaskData> pendingUserTasks= dataManager.getPendingUserTasks();
    		ArrayList<UserTaskData> userTasksToPersist = DataManagerHelper.differenceSimilar(userTasks, pendingUserTasks);
    		    		
    		dataManager.addUserTasks(userTasksToPersist.iterator());
    		
    		//invokes an update on the specified fragment for refreshing the actual task list
    		GardenApplication.getInstance().notifyUpdate();
    		System.gc();
    		return userTasksToPersist;
    	
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    		Log.e("TaskGenerator@backgroundservice Exception", ex.getMessage());
    		return null;
    	}
    }
  }



/**
 * 
 * 
 *
 */
class WeatherUpdateTask implements Runnable {
	private BackgroundService service;
	
	
	/**
	 * 
	 * @param service
	 */
	public WeatherUpdateTask(BackgroundService service) {
		this.service = service;
	}

	/**
	 * 
	 */
    public void run() {
    	
		this.loadWeather();
    	
        service.weatherUpdateTask = new WeatherUpdateTask(service);  
        service.handlerWeatherUpdate.postDelayed(service.weatherUpdateTask, service.timeIntervalWeatherUpdate);
    }
    
    /**
     * Loads weather data
     */
    private void loadWeather() {
    	try {
    		//gets the data manager to read and write data to the database
    		DataManager dataManager = new DataManager(
    			this.service.getApplicationContext().getContentResolver());
    	
    		UserData user = dataManager.getUserSettings();
    		String location = user.getGardenLocation();
    		Double longitude = user.getGardenGPSLongitude();
    		Double latitude = user.getGardenGPSLatitude();
    		  		
    		WeatherOWMAsync weatherAsync = new WeatherOWMAsync();
			
    		if (location == null || location.equals(""))
    		{
    			longitude = GardenApplication.getInstance().getDefaultLongitude();
    			latitude = GardenApplication.getInstance().getDefaultLatitude();
    		}
    		
    		if(location == null || location.equals("") || location.equals("GPS"))
    			weatherAsync.execute(String.valueOf(longitude), String.valueOf(latitude));
    		else
    			weatherAsync.execute(location);
    						
    		weatherAsync.setSettings(new IWeatherLoadOWM() {
    			public void load(WeatherDataOWM currentWeather) {
    				GardenApplication.getInstance().storeWeather(WeatherDataOWM.getInstance());
    			}
    		});
    	
    	} catch (Exception ex){
    		ex.printStackTrace();
    	}
    	
    }
}





/**
 * Backgroundservice for Remindtask, collecting data, generating notifications, tasks
 */

public class BackgroundService extends Service implements Runnable {
    public NotificationManager mNM;
    
	Handler handlerTaskGen = new Handler();
	Handler handlerWeatherUpdate = new Handler();
	
	int timeIntervalTaskGen=36000;
	int timeIntervalWeatherUpdate = timeIntervalTaskGen-10000;
	
	public static boolean isRunning = true;
	
	
	// to be able to cancel pending callback
	RemindTask remindTask = null;
	
	WeatherUpdateTask weatherUpdateTask = null;


    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.service_started;
    
    //this field is used to create a specified generator to generate user tasks
    private TaskGenerator taskGenerator;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	BackgroundService getService() {
            return BackgroundService.this;
        }
    }
    
    /**
     * Gets the specified task generator.
     * @return
     */
    public TaskGenerator getTaskGenerator() {
    	return this.taskGenerator;
    }
    
    @Override
    public void onCreate() {
    	isRunning = true;
    	
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        /*
        timer = new Timer();
        timer.schedule(new RemindTask(this), 10 * 1000, 30 * 1000);
        */
        this.taskGenerator = TaskGeneratorFactory.createTaskGenerator("MeanBased");
        
      
        weatherUpdateTask = new WeatherUpdateTask(this);
        handlerWeatherUpdate.postDelayed(weatherUpdateTask, timeIntervalWeatherUpdate);
        
        // Display a notification about us starting.  We put an icon in the status bar.
        remindTask = new RemindTask(this);
    	handlerTaskGen.postDelayed(remindTask, timeIntervalTaskGen);
        showNotification();
    }

    public void run()
    {
    	 Toast.makeText(this, "generator ausgefuehrt.", Toast.LENGTH_SHORT).show();
    	 Log.i("LocalService", "generator ausgefuehrt ");
    	
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    	isRunning = false;
    	
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        
		//  callbacks will be removed
        if (this.remindTask != null)
        	handlerTaskGen.removeCallbacks(this.remindTask);
        
        if (this.weatherUpdateTask != null)
        	handlerWeatherUpdate.removeCallbacks(this.weatherUpdateTask);

		
        // Tell the user we stopped.
        Toast.makeText(this, getString(R.string.service_has_stopped), Toast.LENGTH_SHORT).show();
        
        Log.i("LocalService", "Dead now.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.service_started);

        //intent to get to the MainActivity on touch
        Intent intent = new Intent(this, MainActivity.class);
        
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
		
        //set the notification
		Notification.Builder builder = new Notification.Builder(getApplicationContext());
		builder
		 .setSmallIcon(R.drawable.regnomender_icon_gray)
		 .setContentTitle(getString(R.string.app_name))
		 .setContentText(text)
		 .setContentIntent(contentIntent);
		  
		//build it
		Notification notific = builder.getNotification();
		//notification will never go away
		notific.flags = Notification.FLAG_ONGOING_EVENT;
		
		//notify
		mNM.notify(NOTIFICATION, notific);
    }
}

