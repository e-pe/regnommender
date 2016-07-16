package uni.augsburg.regnommender.dataAccess;

import java.util.*;
import android.net.*;

/**
 * 
 * This class provides the functionality for managing repositories for performing CRUD operations on database tables.
 *
 */
public class GardenRepositoryManager {
	private HashMap<Uri, GardenRepository> repositories; 
	
	/**
	 * Constructor
	 */
	public GardenRepositoryManager() {
		this.repositories = new HashMap<Uri, GardenRepository>();
	}
	
	/**
	 * Stores a repository object.
	 * 
	 * @param repository
	 */
	public void put(GardenRepository repository){
		this.repositories.put(repository.getUri(), repository);
	}
	
	/**
	 * Finds the specific repository by uri.
	 * 
	 * @param uri of the repository
	 * @return The repository.
	 */
	public GardenRepository get(Uri uri) {
		Set<Uri> keys = this.repositories.keySet();
		Iterator<Uri> iterator =  keys.iterator();
		
		while(iterator.hasNext()){
			Uri key = iterator.next();
			if(key.toString().indexOf(uri.toString()) > - 1){
				return this.repositories.get(key);
			}
		}
		
		return null;
	}
}
