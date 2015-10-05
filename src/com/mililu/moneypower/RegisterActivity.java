package com.mililu.moneypower;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	Button btnBack, btnRegister;
	EditText txtUserName, txtPassword, txtConfirmPass;
	DataBaseAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    txtUserName = (EditText)findViewById(R.id.txt_username);
	    txtPassword = (EditText)findViewById(R.id.txt_password);
	    txtConfirmPass = (EditText)findViewById(R.id.txt_conformpass);
	    btnRegister=(Button)findViewById(R.id.btn_createaccount);
	    btnBack=(Button)findViewById(R.id.btn_backtologin);
	    
	    // Set OnClick Listener on SignUp button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnRegister.setOnClickListener(new MyEvent());
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_backtologin)
			{
				RegisterActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createaccount) {
				InsertAccount();
			}
		}
	}
	
	private void InsertAccount(){
		String username=txtUserName.getText().toString();
		String password=txtPassword.getText().toString();
		String confirmpass=txtConfirmPass.getText().toString();

		// check if any of the fields are vaccant
		if(username.equals("")||password.equals("")||confirmpass.equals(""))
		{
				Toast.makeText(getApplicationContext(), "Vui lÃ²ng Ä‘iá»�n Ä‘áº§y Ä‘á»§ thÃ´ng tin", Toast.LENGTH_LONG).show();
				return;
		}
		// check if both password matches
		if(!password.equals(confirmpass))
		{
			Toast.makeText(getApplicationContext(), "Máº­t kháº©u nháº­p láº¡i khÃ´ng trÃ¹ng khá»›p", Toast.LENGTH_LONG).show();
			return;
		}
		else
		{
			if (dbAdapter.isAccountExit(username)){
				Toast.makeText(getApplicationContext(), "TÃ i khoáº£n Ä‘Ã£ tá»“n táº¡i !!!", Toast.LENGTH_LONG).show();
			}
			else{
				// Save the Data in Database
			    dbAdapter.InsertAccount(username, password);
			    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
			    RegisterActivity.this.finish();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
 
		dbAdapter.close();
	}

}
