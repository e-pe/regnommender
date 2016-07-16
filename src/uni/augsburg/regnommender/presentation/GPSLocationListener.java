package uni.augsburg.regnommender.presentation;

import uni.augsburg.regnommender.presentation.fragments.SettingsFragment;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/* Class My Location Listener */

public class GPSLocationListener implements LocationListener

{
	SettingsFragment fragment;

	public GPSLocationListener(SettingsFragment fragment) {
		this.fragment = fragment;
	}
	
	public void onLocationChanged(Location loc)

	{

		//loc.getLatitude();

		//loc.getLongitude();

		String Text = "My current location is: " +

		"Latitud = " + loc.getLatitude() +

		"Longitud = " + loc.getLongitude();

		Log.d("LOC", Text);
		
		fragment.locationToast(loc.getLongitude(), loc.getLatitude());

	}

	public void onProviderDisabled(String provider)

	{
		
		Log.d("LOC", "Gps Disabled");

	}

	public void onProviderEnabled(String provider)

	{

		Log.d("LOC", "Gps Enabled");
	}

	public void onStatusChanged(String provider, int status, Bundle extras)

	{

	}

	/* End of Class MyLocationListener */

}