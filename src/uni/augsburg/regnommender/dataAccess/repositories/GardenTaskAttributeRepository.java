package uni.augsburg.regnommender.dataAccess.repositories;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;

import uni.augsburg.regnommender.dataAccess.*;

/**
 * 
 * This class provides CRUD functionality on the table TaskAttributes.
 *
 */
public class GardenTaskAttributeRepository extends GardenRepository {

	/**
	 * 
	 * @param sqlite
	 */
	public GardenTaskAttributeRepository(SQLiteOpenHelper sqlite) {
		super(sqlite);

	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(ContentValues initialValues) {
		// TODO Auto-generated method stub
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
		
		Cursor cursor = db.rawQuery(String.format("select * from %s where %s", 
				GardenRepositoryMetaData.TaskAttributeTableMetaData.TABLE_NAME,
				selection), null);

		return cursor;
	}
	
	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.TaskAttributeTableMetaData.CONTENT_URI;
	}

}
