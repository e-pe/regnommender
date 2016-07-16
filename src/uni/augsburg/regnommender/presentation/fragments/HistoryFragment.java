package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.PlantData;
import uni.augsburg.regnommender.businessLogic.UserTaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskGroupData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.Helpers;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * The Class HistoryFragment is responsible for listing all tasks marked as "done".
 * It also shows the reward of the user (points are awarded when a task is "done").
 * If a task has been wrongly marked as "done" the task can be "undone". The awarded points 
 * for the relevant task will be removed as well.
 * 
 */
public class HistoryFragment extends ListFragment {
	
	/** The HtmlView tst. */
	HtmlView_task tst;
	
	/** The group list. */
	private ArrayList<UserTaskGroupData> groupList = new ArrayList<UserTaskGroupData>();
	
	/** The progress value. */
	int progressValue = 0;
	
	/** The HistoryItemAdapter. */
	HistoryItemAdapter adapter;
	
    /* (non-Javadoc)
     * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
    	
    	return inflater.inflate(R.layout.historyfragment, container, false);
    }
    
    /* (non-Javadoc)
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	LinearLayout layout = (LinearLayout) this.getActivity().findViewById(R.id.fragment_container_filter_tasks);
    	layout.setVisibility(View.GONE);
        
        ListFragment fragment = (ListFragment)getFragmentManager()
				.findFragmentById(R.id.fragment_container);
        

        adapter = new HistoryItemAdapter(getView().getContext(),R.layout.listitem_history, groupList, this);
        refreshList();
        adapter.setNotifyOnChange(true);
        
        if (fragment != null)	//prevent crash on very fast switches
        	fragment.setListAdapter(adapter);
    }
    
    
	/* (non-Javadoc)
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		UserTaskGroupData group = groupList.get(position);
		
		String taskName = group.getTask().getName();
		String taskDesc = group.getTask().getDescription();
		String category_picture = group.getTask().getCategoryPictureIcon();
		String category_button_picture = group.getTask().getCategoryPictureButton();
		
		ArrayList<PlantData> list = new ArrayList<PlantData>();
		
		Iterator<UserTaskData> iter;
		try {
			iter = group.getUserTasks();
			
			while(iter.hasNext()) {
				PlantData plant = iter.next().getPlant();
				if (plant.getId() != null)
					list.add(plant);
			}
			
			try {
				tst = new HtmlView_task(getActivity(), list, taskName, taskDesc, category_picture, category_button_picture, v);
				Button backWebview = (Button)tst.findViewById(R.id.webview_task_button1);
				backWebview.setOnClickListener(mCorkyListener);
				Button notDoneButton = (Button)tst.findViewById(R.id.webview_task_button2);
				notDoneButton.setText("Unerledigt");
				notDoneButton.setOnClickListener(mCorkyNotDoneListener);
				tst.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
	
	
	/** The onClickListener */
	private android.view.View.OnClickListener mCorkyListener = new android.view.View.OnClickListener() {
	    public void onClick(View v) {
	    	tst.cancel();
	    }
	};	
	/** The onClickListener listener. */
	private android.view.View.OnClickListener mCorkyNotDoneListener = new android.view.View.OnClickListener() {
	    public void onClick(View v) {
	    	tst.cancel();
	    	tst.getView().findViewById(R.id.history_checkBox).performClick();
	    }
	};	
    
    
	/**
	 * Updates the list which is used in the listfragment.
	 *
	 * @return the array list
	 */
	private ArrayList<UserTaskGroupData> updateList() {
		final ArrayList<UserTaskGroupData> list = new  ArrayList<UserTaskGroupData>();
    	
		GardenContext ctx = new GardenContext(this.getActivity().getApplicationContext()); 	
		ctx.getDoneTasksByUser(new ISuccess<Iterator <UserTaskGroupData>>(){
			
			public void invoke(Iterator <UserTaskGroupData> groups){
				
				//get usertasks				
				while(groups.hasNext()) {				
					list.add(groups.next());
				}							
		}},
		new IFailure<Exception>(){

			public void invoke(Exception exception) {
				exception.printStackTrace();
			}
		});
		
		return list;
	}  
	
	
	/**
	 * Automatically updates the groupList.
	 */
	public void refreshList() {
		adapter.clear();
		ArrayList<UserTaskGroupData> groups = updateList();
		adapter.addAll(groups);
		
		
		// Calculation of score
		long totalScore = Helpers.getTotalScoreOfAllGroups(groups);
		
		//set score
		TextView t_totalscore = (TextView) getView().findViewById(R.id.history_totalscore);
		t_totalscore.setText(String.valueOf("Gesamtpunktzahl: "+totalScore));
		
		//set level
		TextView t_level = (TextView) getView().findViewById(R.id.history_level);
		
		int maxLevelScore = 50;
		
		//level names
		String levelname = "";
		int levelNr = (int)(totalScore/maxLevelScore);
		
		String levelNames[] = new String[]{"Novize","Gärtner-Gehilfe","Gärtner","Profi-Gärtner","Gartenzwerg"};
		
		if (levelNr < levelNames.length-1) levelname = levelNr+": "+ levelNames[levelNr];
		else levelname = levelNr+": "+ levelNames[levelNames.length-1];
		
		t_level.setText("Stufe "+levelname);
		
		//progress
		ProgressBar progress = (ProgressBar) getView().findViewById(R.id.history_progressBar1);
		progressValue = (int)((100.0/maxLevelScore)*(totalScore%maxLevelScore));		
		progress.setProgress(progressValue);
		
		TextView stepProgress = (TextView) getView().findViewById(R.id.history_progress);
		stepProgress.setText("Stufenfortschritt: "+progressValue+" %");
		
		// set text if list is empty
		TextView t = (TextView) getView().findViewById(R.id.history_empty_overlay);
		if(groupList.isEmpty()) {
			t.setVisibility(View.VISIBLE);
		} else {
			t.setVisibility(View.GONE);
		}
	}
    
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		//progress
		ProgressBar progress = (ProgressBar) getView().findViewById(R.id.history_progressBar1);
		progress.setProgress(progressValue);
	}
}