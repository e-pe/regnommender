package uni.augsburg.regnommender.presentation.fragments;

import uni.augsburg.regnommender.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * The Class SplashScreen is only used to show the splashscreen.
 */
public class SplashScreen extends Activity{
		
		/** The _splash time. */
		protected int splashTime = 3000; 
		
		/** The splash tread. */
		private Thread splashTread;

		/**
		 * Called when the activity is first created.
		 *
		 * @param savedInstanceState the saved instance state
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.splash);

		    final SplashScreen sPlashScreen = this; 

		    // thread for displaying the SplashScreen
		    splashTread = new Thread() {
		        @Override
		        public void run() {
		            try {
		            	synchronized(this){

		            		//wait15 sec
		            		wait(splashTime);
		            	}           	
		            } catch(InterruptedException e) {}
		            finally {
		                finish();

		                //start activity "MainActivity"
		                Intent i = new Intent();
		                i.setClass(sPlashScreen, MainActivity.class);
		        		startActivity(i);

		        	   SplashScreen.this.finish();
		            }
		        }
		    };
		    splashTread.start();
		}

		//Function for handling the touch event
		/* (non-Javadoc)
		 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
		 */
		@Override
		public boolean onTouchEvent(MotionEvent event) {
		    if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    	synchronized(splashTread){
		    		splashTread.notifyAll();
		    	}
		    }
		    return true;
		}
}
