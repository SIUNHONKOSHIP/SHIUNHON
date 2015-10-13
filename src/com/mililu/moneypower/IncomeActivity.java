package com.mililu.moneypower;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class IncomeActivity extends Activity implements OnItemSelectedListener{
	// Spinner element
	Spinner spinnerWallet, spinnerCategoryIncome;
    Cursor walletsCursor, categoryIncomeCursor;
    DataBaseAdapter dbAdapter;
    Button btnSubmit, btnCreateCategory, btnBack, btnDate, btnTime;
    EditText txtAmount, txtNotice, txtDate, txtTime;
    int id_wallet, id_income, id_curent_user;
    String name_wallet, name_income;
    int mYear, mMonth, mDay, mHour, mMinute;
    Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        
        Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
        
        // Spinner element
        spinnerWallet = (Spinner) findViewById(R.id.spinner_wallet);
        spinnerCategoryIncome = (Spinner)findViewById(R.id.spinner_danhmuc);
        // Spinner click listener
        spinnerWallet.setOnItemSelectedListener(this);
        spinnerCategoryIncome.setOnItemSelectedListener(this);
        loadSpinnerDataWallet();
        loadSpinnerDataCategoryIncome();
        
        btnSubmit = (Button)findViewById(R.id.btn_submit_income);
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack = (Button)findViewById(R.id.btn_incomebacktohome);
        btnBack.setOnClickListener(new MyEvent());
        btnCreateCategory = (Button)findViewById(R.id.btn_createcategoryincome);
        btnCreateCategory.setOnClickListener(new MyEvent());
        btnDate = (Button)findViewById(R.id.btn_income_date);
        btnDate.setOnClickListener(new MyEvent());
        btnTime = (Button)findViewById(R.id.btn_income_time);
        btnTime.setOnClickListener(new MyEvent());
        
        txtAmount = (EditText)findViewById(R.id.txt_income_amount);
        txtNotice = (EditText)findViewById(R.id.txt_income_notice);
        txtDate = (EditText)findViewById(R.id.txt_income_date);
        txtTime = (EditText)findViewById(R.id.txt_income_hour);
        setCurrentDate();
        setCurrentTime();
    }
    /**
	 * thiet lap ngay thang nam hien tai
	 */
	 public void setCurrentDate(){
		 Calendar cal=Calendar.getInstance();
		 mDay=cal.get(Calendar.DAY_OF_MONTH);
		 mMonth=cal.get(Calendar.MONTH);
		 mYear=cal.get(Calendar.YEAR);
		 txtDate.setText(mDay+"-"+(mMonth+1)+"-"+mYear);
	 }
    /**
	 * thiet lap gio hien tai
	 */
	 public void setCurrentTime(){
		 Calendar cal=Calendar.getInstance();
		 mHour=cal.get(Calendar.HOUR);
		 mMinute=cal.get(Calendar.MINUTE);
		 txtTime.setText(mHour+":"+(mMinute));
	 }
    /**
     * Function to load the spinner data from SQLite database
     * */    
    private void loadSpinnerDataWallet() {
    	//Toast.makeText(getApplicationContext(), "ID: " + id_curent_user, Toast.LENGTH_LONG).show();
    	dbAdapter = new DataBaseAdapter(getApplicationContext());

        // Spinner Drop down cursor
        walletsCursor = dbAdapter.getListWalletOfUser(id_curent_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_WALLET" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,walletsCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerWallet.setAdapter(dataAdapter);
        
    }
    /**
     * Load category income into spinner
     */
    private void loadSpinnerDataCategoryIncome() {
    	dbAdapter = new DataBaseAdapter(getApplicationContext());
        // Spinner Drop down cursor
        categoryIncomeCursor = dbAdapter.getCategoryIncomeCursor();
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_INCOME" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              categoryIncomeCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerCategoryIncome.setAdapter(dataAdapter);
    }
    
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_submit_income)
			{
				DoInsertIncome();
			}
			else if(v.getId() == R.id.btn_incomebacktohome){
				IncomeActivity.this.finish();
			}
			else if(v.getId() == R.id.btn_income_date){
				showDialog(1);
			}
			else if(v.getId() == R.id.btn_income_time){
				showDialog(2);
			}
		}
	}
    
    private void DoInsertIncome(){
    	String mAmount = txtAmount.getText().toString();
    	String mNotice = txtNotice.getText().toString();
    	String mDate = txtDate.getText().toString();
    	String mTime = txtTime.getText().toString();
    	if(mAmount.equals("")||mDate.equals("")||mTime.equals("")){
    		Toast.makeText(getApplicationContext(), "Please insert amount, date and hour", Toast.LENGTH_LONG).show();
    	}
    	else {
    		int curentmoney = dbAdapter.getAmountOfWallet(String.valueOf(id_wallet));
    		int newmoney = curentmoney + Integer.valueOf(mAmount);
    		dbAdapter.insertDiaryIncome(Integer.valueOf(mAmount), id_wallet, id_income, mDate, mTime, mNotice);
    		dbAdapter.updateWallet(id_wallet, name_wallet, newmoney);
    		dbAdapter.close();
    		//Toast.makeText(getApplicationContext(), dbAdapter.getAmountOfWallet(String.valueOf(id_wallet)), Toast.LENGTH_LONG).show();
    		ClearTextBox();
    	}
    }
    
    private void ClearTextBox(){
    	txtAmount.setText("");
    	txtNotice.setText("");
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spinner_wallet)
		{
			walletsCursor.moveToPosition(position);
			id_wallet = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
			name_wallet = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("NAME_WALLET"));
		}
		else if(spinner.getId() == R.id.spinner_danhmuc)
		{
			categoryIncomeCursor.moveToPosition(position);
			id_income = categoryIncomeCursor.getInt(categoryIncomeCursor.getColumnIndexOrThrow("_id"));
			name_income = categoryIncomeCursor.getString(categoryIncomeCursor.getColumnIndexOrThrow("NAME_INCOME"));
		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    
	/**
	 * xu ly DatePickerDialog
	 */
	 private DatePickerDialog.OnDateSetListener dateChange = new OnDateSetListener() {
		 @Override
		 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			 // TODO Auto-generated method stub
			 mYear=year;
			 mMonth=monthOfYear;
			 mDay=dayOfMonth;
			 txtDate.setText(mDay+"-"+(mMonth+1)+"-"+mYear);
		 }
	 };
	 private TimePickerDialog.OnTimeSetListener timeChange = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			txtTime.setText(mHour + ":" + mMinute);
		}
	};
	 
	 @Override
	 protected Dialog onCreateDialog(int id) {
		 // TODO Auto-generated method stub
		 if(id==1){
			 return new DatePickerDialog(this, dateChange, mYear, mMonth, mDay);
		 }
		 else if(id==2){
			 return new TimePickerDialog(this, timeChange, mHour, mMinute, false );
		 }
		 return null;
	 }
}
