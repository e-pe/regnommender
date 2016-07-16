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
import uni.augsburg.regnommender.core.IUpdateNotify;
import uni.augsburg.regnommender.infrastructure.GardenApplication;
import uni.augsburg.regnommender.presentation.GardenContext;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class TaskFragment is used to display the tasks.
 */
public class TaskFragment extends ListFragment{
	
	/** The task htmlv. */
	HtmlView_task taskHtmlv;
	
	/** The group list. */
	private ArrayList<UserTaskGroupData> groupList = new ArrayList<UserTaskGroupData>();
	
	/** The adapter. */
	TaskItemAdapter adapter;
	
	/** The is visible. */
	boolean isVisible = false;
	
	ArrayList<Bitmap> loadedAdapterBitmaps = new ArrayList<Bitmap>();
	
	
	/* (non-Javadoc)
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
    	return inflater.inflate(R.layout.taskfragment, container, false);
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
        
        adapter = new TaskItemAdapter(getView().getContext(),R.layout.listitem, groupList, loadedAdapterBitmaps, this);
        refreshList();
        adapter.setNotifyOnChange(true);
        
        if (fragment != null) 	//prevent crash on very fast switches
        	fragment.setListAdapter(adapter);
        
        GardenApplication.getInstance().setUpdateNotifyListener(new IUpdateNotify() {
			public void update() {
				if(adapter != null && isVisible)
					refreshList();
			}
        });
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
			
			if (taskHtmlv == null || (taskHtmlv != null && !taskHtmlv.isShowing()))
			try {
				taskHtmlv = new HtmlView_task(getActivity(), list, taskName, taskDesc, category_picture, category_button_picture, v);
				Button backWebview = (Button)taskHtmlv.findViewById(R.id.webview_task_button1);
				Button doneWebView = (Button)taskHtmlv.findViewById(R.id.webview_task_button2);
				backWebview.setOnClickListener(mCorkyListener);
				doneWebView.setOnClickListener(mCorkyDoneListener);
				
				taskHtmlv.setOnDismissListener(dismissListener);
				
				taskHtmlv.show();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}

	/**
	 * On click.
	 *
	 * @param dialog the dialog
	 * @param which the which
	 */
	public void onClick(DialogInterface dialog, int which) {
		
	}
	
	/** The m corky listener. */
	private android.view.View.OnClickListener mCorkyListener = new android.view.View.OnClickListener() {
	    public void onClick(View v) {
	    	//Toast.makeText(getActivity(), "test1", Toast.LENGTH_SHORT).show();
	    	taskHtmlv.cancel();
	    }
	};	
	
	/** The m corky done listener. */
	private android.view.View.OnClickListener mCorkyDoneListener = new android.view.View.OnClickListener() {
	    public void onClick(View v) {
	    	taskHtmlv.getView().findViewById(R.id.checkBox1).performClick();
	    	// auf erledigt klicken im zuvor angeklickten ListItem
	    	taskHtmlv.cancel();
	    }
	};	
	

	/** The dismiss listener. */
	private android.content.DialogInterface.OnDismissListener dismissListener = new android.content.DialogInterface.OnDismissListener() {

		public void onDismiss(DialogInterface arg0) {
			taskHtmlv.cleanBitmaps();
		}

	};	
	
	
	/**
	 * Update list.
	 *
	 * @return the array list
	 */
	private ArrayList<UserTaskGroupData> updateList() {
		final ArrayList<UserTaskGroupData> list = new  ArrayList<UserTaskGroupData>();
    	
		GardenContext ctx = new GardenContext(this.getActivity().getApplicationContext()); 	
		ctx.getPendingTasksByUser(new ISuccess<Iterator <UserTaskGroupData>>(){
			
			public void invoke(Iterator <UserTaskGroupData> groups){
				
				//get usertasks				
				while(groups.hasNext()) {				
					list.add(groups.next());
				}							
		}},
		new IFailure<Exception>(){

			public void invoke(Exception exception) {
				exception.printStackTrace();
				Log.e("TaskFragment update",exception.getMessage());
			}
		});
		
		return list;
	}  
	
	/**
	 * updates automatically groupList.
	 */
	public void refreshList() {
		adapter.clear();
		adapter.addAll(updateList());
		
		// set text if list is empty
		TextView t = (TextView) getView().findViewById(R.id.tasklist_empty_overlay);
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
		isVisible = true;
        refreshList();
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		isVisible = false;
		
		
		for (int i = 0; i < loadedAdapterBitmaps.size(); i++)
			loadedAdapterBitmaps.get(i).recycle();
		
		loadedAdapterBitmaps.clear();
	}
}