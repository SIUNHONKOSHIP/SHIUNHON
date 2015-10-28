package com.mililu.moneypower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCategoryIncomeActivity extends Activity {
	DataBaseAdapter dbAdapter;
	Button btnBack, btnInsert;
	EditText txtNameIncome;
	int id_curent_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_create_category_income);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    txtNameIncome = (EditText)findViewById(R.id.txt_createcategoryincome_nameincome);
	    btnBack=(Button)findViewById(R.id.btn_createcategoryincome_back);
	    btnInsert=(Button)findViewById(R.id.btn_createcategoryincome_OK);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnInsert.setOnClickListener(new MyEvent());
	    
	    //Intent intent = getIntent();
		//Bundle bundle = intent.getBundleExtra("DATA");
		//id_curent_user = bundle.getInt("ID_ACCOUNT");
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createcategoryincome_back)
			{
				CreateCategoryIncomeActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createcategoryincome_OK) {
					InsertCategoryIncome();
			}
		}
	}
	
	private void InsertCategoryIncome(){
		String nameincome = txtNameIncome.getText().toString();

		// check if any of the fields are vaccant
		if(nameincome.equals(""))
		{
				Toast.makeText(this, "Please write name of income", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.insertCategoryIncome(nameincome);
			Toast.makeText(getApplicationContext(), "Create Successful", Toast.LENGTH_LONG).show();
			CreateCategoryIncomeActivity.this.finish();
		}
	}
}