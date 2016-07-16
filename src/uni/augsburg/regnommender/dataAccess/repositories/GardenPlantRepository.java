package uni.augsburg.regnommender.dataAccess.repositories;

import uni.augsburg.regnommender.dataAccess.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;


/**
 * This class provides CRUD functionality on the table Plants.
 *
 */
public class GardenPlantRepository extends GardenRepository {
	/**
	 * 
	 * @param sqlite
	 */
	public GardenPlantRepository(SQLiteOpenHelper sqlite) {
		super(sqlite);
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(ContentValues values) {		
		SQLiteDatabase db = this.sqlite.getWritableDatabase();
		
		long rowId = db.insert(
				GardenRepositoryMetaData.PlantTableMetaData.TABLE_NAME, 
				GardenRepositoryMetaData.PlantTableMetaData.PLANT_NAME, values);
				
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = this.sqlite.getReadableDatabase();	
		
		Cursor cursor = db.rawQuery(String.format("select * from %s p ORDER BY %s %s", 
				GardenRepositoryMetaData.PlantTableMetaData.TABLE_NAME,GardenRepositoryMetaData.PlantTableMetaData.PLANT_NAME, sortOrder), null);

		return cursor;
	}

	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.PlantTableMetaData.CONTENT_URI;
	}

}
