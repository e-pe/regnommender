package uni.augsburg.regnommender.infrastructure.weather;

import java.util.Date;

/**
 * 
 * @author Andreas
 *
 */
public class WeatherCurrentConditionOWM {


	double tempCelsius;
	double tempCelsiusMin;
	double tempCelsiusMax;
	
	double humidity;	//%
	double pressure;	//hPa
	
	double windSpeed;	//m/s
	double windDeg;
	
	double rainPerHour;	//mm
	
	double lat;
	double lon;
	
	Date dt; 			//from unixtime
	
	long stationId;
	
	String name;
	
	
	
	

	/**
	 * @return the tempCelsius
	 */
	public double getTempCelsius() {
		return tempCelsius;
	}

	/**
	 * @param tempCelsius the tempCelsius to set
	 */
	public void setTempCelsius(double tempCelsius) {
		this.tempCelsius = tempCelsius;
	}

	/**
	 * @return the tempCelsiusMin
	 */
	public double getTempCelsiusMin() {
		return tempCelsiusMin;
	}

	/**
	 * @param tempCelsiusMin the tempCelsiusMin to set
	 */
	public void setTempCelsiusMin(double tempCelsiusMin) {
		this.tempCelsiusMin = tempCelsiusMin;
	}

	/**
	 * @return the tempCelsiusMax
	 */
	public double getTempCelsiusMax() {
		return tempCelsiusMax;
	}

	/**
	 * @param tempCelsiusMax the tempCelsiusMax to set
	 */
	public void setTempCelsiusMax(double tempCelsiusMax) {
		this.tempCelsiusMax = tempCelsiusMax;
	}

	/**
	 * @return the humidity
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	/**
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * @param pressure the pressure to set
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * @return the windSpeed
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the windDeg
	 */
	public double getWindDeg() {
		return windDeg;
	}

	/**
	 * @param windDeg the windDeg to set
	 */
	public void setWindDeg(double windDeg) {
		this.windDeg = windDeg;
	}

	/**
	 * @return the rainPerHour
	 */
	public double getRainPerHour() {
		return rainPerHour;
	}

	/**
	 * @param rainPerHour the rainPerHour to set
	 */
	public void setRainPerHour(double rainPerHour) {
		this.rainPerHour = rainPerHour;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the dt
	 */
	public Date getDt() {
		return dt;
	}

	/**
	 * @param dt the dt to set
	 */
	public void setDt(Date dt) {
		this.dt = dt;
	}

	/**
	 * @return the stationId
	 */
	public long getStationId() {
		return stationId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
