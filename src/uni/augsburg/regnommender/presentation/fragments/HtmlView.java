package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.presentation.Helpers;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class HtmlView.
 */
public class HtmlView  extends Dialog{
	
	/** The web view. */
	private WebView webView;
	
	/** The plantdata to load. */
	PlantData plantDataToLoad = new PlantData();
	
	
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
	 * Instantiates a new html view.
	 *
	 * @param context the context
	 * @param plantDataToLoad the plant data to load
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public HtmlView(Context context, final PlantData plantDataToLoad) throws IOException {
		super(context);
		LayoutInflater inflater = (LayoutInflater)this.getLayoutInflater();
        View vi = inflater.inflate(R.layout.webview, null);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
        setContentView(vi);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.taskdialogtitle);    		
        TextView textViewTitle = (TextView) this.findViewById(R.id.text_taskDialog_Title);
		textViewTitle.setText("Infos und Tipps");
		ImageView imageViewTitle = (ImageView) this.findViewById(R.id.header);
		imageViewTitle.setImageResource(R.drawable.info_small);
		
		this.webView =(WebView) findViewById(R.id.webview);
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
						view.loadUrl("javascript:setBars3('sun',"+	mapTo3Bars(plantDataToLoad.getSunlightRequirement())+")");
						view.loadUrl("javascript:setBars3('money',"+mapTo3Bars(plantDataToLoad.getPrice())+")");
						view.loadUrl("javascript:setBars3('water',"+mapTo3Bars(plantDataToLoad.getWaterConsumption())+")");
						view.loadUrl("javascript:setBars3('time',"+	mapTo3Bars(plantDataToLoad.getTimeConsumption())+")");
						view.loadUrl("javascript:setPlantPicture(0,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl1()+"')");
						view.loadUrl("javascript:setPlantPicture(1,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl2()+"')");	
						view.loadUrl("javascript:setPlantPicture(2,'"+GardenRepositoryMetaData.PICTURE_ASSETS_PATH_FROMHTML+plantDataToLoad.getImageUrl3()+"')");
						
						view.loadUrl("javascript:showFinalPage()");	

					}
			    }
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.loadUrl("file:///android_asset/template/template.html");	
	}
}
