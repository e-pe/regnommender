package uni.augsburg.regnommender.dataAccess.repositories;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import uni.augsburg.regnommender.dataAccess.*;

/**
 * 
 *This class provides CRUD functionality on the table UserTask. 
 *
 */
public class GardenUserTaskRepository extends GardenRepository {

	/**
	 * 
	 * @param sqlite
	 */
	public GardenUserTaskRepository(SQLiteOpenHelper sqlite) {
		super(sqlite);
	}
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(ContentValues values) {
		SQLiteDatabase db = this.sqlite.getWritableDatabase();
		
		long rowId = db.insert (
				GardenRepositoryMetaData.UserTaskTableMetaData.TABLE_NAME, null, values);
					
		if (rowId > 0) {
			return ContentUris.withAppendedId(this.getUri(), rowId);
		}
		
		return null;
	}

	@Override
	public int delete(String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = this.sqlite.getReadableDatabase();	
		
		return db.update(
				GardenRepositoryMetaData.UserTaskTableMetaData.TABLE_NAME, 
				values, 
				GardenRepositoryMetaData.UserTaskTableMetaData._ID + "=" + values.getAsString(GardenRepositoryMetaData.UserTaskTableMetaData._ID), 
				null);
	}

	@Override
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = this.sqlite.getReadableDatabase();
		
		String queryType = projection != null ? projection[0] : "";
		
		//groups the specified user tasks
		if(queryType.equals("userTaskGroups")) {	
			
			String query = this.getUserTaskGroupQuery(selection, sortOrder);
			
			return db.rawQuery(query, null);
		
		//selects user tasks for the specified group
		} else if (queryType.equals("userTasksByGroup")) {
			
			String query = this.getUserTaskQuery(selection, sortOrder);
					
			return db.rawQuery(query, null);
		
		//selects all user tasks 
		} else if (queryType.equals("userTasks")) {
						
			String query = this.getUserTaskQuery(selection, sortOrder);
								
			return db.rawQuery(query, null);
			
		//selects the user task that was done as last one
		} else if (queryType.equals("userTasksByDate")) {
			String query = this.getUserTaskQuery(selection, sortOrder);
					
			return db.rawQuery(query, null);
		}
			
		return null;
	}
	
	/**
	 * 
	 * @param selection
	 * @return
	 */
	private String getUserTaskGroupQuery(String selection, String sortOrder) {
		String columns = "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID + "," 
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData._ID + "," 
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME + "," 
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_THRESHOLD+ ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_DURATION + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON + "," 
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON;

		// does something similar to:
		// select * from UserTasks u, Tasks t where u.taskId = t._id and u.isDone=0 GROUP BY u.groupId
		String query = String.format("select %s from %s u, %s t where u.%s = t.%s and u.%s group by u.%s", 
				columns,
				GardenRepositoryMetaData.UserTaskTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.TaskTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID,
				GardenRepositoryMetaData.TaskTableMetaData._ID,
				selection,
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID);

		if (selection.equals(GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE+"=1"))
			query+=" ORDER BY "+GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON+" "+sortOrder;
		else
			query+=" ORDER BY "+GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ADDED_ON+" "+sortOrder;
		
		return query;
	}
	
	/*
	 * 
	 */
	private String getUserTaskQuery(String selection, String sortOrder) {
		String columns = "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_GROUP_ID + "," 
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData._ID+ ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_PRIORITY + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.PLANT_ID + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_DONE_ON + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ADDED_ON + ","
				+ "u." + GardenRepositoryMetaData.UserTaskTableMetaData.TASK_AUTO_REMOVED + ","
				
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_NAME + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_DESCRIPTION + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_THRESHOLD+ ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.IS_PLANT_TASK + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_DURATION + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_ICON + ","
				+ "t." + GardenRepositoryMetaData.TaskTableMetaData.TASK_CATEGORY_PICTURE_BUTTON + ","
				
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_PLANT_TIME + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_BLOOM_TIME + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_CATEGORY+ ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_COST + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_CUT + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_CUT + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_CUTTING + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_FERTILIZE + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_PLANT + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_POUR + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_PROCESS + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_DESC_SEED + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_FERTILIZER + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_FRUIT_TIME + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL1 + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL2 + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_IMAGE_URL3 + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_NAME + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_SUNLIGHT_REQUIREMENT+ ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_TIME_CONSUMPTION + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_TOXIC + ","
				+ "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_WATER_CONSUMPTION;
							
		String query = String.format("SELECT %s FROM ((%s u INNER JOIN %s t ON u.%s = t.%s) LEFT OUTER JOIN %s p ON u.%s = p.%s) WHERE %s",
				columns,
				GardenRepositoryMetaData.UserTaskTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.TaskTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.UserTaskTableMetaData.TASK_ID,
				GardenRepositoryMetaData.TaskTableMetaData._ID,
				GardenRepositoryMetaData.PlantTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.UserTaskTableMetaData.PLANT_ID,
				GardenRepositoryMetaData.PlantTableMetaData._ID,
				selection);
		
		if(sortOrder != null)
			query += " ORDER BY "+ sortOrder;
		
		return query;
	}
	
	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.UserTaskTableMetaData.CONTENT_URI;
	}
}
