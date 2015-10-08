package com.mililu.moneypower;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    
	    txtUserName = (EditText)findViewById(R.id.txt_username);
	    //Thiet lap font de su dung tu assets
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        //Thiet lap font cho Username
        txtUserName.setTypeface(font);
        
	    txtPassword = (EditText)findViewById(R.id.txt_password);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    txtPassword.setTypeface(font);
	    
	    txtConfirmPass = (EditText)findViewById(R.id.txt_conformpass);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    txtConfirmPass.setTypeface(font);
	    
	    btnRegister=(Button)findViewById(R.id.btn_createaccount);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUE.TTF");
	    btnRegister.setTypeface(font);
	    
	    btnBack=(Button)findViewById(R.id.btn_backtologin);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    btnBack.setTypeface(font);
	    
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
				Toast.makeText(getApplicationContext(), "Please enter username and password.", Toast.LENGTH_LONG).show();
				return;
		}
		// check if both password matches
		if(!password.equals(confirmpass))
		{
			Toast.makeText(getApplicationContext(), "The passwords are not match.", Toast.LENGTH_LONG).show();
			return;
		}
		else
		{
			if (dbAdapter.isAccountExit(username)){
				Toast.makeText(getApplicationContext(), "The username is duplicate, try again.", Toast.LENGTH_LONG).show();
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
