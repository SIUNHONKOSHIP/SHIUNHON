package com.mililu.moneypower;


import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StatisticbyMonthActivity extends Activity{
	
	// Khai bao bien
	SQLiteDatabase db = null;
	Button btnBack, btnPrevious, btnNext;
	DataBaseAdapter dbAdapter;
	int id_curent_user, mMonth, mYear;
	TextView txtTittle, txtDate, txtTotalExpenditure, txtTotalIncome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_statisticbymonth);
		
		// Get The Reference Of View
	    txtTittle=(TextView)findViewById(R.id.tv_statisticbymonth_title);
	    txtDate=(TextView)findViewById(R.id.tv_statisticbymonth_date);
	    txtTotalIncome=(TextView)findViewById(R.id.tv_statisticbymonth_totalincome);
	    txtTotalExpenditure=(TextView)findViewById(R.id.tv_statisticbymonth_totalexpenditure);
	    btnBack=(Button)findViewById(R.id.btn_statisticbymonth_back);
        btnNext=(Button)findViewById(R.id.btn_statisticbymonth_nextmonth);
        btnPrevious=(Button)findViewById(R.id.btn_statisticbymonth_perviousmonth);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnNext.setOnClickListener(new MyEvent());
	    btnPrevious.setOnClickListener(new MyEvent());
	    
	    // Get data of Bundle
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
	    
	    setCurrentDate();
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		getTotalIncome(mMonth, mYear, id_curent_user);
	    getTotalExpenditure(mMonth, mYear, id_curent_user);
	}
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_statisticbymonth_back){
				StatisticbyMonthActivity.this.finish();
			}
			if(v.getId()==R.id.btn_statisticbymonth_perviousmonth){
				if ((mMonth < 13) && (mMonth > 1)){
					mMonth -= 1;
				}
				else if (mMonth == 1) {
					mMonth = 12;
					mYear --;
				}
				txtDate.setText((mMonth)+"-"+mYear);
				getTotalIncome(mMonth, mYear, id_curent_user);
			    getTotalExpenditure(mMonth, mYear, id_curent_user);
			}
			if (v.getId()==R.id.btn_statisticbymonth_nextmonth){
				if ((mMonth < 12) && (mMonth > 0)){
					mMonth += 1;
				}
				else if (mMonth == 12){
					mMonth = 1;
					mYear ++;
				}
				txtDate.setText((mMonth)+"-"+mYear);
				getTotalIncome(mMonth, mYear, id_curent_user);
			    getTotalExpenditure(mMonth, mYear, id_curent_user);
			}
		}
	}
	
	private void getTotalIncome(int month, int year, int id_user){
		int mIncome = dbAdapter.getTotalIncome(month, year, id_user);
		txtTotalIncome.setText("Toatal Income: " + mIncome);
	}
	private void getTotalExpenditure(int month, int year, int id_user){
		int mExpen = dbAdapter.getTotalExpenditure(month, year, id_user);
		txtTotalExpenditure.setText("Total Expenditure: " + mExpen);
	}
	
	public void setCurrentDate(){
		Calendar cal=Calendar.getInstance();
		mMonth=(cal.get(Calendar.MONTH)+1);
		mYear=cal.get(Calendar.YEAR);
		txtDate.setText((mMonth)+"-"+mYear);
	}
}
