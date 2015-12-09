package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class StatisticActivity2 extends Activity implements OnItemSelectedListener{
	// Khai bao bien
	Button btnPrevious, btnNext, btnBack;
	DataBaseAdapter dbAdapter;
	int id_user, mMonth, mYear;
	long mIncome, mExpenditure;
	TextView txtDate, txtTotalExpenditure, txtTotalIncome;
	private View mChart;
	LinearLayout chartContainer;
	ListAdapter adapterIncome, adapterExpenditure;
	String typeStatistic[]={"Statistic By Month", "Statistic by Year"};
	
	private String[] ListMonth = new String[] {"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"};
	Spinner spnTypeStatistic, spnTypeChart;
	String[] NameIncome, NameExpense;
	double[] MoneyIncome, MoneyExpense;
	double[] income;
	double[] expense;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistic2);
        
        // Get The Reference Of View
	    txtDate=(TextView)findViewById(R.id.tv_statistic2_date);
	    txtTotalIncome=(TextView)findViewById(R.id.tv_statistic2_totalincome);
	    txtTotalExpenditure=(TextView)findViewById(R.id.tv_statistic2_totalexpenditure);
        btnNext=(Button)findViewById(R.id.btn_statistic2_next);
        btnPrevious=(Button)findViewById(R.id.btn_statistic2_previous);
        btnBack = (Button)findViewById(R.id.btn_statistic2_back);
        spnTypeChart = (Spinner)findViewById(R.id.spn_statistic2_typechart);
        spnTypeStatistic = (Spinner)findViewById(R.id.spn_statistic2_typestatistic);
        chartContainer = (LinearLayout) findViewById(R.id.layout_statistic2_chart);
		
        // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Spinner click listener
	    spnTypeStatistic.setOnItemSelectedListener(this);
	    spnTypeChart.setOnItemSelectedListener(this);
	    loadSpnTypeStatistic();
	    loadSpnTypeChart();
	    // Set OnClick Listener on button 
	    btnNext.setOnClickListener(new MyEvent());
	    btnPrevious.setOnClickListener(new MyEvent());
	    btnBack.setOnClickListener(new MyEvent());
	    id_user = HomeActivity.id_user;
		GetCurrentDate();
	}
	
    private void loadSpnTypeStatistic() {
        // Creating adapter for spinner
        ArrayAdapter<String> dataStatisticAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeStatistic);

        //dataStatisticAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // attaching data adapter to spinner
        spnTypeStatistic.setAdapter(dataStatisticAdapter);
    }
    
    private void loadSpnTypeChart() {
    	// Creating adapter for spinner
    	String typeChart[] = {};
    	if (spnTypeStatistic.getSelectedItemPosition() == 0){
    		typeChart = new String[] {"Income", "Expense"};
    	}
    	else if(spnTypeStatistic.getSelectedItemPosition() == 1){
    		typeChart = new String[] {"Income", "Expense", "Genaral"};
    	}
    	ArrayAdapter<String> dataChartAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeChart);
        //dataChartAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // attaching data adapter to spinner
        spnTypeChart.setAdapter(dataChartAdapter);
    }
	
	private void getTotalIncome(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mIncome = dbAdapter.getTotalIncome(mMonth, mYear, id_user);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mIncome = dbAdapter.getTotalIncome(mYear, id_user);
		}
		txtTotalIncome.setText("Toatal Income: " + NumberFormat.getCurrencyInstance().format(mIncome));
		
	}
	private void getTotalExpenditure(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mExpenditure = dbAdapter.getTotalExpenditure(mMonth, mYear, id_user);
		}
		else if(spnTypeStatistic.getSelectedItemPosition() == 1){
			mExpenditure = dbAdapter.getTotalExpenditure(mYear, id_user);
		}
		txtTotalExpenditure.setText("Total Expenditure: " + NumberFormat.getCurrencyInstance().format(mExpenditure));
	}
	
	private void CalcultateIncome(){
			double balance = 0;
			Cursor cursorIncome = null;
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				cursorIncome = dbAdapter.getListIncomeOfMonth(mMonth, mYear, id_user);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				cursorIncome = dbAdapter.getListIncomeOfYear(mYear, id_user);
			}
			if (cursorIncome.getCount()>0){
				// Pie Chart Section Names
				NameIncome = new String[cursorIncome.getCount()];
				// Pie Chart Section Value
				MoneyIncome = new double[cursorIncome.getCount()];
				
				int count = 0;
				
				/// Set up data for chart
				cursorIncome.moveToFirst();
				while(!cursorIncome.isAfterLast()){
					String name = cursorIncome.getString(cursorIncome.getColumnIndexOrThrow("NAME_INCOME"));
					NameIncome[count] = name;
					int id_income = cursorIncome.getInt(cursorIncome.getColumnIndexOrThrow("ID_CATEGORY"));
					if (spnTypeStatistic.getSelectedItemPosition() == 0){
						balance = dbAdapter.CalculateIncomeByMonth(id_income, mMonth, mYear, id_user);
					}
					else if (spnTypeStatistic.getSelectedItemPosition() == 1){
						balance = dbAdapter.CalculateIncomeByYear(id_income, mYear, id_user);
					}
					MoneyIncome[count] = balance;
					cursorIncome.moveToNext();
					count++;
				}
				cursorIncome.close();
				// Draw chart
				DrawBarChart(NameIncome, MoneyIncome, chartContainer, mChart);
			}
			else {
				// remove any views before u paint the chart
				chartContainer.removeAllViews();
				chartContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			}
	}
	
	private void CalcultateExpense(){
			double balance = 0;
			Cursor cursorExpen = null;
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				cursorExpen = dbAdapter.getListExpenditureOfMonth(mMonth, mYear, id_user);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				cursorExpen = dbAdapter.getListExpenditureOfYear(mYear, id_user);
			}
			if (cursorExpen.getCount()>0){
				// Pie Chart Section Names
				NameExpense = new String[cursorExpen.getCount()];
				// Pie Chart Section Value
				MoneyExpense = new double[cursorExpen.getCount()];
				
				int count = 0;
				
				/// Set up data for chart
				cursorExpen.moveToFirst();
				while(!cursorExpen.isAfterLast()){
					String name = cursorExpen.getString(cursorExpen.getColumnIndexOrThrow("NAME_EXP"));
					NameExpense[count] = name;
					int id_expend = cursorExpen.getInt(cursorExpen.getColumnIndexOrThrow("ID_PARENT_CATEGORY"));
					if (spnTypeStatistic.getSelectedItemPosition() == 0){
						balance = dbAdapter.CalculateExpendByMonth(id_expend,mMonth, mYear, id_user);
					}
					else if (spnTypeStatistic.getSelectedItemPosition() == 1){
						balance = dbAdapter.CalculateExpendByYear(id_expend, mYear, id_user);
					}
					
					MoneyExpense[count] = balance;
					cursorExpen.moveToNext();
					count++;
				}
				cursorExpen.close();
				// Draw chart
				DrawBarChart(NameExpense, MoneyExpense, chartContainer, mChart);
			}
			else {
				// remove any views before u paint the chart
				chartContainer.removeAllViews();
				chartContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
				
			}
	}
	
	public void GetCurrentDate(){
		Calendar cal=Calendar.getInstance();
		// by month 
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mMonth=(cal.get(Calendar.MONTH)+1);
			mYear=cal.get(Calendar.YEAR);
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear=cal.get(Calendar.YEAR);
			txtDate.setText(String.valueOf(mYear));
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_statistic2_typestatistic)
		{
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				txtDate.setText(mMonth +"/"+mYear);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				txtDate.setText(String.valueOf(mYear));
			}
			getTotalIncome();
			getTotalExpenditure();
			loadSpnTypeChart();
		}
		if(spinner.getId() == R.id.spn_statistic2_typechart)
		{
				if (spnTypeChart.getSelectedItemPosition() == 0){
					CalcultateIncome();
				}
				else if (spnTypeChart.getSelectedItemPosition() == 1){
					CalcultateExpense();
				}
				else if (spnTypeChart.getSelectedItemPosition() == 2){
					DrawLineChart();
				}

		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_statistic2_back){
				StatisticActivity2.this.finish();
			}
			if(v.getId()==R.id.btn_statistic2_previous){
				setPreviousDate();
				getTotalIncome();
			    getTotalExpenditure();
			    if (spnTypeChart.getSelectedItemPosition() == 0){
			    	CalcultateIncome();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 1){
			    	CalcultateExpense();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 2){
			    	DrawLineChart();
			    }
			}
			if (v.getId()==R.id.btn_statistic2_next){
				setNextDate();
				getTotalIncome();
			    getTotalExpenditure();
			    if (spnTypeChart.getSelectedItemPosition() == 0){
			    	CalcultateIncome();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 1){
			    	CalcultateExpense();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 2){
			    	DrawLineChart();
			    }
			}
		}
	}
	
	private void setPreviousDate(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			if ((mMonth < 13) && (mMonth > 1)){
				mMonth -= 1;
			}
			else if (mMonth == 1) {
				mMonth = 12;
				mYear --;
			}
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear--;
			txtDate.setText(String.valueOf(mYear));
		}
	}
	private void setNextDate(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			if ((mMonth < 12) && (mMonth > 0)){
				mMonth += 1;
			}
			else if (mMonth == 12){
				mMonth = 1;
				mYear ++;
			}
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear ++;
			txtDate.setText(String.valueOf(mYear));
		}
	}
	
	private void DrawBarChart(String[] name, double[] value, LinearLayout chartContainer, View mChart){
		double YaxixMax = 0;
		for (int i = 0; i < value.length; i++){
			if ( YaxixMax < value[i]){
				YaxixMax = value[i];
			}
		}
		// Creating an XYSeries for Income
		XYSeries valueSeries = null;
		if (spnTypeChart.getSelectedItemPosition() == 0){
			valueSeries = new XYSeries("Income");
		}
		else if (spnTypeChart.getSelectedItemPosition() == 1){
			valueSeries = new XYSeries("Expense");
		}
		// Adding data to Series
		for(int i=0;i<name.length;i++){
			valueSeries.add(i,value[i]);
		}
		
		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(valueSeries);
		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer valueRenderer = new XYSeriesRenderer();
		if (spnTypeChart.getSelectedItemPosition() == 0){
			valueRenderer.setColor(Color.GREEN); //color of the graph set to cyan
		}
		else if (spnTypeChart.getSelectedItemPosition() == 1){
			valueRenderer.setColor(Color.RED); //color of the graph set to cyan
		}
		valueRenderer.setFillPoints(true);
		valueRenderer.setLineWidth(10f);
		valueRenderer.setDisplayChartValues(true);
		valueRenderer.setDisplayChartValuesDistance(5); //setting chart value distance
		valueRenderer.setChartValuesTextSize(20f);
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		multiRenderer.setXLabels(0);
		
		/***
		 * Customizing graphs
		 */
		//setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		multiRenderer.setLabelsTextSize(20);
		//setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		//setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, true);
		//setting click false on graph
		multiRenderer.setClickEnabled(false);
		//setting zoom to false on both axis
		multiRenderer.setZoomEnabled(true, true);
		//setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		//setting lines to display on x axis
		multiRenderer.setShowGridX(false);
		//setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		//setting displaying line on grid
		multiRenderer.setShowGrid(false);
		//setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(true);
		//setting displaying lines on graph to be formatted(like using graphics)
		multiRenderer.setAntialiasing(true);
		//setting to in scroll to false
		multiRenderer.setInScroll(true);
		//setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		multiRenderer.setLegendTextSize(20f);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		//setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setYAxisMax(YaxixMax*1.2);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-1);
		//setting max values to be display in x axis
		multiRenderer.setXAxisMax(name.length);
		multiRenderer.setBarSpacing(1f);
		multiRenderer.setBarWidth(50f);
		multiRenderer.setMarginsColor(Color.WHITE);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setMargins(new int[]{20, 20, 20, 20});
		
		for(int i=0; i< name.length;i++){
			multiRenderer.addXTextLabel(i, name[i]);
		}
		
		// Adding valuesRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(valueRenderer);
		
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		chartContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//drawing bar chart
		mChart = ChartFactory.getBarChartView(StatisticActivity2.this, dataset, multiRenderer,Type.STACKED);
		
		//adding the view to the linearlayout
		chartContainer.addView(mChart);
	}
	
	private void DrawLineChart() {
		income = new double[12];
		expense = new double[12];
		
		for (int i = 0; i < 12; i ++){
			income[i] = dbAdapter.getTotalIncome((i+1),mYear, id_user);
			expense[i] = dbAdapter.getTotalExpenditure((i+1), mYear, id_user);
		}
		
		double YaxixMax = 0;
		for (int i = 0; i < ListMonth.length; i++){
			if (YaxixMax < income[i]){
				YaxixMax = income[i];
			}
			if (YaxixMax < expense[i]){
				YaxixMax = expense[i];
			}
		}
		// Creating an XYSeries for Income
		XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for(int i=0;i<ListMonth.length;i++){
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
		multiRenderer.setChartTitle("Income vs Expense Chart " + "(" + mYear + ")");
		multiRenderer.setYTitle("Amount");
		/***
		 * Customizing graphs
		 */
		//setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		multiRenderer.setLabelsTextSize(18);
		//setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		//setting pan enablity which uses graph to move on both axis
		//multiRenderer.setPanEnabled(false, false);
		//setting click false on graph
		multiRenderer.setClickEnabled(false);
		//setting zoom to false on both axis
		multiRenderer.setZoomEnabled(true, true);
		//setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		//setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		//setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
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
		multiRenderer.setLegendHeight(30);
		multiRenderer.setLegendTextSize(18);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		//setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		//setting no of values to display in y axis
		multiRenderer.setYLabels(15);
		// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(YaxixMax*1.1);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-1);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(ListMonth.length);
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
		multiRenderer.setMargins(new int[]{50, 30, 20, 20});
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		for(int i=0; i< ListMonth.length;i++){
			multiRenderer.addXTextLabel(i, ListMonth[i]);
		}
		
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);
		
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		//drawing bar chart
		mChart = ChartFactory.getLineChartView(StatisticActivity2.this, dataset, multiRenderer);
		//adding the view to the linearlayout
		chartContainer.addView(mChart);
			
	}
}