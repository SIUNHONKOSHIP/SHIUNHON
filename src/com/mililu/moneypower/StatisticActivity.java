package com.mililu.moneypower;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StatisticActivity extends TabActivity {
	private TabHost mTabHost;
	private Button btnBack;
	private TabSpec monthspec, yearspec;
	
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistic);

        mTabHost = getTabHost();

        // Tab for month statistic
	    monthspec = mTabHost.newTabSpec("month");
	    monthspec.setIndicator("month", getResources().getDrawable(R.drawable.icon_info));
	    Intent monthIntent = new Intent(this, StatisticbyMonthActivity.class);
	    monthspec.setContent(monthIntent);
        
        
	    // Tab for month statistic
	    yearspec = mTabHost.newTabSpec("year");
	    yearspec.setIndicator("Year", getResources().getDrawable(R.drawable.icon_info));
	    Intent yearIntent = new Intent(this, StatisticbyYearActivity.class);
	    yearspec.setContent(yearIntent);
        
	    mTabHost.addTab(monthspec);
	    mTabHost.addTab(yearspec);
	    
	    
        // Get The Reference Of View
	    btnBack=(Button)findViewById(R.id.btn_statistic_back);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	}
   
   
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_statistic_back)
			{
				StatisticActivity.this.finish();
			}
		}
	}
}
