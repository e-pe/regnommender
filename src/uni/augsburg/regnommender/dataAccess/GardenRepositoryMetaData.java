package uni.augsburg.regnommender.dataAccess;

import android.net.*;
import android.provider.*;

/**
 * 
 * This class provides meta data information required to retrieve data from the specific tables.
 *
 */
public class GardenRepositoryMetaData {
	public static final String AUTHORITY = "uni.augsburg.regnommender.dataAccess.GardenProvider";
	
	public static final String DATABASE_NAME = "garden.db";
	public static final int DATABASE_VERSION = 6;
	
	public static final String PICTURE_PLANTS_ASSETS_PATH = "plantPictures/";
	public static final String PICTURE_CATEGORY_ASSETS_PATH = "categoryPictures/";
	public static final String PICTURE_ASSETS_PATH_FROMHTML = "../plantPictures/";
	
	public static final String PLANTS_TABLE_NAME = "Plants";
	public static final String USER_PLANTS_TABLE_NAME = "UserPlants";
	public static final String USER_TABLE_NAME = "User";
	public static final String USER_STATE_TABLE_NAME = "UserState";
	public static final String APPOINTMENTS_TABLE_NAME = "Appointments";
	public static final String FERTILIZERS_TABLE_NAME = "Fertilizers";
	public static final String TASKS_TABLE_NAME = "Tasks";
	public static final String TASK_ATTRIBUTES_TABLE_NAME = "TaskAttributes";
	public static final String USER_TASKS_TABLE_NAME = "UserTasks";
	
	/**
	 * Constructor
	 */
	private GardenRepositoryMetaData() {}
	
	/**
	 * This class represents a meta data object for storing columns of the table Plants.
	 *
	 */
	public static final class PlantTableMetaData implements BaseColumns {
		private PlantTableMetaData() {}
		
		public static final String TABLE_NAME = PLANTS_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir./vnd.recommender.garden.plant";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.recommender.garden.plant";
	
		public static final String DEFAULT_SORT_ORDER = "";
		
		public static final String PLANT_NAME = "name";		
		public static final String PLANT_DESC = "description";
		public static final String PLANT_DESC_PLANT = "descPlant";
		public static final String PLANT_DESC_SEED = "descSeed";
		public static final String PLANT_DESC_CUTTING = "descCutting";
		public static final String PLANT_DESC_FERTILIZE = "descFertilize";
		public static final String PLANT_DESC_POUR = "descPour";
		public static final String PLANT_DESC_PROCESS = "descProcess";
		public static final String PLANT_DESC_CUT = "descCUT";
		
		public static final String PLANT_IMAGE_URL1 = "imageUrl1";
		public static final String PLANT_IMAGE_URL2 = "imageUrl2";
		public static final String PLANT_IMAGE_URL3 = "imageUrl3";
		
		public static final String PLANT_WATER_CONSUMPTION = "waterConsumption";
		public static final String PLANT_SUNLIGHT_REQUIREMENT = "sunlightRequirement";
		public static final String PLANT_FERTILIZER = "fertilizer";
		public static final String PLANT_TIME_CONSUMPTION = "timeConsumption";
		public static final String PLANT_COST = "cost";
		public static final String PLANT_CUT = "cut";
		public static final String PLANT_TOXIC = "toxic";
		
		public static final String PLANT_CATEGORY = "category";
		
		public static final String PLANT_PLANT_TIME = "plantTime";
		public static final String PLANT_FRUIT_TIME = "fruitTime";
		public static final String PLANT_BLOOM_TIME = "bloomTime";
	}
	
	/**
	 * This class represents a meta data object for storing columns of the table UserPlant.
	 *
	 */
	public static final class UserPlantTableMetaData implements BaseColumns {
		private UserPlantTableMetaData() {}
			
		public static final String TABLE_NAME = USER_PLANTS_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String DEFAULT_SORT_ORDER = "";
		
		public static final String PLANT_ID = "plantId";
		public static final String PLANT_ADDED_ON = "plantAddedOn";
		public static final String PLANT_REMOVED_ON = "plantRemovedOn";
		public static final String PLANT_IS_REMOVED = "plantIsRemoved";
		public static final String PLANT_STATUS = "plantStatus";
		
	}
	
	/**
	 * This class represents a meta data object for storing columns of the table User.
	 *
	 */
	public static final class UserTableMetaData implements BaseColumns {
		private UserTableMetaData() {}
		
		public static final String TABLE_NAME = USER_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String DEFAULT_SORT_ORDER = "";
		
		public static final String USER_NAME = "name";
		public static final String USER_STRAIN_SETTING = "strainSetting";
		public static final String USER_TIME_SETTING = "timeSetting";
		public static final String USER_FINANCE_SETTING = "financeSetting";
		public static final String USER_GARDEN_LOCATION = "gardenLocation";
		public static final String USER_GARDEN_GPS_LONGITUDE = "gardenGPSLongitude";
		public static final String USER_GARDEN_GPS_LATITUDE = "gardenGPSLatitude";
		public static final String USER_HAS_CHILDREN = "hasChildren";
		public static final String USER_USE_GARDEN_PRODUCTS = "useGardenProducts";
		public static final String USER_REMINDER_SETTING = "reminderSetting";		
		public static final String USER_GARDEN_PREFERENCE = "gardenPreference";
		public static final String USER_GARDEN_CURRENT = "gardenCurrent";
		public static final String USER_CROP_SETTING = "cropSetting";
		public static final String USER_DECO_SETTING = "decoSetting";
		public static final String USER_TIDY_SETTING = "tidySetting";
	}
	
	/**
	 * This class represents a meta data object for storing columns of the table Appointments.
	 *
	 */
	public static final class AppointmentTableMetaData implements BaseColumns {
		private AppointmentTableMetaData() {}
		
		public static final String TABLE_NAME = APPOINTMENTS_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String DEFAULT_SORT_ORDER = "";
	}	

	
	/**
	 * This class represents a meta data object for storing columns of the table Tasks.
	 *
	 */
	public static final class TaskTableMetaData implements BaseColumns {
		private TaskTableMetaData() {}
		
		public static final String TABLE_NAME = TASKS_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String TASK_NAME = "taskName";
		public static final String TASK_DESCRIPTION = "taskDescription";
		public static final String TASK_THRESHOLD = "threshold";
		public static final String IS_PLANT_TASK = "isPlantTask";
		public static final String TASK_DURATION = "duration";
		
		public static final String TASK_CATEGORY_PICTURE_ICON = "categoryPictureIcon";
		public static final String TASK_CATEGORY_PICTURE_BUTTON = "categoryPictureButton";
	}
	
	/**
	 * This class represents a meta data object for storing columns of the table UserTasks.
	 *
	 */
	public static final class UserTaskTableMetaData implements BaseColumns {
		private UserTaskTableMetaData() {}
		
		public static final String TABLE_NAME = USER_TASKS_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String TASK_ID = "taskId";
		public static final String PLANT_ID = "plantId";
		public static final String TASK_DONE = "isDone";
		public static final String TASK_DONE_ON = "doneOn";
		public static final String TASK_GROUP_ID = "groupId";
		public static final String TASK_PRIORITY = "priority";
		public static final String TASK_ADDED_ON = "addedOn";
		public static final String TASK_AUTO_REMOVED = "autoRemoved";
	}
	
	/**
	 * This class represents a meta data object for storing columns of the table TaskAttributes.
	 *
	 */
	public static final class TaskAttributeTableMetaData implements BaseColumns {
		private TaskAttributeTableMetaData() {}
		
		public static final String TABLE_NAME = TASK_ATTRIBUTES_TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase());
		
		public static final String TASK_ID = "taskId";
		public static final String TASK_ATTRIBUTE_NAME = "name";
		public static final String TASK_ATTRIBUTE_WEIGHT = "weight";
		public static final String TASK_ATTRIBUTE_LOW_THRESHOLD = "lowThreshold";
		public static final String TASK_ATTRIBUTE_HIGH_THRESHOLD = "highThreshold";
		public static final String TASK_ATTRIBUTE_CLASS_PATH = "classPath";
		public static final String TASK_ATTRIBUTE_SOURCE_CLASS_PATH = "sourceClassPath";
		public static final String TASK_ATTRIBUTE_SOURCE_CONTEXT_SENSITIVE = "sourceContextSensitive";
		public static final String TASK_ATTRIBUTE_SOURCE_CONTEXT_NAME = "sourceContextName";

	}
			
}
