package com.mililu.moneypower;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryIncomeActivity extends Activity{
	Button btnBack, btnAdd;
	DataBaseAdapter dbAdapter;
	List<Income>list_income ;
	ArrayAdapterCategoryIncome aaCategoryIncome;
	int id_curent_user;
	TextView tvTittle;
	ListView lvCategoryIncome;
	Cursor cursorCategoryIncome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_category_income);
		
		// Get The Reference Of View
	    tvTittle=(TextView)findViewById(R.id.tv_categoryincome_title);
	    btnBack=(Button)findViewById(R.id.btn_categoryincome_back);
	    btnAdd=(Button)findViewById(R.id.btn_categoryincome_create);
	    lvCategoryIncome = (ListView)findViewById(R.id.lv_categoryincome_listincome);
		
	    // khoi tao gia tri
	    list_income = new ArrayList<Income>();
	    
	    // Set font
        //Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        //Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
        //txtTittle.setTypeface(font_light);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnAdd.setOnClickListener(new MyEvent());
	    
	    // Set OnItemClick Listener on listview
	    //lvDiary.setOnItemClickListener(new MyEventItemOnClick());
	    
	    // Get data of Bundle
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA");
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
	    
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		ShowListIncome();
	}
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_categoryincome_back)
			{
				CategoryIncomeActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_categoryincome_create)
			{
				// Open activity create Income Category
				Intent intent = new Intent (getApplicationContext(), CreateCategoryIncomeActivity.class);
				startActivity(intent);
			}
		}
	}
	
	private void ShowListIncome(){
		cursorCategoryIncome = dbAdapter.getCategoryIncomeCursor();
		if (cursorCategoryIncome.getCount()<1){
			Toast.makeText(CategoryIncomeActivity.this, "You don't have any category !!", Toast.LENGTH_LONG).show();
		}
		else{
			list_income.clear();
			cursorCategoryIncome.moveToFirst();
			while(!cursorCategoryIncome.isAfterLast()){
				Income data = new Income();
				data.setId(cursorCategoryIncome.getInt(cursorCategoryIncome.getColumnIndexOrThrow("ID_INC")));
				data.setName(cursorCategoryIncome.getString(cursorCategoryIncome.getColumnIndexOrThrow("NAME_INCOME")));
				list_income.add(data);
				cursorCategoryIncome.moveToNext();
		 	}
			cursorCategoryIncome.close();
			aaCategoryIncome = new ArrayAdapterCategoryIncome(CategoryIncomeActivity.this, R.layout.layout_for_category_item, list_income);
			lvCategoryIncome.setAdapter(aaCategoryIncome);
		}
	}
}
