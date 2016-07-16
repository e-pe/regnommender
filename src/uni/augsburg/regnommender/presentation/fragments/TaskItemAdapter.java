package uni.augsburg.regnommender.presentation.fragments;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import uni.augsburg.regnommender.R;
import uni.augsburg.regnommender.businessLogic.TaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskData;
import uni.augsburg.regnommender.businessLogic.UserTaskGroupData;
import uni.augsburg.regnommender.core.IFailure;
import uni.augsburg.regnommender.core.ISuccess;
import uni.augsburg.regnommender.core.MiscValues;
import uni.augsburg.regnommender.dataAccess.GardenRepositoryMetaData;
import uni.augsburg.regnommender.presentation.GardenContext;
import uni.augsburg.regnommender.presentation.Helpers;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class TaskItemAdapter is used by the TaskFragment (@see TaskFragment)
 * and can be extended to filter these tasks, for example introducing attributes
 * like "priority", "amount of work", etc..
 */
public class TaskItemAdapter extends ArrayAdapter<UserTaskGroupData> {
	
	/** The group list. */
	private ArrayList<UserTaskGroupData> groupList;
	
	/** The context. */
	private Context context;
	
	/** The frag. */
	TaskFragment frag;
	
	HashSet <Integer> bitmapsForPositionLoaded = new HashSet<Integer>();
	ArrayList<Bitmap> loadedAdapterBitmaps;
	

	
	/**
	 * Instantiates a new task item adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param groupList the group list
	 * @param frag the fragment
	 */
	public TaskItemAdapter(Context context, int textViewResourceId, ArrayList<UserTaskGroupData> groupList, ArrayList<Bitmap> loadedAdapterBitmaps, TaskFragment frag) {
		super(context, textViewResourceId, groupList);
		this.context=context;
		this.groupList = groupList;
		this.frag = frag;
		
		this.loadedAdapterBitmaps = loadedAdapterBitmaps;
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
				v = vi.inflate(R.layout.listitem, null);
			
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
				TextView name = (TextView) v.findViewById(R.id.name);
				TextView description = (TextView) v.findViewById(R.id.description);
				TextView addedOn = (TextView) v.findViewById(R.id.task_addedOn);
				TextView score = (TextView) v.findViewById(R.id.task_score);

				if (name != null) {
					name.setText(task.getName());
				}

				if(description != null) {
					description.setText(task.getDescription());
				}
				
				if(addedOn != null) {
					DateFormat formatter = new SimpleDateFormat("HH:mm:ss\ndd.MM.yyyy");
					String s = formatter.format(uTask.getAddedOn());
					addedOn.setText(s);
				}
				
				if (score != null) {
					score.setText(Helpers.getScoreOfUserTaskGroup(uTask,groupList.get(position)));
				}
				
				//change images according to plants
				
				ImageView iv0= (ImageView) v.findViewById(R.id.taskCategory);
				if(iv0 != null){
					Bitmap bmp;
					try {
						bmp = BitmapFactory.decodeStream(context.getAssets().open(GardenRepositoryMetaData.PICTURE_CATEGORY_ASSETS_PATH+task.getCategoryPictureIcon()));
						iv0.setImageBitmap(bmp);
						
						if (!bitmapsForPositionLoaded.contains(position)) loadedAdapterBitmaps.add(bmp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				ImageView iv_array[] = {
					(ImageView) v.findViewById(R.id.taskPlant0),
					(ImageView) v.findViewById(R.id.taskPlant1),
					(ImageView) v.findViewById(R.id.taskPlant2),
					(ImageView) v.findViewById(R.id.taskPlant3),
					(ImageView) v.findViewById(R.id.taskPlant4),
				};
				
				//set plant pictures in ui
				if(task.getIsPlantTask())
				{
					try {
							Iterator<UserTaskData> plantUserTasks= groupList.get(position).getUserTasks();
		
							for(int i= 0; i < iv_array.length; i++ )
							{
									if (plantUserTasks.hasNext()) {
										UserTaskData ut = plantUserTasks.next();
										
										/*Bitmap bmp = BitmapFactory.decodeStream(
													context.getAssets().open(GardenRepositoryMetaData.PICTURE_PLANTS_ASSETS_PATH
															+ut.getPlant().getImageUrl1()
															));
										*/
										Bitmap bmp = Helpers.decodeSampledBitmap(100, 120, context, GardenRepositoryMetaData.PICTURE_PLANTS_ASSETS_PATH+ut.getPlant().getImageUrl1());
										
										if (!bitmapsForPositionLoaded.contains(position)) loadedAdapterBitmaps.add(bmp);
										iv_array[iv_array.length-1-i].setVisibility(View.VISIBLE);
										iv_array[iv_array.length-1-i].setImageBitmap(bmp);
									}
									else {
										iv_array[iv_array.length-1-i].setVisibility(View.INVISIBLE);
									}
							}
						} catch (Exception e1) {
							Log.e("TaskItemAdapter",e1.getMessage());
							e1.printStackTrace();
						}
				}
				else {
					for(int i= 0; i < iv_array.length; i++ )
						iv_array[i].setVisibility(View.GONE);
				}
			}
			
			if (!bitmapsForPositionLoaded.contains(position))
				bitmapsForPositionLoaded.add(position);
		}
		
		final UserTaskGroupData taskData = groupList.get(position);
        // This stores a reference to the actual item in the checkbox
        
		// priority display
		try {
			float highestPriority = 0;
			Iterator<UserTaskData> uTaskIt = taskData.getUserTasks();
			while(uTaskIt.hasNext()) {
				UserTaskData uTask = uTaskIt.next();
				if(uTask.getPriority() > highestPriority) {
					highestPriority = uTask.getPriority();
				}
			}
			//int priorityValue = (int) (highestPriority*100);
			ImageView priorityImage = (ImageView)v.findViewById(R.id.priorityImage);
			//TextView priorityText = (TextView)v.findViewById(R.id.priorityValue);
			if (highestPriority > MiscValues.priority_veryhigh) {
				priorityImage.setImageResource(R.drawable.priority_progress_3);
				//priorityText.setText("" + priorityValue);
				//priorityText.setTextColor(Color.RED);
				
			}
			else if (highestPriority > MiscValues.priority_high) {
				priorityImage.setImageResource(R.drawable.priority_progress_2);
				//priorityText.setText("" + priorityValue);
				//priorityText.setTextColor(Color.YELLOW);
			}
			else {
				priorityImage.setImageResource(R.drawable.priority_progress_1);
				//priorityText.setText("" + priorityValue);
				//priorityText.setTextColor(Color.GREEN);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(v != null){
			final CheckBox cb = (CheckBox)v.findViewById(R.id.checkBox1);
			 cb.setChecked(false);
			
			if(cb != null){
				cb.setTag(taskData);
				 cb.setOnClickListener(new View.OnClickListener() {
				        public void onClick(View view) {
				            // This gets the correct item to work with.
				            final UserTaskGroupData clickedItem = (UserTaskGroupData) view.getTag();
				            //System.out.println(clickedItem.getName());
				            
				            AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    		builder.setMessage(R.string.task_done)
				    			   .setIcon(android.R.drawable.ic_dialog_info)
				    			   .setTitle("Task erledigt?")
				    		       .setCancelable(false)
				    		       .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
				    		           public void onClick(DialogInterface dialog, int id) {
				    		        	   cb.setChecked(false);
				    		        	   dialog.cancel();
				    		           }
				    		       })
				    		       .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
				    		           public void onClick(DialogInterface dialog, int id) {
				    		        	   GardenContext ctx = new GardenContext(context);
				    		        	   
				    		        	   clickedItem.markUserTasksAsDone();
				    		        	   
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
