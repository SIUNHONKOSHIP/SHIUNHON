package com.mililu.moneypower;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateWalletActivity extends Activity {
	DataBaseAdapter dbAdapter;
	Button btnBack, btnInsertWallet;
	EditText txtNameWallet, txtMoney;
	int id_curent_user;
	int action; // lÆ°u tráº¡ng thÃ¡i hÃ nh Ä‘á»™ng: 1 - insert, 2 - update
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_wallet);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    txtNameWallet = (EditText)findViewById(R.id.txt_namewallet);
	    txtMoney = (EditText)findViewById(R.id.txt_moneyinwallet);
	    btnBack=(Button)findViewById(R.id.btn_createwallettowallet);
	    btnInsertWallet=(Button)findViewById(R.id.btn_insertwallet);
	    
	    // Set OnClick Listener on SignUp button 
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
			if(v.getId()==R.id.btn_createwallettowallet)
			{
				CreateWalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_insertwallet) {
					InsertWallet();
			}
		}
	}
	
	private void InsertWallet(){
		int id =  id_curent_user;
		String namewallet = txtNameWallet.getText().toString();
		String money = txtMoney.getText().toString();

		// check if any of the fields are vaccant
		if(namewallet.equals("")||money.isEmpty()||Integer.valueOf(money)<0)
		{
				Toast.makeText(this, "Vui lÃ²ng Ä‘iá»�n Ä‘áº§y Ä‘á»§ thÃ´ng tin", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.insertWallet(namewallet, Integer.valueOf(money), id);
			Toast.makeText(getApplicationContext(), "VÃ­ Ä‘Ã£ Ä‘Æ°á»£c táº¡o thÃ nh cÃ´ng", Toast.LENGTH_LONG).show();
			CreateWalletActivity.this.finish();
		}
	}
}