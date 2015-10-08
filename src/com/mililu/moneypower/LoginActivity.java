package com.mililu.moneypower;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	// khai bÃ³a biáº¿n
	Button btnLogin, btnRegister;
	EditText txtUserName, txtPassword;
	DataBaseAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Buttons and Edit Text
	    txtUserName = (EditText)findViewById(R.id.txt_usernamelogin);
	    txtPassword = (EditText)findViewById(R.id.txt_passwordlogin);
	    btnLogin=(Button)findViewById(R.id.btn_login);
	    btnRegister=(Button)findViewById(R.id.btn_register);
	    
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
	
	/** Ham xu ly dang nhap
	 * 
	 */
	public void Login(){
		String username = txtUserName.getText().toString(); // táº¡o biáº¿n lÆ°u thÃ´ng tin tÃªn Ä‘Äƒng nháº­p trong textbox
		String password = txtPassword.getText().toString(); // táº¡o biáº¿n lÆ°u thÃ´ng tin máº­t kháº©u trong textbox
		
		// Get validation
		if(username.equals("")||password.equals(""))
		{
				Toast.makeText(getApplicationContext(), "Vui lÃ²ng Ä‘iá»�n Ä‘áº§y Ä‘á»§ thÃ´ng tin", Toast.LENGTH_LONG).show();
				return;
		}
		else{ // náº¿u Ä‘Ã£ Ä‘iá»�n Ä‘á»§ thÃ´ng tin
			String passindatabase = dbAdapter.getAccountPassword(username); // táº¡o biáº¿n lÆ°u thÃ´ng tin pass trong database 
			if (password.equals(passindatabase)){
				// Láº¥y dá»¯ liá»‡u ID cá»§a account
				int id_account = dbAdapter.getAccountId(username);
				//Khai bÃ¡o Bundle
				Bundle bundle=new Bundle();
				//Ä‘Æ°a dá»¯ liá»‡u riÃªng láº» vÃ o Bundle
				bundle.putInt("ID_ACCOUNT", id_account);
				// Táº¡o Intend Ä‘á»ƒ má»Ÿ HomeActivity
				Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
				//Ä�Æ°a Bundle vÃ o Intent
				intent.putExtra("DATA_ACCOUNT", bundle);
				// má»Ÿ Activity
				startActivity(intent);
			}
			else{
				Toast.makeText(LoginActivity.this, "Sai tÃªn Ä‘Äƒng nháº­p hoáº·c máº­t kháº©u", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/** HÃ m xÃ³a database
	 * 
	 */
	public void DeleteDB(){
		String msg = "";
		if (deleteDatabase("DARFTMONEYPOWER.db")==true){
			msg = "xoa database thanh cong";
		}
		else{
			msg = "That bai cmnr";
		}
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
 
		dbAdapter.close();
		txtUserName.setText("");
		txtPassword.setText("");
	}
}
