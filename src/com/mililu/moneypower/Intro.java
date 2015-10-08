package com.mililu.moneypower;

import android.app.Activity;
import android.media.Image;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;

import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class Intro extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_intro);
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();
		
		    
	    /****** Create Thread that will sleep for 3 seconds *************/        
		Thread background = new Thread() {
			public void run() {
				try {
					// Thread will sleep for 5 seconds
					sleep(3000);
	                
					// After 5 seconds redirect to another intent
					Intent i=new Intent(getBaseContext(),LoginActivity.class);
					startActivity(i);
					
					//Remove activity
					finish();
				} 
				catch (Exception e) {
				}
			}
		};
		background.start();
	}
	///// Hàm cũ của Như
	///// public void sleep() {
	/////	/* New Handler to start the Menu-Activity 
    /////     * and close this Splash-Screen after some seconds.*/
    /////    new Handler().postDelayed(new Runnable(){
    /////        @Override
    /////        public void run() {
    /////            /* Create an Intent that will start the Menu-Activity. */
    /////            Intent mainIntent = new Intent(Intro.this,LoginActivity.class);
    /////            Intro.this.startActivity(mainIntent);
    /////           Intro.this.finish();
    /////        }
    /////    }, SPLASH_DISPLAY_LENGTH);
	/////}
	@Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
