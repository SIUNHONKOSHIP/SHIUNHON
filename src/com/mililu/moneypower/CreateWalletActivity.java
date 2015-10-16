package com.mililu.moneypower;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateWalletActivity extends Activity {
	DataBaseAdapter dbAdapter;
	Button btnBack, btnInsertWallet;
	EditText txtNameWallet, txtMoney, txtDescription;
	int id_curent_user;
	TextView tvTittle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_create_wallet);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    Typeface bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    
	    tvTittle = (TextView) findViewById(R.id.tv_createwallet_title);
	    tvTittle.setTypeface(light);
	    
	    txtNameWallet = (EditText)findViewById(R.id.txt_createwallet_namewallet);
	    txtNameWallet.setTypeface(light);
	    
	    txtMoney = (EditText)findViewById(R.id.txt_createwallet_money);
	    txtMoney.setTypeface(light);
	    
	    txtDescription = (EditText)findViewById(R.id.txt_createwallet_description);
	    txtDescription.setTypeface(light);
	    
	    btnBack=(Button)findViewById(R.id.btn_createwallet_back);
	    
	    btnInsertWallet=(Button)findViewById(R.id.btn_createwallet_submit);
	    btnInsertWallet.setTypeface(bold);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnInsertWallet.setOnClickListener(new MyEvent());
	    
	    Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("DATA");
		id_curent_user = bundle.getInt("ID_ACCOUNT");
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createwallet_back)
			{
				CreateWalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createwallet_submit) {
					InsertWallet();
			}
		}
	}
	
	private void InsertWallet(){
		int id =  id_curent_user;
		String namewallet = txtNameWallet.getText().toString();
		String money = txtMoney.getText().toString();
		String descrip = txtDescription.getText().toString();

		// check if any of the fields are vaccant
		if(namewallet.equals("")||money.isEmpty()||Integer.valueOf(money)<0)
		{
				Toast.makeText(this, "Please write your wallet's name and money ☝", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.insertWallet(namewallet, Integer.valueOf(money),  id, descrip);
			Toast.makeText(getApplicationContext(), "Wallet has been created 😏", Toast.LENGTH_LONG).show();
			CreateWalletActivity.this.finish();
		}
	}
}