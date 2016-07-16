package uni.augsburg.regnommender.presentation.fragments;

import java.util.ArrayList;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.dataAccess.GardenDatabaseSchemaManager;
import uni.augsburg.regnommender.presentation.Helpers;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class FilterInventoryFragment is used for filtering entries in the inventory (InventoryFragment).
 * As of now following attributes can be filtered: financial expenditures, existing, seed, cutting.  
 * For the filter to be extended, the class GridViewAdapter (@see GridViewAdapter) can be adapted
 * or addition attributes, as well the variable "values" in method onActivityCreated.
 * 
 */
public class FilterInventoryFragment extends android.app.ListFragment {
	
	/** Contains the attributes for the filter. */
	ArrayList<String> selectedItems = new ArrayList<String>();

	
	/* (non-Javadoc)
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		return inflater.inflate(R.layout.filter_layout, container, false);
	}

	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] values = new String[] {	getString(R.string.filter_inventory_finance),
											getString(R.string.filter_inventory_existing),
											getString(R.string.filter_inventory_seed),
											getString(R.string.filter_inventory_cutting),
											getString(R.string.filter_inventory_preselection)
										};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.simple_list_item_multiple_choice, values);
		setListAdapter(adapter);
		
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		listView.setItemsCanFocus(false);
		 
		triggerFilter();
	}

	
	
	void triggerFilter () {
		InventoryFragment fragment = (InventoryFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_container);
		
		String[] values = new String[] {	getString(R.string.filter_inventory_finance),
				getString(R.string.filter_inventory_existing),
				getString(R.string.filter_inventory_seed),
				getString(R.string.filter_inventory_cutting),
				getString(R.string.filter_inventory_preselection)
			};
		if(fragment != null) {
			GridViewAdapter adapter = (GridViewAdapter) fragment.gridView.getAdapter();
			adapter.filterData(selectedItems, values);
		
			//if(fragment.gridView.getChildCount() > 0) {
			//if(//adapter.getAdapterPlants().isEmpty() == false &&
					//!selectedItems.contains(getString(R.string.filter_inventory_preselection))) {
				//TextView t = (TextView)fragment.getView().findViewById(R.id.preselection_empty_overlay);
				//t.setVisibility(View.INVISIBLE);
			//}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		InventoryFragment fragment = (InventoryFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_container);
		TextView t = (TextView)fragment.getView().findViewById(R.id.preselection_empty_overlay);
		
		if(selectedItems.contains(item)) {
			selectedItems.remove(item);
			if (item.equals(getString(R.string.filter_inventory_preselection))) {
				t.setVisibility(View.INVISIBLE);
			}
		}
		else {
			
			selectedItems.add(item);
		}
	
		triggerFilter();
	}

	
	public void onStart() {
		super.onStart();

		ListView listView = getListView();
		
		if(GardenDatabaseSchemaManager.isFirstStart)
		{
			listView.setItemChecked(4, true);
			if(!selectedItems.contains(getString(R.string.filter_inventory_preselection))) {
				selectedItems.add(getString(R.string.filter_inventory_preselection));
			}
			
			triggerFilter();
		}
	}
}