package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.util.ArrayList;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.presentation.Helpers;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The Class HtmlView_task is used to display the task with certain priority.
 * In detail this contains the general description of the task and all plants that 
 * need attention (if applicable), including information specific for each plant 
 * describing what needs to be considered when executing the task.
 * , 
 */
public class HtmlView_task extends Dialog{
	
	/** The webview. */
	private WebView webView;
	
	/** The View in the list that has been clicked */
	private View view;
	
	/** The grid view. */
	GridView gridView;
	
	/** The plant data to load. */
	static PlantData plantDataToLoad;
	
	/** The task plants. */
	static ArrayList<PlantData> taskPlants;
	
	/** The task name. */
	static String taskName = "";
	
	/** The task desc. */
	static String taskDesc = "";
	
	/** The jump to section. */
	static String jumpToSection = "";
	
	/** Reload the page once to prevent render bugs. */
	boolean reload = false;

	
	Bitmap bmp;	
	GridViewTaskAdapter adapter;
	
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	
	
	/**
	 * Map to3 bars.
	 *
	 * @param value the value
	 * @return the int
	 */
	int mapTo3Bars(double value) {
		return (int)value+1;
	}
	
	
	/**
	 * Instantiates a new html view_task.
	 *
	 * @param context the context
	 * @param taskPlants the task plants
	 * @param task the task
	 * @param taskDesc the task desc
	 * @param category_picture the category_picture
	 * @param category_button_picture the category_button_picture
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public HtmlView_task(final Context context, ArrayList<PlantData> taskPlants, String task, String taskDesc, String category_picture, String category_button_picture, View v) throws IOException {
		super(context);
		view = v;
		LayoutInflater inflater = (LayoutInflater)this.getLayoutInflater();
        View vi = inflater.inflate(R.layout.webview_task, null);
        
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
        
        setContentView(vi);     
              
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.taskdialogtitle);    		
        TextView textViewTitle = (TextView) this.findViewById(R.id.text_taskDialog_Title);
		textViewTitle.setText(task);
		
		HtmlView_task.taskName = task;
		
		ImageView imageViewTitle = (ImageView) this.findViewById(R.id.header);
		
		try {
			bmp = BitmapFactory.decodeStream(context.getAssets().open(GardenRepositoryMetaData.PICTURE_CATEGORY_ASSETS_PATH+category_picture));
			imageViewTitle.setImageBitmap(bmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HtmlView_task.taskPlants = taskPlants;
		
		
		for (int i = 0; i < taskPlants.size()+1; i++) {
		
			String url;
			if (i == 0) url = GardenRepositoryMetaData.PICTURE_CATEGORY_ASSETS_PATH+category_button_picture;
			else url = GardenRepositoryMetaData.PICTURE_PLANTS_ASSETS_PATH+taskPlants.get(i-1).getImageUrl1();
			
			try {
				bmp = BitmapFactory.decodeStream(context.getAssets().open(url));
				bitmaps.add(bmp);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		adapter = new GridViewTaskAdapter(context, taskPlants, bitmaps);
		gridView = (GridView) findViewById(R.id.gridView);
		
		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				if (position == 0) { //general description
					webView.loadUrl("file:///android_asset/template_task_extended/template.html");
				}				
				else {
					
					loadPlantData(HtmlView_task.taskPlants.get(position-1));
					
					/*String toastText = "selektiert";					
					String plantName = ((TextView) v.findViewById(R.id.plantText)).getText().toString();	
					Toast.makeText(context,plantName + " " + toastText, Toast.LENGTH_SHORT).show();*/
				}

			}
		});

        
		this.webView =(WebView) findViewById(R.id.webview_task);

		webView.setWebViewClient(new WebViewClient() {

			   public void onPageFinished(WebView view, String url) {
					if (url.equals("file:///android_asset/template/template.html")) {
						view.loadUrl("javascript:setPlantnameCategory('"+plantDataToLoad.getName()+"','"+plantDataToLoad.getCategory()+"')");
						
						Helpers.fillDescription(view, "description",	plantDataToLoad.getDescription());
						Helpers.fillDescription(view, "descPlant",		plantDataToLoad.getPlantingDescription());
						Helpers.fillDescription(view, "descSeed",		plantDataToLoad.getSeedingDescription());
						Helpers.fillDescription(view, "descCutting",	plantDataToLoad.getScionDescription());
						Helpers.fillDescription(view, "descFertilize", 	plantDataToLoad.getFertilizingDescription());
						Helpers.fillDescription(view, "descPour", 		plantDataToLoad.getPouringDescription());
						Helpers.fillDescription(view, "descProcess", 	plantDataToLoad.getUsingGardenProductsDescription());
						Helpers.fillDescription(view, "descCUT",		plantDataToLoad.getCuttingDescription());
						
						view.loadUrl("javascript:setToxic("+(plantDataToLoad.isToxic()?"1":"0")+")");
						view.loadUrl("javascript:setBars3('sun',"+mapTo3Bars(plantDataToLoad.getSunlightRequirement())+")");
						view.loadUrl("javascript:setBars3('money',"+mapTo3Bars(plantDataToLoad.getPrice())+")");
						view.loadUrl("javascript:setBars3('water',"+mapTo3Bars(plantDataToLoad.getWaterConsumption())+")");
						view.loadUrl("javascript:setBars3('time',"+mapTo3Bars(plantDataToLoad.getTimeConsumption())+")");
						view.loadUrl("javascript:setPlantPicture(0,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl1()+"')");
						view.loadUrl("javascript:setPlantPicture(1,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl2()+"')");	
						view.loadUrl("javascript:setPlantPicture(2,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl3()+"')");
						view.loadUrl("javascript:showFinalPage()");	
						
						if (!HtmlView_task.jumpToSection.equals(""))
							view.loadUrl("javascript:jumpToSection('"+HtmlView_task.jumpToSection+"')");	

					}
					else
						if(url.equals("file:///android_asset/template_task_extended/template.html")) {				        	
				        	
							view.loadUrl("javascript:setHeaderText('"+HtmlView_task.taskName+"')");
							
							view.loadUrl("javascript:addTaskDescTab()");
							Helpers.fillDescription(view, true, HtmlView_task.taskDesc);
							
							// get plant specific description for each plant
							for (PlantData plantData:HtmlView_task.taskPlants) {
								String plantDescription = Helpers.getPlantDescriptionByTaskname(HtmlView_task.taskName, getContext(),plantData);
								
								if (!plantDescription.equals("")) {	//description found
									String header = plantData.getName();
									String picture = GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantData.getImageUrl1();
									
									view.loadUrl("javascript:addTaskTab('"+ header +"','"+picture+"')");
									Helpers.fillDescription(view, false, plantDescription);
									
								}
							}
							view.loadUrl("javascript:finishAkordeon()");
							view.loadUrl("javascript:showFinalPage()");
							
							if (!reload) 
							{
								view.reload();
								reload = true;
							}
						}
			    }
		});	
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		
		loadTaskDescription(taskDesc);
	}
	
	
	/**
	 * Load plant data.
	 *
	 * @param plantDataToLoad the plant data to load
	 */
	void loadPlantData(PlantData plantDataToLoad) {
		
		HtmlView_task.plantDataToLoad = plantDataToLoad;
		webView.loadUrl("file:///android_asset/template/template.html");
		
		jumpToSection = Helpers.getWebsiteSectionNameByTaskname(HtmlView_task.taskName, getContext());
	}
	
	
	/**
	 * Load task description.
	 *
	 * @param taskDesc the task desc
	 */
	void loadTaskDescription(String taskDesc) {
		
		HtmlView_task.taskDesc = taskDesc;
		webView.loadUrl("file:///android_asset/template_task_extended/template.html");	
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() {
		return view;
	}
	
	
	/**
	 * Free bitmap memory.
	 */
	public void cleanBitmaps() {
		bmp.recycle();
		
		for (int i = 0; i< bitmaps.size(); i++)
			bitmaps.get(i).recycle();			
		
		bitmaps.clear();
	}
}
