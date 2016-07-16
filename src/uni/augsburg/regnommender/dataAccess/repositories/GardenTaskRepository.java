package uni.augsburg.regnommender.dataAccess.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import uni.augsburg.regnommender.dataAccess.*;

/**
 * 
 * This class provides CRUD functionality on the table Tasks.
 *
 */
public class GardenTaskRepository extends GardenRepository {

	public GardenTaskRepository(SQLiteOpenHelper sqlite) {
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
		
		Cursor cursor = db.rawQuery(String.format("select * from %s t", 
				GardenRepositoryMetaData.TaskTableMetaData.TABLE_NAME), null);

		return cursor;
	}
	
	@Override
	public Uri getUri() {
		return GardenRepositoryMetaData.TaskTableMetaData.CONTENT_URI;
	}

}
