package uni.augsburg.regnommender.dataAccess.repositories;

import uni.augsburg.regnommender.dataAccess.GardenRepository;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import android.content.*;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.*;

/**
 * 
 * This class provides CRUD functionality on the table User. 
 *
 */
public class GardenUserRepository extends GardenRepository {

	/**
	 * 
	 * @param sqlite
	 */
	public GardenUserRepository(SQLiteOpenHelper sqlite) {
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
				GardenRepositoryMetaData.UserTableMetaData.TABLE_NAME, null, values);
		
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
				GardenRepositoryMetaData.UserTableMetaData.TABLE_NAME, 
				values, 
				GardenRepositoryMetaData.UserTableMetaData._ID + "=" + "1", 
				null);
	}

	@Override
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {	
		SQLiteDatabase db = this.sqlite.getReadableDatabase();	
		
		Cursor cursor = db.rawQuery(String.format("select * from %s u where u.%s = 1", 
				GardenRepositoryMetaData.UserTableMetaData.TABLE_NAME,
				GardenRepositoryMetaData.UserTableMetaData._ID), null);
		
		return cursor;
	}
	
	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.UserTableMetaData.CONTENT_URI;
	}	

}
