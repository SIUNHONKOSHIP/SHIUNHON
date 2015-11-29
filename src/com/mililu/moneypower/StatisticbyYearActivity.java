package com.mililu.moneypower;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.color;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticbyYearActivity extends Activity{
	// Khai bao bien
	SQLiteDatabase db = null;
	Button btnPrevious, btnNext;
	DataBaseAdapter dbAdapter;
	int id_user, mYear, mIncome, mExpenditure;
	Cursor cursorIncome, cursorExpen;
	TextView txtDate, txtTotalExpenditure, txtTotalIncome;
	List <String> list_income, list_expenditure;
	ListView lvStatisticIncome, lvStatisticExpen;
	ListAdapter adapterIncome, adapterExpenditure;
	private View mPieChartIncome, mPieChartExpend;
	LinearLayout chartContainerIncome, chartContainerExpend ;
	// Color of each Pie Chart Sections
	int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN,
			Color.RED, Color.GRAY, Color.YELLOW, Color.rgb(100, 50, 200) };
	private View mLineChart;
	private String[] mMonth = new String[] {"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
	};
	
	int[] income;
	int[] expense;
	

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
		
		income = new int[12];
		expense = new int[12];
	}
	
	@Override
	protected void onStart(){
		super.onStart();
			
		getTotalIncome(mYear, id_user);
		getTotalExpenditure(mYear, id_user);
		CalcultateIncome();
		CalcultateExpenditure();
		setListViewHeightBasedOnChildren(lvStatisticExpen);
		setListViewHeightBasedOnChildren(lvStatisticIncome);
		DrawLineChart();
	}
		
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_statisticbymonth_perviousmonth){
				
				mYear--;
				txtDate.setText(String.valueOf(mYear));
				getTotalIncome(mYear, id_user);
				getTotalExpenditure(mYear, id_user);
				CalcultateIncome();
				CalcultateExpenditure();
				setListViewHeightBasedOnChildren(lvStatisticExpen);
				setListViewHeightBasedOnChildren(lvStatisticIncome);
				DrawLineChart();
			}
			if (v.getId()==R.id.btn_statisticbymonth_nextmonth){
				mYear ++;
				txtDate.setText(String.valueOf(mYear));
				getTotalIncome(mYear, id_user);
				getTotalExpenditure(mYear, id_user);
				CalcultateIncome();
				CalcultateExpenditure();
				setListViewHeightBasedOnChildren(lvStatisticExpen);
				setListViewHeightBasedOnChildren(lvStatisticIncome);
				DrawLineChart();
			}
		}
	}
	
	private void getTotalIncome(int year, int id_user){
		mIncome = dbAdapter.getTotalIncome(year, id_user);
		txtTotalIncome.setText("Toatal Income: " + NumberFormat.getCurrencyInstance().format(mIncome));
	}
	private void getTotalExpenditure(int year, int id_user){
		mExpenditure = dbAdapter.getTotalExpenditure(year, id_user);
		txtTotalExpenditure.setText("Total Expenditure: " + NumberFormat.getCurrencyInstance().format(mExpenditure));
	}
		
	private void CalcultateIncome(){
		double balance = 0;
		list_income.clear();
		cursorIncome = dbAdapter.getListIncomeOfYear(mYear, id_user);
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
				balance = dbAdapter.CalculateIncomeByYear(id_income, mYear, id_user);
				MoneyIncome[count] = balance;
				String rate = new DecimalFormat("##.##").format(balance*100/mIncome);
				list_income.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorIncome.moveToNext();
				count++;
			}
				cursorIncome.close();
				adapterIncome = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_income);
				lvStatisticIncome.setAdapter(adapterIncome);
				DrawPieChart(NameIncome, MoneyIncome, chartContainerIncome, mPieChartIncome);
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
		cursorExpen = dbAdapter.getListExpenditureOfYear(mYear, id_user);
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
				balance = dbAdapter.CalculateExpendByYear(id_expend, mYear, id_user);
				MoneyExpend[count] = balance;
				String rate = new DecimalFormat("##.##").format(balance*100/mExpenditure);
				list_expenditure.add(name + ": " + NumberFormat.getCurrencyInstance().format(balance) + " (" + rate + "%)");
				cursorExpen.moveToNext();
				count++;
			}
			cursorExpen.close();
			adapterExpenditure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_expenditure);
			lvStatisticExpen.setAdapter(adapterExpenditure);
			
			DrawPieChart(NameExpend, MoneyExpend, chartContainerExpend, mPieChartExpend);
		}
		else {
			chartContainerExpend.removeAllViews(); /// delete chart
			chartContainerExpend.setLayoutParams(new LinearLayout.LayoutParams(0, 0)); // hide linear layout
			list_expenditure.add("NODATA");
			adapterExpenditure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_expenditure);
			lvStatisticExpen.setAdapter(adapterExpenditure);
		}
	}
	
	public void GetCurrentDate(){
		Calendar cal=Calendar.getInstance();
		mYear=cal.get(Calendar.YEAR);
		txtDate.setText(String.valueOf(mYear));
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
	
	private void DrawPieChart(String[] name, double[] value, LinearLayout chartContainer, View mChart) {
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
		
		defaultRenderer.setChartTitle("Statistic in " + mYear);
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
	
	private void DrawLineChart(){
		int[] x = { 0,1,2,3,4,5,6,7, 8, 9, 10, 11 };
		
		for (int i = 0; i < 12; i ++){
			income[i] = dbAdapter.getTotalIncome((i+1),mYear, id_user);
			expense[i] = dbAdapter.getTotalExpenditure((i+1), mYear, id_user);
		}
		// Creating an XYSeries for Income
		XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for(int i=0;i<x.length;i++){
		incomeSeries.add(i,income[i]);
		expenseSeries.add(i,expense[i]);
		}
		 
		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);
		 
		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.GREEN); //color of the graph set to cyan
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2f);
		incomeRenderer.setDisplayChartValues(true);
		//setting chart value distance
		incomeRenderer.setDisplayChartValuesDistance(10);
		//setting line graph point style to circle
		incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		//setting stroke of the line chart to solid
		incomeRenderer.setStroke(BasicStroke.SOLID);
		incomeRenderer.setChartValuesTextSize(15);
		 
		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.RED);
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		//setting line graph point style to circle
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		//setting stroke of the line chart to solid
		expenseRenderer.setStroke(BasicStroke.SOLID);
		expenseRenderer.setChartValuesTextSize(15);
		
		 
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		//multiRenderer.setChartTitle("Income vs Expense Chart");
		multiRenderer.setXTitle("Year " + mYear);
		multiRenderer.setLabelsColor(Color.BLACK);
		//multiRenderer.setYTitle("Amount");
		multiRenderer.setXLabelsColor(Color.BLACK);
		/***
		* Customizing graphs
		*/
		//setting text size of the title
		//multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		//multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		//multiRenderer.setLabelsTextSize(18);
		//setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(true);
		//setting pan enablity which uses graph to move on both axis
		//multiRenderer.setPanEnabled(false, false);
		//setting click false on graph
		//multiRenderer.setClickEnabled(false);
		//setting zoom to false on both axis
		//multiRenderer.setZoomEnabled(true, true);
		//setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		//setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		//setting legend to fit the screen size
		multiRenderer.setFitLegend(false);
		//setting displaying line on grid
		//multiRenderer.setShowGrid(true);
		//setting zoom to false
		//multiRenderer.setZoomEnabled(true);
		//setting external zoom functions to false
		//multiRenderer.setExternalZoomEnabled(true);
		//setting displaying lines on graph to be formatted(like using graphics)
		//multiRenderer.setAntialiasing(true);
		//setting to in scroll to false
		//multiRenderer.setInScroll(true);
		//setting to set legend height of the graph
		//multiRenderer.setLegendHeight(30);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		//setting text style
		//multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		//setting no of values to display in y axis
		multiRenderer.setYLabels(15);
		// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		//multiRenderer.setYAxisMax(4000);
		//setting used to move the graph on xaxiz to .5 to the right
		//multiRenderer.setXAxisMin(-0.5);
		//setting used to move the graph on xaxiz to .5 to the right
		//multiRenderer.setXAxisMax(11);
		//setting bar size or space between two bars
		//multiRenderer.setBarSpacing(0.5);
		//Setting background color of the graph to transparent
		//multiRenderer.setBackgroundColor(Color.WHITE);
		//Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(Color.WHITE);
		//multiRenderer.setApplyBackgroundColor(true);
		//multiRenderer.setScale(2f);
		//setting x axis point size
		//multiRenderer.setPointSize(4f);
		
		//setting the margin size for the graph in the order top, left, bottom, right
		multiRenderer.setMargins(new int[]{20, 20, 20, 20});
		
		for(int i=0; i< x.length;i++){
			multiRenderer.addXTextLabel(i, mMonth[i]);
		}
		 
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);
		 
		//this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.linechart);
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		//drawing bar chart
		mLineChart = ChartFactory.getLineChartView(StatisticbyYearActivity.this, dataset, multiRenderer);
		//adding the view to the linearlayout
		chartContainer.addView(mLineChart);
		 
		}
}