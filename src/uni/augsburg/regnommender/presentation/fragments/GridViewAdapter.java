package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantStatus;
import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.Helpers;
import uni.augsburg.regnommender.presentation.Plant;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * The Class GridViewAdapter is used to display the content (i.e. plants)
 * of the inventory (@see InventoryFragment).
 * It is also used by the filter within the FilterInventoryFragment to filter
 * certain attributes (@see FilterInventoryFragment). 
 * 
 */
public class GridViewAdapter extends BaseAdapter implements Filterable{
	
	/** The mininum picture height. */
	final int minPictureHeight = 300;
	
	/** The minnimum picture width. */
	final int minPictureWidth = 250;

	/** The context of the activity. */
	private Context context;
	
	/** The layoutinflater. */
	private LayoutInflater layoutInflater;
	
	/** All available plants in the inventory. */
	private ArrayList<Plant> adapterPlants;
	
	/** Original values for the adapter plants. */
	private ArrayList<Plant> adapterPlantsOriginalValues;

	private Drawable unselected = null, selected = null;
	
	
	HashSet <Integer> eventAlreadySet = new HashSet<Integer>();
	
	/**
	 * Instantiates a new gridview adapter.
	 *
	 * @param c the context
	 * @param adapterPlants all available plants
	 */
	GridViewAdapter(Context c, ArrayList<Plant> adapterPlants) {
		context = c;
		this.adapterPlantsOriginalValues = adapterPlants;
		this.adapterPlants = adapterPlants;
		layoutInflater = LayoutInflater.from(context);
		
		eventAlreadySet.clear();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return adapterPlants.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return adapterPlants.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	
	/** The tst. */
	HtmlView tst;
	
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		//Log.d("MyAdapter", "getView called " + position); // TODO debug

		View grid;
		if (convertView == null) {
			grid = new View(context);
			grid = layoutInflater.inflate(R.layout.gridlayout, null);
		} else {
			grid = (View) convertView;
		}

		ImageView imageView = (ImageView) grid.findViewById(R.id.image);
		imageView.setMinimumHeight(minPictureHeight);
		imageView.setMinimumWidth(minPictureWidth);
		imageView.setImageBitmap(adapterPlants.get(position).getPicture());

		CheckedTextView checkedTextView = (CheckedTextView) grid
				.findViewById(R.id.checkedText);
		checkedTextView.setText(String.valueOf(adapterPlants.get(position).getName()));

		if (adapterPlants.get(position).getStatus() != PlantStatus.not_existing && adapterPlants.get(position).isRemoved() == false)
			checkedTextView.setChecked(true);
		else
			checkedTextView.setChecked(false);

		
		// fix the height of the background image according to the real height of the linear layout that is in front of it
		final ImageView backgroundImage = (ImageView) grid.findViewById(R.id.inventory_item_background);	
		backgroundImage.getLayoutParams().height = 0;	
		
		final LinearLayout linLay = (LinearLayout) grid.findViewById(R.id.inventory_grid_lin_layout);
		
		
		
		// get the height of the LinearLayout when its done and set the backgroundImage to this height
		ViewTreeObserver vto = linLay.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {
		    	//change height
		    	backgroundImage.getLayoutParams().height = linLay.getMeasuredHeight();
		    	
			    //redraw the picture
		    	backgroundImage.requestLayout();
		    	
		        //finalWidth = imageView.getMeasuredWidth();
		        return true;
		    }
		});
		

		Resources res = context.getResources();
		if (selected == null) 	selected = res.getDrawable(R.drawable.selected);
		if (unselected == null) unselected = res.getDrawable(R.drawable.unselected);
		
		if (adapterPlants.get(position).isSelected())
			backgroundImage.setImageDrawable(selected);
		else
			backgroundImage.setImageDrawable(unselected);

		ImageView imageButton = (ImageView) grid
				.findViewById(R.id.info_button);
		imageButton.setTag(adapterPlants.get(position).getPlantData().getId());

		imageButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				((ImageView) v).setBackgroundResource(R.drawable.info);
				
				if (tst == null || (tst != null && !tst.isShowing()))
					try {
						tst = new HtmlView(context, Helpers.getPlantDataFromId(adapterPlants,  (String)v.getTag() ) );
						Button backWebview = (Button)tst.findViewById(R.id.webviewbutton1);
						backWebview.setOnClickListener(webViewListener);
						tst.show();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

		});

		imageButton.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {	
				if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) 
					((ImageView) v).setBackgroundResource(R.drawable.info);
				else
					((ImageView) v).setBackgroundResource(R.drawable.info_select);
				
				return false;
			}
		});
		
		eventAlreadySet.add(position);
		
		return grid;
	}
	
	
	UserData userSettings = new UserData();
	
	/**
	 * Filter the plants.
	 *
	 * @param filter the filter
	 * @param filterValues the filtervalues
	 */
	public void filterData(ArrayList<String> filter, String[] filterValues){
		ArrayList<Plant> list = new ArrayList<Plant>();	

		GardenContext ctx = new GardenContext(context);
		ctx.getUserSettings(new ISuccess<UserData>() {

			public void invoke(UserData data) {
				userSettings = data;
			}	
		}, new IFailure<Exception>(){
			public void invoke(Exception exception) {	
			}
		});	
		
		
		if(filter.size() == 0){
			list = new ArrayList<Plant>(adapterPlantsOriginalValues);

		}
		else{
			for(int i = 0; i < adapterPlantsOriginalValues.size(); i++){
				boolean exists = 	adapterPlantsOriginalValues.get(i).getStatus() == PlantStatus.exists;
				boolean isRemoved = adapterPlantsOriginalValues.get(i).isRemoved();
				boolean steckling = adapterPlantsOriginalValues.get(i).getStatus() == PlantStatus.cutting;
				boolean aussaat = 	adapterPlantsOriginalValues.get(i).getStatus() == PlantStatus.seed;
				boolean finanz = 	adapterPlantsOriginalValues.get(i).getPlantData().getPrice() <= userSettings.getFinanceSetting();
				boolean preselected = Helpers.is_preselected(adapterPlantsOriginalValues.get(i), userSettings);
									
				boolean filter_existing = false;
				boolean filter_cutting = false;
				boolean filter_seeding = false;
				boolean filter_finance = false;
				boolean filter_preselected= false;
								
				for(int j = 0; j < filter.size(); j++){
					if(filter.get(j) == filterValues[1]) 	filter_existing = true;
					if(filter.get(j) == filterValues[3]) 	filter_cutting = true;
					if(filter.get(j) == filterValues[2]) 	filter_seeding = true;
					if(filter.get(j) == filterValues[0]) 	filter_finance = true;
					if(filter.get(j) == filterValues[4])	filter_preselected= true;
				}
				
				if (!isRemoved) {
					//filter by only finance
					if (filter_finance && !filter_existing && !filter_cutting && !filter_seeding && !filter_preselected) {
						if (finanz) list.add(adapterPlantsOriginalValues.get(i));
					}
					else
					if((filter_preselected && preselected)) //filter by preselection
					{	
						//filter by exists, cutting, seeding
						if ((filter_existing && exists) || (filter_cutting && steckling) || (filter_seeding && aussaat) )
						{
							if (filter_finance) //by finance
							{
								if (finanz) list.add(adapterPlantsOriginalValues.get(i));
							}
							else list.add(adapterPlantsOriginalValues.get(i)); //not finance
						}
						else if(!filter_existing && !filter_cutting && !filter_seeding) // just add them all if preselected
						{
							if(preselected)list.add(adapterPlantsOriginalValues.get(i));
						}
					}
					else //do not filter by preselection
					{
						//filter by exists, cutting, seeding
						if ((filter_existing && exists) || (filter_cutting && steckling) || (filter_seeding && aussaat) )
							if (filter_finance)//by finance
							{
								if (finanz) list.add(adapterPlantsOriginalValues.get(i));
							}
							else list.add(adapterPlantsOriginalValues.get(i)); // just add them all
					}
				}
			}
		}
		
		this.adapterPlants = list;
		notifyDataSetChanged();   
	}

	/* (non-Javadoc)
	 * @see android.widget.Filterable#getFilter()
	 */
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** The webview listener. */
	private android.view.View.OnClickListener webViewListener = new android.view.View.OnClickListener() {
	    
    	/* (non-Javadoc)
    	 * @see android.view.View.OnClickListener#onClick(android.view.View)
    	 */
    	public void onClick(View v) {
	    	tst.cancel();
	    }
	};

	public ArrayList<Plant> getAdapterPlants() {
		return adapterPlants;
	}
}
