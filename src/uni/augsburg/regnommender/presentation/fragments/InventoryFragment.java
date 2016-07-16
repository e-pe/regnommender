package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.PlantStatus;
import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.UserPlantData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.dataAccess.GardenDatabaseSchemaManager;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.infrastructure.GardenApplication;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.Helpers;
import uni.augsburg.regnommender.presentation.Plant;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class InventoryFragment is used to display the contents of the 
 * inventory (i.e. plants). Within the class, methods from the 
 * GridViewAdapter-class are invoked to filter the plants based on selected
 * attributes.
 * The attribute "Finanzaufwand" can be set in the settings (@see SettingsFragment);
 * if active, only plants below the price limit set in the settings will be shown.
 * 
 * 
 */
public class InventoryFragment extends Fragment {
	
	/** The plants. */
	ArrayList<Plant> plants = new ArrayList<Plant>();
	
	/** The usersettings. */
	private UserData userSettings = new UserData();
	
	/** The unselected. */
	private Drawable unselected = null, selected = null;
	
	GridViewAdapter adapter;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

    	final Context context = getActivity().getApplicationContext();
    	
		Resources res = context.getResources();
		if (selected == null) 	selected = res.getDrawable(R.drawable.selected);
		if (unselected == null) unselected = res.getDrawable(R.drawable.unselected);
    	
		refreshInventory(context);
    	
		return inflater.inflate(R.layout.inventoryfragment, container, false);
	}
	
	
	void refreshInventory(final Context context) {
    	plants.clear();
    	
    	
    	GardenContext gardenCtx = new GardenContext(context);
    	gardenCtx.getAllPlants(new ISuccess<Iterator<PlantData>>(){
			public void invoke(Iterator<PlantData> data) {
				while(data.hasNext()) {
					PlantData dataItem = data.next();
					Log.d("database content", dataItem.getName());
					
					Bitmap bmp = null;
					try {
						bmp = BitmapFactory.decodeStream(context.getAssets().open(GardenRepositoryMetaData.PICTURE_PLANTS_ASSETS_PATH+dataItem.getImageUrl1()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					plants.add(new Plant(dataItem.getName(), bmp , PlantStatus.not_existing, dataItem, false));
				}
			}
			
		},new IFailure<Exception>(){
			public void invoke(Exception exception) {
								
			}
			
		});
    	
    	gardenCtx.getPlantsByUser(new ISuccess<Iterator<UserPlantData>>(){
			public void invoke(Iterator<UserPlantData> data) {
					while(data.hasNext()) {
						UserPlantData dataItem = data.next();
						
						Plant plantToUpdate = Helpers.getPlantFromId(plants, dataItem.getPlant().getId());
						plantToUpdate.setStatus(dataItem.getPlantStatus());
						plantToUpdate.setRemoved(dataItem.getIsRemoved());
						
						Log.d("plant status update", "plant: "+ dataItem.getPlant().getName()+ "; status: "+dataItem.getPlantStatus());
					}
				}
			
		}, new IFailure<Exception>(){
			public void invoke(Exception exception) {
								
			}
			
		});
	}
	

	/**
	 * Make toxic alert.
	 *
	 * @param v the v
	 */
	public void makeToxicAlert(View v) {
    	boolean atLeastOneToxicPlant = false;
		String toxicMessage = ("Achtung! Teile von folgenden Pflanzen sind giftig:");
		ArrayList<Plant> selectedPlants = Helpers.getSelectedPlants(plants);
		for (Plant p : selectedPlants) {
			if (p.getPlantData().isToxic()) {
				atLeastOneToxicPlant = true;
				toxicMessage += ("\n" + p.getName());
			}
		}
		if(atLeastOneToxicPlant) {
			AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
    		builder.setMessage(toxicMessage)
    			   .setIcon(android.R.drawable.ic_dialog_alert)
				   .setTitle("Giftige Pflanze(n) hinzugefügt!")
    		       .setCancelable(true)
    		       .setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					}
				});
    		final AlertDialog alert = builder.create();
            
            alert.show();
		}
	}
	
	
	/**
	 * makes practicable and toxic alerts,
	 * checks the selection and adds selected plants
	 * former makeNotPracticableAlert() function
	 *
	 * @param v the view (button) that has been clicked
	 */
	public void checkAndAddSelected(final View v, final PlantStatus wasSelectedAs) {
		String notPracticable = v.getContext().getString(R.string.isnotPracticableDetection);
		final String statusName;
		if (wasSelectedAs == PlantStatus.cutting) {
			statusName = "Steckling";
		} else if (wasSelectedAs == PlantStatus.seed) {
			statusName = "Aussaat";
		} else {
			statusName = "unknown";
		}
		
    	boolean atLeastOneNotPracticable = false;
		String message = ("Achtung! Die von Ihnen ausgewählte Hinzufüge-Methode ist für folgende Pflanze(n) nicht praktikabel:");
		ArrayList<Plant> selectedPlants = Helpers.getSelectedPlants(plants);
		final ArrayList<Plant> notPracticablePlants = new ArrayList<Plant>();
		for (Plant p : selectedPlants) {
			if (wasSelectedAs == PlantStatus.cutting && p.getPlantData().getScionDescription().equals(notPracticable) ||
				wasSelectedAs == PlantStatus.seed && p.getPlantData().getSeedingDescription().equals(notPracticable)) {
				atLeastOneNotPracticable = true;
				notPracticablePlants.add(p);
				message += ("\n" + p.getName());
			}
		}
		
		// make not practicable alert
		message += ("\n\nMöchten Sie diese Pflanzen trotzdem als " + statusName + " hinzufügen?");
		if(atLeastOneNotPracticable) {
			AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
    		builder.setMessage(message)
    			   .setIcon(android.R.drawable.ic_dialog_alert)
				   .setTitle("Für Pflanze(n) nicht praktikable Hinzufüge-Methode!")
    		       .setCancelable(true)
    		       .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						for (Plant p : notPracticablePlants) {
							p.setSelected(false);
						}		
						addSelected(v, wasSelectedAs, statusName);
					}
				})
    		       .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						addSelected(v, wasSelectedAs, statusName);
					}
				});
    		final AlertDialog alert = builder.create();
            alert.show();
		}
		else {
			addSelected(v, wasSelectedAs, statusName);
		}
	}
	
	/**
	 * adds the selected plants
	 * @param v the view (button) that has been clicked
	 * @param wasSelectedAs the plantStatus
	 * @param statusName the name of <code>wasSelectedAs</code>
	 */
	public void addSelected(View v, PlantStatus wasSelectedAs, String statusName) {
		if(userSettings.getHasChildren()) {
			makeToxicAlert(v);
		}
		
		Helpers.addSelectedWithStatus(plants, wasSelectedAs, v.getContext());
		((GridViewAdapter) gridView.getAdapter()).notifyDataSetChanged(); // refresh
																	// view
		Toast.makeText(getView().getContext(),
				"Selektion als " + statusName + " hinzugefügt!",
				Toast.LENGTH_SHORT).show();
		menuNoSelect();
	}



	/**
	 * Remove all selection buttons.
	 */
	public void menuNoSelect() {
		((TextView) getView().findViewById(R.id.textView_menu))
				.setText(R.string.menu_label_noSelect);
		((Button) getView().findViewById(R.id.button_seed))
				.setVisibility(View.GONE);
		((Button) getView().findViewById(R.id.button_cutting))
				.setVisibility(View.GONE);
		((Button) getView().findViewById(R.id.button_exists))
				.setVisibility(View.GONE);
		((Button) getView().findViewById(R.id.button_remove))
				.setVisibility(View.GONE);
		
		getView().findViewById(R.id.LinearLayout1).setBackgroundDrawable(null);
	}

	/** The grid view. */
	GridView gridView;

	/** The button_remove. */
	Button button_remove;

	/** The button_seed. */
	Button button_seed;
	
	/** The button_cutting. */
	Button button_cutting;
	
	/** The button_exists. */
	Button button_exists;

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * Get UserData
		 */
		GardenContext ctx = new GardenContext(this.getActivity().getApplicationContext());
		ctx.getUserSettings(new ISuccess<UserData>() {

			public void invoke(UserData data) {
				userSettings = data;
			}	
		}, new IFailure<Exception>(){
			public void invoke(Exception exception) {	
			}
		});	
		

    	LinearLayout layout = (LinearLayout) this.getActivity().findViewById(R.id.fragment_container_filter_tasks);
    	layout.setVisibility(View.VISIBLE);
		adapter = new GridViewAdapter(getView().getContext(), plants);
		
		gridView = (GridView) getView().findViewById(R.id.grid);
		button_remove = (Button) getView().findViewById(R.id.button_remove);
		button_seed = (Button) getView().findViewById(R.id.button_seed);
		button_cutting = (Button) getView().findViewById(R.id.button_cutting);
		button_exists = (Button) getView().findViewById(R.id.button_exists);
		gridView.setAdapter(adapter);

		boolean isPreselectionEmpty = true;
		TextView preselectionEmpty = (TextView) getView().findViewById(R.id.preselection_empty_overlay);
		//make first run preselection
		for(Plant currentPlant: plants)
		{
			if(GardenDatabaseSchemaManager.isFirstStart)
				{
					
					if(Helpers.is_preselected(currentPlant, userSettings)) {
						//currentPlant.setSelected(true);
						isPreselectionEmpty = false;
					}
				
					/*if (Helpers.isOnePlantSelected(plants)) {
						preselectionEmpty.setVisibility(View.INVISIBLE);
						
						((TextView) getView().findViewById(R.id.textView_menu))
								.setText(R.string.menu_label_select);
						((Button) getView().findViewById(R.id.button_seed))
								.setVisibility(View.VISIBLE);
						((Button) getView().findViewById(R.id.button_cutting))
								.setVisibility(View.VISIBLE);
						((Button) getView().findViewById(R.id.button_exists))
								.setVisibility(View.VISIBLE);
						getView().findViewById(R.id.LinearLayout1).setBackgroundResource(R.drawable.selectmenu_background);
		
						if (Helpers.areSelectedPlantsChecked(plants))
							((Button) getView().findViewById(R.id.button_remove))
									.setVisibility(View.VISIBLE);
						else
							((Button) getView().findViewById(R.id.button_remove))
									.setVisibility(View.GONE);
					} else {
						menuNoSelect();
						
						preselectionEmpty.setVisibility(View.VISIBLE);
					}*/
			}
			
		}
		if(isPreselectionEmpty) {
			if(GardenDatabaseSchemaManager.isFirstStart)
				preselectionEmpty.setVisibility(View.VISIBLE);
		}
		else {
			preselectionEmpty.setVisibility(View.INVISIBLE);
		}
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				ImageView backgroundImage = (ImageView) v.findViewById(R.id.inventory_item_background);
				Plant currentPlant = Helpers.getPlantByName(
						((CheckedTextView) v.findViewById(R.id.checkedText))
								.getText().toString(), plants);

				if (!currentPlant.isSelected()) {
					backgroundImage.setImageDrawable(selected);
					currentPlant.setSelected(true);
					//first run && category && cost && timeC

				} else {
					backgroundImage.setImageDrawable(unselected);
					currentPlant.setSelected(false);
				}

				if (Helpers.isOnePlantSelected(plants)) {
					((TextView) getView().findViewById(R.id.textView_menu))
							.setText(R.string.menu_label_select);
					((Button) getView().findViewById(R.id.button_seed))
							.setVisibility(View.VISIBLE);
					((Button) getView().findViewById(R.id.button_cutting))
							.setVisibility(View.VISIBLE);
					((Button) getView().findViewById(R.id.button_exists))
							.setVisibility(View.VISIBLE);
					
					getView().findViewById(R.id.LinearLayout1).setBackgroundResource(R.drawable.selectmenu_background);

					if (Helpers.areSelectedPlantsChecked(plants))
						((Button) getView().findViewById(R.id.button_remove))
								.setVisibility(View.VISIBLE);
					else
						((Button) getView().findViewById(R.id.button_remove))
								.setVisibility(View.GONE);
				} else {
					menuNoSelect();
				}

			}
		});

		button_remove.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Helpers.removeSelected(plants, v.getContext());
				((GridViewAdapter) gridView.getAdapter()).notifyDataSetChanged(); // refresh
																			// view
				Toast.makeText(getView().getContext(), "Selektion entfernt!",
						Toast.LENGTH_SHORT).show();
				menuNoSelect();
			}

		});

		button_seed.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAndAddSelected(v, PlantStatus.seed);
			}
		});

		button_cutting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAndAddSelected(v, PlantStatus.cutting);
			}
		});

		button_exists.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(userSettings.getHasChildren()) {
					makeToxicAlert(v);
				}
				Helpers.addSelectedWithStatus(plants, PlantStatus.exists, v.getContext());

				((GridViewAdapter) gridView.getAdapter()).notifyDataSetChanged(); // refresh
																			// view
				Toast.makeText(getView().getContext(),
						"Selektion als \"vorhanden\" hinzugefügt!",
						Toast.LENGTH_SHORT).show();
				menuNoSelect();
			}
		});

	}

	/**
	 * This is called if the user chose another fragment.
	 */
	public void onPause() {
		super.onPause();
		gridView.setVisibility(View.INVISIBLE);	//prevent crash on Home button because of recycle

		gridView.clearAnimation();
		
		Helpers.deselectAllPlants(plants); // deselect all plants
		
		for (int i = 0; i < plants.size(); i++)
			plants.get(i).getPicture().recycle();
		
		plants.clear();
		
		if(!GardenDatabaseSchemaManager.isFirstStart) GardenApplication.getInstance().allowUpdates();
	}

	/**
	 * This is called if the user chose this fragment.
	 */
	public void onResume() {
		super.onResume();
		refreshInventory(getActivity().getApplicationContext());	//someone could exchange the database while the program is running
		
		//reset the adapter so that alle images are reloaded -> prevents crash with home button
		adapter = new GridViewAdapter(getView().getContext(), plants);
		gridView.setAdapter(adapter);

		//gridView.smoothScrollToPosition(0); // scroll always to top of view
		gridView.setVisibility(View.VISIBLE);
		
		if(GardenDatabaseSchemaManager.isFirstStart) GardenApplication.getInstance().denyUpdates();
	}
}
