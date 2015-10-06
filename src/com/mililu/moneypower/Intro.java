package com.mililu.moneypower;

import android.app.Activity;
import android.media.Image;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;

import android.os.Handler;

public class Intro extends Activity {
	
	private final int SPLASH_DISPLAY_LENGTH = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_intro);
	ActionBar actionBar = getActionBar();
	actionBar.hide();
		
//	sleep();
	}
	
	public void sleep() {
		/* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Intro.this,LoginActivity.class);
                Intro.this.startActivity(mainIntent);
                Intro.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
	}

}
