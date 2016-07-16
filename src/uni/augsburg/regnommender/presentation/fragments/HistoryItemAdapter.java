package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.TaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskGroupData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.Helpers;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * The Class HistoryItemAdapter is basically the same as the TaskItemAdapter (@see TaskItemAdapter),
 * however displays all tasks marked as "done". 
 */
public class HistoryItemAdapter extends ArrayAdapter<UserTaskGroupData> implements Filterable{
	
	/** The ArrayList containing the UserTasks. */
	private ArrayList<UserTaskGroupData> groupList;
	
	/** The context. */
	private Context context;
	
	/** The HistoryFragment. */
	HistoryFragment frag;

	
	/**
	 * Instantiates a new history item adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param groupList the group list
	 * @param frag the frag
	 */
	public HistoryItemAdapter(Context context, int textViewResourceId, ArrayList<UserTaskGroupData> groupList, HistoryFragment frag) {
		super(context, textViewResourceId, groupList);
		this.context=context;
		this.groupList = groupList;
		this.frag = frag;
	}

	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItem(int)
	 */
	@Override
	public UserTaskGroupData getItem(int position){
        return this.groupList.get(position);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount()
    {
        return groupList.size();
    }
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
    {
        return position;
    }

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
				LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitem_history, null);
			
		}
		
		if(groupList.size() > 0){
			TaskData task = groupList.get(position).getTask();
			
			UserTaskData uTask = null;
			
			try {
				uTask = groupList.get(position).getUserTasks().next();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (task != null) {
				TextView name = (TextView) v.findViewById(R.id.history_name);
				TextView description = (TextView) v.findViewById(R.id.history_description);
				TextView doneOn = (TextView) v.findViewById(R.id.history_doneOn);
				TextView score = (TextView) v.findViewById(R.id.history_score);

				if (name != null) {
					name.setText(task.getName());
				}

				if(description != null) {
					description.setText(task.getDescription());
				}
				
				if(doneOn != null && uTask != null) {
					DateFormat formatter = new SimpleDateFormat("HH:mm:ss    dd.MM.yyyy");;
					String s = formatter.format(uTask.getDoneOn());
					
					doneOn.setText(s);
				}
				
				if (score != null) {
	
					score.setText(Helpers.getScoreOfUserTaskGroup(uTask,groupList.get(position)));
				}
				
				//change images according to plants
				
				ImageView iv0= (ImageView) v.findViewById(R.id.history_taskCategory);
				if(iv0 != null){
					Bitmap bmp;
					try {
						bmp = BitmapFactory.decodeStream(context.getAssets().open(GardenRepositoryMetaData.PICTURE_CATEGORY_ASSETS_PATH+task.getCategoryPictureIcon()));
						iv0.setImageBitmap(bmp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
		final UserTaskGroupData taskData = groupList.get(position);
        // This stores a reference to the actual item in the checkbox
		
		if(v != null){
			final CheckBox cb = (CheckBox)v.findViewById(R.id.history_checkBox);
			cb.setChecked(true);
			
			if(cb != null){
				cb.setTag(taskData);
				 cb.setOnClickListener(new View.OnClickListener() {
				        public void onClick(View view) {
				            // This gets the correct item to work with.
				            final UserTaskGroupData clickedItem = (UserTaskGroupData) view.getTag();
				            
				            AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    		builder.setMessage(R.string.task_undone)
				    		       .setCancelable(false)
				    		       .setIcon(android.R.drawable.ic_dialog_info)
				    			   .setTitle("Task nicht erledigt?")
				    		       .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
				    		           public void onClick(DialogInterface dialog, int id) {
				    		        	   dialog.cancel();
				    		        	   cb.setChecked(true);
				    		           }
				    		       })
				    		       .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
				    		           public void onClick(DialogInterface dialog, int id) {
				    		        	   GardenContext ctx = new GardenContext(context);
				    		        	   
				    		        	   clickedItem.markUserTasksAsUnDone();
				    		        	   
				    		        	   ctx.updateUserTaskGroup(clickedItem, new ISuccess<Object>(){
				    		       			
				    		       			public void invoke(Object obj){
					
				    		       			}},
				    		       			new IFailure<Exception>(){

				    		       			public void invoke(Exception exception) {
				    		       				exception.printStackTrace();
				    		       			}
				    		       		});
				    		        	   
				    		        	   
				    		        	   frag.refreshList();
				    		        	   
				    		           }
				    		       });
				    		
				    		final AlertDialog alert = builder.create();
				            
				            alert.show();
				        }
				    });
			}
		}		
		
		return v;
	}
}
