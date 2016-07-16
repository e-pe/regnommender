package uni.augsburg.regnommender.infrastructure.weather;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 
 * @author Andreas
 *
 */
public class OpenWeatherMapParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	
	private WeatherCurrentConditionOWM parseJSONWeather(String jString) {
		try {
			JSONObject jObject = new JSONObject(jString);
			
			JSONArray listitemArray = jObject.getJSONArray("list"); 
			
			JSONObject mainObject =  listitemArray.getJSONObject(0).getJSONObject("main");
			JSONObject rainObject =  listitemArray.getJSONObject(0).getJSONObject("rain");
			JSONObject windObject =  listitemArray.getJSONObject(0).getJSONObject("wind");
			JSONObject coordObject = listitemArray.getJSONObject(0).getJSONObject("coord");
			
			
			WeatherCurrentConditionOWM weatherCurrCond = new WeatherCurrentConditionOWM();
			
			weatherCurrCond.setTempCelsius(		mainObject.getDouble("temp")-273.15);
			weatherCurrCond.setTempCelsiusMin(	mainObject.getDouble("temp_min")-273.15);
			weatherCurrCond.setTempCelsiusMax( 	mainObject.getDouble("temp_max")-273.15);
			
			weatherCurrCond.setHumidity(		mainObject.getDouble("humidity"));
			weatherCurrCond.setPressure(		mainObject.getDouble("pressure"));
			
			weatherCurrCond.setWindSpeed( 		windObject.getDouble("speed"));
			weatherCurrCond.setWindDeg(			windObject.getDouble("deg"));
			
			weatherCurrCond.setLat( 			coordObject.getDouble("lat"));
			weatherCurrCond.setLon( 			coordObject.getDouble("lon"));
			
			Date date = new Date();
			date.setTime(listitemArray.getJSONObject(0).getLong("dt")*1000);		
			weatherCurrCond.setDt(date);
			
			weatherCurrCond.setStationId(listitemArray.getJSONObject(0).getLong("id"));
			
			weatherCurrCond.setName(listitemArray.getJSONObject(0).getString("name"));
			
			
			Iterator <String> itRain = rainObject.keys();
			while (itRain.hasNext()) {
				String key = itRain.next();
				double value = rainObject.getDouble(key);
				double valPerHour = value/Double.valueOf(key.substring(0, 1));
				
				//System.out.println(key+":"+value+" per hour:"+valPerHour);
				
				weatherCurrCond.setRainPerHour(valPerHour);
			}
			
			return weatherCurrCond;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public WeatherCurrentConditionOWM getCurrentWeatherFromUrl(String url) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return parseJSONWeather(json);

	}
	
}
