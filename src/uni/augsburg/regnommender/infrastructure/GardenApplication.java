package uni.augsburg.regnommender.infrastructure;

import uni.augsburg.regnommender.core.*;
import uni.augsburg.regnommender.infrastructure.weather.*;

/**
 * 
 * This class represents a global object for storing weather conditions and providing an update mechanism between the task generator and the presentation layer. 
 *
 */
public class GardenApplication {
	private static GardenApplication application;
	private IUpdateNotify updateNotifyListener;
	private boolean updateAllowed=true;
	private WeatherDataOWM weather;
	private double defaultLongitude = 10.898333; //Augsburg
	private double defaultLatitude  = 48.371667; //Augsburg
			
	/**
	 * Constructor
	 */
	private GardenApplication() {
		
	}
	
	/**
	 * Gets the current instance of the application.
	 * 
	 * @return The application.
	 */
	public static GardenApplication getInstance() {
		if(application == null)
			application = new GardenApplication();
		
		return application;
	}
	
	/**
	 * Sets the update notify listener.
	 * 
	 * @param updateNotify
	 */
	public void setUpdateNotifyListener(IUpdateNotify listener) {
		this.updateNotifyListener = listener;
	}
	
	/**
	 * do not interrupt application in critical section
	 */
	public boolean isUpdateAllowed()
	{
		return this.updateAllowed;
	}
	/**
	 * leave critical section
	 */
	public void allowUpdates()
	{
		this.updateAllowed=true;
	}
	/**
	 * enter critical section
	 */
	public void denyUpdates()
	{
		this.updateAllowed=false;
	}
	
	/**
	 * Notifies the application to perform the update.
	 */
	public void notifyUpdate() {
		if(this.updateNotifyListener != null)
			this.updateNotifyListener.update();
	}
	
	/**
	 * 
	 * @param weather
	 */
	public void storeWeather(WeatherDataOWM weather) {
		this.weather = weather;
	}
	
	/**
	 * Gets the current weather by latitude and longitude.
	 * @return
	 */
	public WeatherDataOWM getWeatherData(String location, double latitude, double longitude) {
		return this.weather;
	}

	/**
	 * Gets the default longitude.
	 * @return The longitude of Augsburg.
	 */
	public double getDefaultLongitude() {
		return defaultLongitude;
	}

	/**
	 * Gets the default latitude.
	 * @return The latitude of Augsburg.
	 */
	public double getDefaultLatitude() {
		return defaultLatitude;
	}
}
