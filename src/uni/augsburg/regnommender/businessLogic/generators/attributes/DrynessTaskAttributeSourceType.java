package uni.augsburg.regnommender.businessLogic.generators.attributes;

import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeContextSourceType;
import uni.augsburg.regnommender.businessLogic.generators.ITaskAttributeSource;
import uni.augsburg.regnommender.infrastructure.GardenApplication;
import uni.augsburg.regnommender.infrastructure.weather.WeatherCurrentConditionOWM;
import uni.augsburg.regnommender.infrastructure.weather.WeatherDataOWM;

/**
 *  Dryness attribute uses weather to calculate dryness factor
 *
 */
public class DrynessTaskAttributeSourceType implements ITaskAttributeContextSourceType<UserData, Float> {
	private UserData context;
	private ITaskAttributeSource<Float> source;
	//private HashMap<WeatherCondition,Float> weatherConditionIntervalMap;
	
	/**
	 * 
	 * @param context
	 */
	public DrynessTaskAttributeSourceType(Object context) {
		this.setContext((UserData)context);
		this.source = this.createSource();	
		
		//this.weatherConditionIntervalMap = this.createWeatherConditionIntervalMap(
				//new HashMap<WeatherCondition,Float>());		
	}
	/*
	private HashMap<WeatherCondition,Float> createWeatherConditionIntervalMap(
			HashMap<WeatherCondition,Float> map) {
		
		map.put(WeatherCondition.chance_of_rain, 	0.4f);
		map.put(WeatherCondition.chance_of_snow, 	0.0f);
		map.put(WeatherCondition.chance_of_storm, 	0.4f);
		map.put(WeatherCondition.chance_of_tstorm, 	0.4f);
		map.put(WeatherCondition.cloudy, 			1.0f);
		map.put(WeatherCondition.dust, 				1.0f);
		map.put(WeatherCondition.flurries, 			0.0f);
		map.put(WeatherCondition.fog, 				0.3f);
		map.put(WeatherCondition.haze, 				0.3f);
		map.put(WeatherCondition.icy, 				0.0f);
		map.put(WeatherCondition.mist, 				0.3f);
		map.put(WeatherCondition.mostly_cloudy, 	1.0f);
		map.put(WeatherCondition.mostly_sunny, 		1.0f);
		map.put(WeatherCondition.partly_cloudy, 	1.0f);
		map.put(WeatherCondition.rain, 				0.0f);
		map.put(WeatherCondition.sleet, 			1.0f);
		map.put(WeatherCondition.smoke, 			1.0f);
		map.put(WeatherCondition.snow, 				0.0f);
		map.put(WeatherCondition.storm, 			0.0f);
		map.put(WeatherCondition.sunny, 			1.0f);
		map.put(WeatherCondition.thunderstorm, 		0.0f);
		
		return map;
	}
	*/
	
	private float getTemperatureIntervalValue(double temp) {	
		temp = Math.round(temp);
		if (temp < 5) 		return 0.0f;
		else if(temp < 10) 	return 0.1f;
		else if(temp < 15) 	return 0.15f;
		else if(temp < 18) 	return 0.4f;
		else if(temp < 20) 	return 0.6f;
		else if(temp < 23) 	return 0.7f;
		else if(temp < 25)  return 0.8f;
		else return 1.0f;
	}
	
	
	private float getWeatherRainIntervalValue(double rainPerHourMM) {		
		if (rainPerHourMM == 0) 		return 1.0f;
		else if(rainPerHourMM  < 0.5) 	return 0.8f;
		else if(rainPerHourMM  < 1.0) 	return 0.6f;
		else if(rainPerHourMM  < 1.5) 	return 0.4f;
		else if (rainPerHourMM < 2.0) 	return 0.2f;
		else return 0.0f;
	}
	
	
	/**
	 * 
	 * @return
	 */
	/*private float getIntervalValue(WeatherCondition condition, int temperature) {
		float weatherConditionValue = this.weatherConditionIntervalMap.get(condition);
		float weatherTemperatureValue = this.getTemperatureIntervalValue(temperature);
		float meanValue = (weatherConditionValue + weatherTemperatureValue) / 2.0f;
		
		return meanValue;
	}*/
	
	
	/**
	 * 
	 * @return
	 */
	private float getIntervalValue(double rainPerHourMM, double temperature) {
		float weatherRainValue = this.getWeatherRainIntervalValue(rainPerHourMM);
		float weatherTemperatureValue = this.getTemperatureIntervalValue(temperature);
		float meanValue = (weatherRainValue + weatherTemperatureValue*3) / 4.0f;
		
		return meanValue;
	}
	
	/**
	 * 
	 */
	public DrynessTaskAttributeSourceType(){
		this.source = this.createSource();
	}
	
	/**
	 * 
	 */
	public String getName() {
		return null;
	}

	/**
	 * 
	 */
	public ITaskAttributeSource<Float> getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return
	 */
	public ITaskAttributeSource<Float> createSource() {
		return new ITaskAttributeSource<Float>() {

			
			public Float getValue() {
				GardenApplication application = GardenApplication.getInstance();
				/*
				WeatherData weather = application.getWeatherData(
						context.getGardenLocation(),
						context.getGardenGPSLatitude(), 
						context.getGardenGPSLongitude());
				
				if(weather != null) {
					WeatherDataCurrent currentWeather = weather.getCurrentData();
								
					return getIntervalValue(
						currentWeather.getConditionFromIcon(), 
						currentWeather.getCurrentTemp_C());
				}*/
				
				

				WeatherDataOWM weather = application.getWeatherData(
						context.getGardenLocation(),
						context.getGardenGPSLatitude(), 
						context.getGardenGPSLongitude());
				
				if(weather != null) {
					WeatherCurrentConditionOWM currentWeather = weather.getCurrentData();
								
					return getIntervalValue(
						currentWeather.getRainPerHour(), 
						currentWeather.getTempCelsius());
				}
				
				//returns a default value if no weather data could be requested from the server
				
				return 0.5f; 
			}
			
		};
	}

	/**
	 * 
	 */
	public UserData getContext() {		
		return this.context;
	}

	/**
	 * 
	 */
	public void setContext(UserData value) {
		this.context = value;
	}
}
