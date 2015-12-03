package com.mililu.moneypower;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticbyMonthActivity extends Activity{
	
	// Khai bao bien
	SQLiteDatabase db = null;
	Button btnPrevious, btnNext;
	DataBaseAdapter dbAdapter;
	int id_user, mMonth, mYear;
	long mIncome, mExpenditure;
	Cursor cursorIncome, cursorExpen;
	TextView txtDate, txtTotalExpenditure, txtTotalIncome;
	List <String> list_income, list_expenditure;
	ListView lvStatisticIncome, lvStatisticExpen;
	ListAdapter adapterIncome, adapterExpenditure;
	private View mChartIncome, mChartExpend;
	LinearLayout chartContainerIncome, chartContainerExpend, yearlayout ;
	
	// Color of each Pie Chart Sections
	int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN,
					Color.RED, Color.GRAY, Color.YELLOW, Color.rgb(100, 50, 200) };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_statisticbymonth);
		
		// Get The Reference Of View
	    txtDate=(TextView)findViewById(R.id.tv_statisticbymonth_date);
	    txtTotalIncome=(TextView)findViewById(R.id.tv_statisticbymonth_totalincome);
	    txtTotalExpenditure=(TextView)findViewById(R.id.tv_statisticbymonth_totalexpenditure);
        btnNext=(Button)findViewById(R.id.btn_statisticbymonth_nextmonth);
        btnPrevious=(Button)findViewById(R.id.btn_statisticbymonth_perviousmonth);
        lvStatisticExpen = (ListView)findViewById(R.id.lv_statisticbymonth_expenditure);
        lvStatisticIncome = (ListView)findViewById(R.id.lv_statisticbymonth_income);
        chartContainerIncome = (LinearLayout) findViewById(R.id.chart_income);
        chartContainerExpend = (LinearLayout) findViewById(R.id.chart_expend);
        yearlayout = (LinearLayout)findViewById(R.id.year_layout);
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    list_income = new ArrayList<String>();
	    list_expenditure = new ArrayList<String>();
	    
	    // Set OnClick Listener on button 
	    btnNext.setOnClickListener(new MyEvent());
	    btnPrevious.setOnClickListener(new MyEvent());
	    
	    id_user = HomeActivity.id_user;
	    
	    GetCurrentDate();
	    yearlayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0)); // hide year layout 
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
			// Pie Chart Section Names
			String[] NameIncome = new String[cursorIncome.getCount()];
			// Pie Chart Section Value
			double[] MoneyIncome = new double[cursorIncome.getCount()];
			
			int count = 0;
			
			cursorIncome.moveToFirst();
			while(!cursorIncome.isAfterLast()){
				String name = cursorIncome.getString(cursorIncome.getColumnIndexOrThrow("NAME_INCOME"));
				NameIncome[count] = name;
				int id_income = cursorIncome.getInt(cursorIncome.getColumnIndexOrThrow("ID_CATEGORY"));
				balance = dbAdapter.CalculateIncomeByMonth(id_income, mMonth, mYear, id_user);
				MoneyIncome[count] = balance;
				String rate = new DecimalFormat("##.##").format(balance*100/mIncome);
				list_income.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorIncome.moveToNext();
				count++;
			}
			cursorIncome.close();
			adapterIncome = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_income);
			lvStatisticIncome.setAdapter(adapterIncome);
			
			//// SET up CHART
			DrawChart(NameIncome, MoneyIncome, chartContainerIncome, mChartIncome);
			
		}
		else {
			// remove any views before u paint the chart
			chartContainerIncome.removeAllViews();
			chartContainerIncome.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			
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
			
			// Pie Chart Section Names
			String[] NameExpend = new String[cursorExpen.getCount()];
			// Pie Chart Section Value
			double[] MoneyExpend = new double[cursorExpen.getCount()];
			
			int count = 0;
			
			cursorExpen.moveToFirst();
			while(!cursorExpen.isAfterLast()){
				String name = cursorExpen.getString(cursorExpen.getColumnIndexOrThrow("NAME_EXP"));
				NameExpend[count] = name;
				int id_expend = cursorExpen.getInt(cursorExpen.getColumnIndexOrThrow("ID_PARENT_CATEGORY"));
				balance = dbAdapter.CalculateExpendByMonth(id_expend, mMonth, mYear, id_user);
				MoneyExpend[count] = balance;
				String rate = new DecimalFormat("##.##").format(balance*100/mExpenditure);
				list_expenditure.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorExpen.moveToNext();
				count++;
			}
			cursorExpen.close();
			adapterExpenditure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_expenditure);
			lvStatisticExpen.setAdapter(adapterExpenditure);
			
			/// set up chart
			DrawChart(NameExpend, MoneyExpend, chartContainerExpend, mChartExpend);
			
		}
		else {
			
			chartContainerExpend.removeAllViews();
			chartContainerExpend.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
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
	
	private void DrawChart(String[] name, double[] value, LinearLayout chartContainer, View mChart) {
		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries(
				" Android version distribution as on October 1, 2012");
		for (int i = 0; i < value.length; i++) {
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(name[i], value[i]);
		}
		
		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		for (int i = 0; i < value.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			//seriesRenderer.setDisplayBoundingPoints(true); //.setDisplayChartValues(true);
			//Adding colors to the chart
			defaultRenderer.setBackgroundColor(Color.BLACK);
			defaultRenderer.setApplyBackgroundColor(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
		
		defaultRenderer.setChartTitle("Statistic in " + mMonth + "/" + mYear);
		defaultRenderer.setChartTitleTextSize(20);
		defaultRenderer.setLabelsTextSize(15);
		defaultRenderer.setZoomButtonsVisible(true);
		
		// this part is used to display graph on the xml
		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		// Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
		// distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");
		
		// Start Activity
		// startActivity(intent);
		
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		chartContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300));
		// drawing pie chart
		mChart = ChartFactory.getPieChartView(getBaseContext(),
				distributionSeries, defaultRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);
		
	}
}