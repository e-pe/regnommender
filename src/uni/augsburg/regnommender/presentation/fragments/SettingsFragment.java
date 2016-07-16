package uni.augsburg.regnommender.presentation.fragments;

import java.util.ArrayList;
import java.util.TreeSet;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.core.BackgroundService;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.core.MiscValues;
import uni.augsburg.regnommender.dataAccess.GardenDatabaseSchemaManager;
import uni.augsburg.regnommender.infrastructure.GardenApplication;
import uni.augsburg.regnommender.infrastructure.weather.WeatherOWMAsync;
import uni.augsburg.regnommender.presentation.GardenCategory;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.GPSLocationListener;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class SettingsFragment is used to display the settings.
 * "Garten bisher" has following effect:
 * - in tutorial-mode, plants will be suggested that might already
 * be in your garden
 * "Mein Wunschgarten" has following effects:
 * - Based on the chosen images, the control or the "Zeitaufwand" and 
 * "Finanzieller Aufwand" is automatically adjusted and tasks are generated
 * based on those images
 * The setting for "Finazieller Aufwand" is used by the identically named
 * filter-attribute in "FilterInventoryFragment"; based on the setting, 
 * only plants below the giben price limit will be shown, if the filter is 
 * active.
 * 
 */
public class SettingsFragment extends Fragment {
	
	/** The user settings. */
	private UserData userSettings = new UserData();
	
	/** The gps timeout handler. */
	private Handler gpsTimeoutHandler;
	
	/** The gps timeout. */
	private GPSTimeout gpsTimeout;
	
	
	/** The Constant TAG. */
	private static final String TAG = SettingsFragment.class.getSimpleName();
	
	/** The Constant PREFS_NAME. */
	public static final String PREFS_NAME = "SettingsPrefsFile";
	
	/** Widgets. */
	
	/** GridView for current garden "Mein Garten bisher" */
	private GridView grid_current;
	
	/** The GridView for preference garden "Mein Wunschgarten" */
	private GridView grid_preference;
	
	/** My name */
	private EditText myName;
	
	/** The seek bar for strain (Kraftaufwand). */
	private SeekBar seekBarStrain;
	
	/** The seek bar for time (Zeitaufwand). */
	private SeekBar seekBarTime;
	
	/** The seek bar for finance (Finanzieller Aufwand). */
	private SeekBar seekBarFinance;
	
	/** The seek bar for reminder notifications. */
	private SeekBar seekBarReminder;
	
	/** The check box for use of garden products. */
	private CheckBox checkBoxProducts;
	
	/** The check box for toxic plants. */
	private CheckBox checkBoxToxic;
	
	/** The gps button. */
	private Button gpsButton;
	
	/** The city name. */
	private EditText cityName;
	
	// private Settings settings = new Settings();
	
	/** The context */
	private Context con;
	
	/** The image adapter. */
	private ImageAdapter imageAdapter;
	
	/** The location manager. */
	private LocationManager loc_man = null;
	
	/** The mloc listener. */
	private LocationListener mlocListener = null;
	
	
	
	/**
	 * The Class GPSTimeout.
	 */
	class GPSTimeout implements Runnable {
		
		/** The fragment. */
		SettingsFragment fragment;
		
		/** The listener. */
		LocationListener listener;
		
		/**
		 * Instantiates a new gPS timeout.
		 *
		 * @param fragment the fragment
		 * @param listener the listener
		 */
		public GPSTimeout(SettingsFragment fragment, LocationListener listener) {
			this.fragment = fragment;
			this.listener = listener;
			
		}
	    
    	/* (non-Javadoc)
    	 * @see java.lang.Runnable#run()
    	 */
    	public void run() {
	    	fragment.loc_man.removeUpdates((this.listener));
	    	Toast.makeText(fragment.con.getApplicationContext(),getString(R.string.toast_fail_gps), Toast.LENGTH_LONG).show();
	    	getActivity().findViewById(R.id.progressBarGPS).setVisibility(View.GONE);
	    	getActivity().findViewById(R.id.gps_register).setEnabled(true);
	    }
	}
	
	


	
	
	void initWidgets() {
		
		/**
		 * Initialize Widgets
		 */
		
		/*
		 * ScrollView
		 */
		ScrollView scrollview = (ScrollView)getView().findViewById(R.id.scroll);
		scrollview.smoothScrollTo(0, 0);
		
	
		if (this.getActivity().findViewById(R.id.menuitem_refresh) != null) 
				this.getActivity().findViewById(R.id.menuitem_refresh).setVisibility(View.INVISIBLE);

		
		/*
		 * GridView "Mein Garten bisher"
		 */
		
		grid_current = (GridView) this.getActivity().findViewById(R.id.grid_current);
		if (grid_current != null) { 	//prevent crash on very fast switches
			grid_current.setAdapter(imageAdapter);
		
			grid_current.setOnItemClickListener(new OnItemClickListener() {
	    		public void onItemClick(AdapterView parent, View v, int position, long id) {
	    			TreeSet<GardenCategory> gardenCurrent = getUserSettings().getGardenCurrent();
	    			GardenCategory selectedCategory = GardenCategory.raw;
					switch (position) {
						case 0: selectedCategory = GardenCategory.raw; break;
	    				case 1: selectedCategory = GardenCategory.lawn; break;
	    				case 2: selectedCategory = GardenCategory.escaped; break;
	    				//case 3: selectedCategory = GardenCategory.structured; break;
	    				case 3: selectedCategory = GardenCategory.herbs; break;
	    				case 4: selectedCategory = GardenCategory.trees; break;
	    				case 5: selectedCategory = GardenCategory.orchard; break;
	    				case 6: selectedCategory = GardenCategory.vegetables; break;
	    				case 7: selectedCategory = GardenCategory.flowerbed; break;
					}
	    			if(gardenCurrent.contains(selectedCategory)) {
	    				//userSettings.getGridCurrentItemList().remove(pos);
	    				gardenCurrent.remove(selectedCategory);
	    				getUserSettings().setGardenCurrent(gardenCurrent);
	    				//v.setBackgroundResource(imageAdapter.getmGalleryItemBackground());
	    				v.setBackgroundResource(R.drawable.unselected_black);
	    			} else {
	    				gardenCurrent.add(selectedCategory);
	    				getUserSettings().setGardenCurrent(gardenCurrent);
	    				//userSettings.getGridCurrentItemList().add(pos);
	    				v.setBackgroundResource(R.drawable.selected_cyan);
	    			}
	    			String toastText = getString(R.string.toast_gardenOld_properties) + getUserSettings().getGardenCurrent().toString().replace("raw", "Rohzustand").replace("lawn", "Rasen").replace("escaped", "verwildert").replace("structured", "strukturiert").replace("trees", "Bäume").replace("orchard", "Obstgarten").replace("vegetables", "Gemüsegarten").replace("herbs", "Kräutergarten").replace("flowerbed", "Blumenbeete");
	    			Toast.makeText(SettingsFragment.this.getActivity(), "" + toastText,/*getUserSettings().getGardenCurrent(),*/ Toast.LENGTH_SHORT).show();
	    		}
			});
		}
    	
    	
    	/*
    	 * GridView "Mein Wunschgarten"
    	 */
    	grid_preference = (GridView) this.getActivity().findViewById(R.id.grid_preference);
    	if (grid_preference != null) {
    		grid_preference.setAdapter(imageAdapter);
 
	    	grid_preference.setOnItemClickListener(new OnItemClickListener() {
	    		public void onItemClick(AdapterView parent, View v, int position, long id) {
	    			GardenCategory selectedCategory = GardenCategory.raw;
	    			double crop = 0;
	    			double deco = 0;
	    			double tidy = 0;
	    			double time = 0;
	    			double strain = 0;
	    			double finance = 0;
	
					switch (position) {
						case 0: selectedCategory = GardenCategory.herbs;
						break;
	    				case 1: selectedCategory = GardenCategory.lawn;
	    				break;
	    				case 2: selectedCategory = GardenCategory.escaped;
	    				break;
	    				case 3: selectedCategory = GardenCategory.structured;
	    				break;
	    				case 4: selectedCategory = GardenCategory.trees;
	    				break;
	    				case 5: selectedCategory = GardenCategory.orchard;
	    				break;
	    				case 6: selectedCategory = GardenCategory.vegetables;
	    				break;
	    				//case 7: selectedCategory = GardenCategory.herbs; break;
	    				case 7: selectedCategory = GardenCategory.flowerbed;
	    				break;
					}
					
	
	    			if(getUserSettings().getGardenPreferences().contains(selectedCategory)) {
	    				//userSettings.getGridCurrentItemList().remove(pos);
	    				getUserSettings().getGardenPreferences().remove(selectedCategory);
	    				//v.setBackgroundResource(imageAdapter.getmGalleryItemBackground());
	    				v.setBackgroundResource(R.drawable.unselected_black);
	    			} else {
	    				getUserSettings().getGardenPreferences().add(selectedCategory);
	    				//userSettings.getGridCurrentItemList().add(pos);
	    				v.setBackgroundResource(R.drawable.selected_cyan);
	    			}
	    			float maxVals=getUserSettings().getGardenPreferences().size();
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.herbs))
					{
						crop +=    (float)MiscValues.herbsValues[0]/maxVals;
						deco +=    (float)MiscValues.herbsValues[1]/maxVals;
						tidy +=    (float)MiscValues.herbsValues[2]/maxVals;
						strain +=  (float)MiscValues.herbsValues[3]/maxVals;
						//time +=    (float)herbsValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.herbsValues[4], time);
						//finance += (float)herbsValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.herbsValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.lawn))
					{
						crop +=    (float)MiscValues.lawnValues[0]/maxVals;
						deco +=    (float)MiscValues.lawnValues[1]/maxVals;
						tidy +=    (float)MiscValues.lawnValues[2]/maxVals;
						strain +=  (float)MiscValues.lawnValues[3]/maxVals;
						//time +=    (floaMiscValues.t)lawnValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.lawnValues[4], time);
						//finance += (float)lawnValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.lawnValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.escaped))
					{
						crop +=    (float)MiscValues.escapedValues[0]/maxVals;
						deco +=    (float)MiscValues.escapedValues[1]/maxVals;
						tidy +=    (float)MiscValues.escapedValues[2]/maxVals;
						strain +=  (float)MiscValues.escapedValues[3]/maxVals;
						//time +=    (float)escapedValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.escapedValues[4], time);
						//finance += (float)escapedValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.escapedValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.structured))
					{
						crop +=    (float)MiscValues.structuredValues[0]/maxVals;
						deco +=    (float)MiscValues.structuredValues[1]/maxVals;
						tidy +=    (float)MiscValues.structuredValues[2]/maxVals;
						strain +=  (float)MiscValues.structuredValues[3]/maxVals;
						//time +=    (float)structuredValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.structuredValues[4], time);
						//finance += (float)structuredValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.structuredValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.trees))
					{
						crop +=    (float)MiscValues.treesValues[0]/maxVals;
						deco +=    (float)MiscValues.treesValues[1]/maxVals;
						tidy +=    (float)MiscValues.treesValues[2]/maxVals;
						strain +=  (float)MiscValues.treesValues[3]/maxVals;
						//time +=    (float)treesValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.treesValues[4], time);
						//finance += (float)treesValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.treesValues[4], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.orchard))
					{
						crop +=    (float)MiscValues.orchardValues[0]/maxVals;
						deco +=    (float)MiscValues.orchardValues[1]/maxVals;
						tidy +=    (float)MiscValues.orchardValues[2]/maxVals;
						strain +=  (float)MiscValues.orchardValues[3]/maxVals;
						//time +=    (float)orchardValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.orchardValues[4], time);
						//finance += (float)orchardValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.orchardValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.vegetables))
					{
						crop +=    (float)MiscValues.vegetablesValues[0]/maxVals;
						deco +=    (float)MiscValues.vegetablesValues[1]/maxVals;
						tidy +=    (float)MiscValues.vegetablesValues[2]/maxVals;
						strain +=  (float)MiscValues.vegetablesValues[3]/maxVals;
						//time +=    (float)vegetablesValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.vegetablesValues[4], time);
						//finance += (float)vegetablesValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.vegetablesValues[5], finance);
					}
					if(getUserSettings().getGardenPreferences().contains(GardenCategory.flowerbed))
					{
						crop +=    (float)MiscValues.flowerbedValues[0]/maxVals;
						deco +=    (float)MiscValues.flowerbedValues[1]/maxVals;
						tidy +=    (float)MiscValues.flowerbedValues[2]/maxVals;
						strain +=  (float)MiscValues.flowerbedValues[3]/maxVals;
						//time +=    (float)flowerbedValues[4]/maxVals;
						time  =    Math.max((float)MiscValues.flowerbedValues[4], time);
						//finance += (float)flowerbedValues[5]/maxVals;
						finance =  Math.max((float)MiscValues.flowerbedValues[5], finance);
					}
	
	    			String toastText = getString(R.string.toast_gardenNew_properties) + getUserSettings().getGardenPreferences().toString().replace("raw", "Rohzustand").replace("lawn", "Rasen").replace("escaped", "verwildert").replace("structured", "strukturiert").replace("trees", "Bäume").replace("orchard", "Obstgarten").replace("vegetables", "Gemüsegarten").replace("herbs", "Kräutergarten").replace("flowerbed", "Blumenbeete");
	    			Toast.makeText(SettingsFragment.this.getActivity(), "" + toastText, Toast.LENGTH_SHORT).show();
	
	    			// Werte der Matrix: 0-8, deshalb: Teilen durch 3, um auf settings 0-2 zu kommen
	    			time = (int)(time/3);
	    			finance = (int)(finance/3);
					
	    			// Anpassung der SeekBar-Vorschlaege
	    			if (getUserSettings().getGardenPreferences().size() > 4 &&
	    					time < 2) {
	    				time++;
	    			}
	    			if (getUserSettings().getGardenPreferences().size() > 4 &&
	    					finance < 2) {
	    				finance++;
	    			}
	    			
	    			getUserSettings().setCropSetting(crop);
	    			getUserSettings().setDecoSetting(deco);
	    			getUserSettings().setTidySetting(tidy);
	    			// we know nothing is set yet.. so we make a recommenditon
	    			if(GardenDatabaseSchemaManager.isFirstStart)
	    			{
	    				    getUserSettings().setFinanceSetting(finance);
	    				    getUserSettings().setTimeSetting(time);
	    				    getUserSettings().setStrainSetting(strain);
	    				    
	    				    //SettingsFragment.this.updateSeekBars();
	
	    			    	seekBarTime.setProgress((int)time);
	    					seekBarFinance.setProgress((int)finance);
	    				    
	    		    }  
	    		  }
	
	    	});
    	}
    	
    	
    	
    	/*
    	 * EditText Name
    	 */
    	/*
    	myName = (EditText) this.getActivity().findViewById(R.id.editName);
		myName.setText(getUserSettings().getName());
    	myName.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				getUserSettings().setName(myName.getText().toString());
				return false;
			}
		});
    	*/
    	
    	/*
    	 * SeekBar Kraftaufwand
    	 */
    	/*
    	seekBarStrain = (SeekBar) this.getActivity().findViewById(R.id.seekBar_strain);
		seekBarStrain.setProgress((int)getUserSettings().getStrainSetting());
    	seekBarStrain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    		
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				getUserSettings().setStrainSetting(progress);	
			}
		});
    	*/
    	
    	
    	/*
    	 * SeekBar Zeitaufwand
    	 */
    	seekBarTime = (SeekBar) this.getActivity().findViewById(R.id.seekBar_time);
    	int progressT = (int)getUserSettings().getTimeSetting();
    	if (seekBarTime != null) {
    		seekBarTime.setProgress(progressT);
    	
	    	final TextView valueTime = (TextView) this.getActivity().findViewById(R.id.value_time);
	    	/* für 7 Stufen
	    	if (progressT == 0) {
				valueTime.setText("< 1 Std./Woche");
			}
			else if (progressT == 6) {
				valueTime.setText("ca. 15 Std./Woche");
			}
			else if (progressT == seekBarTime.getMax()) {
				valueTime.setText("> 20 Std./Woche");
			}
			else {
				valueTime.setText("ca. " + (progressT * 2) + " Std./Woche");
			}*/
	    	if (progressT == 0) {
	    		valueTime.setText(R.string.menu_label_low);
	    	}
	    	else if (progressT == 1) {
	    		valueTime.setText(R.string.menu_label_middle);
	    	}
	    	else if (progressT == 2) {
	    		valueTime.setText(R.string.menu_label_high);
	    	}
	    	seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	    		
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					/* für 7 Stufen
					if (progress == 0) {
						valueTime.setText("< 1 Std./Woche");
					}
					else if (progress == 6) {
						valueTime.setText("ca. 15 Std./Woche");
					}
					else if (progress == seekBar.getMax()) {
						valueTime.setText("> 20 Std./Woche");
					}
					else {
						valueTime.setText("ca. " + (progress * 2) + " Std./Woche");
					} */
					if (progress == 0) {
						valueTime.setText(R.string.menu_label_low);
					}
					else if (progress == 1) {
			    		valueTime.setText(R.string.menu_label_middle);
			    	}
			    	else if (progress == 2) {
			    		valueTime.setText(R.string.menu_label_high);
			    	}
					getUserSettings().setTimeSetting(progress);
				}
	
				public void onStartTrackingTouch(SeekBar seekBar) {
	
				}
	
				public void onStopTrackingTouch(SeekBar seekBar) {
		
				}
	    		
	    	});
    	}
    	
    	/*
    	 * SeekBar Finanzaufwand
    	 */
    	seekBarFinance = (SeekBar) this.getActivity().findViewById(R.id.seekBar_finance);
		int progressF = (int)getUserSettings().getFinanceSetting();
    	if(seekBarFinance != null) {
    		seekBarFinance.setProgress(progressF);
    	
			final TextView valueFinance = (TextView) this.getActivity().findViewById(R.id.value_finance);
			if (progressF == 0) {
				valueFinance.setText(R.string.menu_label_low);
			}
			else if (progressF == 1) {
				valueFinance.setText(R.string.menu_label_middle);
			}
			else if (progressF == 2) {
				valueFinance.setText(R.string.menu_label_high);
			}
			/*
			else if (progressT == seekBarFinance.getMax()) {
				valueFinance.setText("unbegrenzt");
			}
			else {
				valueFinance.setText("< " + (progressT * 100 - 100) + "€/Monat");
			}*/
	    	seekBarFinance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if (progress == 0) {
						valueFinance.setText(R.string.menu_label_low);
					}
					else if (progress == 1) {
						valueFinance.setText(R.string.menu_label_middle);
					}
					else if (progress == 2) {
						valueFinance.setText(R.string.menu_label_high);
					}
					/*else if (progress == seekBar.getMax()) {
						valueFinance.setText("unbegrenzt");
					}
					else {
						valueFinance.setText("< " + (progress * 100 - 100) + "€/Monat");
					}*/
					getUserSettings().setFinanceSetting(progress);
				}
	
				public void onStartTrackingTouch(SeekBar seekBar) {
				
				}
	
				public void onStopTrackingTouch(SeekBar seekBar) {
	
				}
	    		
	    	});
    	}
    	
    	/*
    	 * GPS Button
    	 */
    	gpsButton = (Button) this.getActivity().findViewById(R.id.gps_register);
    	// do only once
   		if (loc_man == null) {
   			loc_man = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
   			mlocListener = new GPSLocationListener(this);
   		}
   		if (gpsButton != null) {
   			gpsButton.setOnClickListener(new View.OnClickListener() {
   			
	   			public void onClick(View v) {
	   				Log.d("LOC", "onClick");
	   				Toast.makeText(v.getContext(),getString(R.string.toast_try_gps), Toast.LENGTH_LONG).show();
	   				getActivity().findViewById(R.id.progressBarGPS).setVisibility(View.VISIBLE);
	   				getActivity().findViewById(R.id.gps_register).setEnabled(false);
	   				
	   				
	   				// always update location (duplicates!)
	   				//loc_man.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	   				
	   				// only one location update
   				
	   				try {
	   					loc_man.requestSingleUpdate(LocationManager.GPS_PROVIDER,  mlocListener, null);
	   					if (gpsTimeoutHandler == null) 
	   						gpsTimeoutHandler = new Handler();
	   					
	   					gpsTimeout = new GPSTimeout(SettingsFragment.this, mlocListener);
	   					gpsTimeoutHandler.postDelayed(gpsTimeout, 30000);
	   				} 
	   				catch (Exception e) {
	   					Toast.makeText(v.getContext(),"GPS-Empfänger Fehler.", Toast.LENGTH_LONG).show();
	   					getActivity().findViewById(R.id.progressBarGPS).setVisibility(View.GONE);
	   					getActivity().findViewById(R.id.gps_register).setEnabled(true);
	   				}
   				
   				}
   			});
   		}
    	
    	/*
    	 * City Name
    	 */
    	cityName = (EditText) this.getActivity().findViewById(R.id.editGPS);
    	if (cityName != null) {
    		if (getUserSettings().getGardenLocation() == null || getUserSettings().getGardenLocation().equals(""))
    			cityName.setText("Augsburg");
	    	else
	    		cityName.setText(getUserSettings().getGardenLocation());
	    	cityName.setOnKeyListener(new View.OnKeyListener() {
				
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					getUserSettings().setGardenLocation(cityName.getText().toString());
					return false;
				}
			});
    	}
    	
		
		/*
		 * CheckBox Toxic
		 */
		checkBoxToxic = (CheckBox) this.getActivity().findViewById(R.id.checkBox_toxic);
    	if(checkBoxToxic != null) { 
    		checkBoxToxic.setChecked(getUserSettings().getHasChildren());
    	
    		checkBoxToxic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

    			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
    				getUserSettings().setHasChildren(isChecked);
    			}
    		
    		});
    	}
    	
    	/*
    	 * CheckBox Products
    	 */
    	checkBoxProducts = (CheckBox) this.getActivity().findViewById(R.id.checkBox_products);
    	if (checkBoxProducts != null) {
    		checkBoxProducts.setChecked(getUserSettings().getUseGardenProducts());
    	
    		checkBoxProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

    			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
    				getUserSettings().setUseGardenProducts(isChecked);
    			}
    		
    		});
    	}
    	/*
    	 * SeekBar Reminder
    	 */
    	seekBarReminder = (SeekBar) this.getActivity().findViewById(R.id.seekBar_reminder);
    	if (seekBarReminder != null) {
    		seekBarReminder.setProgress((int)getUserSettings().getReminderSetting());
    	
    		seekBarReminder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

    			public void onProgressChanged(SeekBar seekBar, int progress,
    					boolean fromUser) {

					getUserSettings().setReminderSetting(progress);
				}
	
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
	
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
	    		
	    	});
    	}
    	
    	/*
    	 * Switch Service
    	 */
    	Switch switchService = (Switch) this.getActivity().findViewById(R.id.switch_service);
		if (switchService != null) {
			switchService.setChecked(BackgroundService.isRunning);
		
	    	switchService.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1)
						arg0.getContext().getApplicationContext().startService(new Intent(getActivity(), BackgroundService.class));
					else 
						arg0.getContext().getApplicationContext().stopService(new Intent(getActivity(), BackgroundService.class));		
				}
			});
		}
    	
    	/*
    	 * Button ShowWeatherData
    	 */
    	Button showWeatherData = (Button) this.getActivity().findViewById(R.id.button_showWeatherData);
    	if (showWeatherData != null) {
    		
	    	showWeatherData.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
	
					//new WeatherAsync(this.getActivity()).execute(getUserSettings().getGardenGPSLongitude(),getUserSettings().getGardenGPSLatitude());
					
					if (SettingsFragment.this.cityName.getText().toString().equals("GPS") || SettingsFragment.this.cityName.getText().toString().equals(""))			
						new WeatherOWMAsync(SettingsFragment.this.getActivity()).execute(String.valueOf(getUserSettings().getGardenGPSLongitude()),String.valueOf(getUserSettings().getGardenGPSLatitude()));
					else 
						new WeatherOWMAsync(SettingsFragment.this.getActivity()).execute(SettingsFragment.this.cityName.getText().toString());
				}
			});
    	}
    	
    	
    	/*
    	 * Button button_exportDB
    	 */
    	Button exportDB = (Button) this.getActivity().findViewById(R.id.button_exportDB);
    	if (exportDB != null) {
    		exportDB.setOnClickListener(new View.OnClickListener() {
			
    			public void onClick(View v) {

    				((MainActivity) getActivity()).exportDB();
    			}
    		});
    	}
    	
    	/*
    	 * Button button_importDB
    	 */
    	Button importDB = (Button) this.getActivity().findViewById(R.id.button_importDB);
    	if (importDB != null) {
    		importDB.setOnClickListener(new View.OnClickListener() {
			
    			public void onClick(View v) {

					((MainActivity) getActivity()).importDB();
				}
    		});
    	}
    	
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		con = this.getActivity().getApplicationContext();
		
		/**
		 * Get UserData
		 */
		GardenContext ctx = new GardenContext(con);
		ctx.getUserSettings(new ISuccess<UserData>() {
			public void invoke(UserData data) {
				setUserSettings(data);

				//set default value if location is not set
				if (data.getGardenLocation() == null || data.getGardenLocation().equals("")) {
   					getUserSettings().setGardenGPSLatitude(GardenApplication.getInstance().getDefaultLatitude());
   					getUserSettings().setGardenGPSLongitude(GardenApplication.getInstance().getDefaultLongitude());
				}
			}	
		}, new IFailure<Exception>(){
			public void invoke(Exception exception) {	
			}
		});	
		
    	LinearLayout layout = (LinearLayout) this.getActivity().findViewById(R.id.fragment_container_filter_tasks);
    	layout.setVisibility(8);
		
		imageAdapter = new ImageAdapter(this.getActivity(), getUserSettings());
		
		Log.d("LOC", "onActivityCreated");
		/*SharedPreferences dataStorage = getActivity().getSharedPreferences(PREFS_NAME, 0);
		settings.setInputName(dataStorage.getString("InputName", ""));
		etc.	*/
		
		GardenApplication.getInstance().denyUpdates();
		if (layout != null) {
			initWidgets();
		}
    	
	}
	
	/**
	 * Update seek bars.
	 */
	public void updateSeekBars()
	{
		seekBarTime = (SeekBar) this.getActivity().findViewById(R.id.seekBar_time);
    	int progressT = (int)getUserSettings().getTimeSetting()*seekBarReminder.getMax()/10;
    	seekBarTime.setProgress(progressT);
    	
    	seekBarFinance = (SeekBar) this.getActivity().findViewById(R.id.seekBar_finance);
		int progressF = (int)getUserSettings().getFinanceSetting();
		seekBarFinance.setProgress(progressF);
		
		
    	//seekBarReminder = (SeekBar) this.getActivity().findViewById(R.id.seekBar_reminder);
    	//seekBarReminder.setProgress((int)getUserSettings().getTimeSetting()*seekBarReminder.getMax()/10);
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	
        return inflater.inflate(R.layout.settingsfragment, container, false);
    }
	@Override
	public void onStart()
	{
		super.onStart();
		GardenApplication.getInstance().denyUpdates();
		
		//reload settings from db
		initWidgets();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		//UserData data = new UserData();
		//data.setName("lkjj");
		//data.setGardenLocation("Augsburg");
		
		//set refresh button visible
		if (this.getActivity().findViewById(R.id.menuitem_refresh) != null && !GardenDatabaseSchemaManager.isFirstStart)
		{ 
			this.getActivity().findViewById(R.id.menuitem_refresh).setVisibility(View.VISIBLE);
			//reenable backgroundservice 
			GardenApplication.getInstance().allowUpdates();
		}
		
		cancelGPS();
		
		/*
		 * Write to DB
		 */
		GardenContext ctx = new GardenContext(con);
		ctx.setUserSettings(getUserSettings(), new ISuccess<UserData>() {

			public void invoke(UserData data) {
				Log.d(TAG, "Save Settings...");
			}
			
		}, new IFailure<Exception>() {

			public void invoke(Exception exception) {
				
			}
			
		});
		

	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
	/*
		//save actual station in data storage
		SharedPreferences dataStorage = getActivity().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editStorage = dataStorage.edit();
		editStorage.putString("InputName", settings.InputName());
		editStorage.putFloat("InputName", (float) settings.SeekBarFinanceSetting());
		// etc.
		editStorage.commit(); */
		
	}
	
	
	/**
	 * The Class ImageAdapter.
	 */
	public class ImageAdapter extends BaseAdapter {
	    
    	/** The m context. */
    	private Context mContext;
	    
    	/** The views. */
    	private ArrayList<View> views;
	    
    	/** The user settings. */
    	private UserData userSettings;

	    /**
    	 * Instantiates a new image adapter.
    	 *
    	 * @param c the c
    	 * @param userSettings the user settings
    	 */
    	public ImageAdapter(Context c, UserData userSettings) {
	        mContext = c;
	        this.userSettings = userSettings;
	        this.views = new ArrayList<View>();
	    }

	    /* (non-Javadoc)
    	 * @see android.widget.Adapter#getCount()
    	 */
    	public int getCount() {
	        return mThumbCurrentIds.length;
	    }

	    /* (non-Javadoc)
    	 * @see android.widget.Adapter#getItem(int)
    	 */
    	public Object getItem(int position) {	        
	    	return this.views.get(position);
	    }

	    /* (non-Javadoc)
    	 * @see android.widget.Adapter#getItemId(int)
    	 */
    	public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    /* (non-Javadoc)
    	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
    	 */
    	public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        
	        imageView.setPadding(7, 7, 7, 7);
	        //imageView.setScaleType(ImageView.ScaleType.CENTER);
	        imageView.setMaxHeight(300);
            imageView.setBackgroundResource(R.drawable.unselected_black);
            
            TreeSet<GardenCategory> gardenCategoryValues;
            if (parent.getId() == R.id.grid_current) {
            	imageView.setImageResource(mThumbCurrentIds[position]);
            	gardenCategoryValues = userSettings.getGardenCurrent();
            }
            else {
            	imageView.setImageResource(mThumbPreferenceIds[position]);
            	gardenCategoryValues = userSettings.getGardenPreferences();
            }

	    	switch(position) {
	    	// Sonderfall 0: raw oder herbs, je nachdem ob current oder preference
	    	case 0: if(parent.getId() == R.id.grid_current) {
	    				if(gardenCategoryValues.contains(GardenCategory.raw)) {
	    					imageView.setBackgroundResource(R.drawable.selected_cyan);
	    				}
	    			} else {
	    				if(gardenCategoryValues.contains(GardenCategory.herbs)) {
	    					imageView.setBackgroundResource(R.drawable.selected_cyan);
	    				}
	    			} break;
	    	case 1: if(gardenCategoryValues.contains(GardenCategory.lawn)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 2: if(gardenCategoryValues.contains(GardenCategory.escaped)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 3: if(gardenCategoryValues.contains(GardenCategory.structured)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	}
	    	if (gardenCategoryValues.contains(GardenCategory.herbs)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 4: if(gardenCategoryValues.contains(GardenCategory.trees)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 5: if(gardenCategoryValues.contains(GardenCategory.orchard)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 6: if(gardenCategoryValues.contains(GardenCategory.vegetables)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	} break;
	    	case 7: if(gardenCategoryValues.contains(GardenCategory.flowerbed)) {
	    		imageView.setBackgroundResource(R.drawable.selected_cyan);
	    	}
	    	}
    		
    		/*	switch(g_cat) {
    			case raw:  
    				Toast.makeText(SettingsFragment.this.getActivity(), "select " + grid_current.getChildAt(0), Toast.LENGTH_SHORT).show();
    				break;
    			case lawn: grid_current.getChildAt(1).setBackgroundResource(R.drawable.selected_cyan); break;
    			case escaped: grid_current.getChildAt(2).setBackgroundResource(R.drawable.selected_cyan); break;
    			case structured: grid_current.getChildAt(3).setBackgroundResource(R.drawable.selected_cyan); break;
    			case trees: grid_current.getChildAt(4).setBackgroundResource(R.drawable.selected_cyan); break;
    			case orchard: grid_current.getChildAt(5).setBackgroundResource(R.drawable.selected_cyan); break;
    			case vegetables: grid_current.getChildAt(6).setBackgroundResource(R.drawable.selected_cyan); break;
    			case flowerbed: grid_current.getChildAt(7).setBackgroundResource(R.drawable.selected_cyan); break;
    			}
    		*/
            
            this.views.add(imageView);

	        return imageView;
	    }

	    // references to our images
	    
	    
	    /** The m thumb current ids. */
    	private Integer[] mThumbCurrentIds = {	R.drawable.roh1_440x338,		// 0 RAW
	    										R.drawable.rasen1_440x338,		// 1 LAWN
	    										R.drawable.wild1_440x338,		// 2 ESCAPED
	    										R.drawable.kraut1_440x338,// 3 STRUCTURED
	    										R.drawable.baum1_440x338,		// 4 TREES
	    										R.drawable.obst1_440x338,		// 5 ORCHARD
	   											R.drawable.vegetable1_440x338,	// 6 VEGETABLES
	   											R.drawable.blume2_440x338,		// 7 FLOWERBED
	   	};
	    
    	/** The m thumb preference ids. */
    	private Integer[] mThumbPreferenceIds = {	R.drawable.kraut1_440x338,		// 0 HERBS
												R.drawable.rasen1_440x338,		// 1 LAWN
												R.drawable.wild1_440x338,		// 2 ESCAPED
												R.drawable.strukturiert1_440x338,// 3 STRUCTURED
												R.drawable.baum1_440x338,		// 4 TREES
												R.drawable.obst1_440x338,		// 5 ORCHARD
												R.drawable.vegetable1_440x338,	// 6 VEGETABLES
												//R.drawable.rasen1_440x338,		// 7 HERBS
												R.drawable.blume2_440x338,		// 7 FLOWERBED
};
	    /*
	    private Integer[] mThumbIds = { R.drawable.sample_1,
				R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
				R.drawable.sample_5, R.drawable.sample_1, R.drawable.sample_1,
				R.drawable.sample_1, R.drawable.sample_1, R.drawable.sample_1,
				R.drawable.sample_3, R.drawable.sample_4}; */
	}
	
	/**
	 * Location toast.
	 *
	 * @param lon the lon
	 * @param lat the lat
	 */
	public void locationToast(double lon, double lat) {
		
		// only shown if settings fragment is active
		Toast.makeText(this.con.getApplicationContext(),"GPS-Daten empfangen:\nLongitude: " + lon + "\nLatitude: " + lat, Toast.LENGTH_LONG).show();

		SettingsFragment.this.cityName.setText("GPS");
		getUserSettings().setGardenLocation("GPS");
		getUserSettings().setGardenGPSLatitude(lat);
		getUserSettings().setGardenGPSLongitude(lon);
		
		gpsTimeoutHandler.removeCallbacks(gpsTimeout);
		getActivity().findViewById(R.id.progressBarGPS).setVisibility(View.GONE);
		getActivity().findViewById(R.id.gps_register).setEnabled(true);
		
		//new WeatherAsync(this.getActivity()).execute(lon,lat);
	}
	
	
	/**
	 * Cancel gps.
	 */
	private void cancelGPS() {
		if (loc_man != null && mlocListener!= null)
			this.loc_man.removeUpdates((this.mlocListener));
		if (gpsTimeoutHandler != null && gpsTimeout!= null)
			gpsTimeoutHandler.removeCallbacks(gpsTimeout);
		
		if (getActivity().findViewById(R.id.progressBarGPS) != null) {
			getActivity().findViewById(R.id.progressBarGPS).setVisibility(View.GONE);
			getActivity().findViewById(R.id.gps_register).setEnabled(true);
		}
	}

	/**
	 * Sets the user settings.
	 *
	 * @param userSettings the new user settings
	 */
	public void setUserSettings(UserData userSettings) {
		this.userSettings = userSettings;
	}
	
	/**
	 * Gets the user settings.
	 *
	 * @return the user settings
	 */
	public UserData getUserSettings() {
		return userSettings;
	}
	
}
