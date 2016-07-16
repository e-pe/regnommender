package uni.augsburg.regnommender.dataAccess;

import java.io.*;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;


/**
 * This class provides the functionality to install a database. 
 * The database will be installed by coping the corresponding database file to the specific path on the device.  
 * http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 *
 */
public class GardenDatabaseSchemaManager extends SQLiteOpenHelper {

	 private final Context context; 
	 private static String DB_PATH = "/data/data/uni.augsburg.regnommender/databases/";   
	 private static String DB_NAME =  GardenRepositoryMetaData.DATABASE_NAME;	 
	 private SQLiteDatabase myDataBase; 
	 
	 public static boolean isFirstStart = false;
	
	 /**
	  * Constructor
	  */
	 public GardenDatabaseSchemaManager(Context context){
		super(context, GardenRepositoryMetaData.DATABASE_NAME, null, 
				GardenRepositoryMetaData.DATABASE_VERSION);  
		
		this.context = context;
		
		try {
			this.createDataBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	 /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	if(checkDataBase()){
    		//do nothing - database already exist
    	}else{
    		isFirstStart = true;
    		
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    		if ((new File(DB_PATH + DB_NAME)).exists())
    			return true;
    		else return false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = this.context.getAssets().open("db/"+DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
  
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("GardenDatabaseHelper","inner onupgrade called");
		
		Log.w("GardenDatabaseHelper", "Upgrading database from version "
		+ oldVersion + " to "
		+ newVersion + ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " +
				GardenRepositoryMetaData.PlantTableMetaData.TABLE_NAME);
		
		db.execSQL("DROP TABLE IF EXISTS " + 
				GardenRepositoryMetaData.UserPlantTableMetaData.TABLE_NAME);
		
		db.execSQL("DROP TABLE IF EXISTS " + 
				GardenRepositoryMetaData.UserTableMetaData.TABLE_NAME);
		
		onCreate(db);
		
	}

}
