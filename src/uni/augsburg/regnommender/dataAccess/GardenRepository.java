package uni.augsburg.regnommender.dataAccess;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;

/**
 * 
 * This abstract class represents a repository for executing CRUD operations for a specific database table.
 *
 */
public abstract class GardenRepository {
	protected SQLiteOpenHelper sqlite;
	
	/**
	 * 
	 * @param sqlite
	 */
	public GardenRepository(SQLiteOpenHelper sqlite){
		this.sqlite = sqlite;
	}
	
	public abstract Uri getUri();
	public abstract String getType(Uri uri);
	public abstract Uri insert(ContentValues initialValues);
	public abstract int delete(String where, String[] whereArgs);
	public abstract int update(ContentValues values, String where, String[] whereArgs);
	public abstract Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder);
}
