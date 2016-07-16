package uni.augsburg.regnommender.dataAccess;

import uni.augsburg.regnommender.dataAccess.repositories.*;

import android.content.*;
import android.database.*;
import android.net.*;

/**
 * 
 * This class represents a router delegating CRUD operations to the specific repository. 
 *
 */
public class GardenProvider extends ContentProvider {
	private GardenDatabaseSchemaManager databaseHelper;
	private GardenRepositoryManager repositoryHelper;
	
	@Override
	public boolean onCreate() {
		this.repositoryHelper = new GardenRepositoryManager();
		this.databaseHelper = new GardenDatabaseSchemaManager(this.getContext());
		
		this.repositoryHelper.put(new GardenUserRepository(this.databaseHelper));	
		this.repositoryHelper.put(new GardenPlantRepository(this.databaseHelper));						
		this.repositoryHelper.put(new GardenUserPlantRepository(this.databaseHelper));									
		this.repositoryHelper.put(new GardenUserTaskRepository(this.databaseHelper));
		this.repositoryHelper.put(new GardenTaskRepository(this.databaseHelper));
		this.repositoryHelper.put(new GardenTaskAttributeRepository(this.databaseHelper));
		
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return this.repositoryHelper.get(uri).delete(selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return this.repositoryHelper.get(uri).getType(uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri insertedUri = this.repositoryHelper.get(uri).insert(values);
		this.getContext().getContentResolver().notifyChange(insertedUri, null);
		
		return insertedUri;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {	
		Cursor cursor = this.repositoryHelper.get(uri).query(projection, selection, selectionArgs, sortOrder);
		cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		return this.repositoryHelper.get(uri).update(values, selection, selectionArgs);
	}
	
}
