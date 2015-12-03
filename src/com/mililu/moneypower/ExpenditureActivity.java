package com.mililu.moneypower;

import java.util.Calendar;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ExpenditureActivity extends Activity implements OnItemSelectedListener {
	// Spinner element
	Spinner spnWallet, spnCategoryExpen, spnCategoryExpenDetail;
    Cursor walletsCursor, categoryExpenCursor, categoryExpenDetailCursor;
    DataBaseAdapter dbAdapter;
    Button btnSubmit, btnCreateCategory, btnBack, btnDate, btnTime;
    EditText txtAmount, txtNotice, txtDate, txtTime;
    int id_wallet, id_expen, id_user, id_expen_detail;
    String name_wallet, name_expen, name_expen_detail;
    int mYear, mMonth, mDay, mHour, mMinute;
    Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_expenditure);
		    
        // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Get id user
	    id_user = HomeActivity.id_user;
        
        // Spinner element
        spnWallet = (Spinner) findViewById(R.id.spn_expen_wallet);
        spnCategoryExpen = (Spinner)findViewById(R.id.spn_expen_danhmuc);
        spnCategoryExpenDetail = (Spinner)findViewById(R.id.spn_expen_danhmuccon);
        // Spinner click listener
        spnWallet.setOnItemSelectedListener(this);
        spnCategoryExpen.setOnItemSelectedListener(this);
        spnCategoryExpenDetail.setOnItemSelectedListener(this);
        loadSpinnerDataWallet();
        loadSpinnerDataCategoryExpen();
        
        btnSubmit = (Button)findViewById(R.id.btn_expen_submit);
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack = (Button)findViewById(R.id.btn_expen_back);
        btnBack.setOnClickListener(new MyEvent());
        btnCreateCategory = (Button)findViewById(R.id.btn_expen_createcategory);
        btnCreateCategory.setOnClickListener(new MyEvent());
        btnDate = (Button)findViewById(R.id.btn_expen_date);
        btnDate.setOnClickListener(new MyEvent());
        btnTime = (Button)findViewById(R.id.btn_expen_time);
        btnTime.setOnClickListener(new MyEvent());
        
        txtAmount = (EditText)findViewById(R.id.txt_expen_amount);
        txtNotice = (EditText)findViewById(R.id.txt_expen_notice);
        txtDate = (EditText)findViewById(R.id.txt_expen_date);
        txtTime = (EditText)findViewById(R.id.txt_expen_time);
        setCurrentDate();
        setCurrentTime();
    }
    /**
	 * thiet lap ngay thang nam hien tai
	 */
	 public void setCurrentDate(){
		 Calendar cal=Calendar.getInstance();
		 mDay=cal.get(Calendar.DAY_OF_MONTH);
		 mMonth=(cal.get(Calendar.MONTH)+1);
		 mYear=cal.get(Calendar.YEAR);
		 txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
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
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());

        // Spinner Drop down cursor
        walletsCursor = dbAdapter.getListWalletOfUser(id_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_WALLET" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,walletsCursor, from, to, 0);

        // attaching data adapter to spinner
        spnWallet.setAdapter(dataAdapter);
        
    }
    /**
     * Load category income into spinner
     */
    private void loadSpinnerDataCategoryExpen() {
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());
        // Spinner Drop down cursor
        categoryExpenCursor = dbAdapter.getCategoryExpenCursor();
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_EXP" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              categoryExpenCursor, from, to, 0);

        // attaching data adapter to spinner
        spnCategoryExpen.setAdapter(dataAdapter);
    }
    
    private void loadSpinnerDataCategoryExpenDetail(int id) {
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());
        // Spinner Drop down cursor
        categoryExpenDetailCursor = dbAdapter.getCategoryExpenDetail(id);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_EXP_DET" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              categoryExpenDetailCursor, from, to, 0);

        // attaching data adapter to spinner
        spnCategoryExpenDetail.setAdapter(dataAdapter);
    }
    
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_expen_submit)
			{
				DoInsertExpen();
			}
			else if(v.getId() == R.id.btn_expen_back){
				ExpenditureActivity.this.finish();
			}
			else if(v.getId() == R.id.btn_expen_date){
				showDialog(1);
			}
			else if(v.getId() == R.id.btn_expen_time){
				showDialog(2);
			}
		}
	}
    
    private void DoInsertExpen(){
    	String mAmount = txtAmount.getText().toString();
    	String mNotice = txtNotice.getText().toString();
    	String mTime = txtTime.getText().toString();
    	if(mAmount.equals("")){
    		Toast.makeText(getApplicationContext(), "Please insert information", Toast.LENGTH_LONG).show();
    	}
    	else {
    		long curentmoney = dbAdapter.getAmountOfWallet(id_wallet);
    		long newmoney = curentmoney - Integer.valueOf(mAmount);
    		
    		Diary diary = new Diary();
    		diary.setAmount(Integer.valueOf(mAmount));
    		diary.setId_wallet(id_wallet);
    		diary.setId_parent_category(id_expen);
    		diary.setId_category(id_expen_detail);
    		diary.setId_account(id_user);
    		diary.setDay(mDay);
    		diary.setMonth(mMonth);
    		diary.setYear(mYear);
    		diary.setTime(mTime);
    		diary.setType(2); // 2 = chi
    		diary.setNotice(mNotice);
    		
    		dbAdapter.insertDiary(diary);
    		dbAdapter.updateWallet(id_wallet, newmoney);
    		Toast.makeText(getApplicationContext(), "Thanh Cong", Toast.LENGTH_SHORT).show();
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
		if(spinner.getId() == R.id.spn_expen_wallet)
		{
			walletsCursor.moveToPosition(position);
			id_wallet = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
			name_wallet = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("NAME_WALLET"));
		}
		else if(spinner.getId() == R.id.spn_expen_danhmuc)
		{
			categoryExpenCursor.moveToPosition(position);
			id_expen = categoryExpenCursor.getInt(categoryExpenCursor.getColumnIndexOrThrow("_id"));
			name_expen = categoryExpenCursor.getString(categoryExpenCursor.getColumnIndexOrThrow("NAME_EXP"));
			loadSpinnerDataCategoryExpenDetail(id_expen);
		}
		else if(spinner.getId() == R.id.spn_expen_danhmuccon)
		{
			categoryExpenDetailCursor.moveToPosition(position);
			id_expen_detail = categoryExpenDetailCursor.getInt(categoryExpenDetailCursor.getColumnIndexOrThrow("_id"));
			name_expen_detail = categoryExpenDetailCursor.getString(categoryExpenDetailCursor.getColumnIndexOrThrow("NAME_EXP_DET"));
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
			 mMonth=monthOfYear + 1;
			 mDay=dayOfMonth;
			 txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
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
			 return new DatePickerDialog(this, dateChange, mYear, mMonth-1, mDay);
		 }
		 else if(id==2){
			 return new TimePickerDialog(this, timeChange, mHour, mMinute, false );
		 }
		 return null;
	 }
	 
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
		    View v = getCurrentFocus();

		    if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
		            v instanceof EditText && !v.getClass().getName().startsWith("android.webkit.")) {
		        int scrcoords[] = new int[2];
		        v.getLocationOnScreen(scrcoords);
		        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
		        float y = ev.getRawY() + v.getTop() - scrcoords[1];

		        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
		            hideKeyboard(this);
		    }
		    return super.dispatchTouchEvent(ev);
		}

		public static void hideKeyboard(Activity activity) {
		    if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
		        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		    }
		}
}
