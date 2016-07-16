package uni.augsburg.regnommender.infrastructure.weather;

import java.util.Date;

import android.util.Log;

/**
 * 
 * @author Andreas
 *
 */
public class WeatherDataOWM {

	private WeatherCurrentConditionOWM currentData;
	
	private Date lastUpdate;
	
	private double lat;
	private double lon;
	
	private String loc;
	
	private final String owmAPICoords = "http://openweathermap.org/data/2.0/find/city?lat=%s&lon=%s&cnt=1";
	private final String owmAPILoc = "http://openweathermap.org/data/2.0/find/name?q=";
	
	private static WeatherDataOWM weatherData = null;
	
	public static WeatherDataOWM getInstance() {
		if (weatherData == null) weatherData = new WeatherDataOWM();
		
		return weatherData;		
	}
	
	
	public WeatherCurrentConditionOWM getCurrentData() {
		return currentData;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
	
	public String getLoc() {
		return loc;
	}	

	public void updateWeather(double lon, double lat) {
		lastUpdate = new Date();
		this.lat = lat;
		this.lon = lon;
		this.loc = null;
		
		String completeURL = generateURL(this.lon, this.lat);
		getWeather(completeURL);		
	}
	
	
	public void updateWeather(String location) {
		lastUpdate = new Date();

		this.loc = location;
		location = location.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace(" ", "%20");
		
		
		String completeURL = generateURL(location);
		getWeather(completeURL);		
	}
	
	
	private void getWeather(String completeURL) {
            OpenWeatherMapParser parser = new OpenWeatherMapParser();

            currentData =  parser.getCurrentWeatherFromUrl(completeURL);
            
            if (currentData != null)
            	lastUpdate = new Date();
	}

	private String coordToString(double val) {
		String c = String.valueOf(val);
		
		/*if (c.charAt(0) == '-')
			c = c.substring(0, 9);
		else
			c = c.substring(0, 8);*/
		
		return c;
	}
	
	private String generateURL(double lon, double lat) {
		Log.d("Weather url",  String.format(owmAPICoords, coordToString(lat), coordToString(lon)));
		return String.format(owmAPICoords, coordToString(lat), coordToString(lon));
	}
	
	
	private String generateURL(String location) {
		Log.d("Weather url",  owmAPILoc + location);
		return owmAPILoc + location;
	}
}
