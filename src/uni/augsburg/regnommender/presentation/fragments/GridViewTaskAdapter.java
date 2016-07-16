package uni.augsburg.regnommender.presentation.fragments;

import java.util.ArrayList;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class GridViewTaskAdapter is used within the webview (which is shown when
 * a task is opened) to display all plants which  are in need of attention.
 * 
 */
public class GridViewTaskAdapter extends BaseAdapter {
	/** The mininmum picture width. */
	final int minPictureWidth = 100;

	/** The context. */
	private Context context;
	
	/** The layout inflater. */
	private LayoutInflater layoutInflater;
	
	/** The adapter plants. */
	private ArrayList<PlantData> adapterPlants;
	
	public ArrayList<Bitmap> bmps;

	/**
	 * Instantiates a new gridview task adapter.
	 *
	 * @param c the context
	 * @param taskPlants the task plants
	 * @param category_picture_button the category_picture_button
	 */
	GridViewTaskAdapter(Context c, ArrayList<PlantData> taskPlants, ArrayList<Bitmap> bitmaps) {
		context = c;
		this.adapterPlants = taskPlants;
		layoutInflater = LayoutInflater.from(context);
		bmps = bitmaps;
	}	
	
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return adapterPlants.size()+1; //allgemein item einbezogen!
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return adapterPlants.get(position+1); //allgemein item einbezogen!
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

		
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		View grid;
		if (convertView == null) {
			grid = new View(context);
			grid = layoutInflater.inflate(R.layout.gridlayout_task, null);
		} else {
			grid = (View) convertView;
		}

		ImageView imageView = (ImageView) grid.findViewById(R.id.image);
		//imageView.setMinimumHeight(minPictureHeight);
		imageView.setMinimumWidth(minPictureWidth);	

		imageView.setImageBitmap(bmps.get(position));

		TextView textView = (TextView) grid.findViewById(R.id.plantText);
		
		if (position == 0) textView.setText("Allgemeines");
		else textView.setText(String.valueOf(adapterPlants.get(position-1).getName()));

		return grid;
	}
}
