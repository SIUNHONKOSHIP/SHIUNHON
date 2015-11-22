package com.mililu.moneypower;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticbyMonthActivity extends Activity{
	
	// Khai bao bien
	SQLiteDatabase db = null;
	Button btnBack, btnPrevious, btnNext;
	DataBaseAdapter dbAdapter;
	int id_user, mMonth, mYear, mIncome, mExpenditure;
	Cursor cursorIncome, cursorExpen;
	TextView txtTittle, txtDate, txtTotalExpenditure, txtTotalIncome;
	List <String> list_income, list_expenditure;
	ListView lvStatisticIncome, lvStatisticExpen;
	ListAdapter adapterIncome, adapterExpenditure;
	
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
        lvStatisticExpen = (ListView)findViewById(R.id.lv_statisticbymonth_expenditure);
        lvStatisticIncome = (ListView)findViewById(R.id.lv_statisticbymonth_income);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    list_income = new ArrayList<String>();
	    list_expenditure = new ArrayList<String>();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnNext.setOnClickListener(new MyEvent());
	    btnPrevious.setOnClickListener(new MyEvent());
	    
	    id_user = HomeActivity.id_user;
	    
	    GetCurrentDate();
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		getTotalIncome(mMonth, mYear, id_user);
	    getTotalExpenditure(mMonth, mYear, id_user);
	    CalcultateIncome();
	    CalcultateExpenditure();
	    setListViewHeightBasedOnChildren(lvStatisticExpen);
	    setListViewHeightBasedOnChildren(lvStatisticIncome);
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
				getTotalIncome(mMonth, mYear, id_user);
			    getTotalExpenditure(mMonth, mYear, id_user);
			    CalcultateIncome();
			    CalcultateExpenditure();
			    setListViewHeightBasedOnChildren(lvStatisticExpen);
			    setListViewHeightBasedOnChildren(lvStatisticIncome);
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
				getTotalIncome(mMonth, mYear, id_user);
			    getTotalExpenditure(mMonth, mYear, id_user);
			    CalcultateIncome();
			    CalcultateExpenditure();
			    setListViewHeightBasedOnChildren(lvStatisticExpen);
			    setListViewHeightBasedOnChildren(lvStatisticIncome);
			}
		}
	}
	
	private void getTotalIncome(int month, int year, int id_user){
		mIncome = dbAdapter.getTotalIncome(month, year, id_user);
		txtTotalIncome.setText("Toatal Income: " + NumberFormat.getCurrencyInstance().format(mIncome));
	}
	private void getTotalExpenditure(int month, int year, int id_user){
		mExpenditure = dbAdapter.getTotalExpenditure(month, year, id_user);
		txtTotalExpenditure.setText("Total Expenditure: " + NumberFormat.getCurrencyInstance().format(mExpenditure));
	}
	
	private void CalcultateIncome(){
		double balance = 0;
		list_income.clear();
		cursorIncome = dbAdapter.getListIncomeOfMonth(mMonth, mYear, id_user);
		if (cursorIncome.getCount()>0){
			
			cursorIncome.moveToFirst();
			while(!cursorIncome.isAfterLast()){
				String name = cursorIncome.getString(cursorIncome.getColumnIndexOrThrow("NAME_INCOME"));
				int id_income = cursorIncome.getInt(cursorIncome.getColumnIndexOrThrow("ID_CATEGORY"));
				balance = dbAdapter.CalculateIncomeByMonth(id_income, mMonth, mYear, id_user);
				String rate = new DecimalFormat("##.##").format(balance*100/mIncome);
				list_income.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorIncome.moveToNext();
			}
			cursorIncome.close();
			adapterIncome = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_income);
			lvStatisticIncome.setAdapter(adapterIncome);
		}
		else {
			list_income.add("NODATA");
			adapterIncome = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_income);
			lvStatisticIncome.setAdapter(adapterIncome);
		}
	}
	
	private void CalcultateExpenditure(){
		double balance = 0;
		list_expenditure.clear();
		cursorExpen = dbAdapter.getListExpenditureOfMonth(mMonth, mYear, id_user);
		if (cursorExpen.getCount()>0){
			
			cursorExpen.moveToFirst();
			while(!cursorExpen.isAfterLast()){
				String name = cursorExpen.getString(cursorExpen.getColumnIndexOrThrow("NAME_EXP"));
				int id_expend = cursorExpen.getInt(cursorExpen.getColumnIndexOrThrow("ID_PARENT_CATEGORY"));
				balance = dbAdapter.CalculateExpendByMonth(id_expend, mMonth, mYear, id_user);
				String rate = new DecimalFormat("##.##").format(balance*100/mExpenditure);
				list_expenditure.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorExpen.moveToNext();
			}
			cursorExpen.close();
			adapterExpenditure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_expenditure);
			lvStatisticExpen.setAdapter(adapterExpenditure);
		}
		else {
			list_expenditure.add("NODATA");
			adapterExpenditure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_expenditure);
			lvStatisticExpen.setAdapter(adapterExpenditure);
		}
	}
	
	public void GetCurrentDate(){
		Calendar cal=Calendar.getInstance();
		mMonth=(cal.get(Calendar.MONTH)+1);
		mYear=cal.get(Calendar.YEAR);
		txtDate.setText((mMonth)+"-"+mYear);
	}
	
	/**** Method for Setting the Height of the ListView dynamically.
	 **** Hack to fix the issue of not showing all the items of the ListView
	 **** when placed inside a ScrollView  ****/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}
}
