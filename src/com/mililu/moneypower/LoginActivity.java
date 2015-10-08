package com.mililu.moneypower;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Typeface;

public class LoginActivity extends Activity {
	// khai bao bien
	
	Button btnLogin, btnRegister;
	EditText txtUserName, txtPassword;
	DataBaseAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		
		
		// Create a instance of SQLite Database

		CreateDB();
	    
	    // Get The Reference Of Buttons and Edit Text
	    
	    txtUserName = (EditText)findViewById(R.id.txt_usernamelogin);
	    //Thiet lap font de su dung tu assets
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        //Thiet lap font cho Username
        txtUserName.setTypeface(font);
        
	    txtPassword = (EditText)findViewById(R.id.txt_passwordlogin);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    txtPassword.setTypeface(font);
	    
	    btnLogin=(Button)findViewById(R.id.btn_login);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUE.TTF");
	    btnLogin.setTypeface(font);
	    
	    btnRegister=(Button)findViewById(R.id.btn_register);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHTITALIC.TTF");
	    btnRegister.setTypeface(font);
	    
	    // Set OnClick Listener on SignUp button 
	    btnLogin.setOnClickListener(new MyEvent());
	    btnRegister.setOnClickListener(new MyEvent());
	    
	}
	
	/** Ham tao su kien khi nhan button
	 *
	 */
	private class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_register)
			{
				Intent intent = new Intent (LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_login) {
				Login();
			}
		}
	}
	
	/**
	 * Create Database
	 */
	private void CreateDB() {
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	}
	
	/** Ham xu ly dang nhap
	 * 
	 */
	public void Login(){
		String username = txtUserName.getText().toString(); 
		String password = txtPassword.getText().toString(); 
		
		// Get validation
		if(username.equals("")||password.equals(""))
		{
				Toast.makeText(getApplicationContext(), "Please insert username and password", Toast.LENGTH_LONG).show();
				return;
		}
		else{ 
			String passindatabase = dbAdapter.getAccountPassword(username);  
			if (password.equals(passindatabase)){
				// Get ID of account
				int id_account = dbAdapter.getAccountId(username);
				//Identify Bundle
				Bundle bundle=new Bundle();
				// set data into Bundle
				bundle.putInt("ID_ACCOUNT", id_account);
				// create Intend 
				Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
				//Set bundle into intent
				intent.putExtra("DATA_ACCOUNT", bundle);
				// Start Activity
				startActivity(intent);
			}
			else{
				Toast.makeText(LoginActivity.this, "Username or Password is not correct", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/** 
	 * Delete database
	 */
	public void DeleteDB(){
		String msg = "";
		if (deleteDatabase("DARFTMONEYPOWER.db")==true){
			msg = "Delete database successful!";
		}
		else{
			msg = "Failed!";
		}
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	/**
	 * Delete and create Database again
	 */
	public void ResetDatabase() {
		DeleteDB();
		CreateDB();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
 
		dbAdapter.close();
		txtUserName.setText("");
		txtPassword.setText("");
	}
	
	//// create menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.login_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//// Set even click for Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.reset_database:
	            ResetDatabase();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
