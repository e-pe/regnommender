package uni.augsburg.regnommender.presentation.fragments;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.DataManager;
import uni.augsburg.regnommender.businessLogic.DataManagerHelper;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.TaskData;
import uni.augsburg.regnommender.businessLogic.UserData;
import uni.augsburg.regnommender.businessLogic.UserTaskData;
import uni.augsburg.regnommender.businessLogic.generators.MeanBasedTaskGenerator;
import uni.augsburg.regnommender.businessLogic.generators.TaskGenerator;
import uni.augsburg.regnommender.businessLogic.generators.simulators.JustGenerateTaskSimulator;
import uni.augsburg.regnommender.businessLogic.generators.simulators.UserAvailabilitySimulator;
import uni.augsburg.regnommender.core.BackgroundService;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.IQuery2;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.dataAccess.GardenDatabaseSchemaManager;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.infrastructure.GardenApplication;
import uni.augsburg.regnommender.presentation.FileDialog;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.SelectionMode;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class MainActivity is responsible for the following:
 * - displaying all fragments (TaskFragment, HisotryFragment, InventoryFragment, 
 * SettingsFragments, FilterInventoryFragment) 
 * - displaying the menu items (toggle help overlay, refresh tasks)
 * - initiating the tutorial mode (if the application is started for the first
 * time)
 */
public class MainActivity extends Activity {
	
	/** The app context. */
	public static Context appContext;
	
	/** The visible. */
	public static boolean visible = true;
	
	/** The actionbar. */
	public ActionBar actionbar;
	
	/** The Task tab. */
	public ActionBar.Tab TaskTab;
    
    /** The History tab. */
    public ActionBar.Tab HistoryTab;
    
    /** The Inventorytab. */
    public ActionBar.Tab Inventorytab;
    
    /** The Settings tab. */
    public ActionBar.Tab SettingsTab;

/**
 * Called when the activity is first created.
 *
 * @param savedInstanceState the saved instance state
 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//show the memory that the app can consume
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		
		
		
        // start background service
        startService(new Intent(MainActivity.this, BackgroundService.class));
		setContentView(R.layout.main);
		appContext = getApplicationContext();
		 
		 // get an instance of FragmentTransaction from your Activity
	       FragmentManager fragmentManager = getFragmentManager();
	       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	       
	        actionbar = getActionBar();
	        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        actionbar.setBackgroundDrawable(new ColorDrawable(-12303292));
	        
	        TaskTab = actionbar.newTab().setText("Aufgaben");
	        HistoryTab = actionbar.newTab().setText("Verlauf");
	        Inventorytab = actionbar.newTab().setText("Inventar");
	        SettingsTab = actionbar.newTab().setText("Einstellungen");
	        
	        Fragment TaskFragment = new TaskFragment();
	        Fragment HistoryFragment = new HistoryFragment();
	        Fragment InventoryFragment = new InventoryFragment();
	        Fragment SettingsFragment = new SettingsFragment();

	        TaskTab.setTabListener(new MyTabsListener(TaskFragment, new FilterDummyFragment()));
	        HistoryTab.setTabListener(new MyTabsListener(HistoryFragment, new FilterDummyFragment()));
	        Inventorytab.setTabListener(new MyTabsListener(InventoryFragment, new FilterInventoryFragment()));
	        SettingsTab.setTabListener(new MyTabsListener(SettingsFragment, new FilterDummyFragment()));
	
	        if (!GardenDatabaseSchemaManager.isFirstStart) {
	        	actionbar.addTab(TaskTab);
	        	actionbar.addTab(HistoryTab);
	        	actionbar.addTab(Inventorytab);
	        }
	        actionbar.addTab(SettingsTab);

	        RelativeLayout helpOverlay = (RelativeLayout) findViewById(R.id.mainimage);
	        helpOverlay.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					toggleHelpOverlay();				
				}
			});

	}
	

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        
        if (GardenDatabaseSchemaManager.isFirstStart) {
        	checkTutorialMode(menu);
        	
        	GardenContext gardenContext = new GardenContext(this);
        	gardenContext.createUserSettings(new ISuccess<UserData>() {

				/**
				 * Invoke.
				 *
				 * @param data the data
				 */
				public void invoke(UserData data) {
					
				}
        		
        	}, new IFailure<Exception>() {

				/**
				 * Invoke.
				 *
				 * @param exception the exception
				 */
				public void invoke(Exception exception) {
				}
        		
        	});
        }
        
        return true;
    }
	
	/**
	 * Check tutorial mode.
	 *
	 * @param menu the menu
	 */
	private void checkTutorialMode(Menu menu) {
		MenuItem itemTutorial = menu.findItem(R.id.menuitem_tutorial);
		MenuItem itemEndTutorial = menu.findItem(R.id.menuitem_endtutorial);
		MenuItem itemHelp = menu.findItem(R.id.menuitem_help);
		MenuItem itemRefresh= menu.findItem(R.id.menuitem_refresh);
		itemTutorial.setVisible(true);
		itemEndTutorial.setVisible(true);
		itemHelp.setVisible(false);
		itemRefresh.setVisible(false);
	}

	
	/**
	 * Toggle help overlay.
	 */
	private void toggleHelpOverlay() {
		RelativeLayout helpOverlay = (RelativeLayout) findViewById(R.id.mainimage);
		TextView helpOverlayArrowText = (TextView) findViewById(R.id.help_overlay_arrow_text);
		TextView helpOverlayText = (TextView) findViewById(R.id.help_overlay_text);
		LinearLayout helpOverlayArrowWithText = (LinearLayout) findViewById(R.id.help_overlay_arrow_with_text);
		
		if (helpOverlay.getVisibility() == View.VISIBLE) 
			helpOverlay.setVisibility(View.INVISIBLE);
		else
		{
				//set texts and move the arrow and arrow description according to the selected tab
				int marginLeft = 0;
			
				if (this.actionbar.getSelectedTab().equals(this.SettingsTab)) {
					helpOverlayArrowText.setText(R.string.help_overlay_settings_arrow_text);
					helpOverlayText.setText(R.string.help_overlay_settings_text);
					
					marginLeft = 580;
				}
				else if (this.actionbar.getSelectedTab().equals(this.HistoryTab)) {
					helpOverlayArrowText.setText(R.string.help_overlay_history_arrow_text);
					helpOverlayText.setText(R.string.help_overlay_history_text);
					
					marginLeft = 260;
				}
				
				else if (this.actionbar.getSelectedTab().equals(this.Inventorytab)) {
					helpOverlayArrowText.setText(R.string.help_overlay_inventory_arrow_text);
					helpOverlayText.setText(R.string.help_overlay_inventory_text);
					
					marginLeft = 400;				
				}
				
				else if (this.actionbar.getSelectedTab().equals(this.TaskTab)) {
					helpOverlayArrowText.setText(R.string.help_overlay_tasks_arrow_text);
					helpOverlayText.setText(R.string.help_overlay_tasks_text);
					
					marginLeft = 180;
				}
				
				//move the text and description underneath the tab-button
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(marginLeft, 0, 0, 0);
				helpOverlayArrowWithText.setLayoutParams(lp);
					
				Animation animation = AnimationUtils.loadAnimation( this, R.anim.fade);
				helpOverlay.startAnimation( animation );
				helpOverlay.setVisibility(View.VISIBLE);			
		}
	}
	
	/**
	 * Switch help overlay off.
	 */
	private void helpOverlayOff() {
		if (findViewById(R.id.mainimage).getVisibility() == View.VISIBLE) 
			findViewById(R.id.mainimage).setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Refresh the tasks.
	 */
	private void refreshTasks()
	{
		Toast.makeText(this, "Tasks aktualisieren", Toast.LENGTH_LONG).show();
		try {
			//gets the data manager to read and write data to the database
			final DataManager dataManager = new DataManager(this.getContentResolver());
		
			//gets the specified task generator
			TaskGenerator taskGenerator = new MeanBasedTaskGenerator();
			
			taskGenerator.setDataSettings(
				//gets the user data required for launching the task generator 
				dataManager.getUserSettings(), 
				//gets the list of all available tasks required for launching the task generator 
				dataManager.getAllTasks(), 
				//gets the list of all available plants required for launching the task generator
				dataManager.getAllPlants(), 
				//gets the list of all available user plants required for launching the task generator
				dataManager.getUserPlants(),
				//defines a query for getting the specified user task for evaluating the specified task attribute
				new IQuery2<TaskData, PlantData, UserTaskData>(){
					public UserTaskData runQuery(TaskData task, PlantData plant){
						if(task.getIsPlantTask())
						{
							String plantId = plant != null ? plant.getId() : null;
							
							return dataManager.getQueryManager().findLastDoneUserTask(task.getId(), plantId);
						}
						else
						{
							return dataManager.getQueryManager().findLastDoneUserTask(task.getId());
						}
						
						//String plantId = plant != null ? plant.getId() : null;
						
						
					}
				});
		
			//generates user tasks and persists them to the database
			ArrayList<UserTaskData> userTasks = taskGenerator.generateUserTasks();
			//gets the set difference for persisting only new user tasks to the database
			ArrayList<UserTaskData> pendingUserTasks= dataManager.getPendingUserTasks();
			ArrayList<UserTaskData> userTasksToPersist = DataManagerHelper.differenceSimilar(userTasks, pendingUserTasks);

			dataManager.addUserTasks(userTasksToPersist.iterator());
			
			//invokes an update on the specified fragment for refreshing the actual task list
			GardenApplication.getInstance().notifyUpdate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("TaskGenerator@backgroundservice Exception", ex.getMessage());
		}
		System.gc();
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		switch(item.getItemId()) {
		
			case R.id.menuitem_help:
				toggleHelpOverlay();
				return true;
				
				// refresh button was pressed
			case R.id.menuitem_refresh:
				refreshTasks();
				return true;
				
			case R.id.menuitem_tutorial:
				
				//tab count = 1 and is in SettingsTab -> add inventory tab and select
				if(this.actionbar.getTabCount() == 1 && this.actionbar.getSelectedTab().equals(this.SettingsTab)) {
					this.actionbar.addTab(this.Inventorytab, 0, true);
				}
				//is in SettingsTab -> select existing inventory tab
				else if (this.actionbar.getSelectedTab().equals(this.SettingsTab)) {
					this.actionbar.selectTab(this.Inventorytab);
				}
				
				//tab count = 2 and is in InventoryTab -> add history and task tab and select task tab
				else if (this.actionbar.getTabCount() == 2 && this.actionbar.getSelectedTab().equals(this.Inventorytab)) {
					this.actionbar.addTab(this.HistoryTab, 0, false);
					this.actionbar.addTab(this.TaskTab, 0, true);
					item.setVisible(false);
					
					GardenDatabaseSchemaManager.isFirstStart = false;
					GardenApplication.getInstance().allowUpdates();
					refreshTasks();
					
					this.recreate();
				}
				
				//is in InventoryTab -> select existing task tab
				else if (this.actionbar.getSelectedTab().equals(this.Inventorytab)) {
					this.actionbar.selectTab(this.TaskTab);
					
				}

				return true;
				
			//pressed tutorial end button
			case R.id.menuitem_endtutorial:
				GardenDatabaseSchemaManager.isFirstStart = false;
				GardenApplication.getInstance().allowUpdates();
				refreshTasks();
				
				this.recreate();
				return true;
		}
		return false;
	}
	
	/**
	 * The listener interface for receiving myTabs events.
	 * The class that is interested in processing a myTabs
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyTabsListener<code> method. When
	 * the myTabs event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyTabsEvent
	 */
	class MyTabsListener implements ActionBar.TabListener {
		
		/** The fragment1. */
		public Fragment fragment1;
		
		/** The fragment2. */
		public Fragment fragment2;
		
		/** The fragment manager. */
		FragmentManager fragmentManager = getFragmentManager();
	    
    	/** The fragment transaction. */
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    
		
		/**
		 * Instantiates a new my tabs listener.
		 *
		 * @param fragment1 the fragment1
		 * @param fragment2 the fragment2
		 */
		public MyTabsListener(Fragment fragment1, Fragment fragment2) {
			this.fragment1 = fragment1;
			this.fragment2 = fragment2;
		}
		
		/* (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		/* (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
//			ft.replace(R.id.fragment_container, fragment1, "sdf");
			
		    ft.replace(R.id.fragment_container, fragment1);
			ft.replace(R.id.fragment_container_filter_tasks, fragment2);
			
			helpOverlayOff();
			
			 if(GardenDatabaseSchemaManager.isFirstStart && actionbar.getTabCount() != 3){
		        	final Dialog dialog = new Dialog(MainActivity.this);
					dialog.setContentView(R.layout.tutorialpopup1);
//					dialog.setTitle(R.string.tutorial_titel_1);
		 
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					dialogButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.text);
					
					if(actionbar.getTabCount() == 1 && actionbar.getSelectedTab().equals(SettingsTab)){
						text.setText(R.string.tutorial_popup_1);
						dialog.setTitle(R.string.tutorial_titel_1);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
					if(actionbar.getTabCount() == 2 && actionbar.getSelectedTab().equals(Inventorytab)){
						text.setText(R.string.tutorial_popup_2);
						dialog.setTitle(R.string.tutorial_titel_2);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
					if(actionbar.getTabCount() == 4 && actionbar.getSelectedTab().equals(TaskTab)){
						text.setText(R.string.tutorial_popup_3);
						dialog.setTitle(R.string.tutorial_titel_3);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
				  }
		}

		/* (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment1);
		}
	}
	
	
	/** The request save. */
	int REQUEST_SAVE = 0;
	
	/** The request load. */
	int REQUEST_LOAD = 1;
	
	
	/**
	 * Copy file.
	 *
	 * @param inPath the in path
	 * @param outPath the out path
	 */
	private void copyFile(String inPath, String outPath) {
		try {
			InputStream myInput = new FileInputStream(inPath);

        	OutputStream myOutput = new FileOutputStream(outPath);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
    	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Im/Export db", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Im/Export db", e.getMessage());
		}
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

			Log.d("onActivityResult", "called");
		
            if (resultCode == Activity.RESULT_OK) {

                String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                
                String DB_PATH = "/data/data/uni.augsburg.regnommender/databases/";   
           	 	String DB_NAME =  GardenRepositoryMetaData.DATABASE_NAME;	 
                
                   if (requestCode == REQUEST_SAVE) {
                            System.out.println("Saving...");
                            
                            copyFile(DB_PATH + DB_NAME,filePath);
    
                    } else if (requestCode == REQUEST_LOAD) {
                            System.out.println("Loading...");
                            
                            copyFile(filePath, DB_PATH + DB_NAME);
                    }
                          
                    Log.d("onActivityResult", filePath);

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
    }
	
	
	/**
	 * Method for exporting the database.
	 */
	public void exportDB() {
		Intent intent = new Intent(MainActivity.this, FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");
        
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_CREATE);
        
        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
        
        startActivityForResult(intent, 0);
	}
	
	/**
	 * Method for importing the database.
	 */
	public void importDB() {
		Intent intent = new Intent(MainActivity.this, FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");
        
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);
        
        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
        
        startActivityForResult(intent, 1);
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_H)
		{
			if(UserAvailabilitySimulator.getAtHome())
			{
				UserAvailabilitySimulator.setAtHome(false);
				Toast.makeText(this, "h pressed, not at home ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				UserAvailabilitySimulator.setAtHome(true);
				Toast.makeText(this, "h pressed, at home ", Toast.LENGTH_SHORT).show();
			}
			
		}
		if(keyCode==KeyEvent.KEYCODE_A)
		{
			if(UserAvailabilitySimulator.getUserAviable())
			{
				UserAvailabilitySimulator.setUserAviable(false);
				Toast.makeText(this, "a pressed, not available", Toast.LENGTH_SHORT).show();
			}
			else
			{
				UserAvailabilitySimulator.setUserAviable(true);
				Toast.makeText(this, "a pressed, available ", Toast.LENGTH_SHORT).show();
			}
			
		}
		if(keyCode==KeyEvent.KEYCODE_J)
		{
			if(JustGenerateTaskSimulator.toggle())
			{
				
				Toast.makeText(this, "GenerateMeaTask", Toast.LENGTH_SHORT).show();
			}
			else
			{

				Toast.makeText(this, "dontDoIt", Toast.LENGTH_SHORT).show();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
}