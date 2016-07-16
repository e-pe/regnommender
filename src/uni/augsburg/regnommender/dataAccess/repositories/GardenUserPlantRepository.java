package uni.augsburg.regnommender.dataAccess.repositories;



import uni.augsburg.regnommender.dataAccess.*;


import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;


/**
 * 
 * This class provides CRUD functionality on the table UserPlants. 
 *
 */
public class GardenUserPlantRepository extends GardenRepository {
	/**
	 * 
	 * @param sqlite
	 */
	public GardenUserPlantRepository(SQLiteOpenHelper sqlite) {
		super(sqlite);
	}
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(ContentValues values) {
		SQLiteDatabase db = this.sqlite.getReadableDatabase();
		
		long rowId =  db.insert(
				GardenRepositoryMetaData.UserPlantTableMetaData.TABLE_NAME, null, values);
		
		if(rowId > 0){
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
				GardenRepositoryMetaData.UserPlantTableMetaData.TABLE_NAME, 
				values, 
				GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID + "=" + values.getAsString(GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID), 
				null);
	}

	@Override
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = this.sqlite.getReadableDatabase();	
		
		String columns = "p." + GardenRepositoryMetaData.PlantTableMetaData.PLANT_PLANT_TIME + ","
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
		
		//SELECT p.name from plants p, userplants u where u.plantId = p._id	
		Cursor cursor = db.rawQuery(String.format("SELECT u.*, " + columns + " from %s p, %s u where p.%s = u.%s", 
				GardenRepositoryMetaData.PlantTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.UserPlantTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.PlantTableMetaData._ID,
				GardenRepositoryMetaData.UserPlantTableMetaData.PLANT_ID), null);

		return cursor;
	}

	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.UserPlantTableMetaData.CONTENT_URI;
	}

}
