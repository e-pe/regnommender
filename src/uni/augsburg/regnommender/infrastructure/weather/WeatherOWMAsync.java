package uni.augsburg.regnommender.infrastructure.weather;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import uni.augsburg.regnommender.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Andreas
 *
 */
public class WeatherOWMAsync extends AsyncTask<String, Void, WeatherDataOWM> {
	
	Activity callerActivity = null;
	WeatherDataOWM weather = WeatherDataOWM.getInstance();
	
	private IWeatherLoadOWM weatherLoad;
	
	/**
	 * 
	 */
	public WeatherOWMAsync() {
		
	}
	
	public WeatherOWMAsync(Activity callerContext) {
		this.callerActivity = callerContext;
	}
	
	protected WeatherDataOWM doInBackground(String... params) {
		if (params.length == 1) {
			Log.d("Weather", "using location");
			weather.updateWeather(params[0]);	//with location
		}
		else 
		{
			Log.d("Weather", "using coordinates");
			weather.updateWeather(Double.valueOf(params[0]), Double.valueOf(params[1])); // with coordinates
		}
		return weather;
	}
	
	/**
	 * 
	 */
	public void setSettings(IWeatherLoadOWM weather) {
		this.weatherLoad = weather;
	}
	
	protected void onPostExecute(WeatherDataOWM result) {
		
		if (result.getCurrentData() != null) {
	        
	         
	        if(this.weatherLoad != null)
	        	this.weatherLoad.load(result);
	        
        	DecimalFormat df = new DecimalFormat("###.##");
        	DateFormat datef = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
	        
	        Log.d("WeatherOWM","Niederschlag/Stunde:"+String.valueOf(df.format(result.getCurrentData().getRainPerHour())+" mm"));
	        Log.d("WeatherOWM","Temperatur:"+String.valueOf(String.valueOf(df.format(result.getCurrentData().getTempCelsius())))+" °C\n");
	        
	        
	        if (callerActivity != null) {
	
		        AlertDialog alertDialog = new AlertDialog.Builder(callerActivity).create();
		        alertDialog.setTitle(callerActivity.getString(R.string.weather_dialog_title));
		        
		        
		        /*
		         * 		"Zeit der Messung: \t\t\t"+  	datef.format(result.getCurrentData().getDt())+"\n" +
		        		"ID der Station: \t\t\t\t\t"+  	String.valueOf(result.getCurrentData().getStationId())+"\n" +
		        		"Name der Station: \t\t\t"+  	String.valueOf(result.getCurrentData().getName())+"\n\n" +
		        				
                		"Temperatur: \t\t\t\t\t"+  		df.format(result.getCurrentData().getTempCelsius())+" °C\n"+
                		"Temperatur min: \t\t\t\t"+  	df.format(result.getCurrentData().getTempCelsiusMin())+" °C\n"+
                		"Temperatur max: \t\t\t"+  		df.format(result.getCurrentData().getTempCelsiusMax())+" °C\n\n"+
                		
                		"Luftfeuchtigkeit:  \t\t\t\t"+	df.format(result.getCurrentData().getHumidity())+" %\n\n"+
                		
                		"Wind-Geschwindigkeit: \t"+		df.format(result.getCurrentData().getWindSpeed())+" m/s | "+ df.format(result.getCurrentData().getWindSpeed()*3.6)+" km/h\n"+
                		"Wind-Richtung: \t\t\t\t"+		df.format(result.getCurrentData().getWindDeg())+"\n\n"+
                		
                		"Luftdruck: \t\t\t\t\t\t"+  	df.format(result.getCurrentData().getPressure())+"  hPa\n\n"+
                		
                		"Niederschlag/Stunde: \t\t"+  	df.format(result.getCurrentData().getRainPerHour())+" mm"
		         */
		        		        
		        
		        String dialogString = callerActivity.getString(R.string.weather_dialog_message);
		        dialogString = String.format(dialogString, 
		        		datef.format(result.getCurrentData().getDt()),
		        		String.valueOf(result.getCurrentData().getStationId()),
		        		String.valueOf(result.getCurrentData().getName()),
		        		df.format(result.getCurrentData().getTempCelsius()),
		        		df.format(result.getCurrentData().getTempCelsiusMin()),
		        		df.format(result.getCurrentData().getTempCelsiusMax()),
		        		df.format(result.getCurrentData().getHumidity()),
		        		df.format(result.getCurrentData().getWindSpeed()), df.format(result.getCurrentData().getWindSpeed()*3.6),
		        		df.format(result.getCurrentData().getWindDeg()),
		        		df.format(result.getCurrentData().getPressure()),
		        		df.format(result.getCurrentData().getRainPerHour())
		        		);
		                
		        alertDialog.setMessage(dialogString);
		        
		        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, callerActivity.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int which) {
		             dialog.dismiss();
		           }
		        });
		        
		        alertDialog.show();        
	        }        
		}
		else {
			if (callerActivity != null) {
				AlertDialog alertDialog = new AlertDialog.Builder(callerActivity).create();
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		        alertDialog.setTitle(callerActivity.getString(R.string.weather_dialog_title));
		        alertDialog.setMessage(callerActivity.getString(R.string.weather_dialog_error_message));
		        
		        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, callerActivity.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int which) {
		             dialog.dismiss();
		           }
		        });
		        
		        alertDialog.show();       
			}
        }        
			
		
	}

}
