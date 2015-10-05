package com.mililu.moneypower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	Button btnIncome, btnExpenditure, btnWallet, btnStatistic;
	TextView txthello;
	DataBaseAdapter dbAdapter;
	int id_curent_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Buttons and Edit Text
	    btnIncome=(Button)findViewById(R.id.btn_income);
	    btnExpenditure=(Button)findViewById(R.id.btn_expenditure);
	    btnWallet=(Button)findViewById(R.id.btn_wallet);
	    btnStatistic=(Button)findViewById(R.id.btn_statistic);
	    txthello = (TextView)findViewById(R.id.tv_row1);
	    
	    // Set OnClick Listener on SignUp button 
	    btnIncome.setOnClickListener(new MyEvent());
	    btnExpenditure.setOnClickListener(new MyEvent());
	    btnWallet.setOnClickListener(new MyEvent());
	    btnStatistic.setOnClickListener(new MyEvent());
	    
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
		txthello.setText("Hello " + id_curent_user);
	}

	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_income)
			{
				//Intent intent = new Intent (HomeActivity.this, IncomeActivity.class);
				//startActivity(intent);
				Toast.makeText(HomeActivity.this, "ChÆ°a cÃ³ code ðŸ˜�", Toast.LENGTH_LONG).show();
			}
			else if(v.getId()==R.id.btn_expenditure) {
				//Intent intent = new Intent (HomeActivity.this, ExpenditureActivity.class);
				//startActivity(intent);
				Toast.makeText(HomeActivity.this, "ChÆ°a cÃ³ code ðŸ˜†", Toast.LENGTH_LONG).show();
			}
			else if(v.getId()==R.id.btn_wallet) {
				Bundle bundle=new Bundle();
				//Ä‘Æ°a dá»¯ liá»‡u riÃªng láº» vÃ o Bundle
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				// Táº¡o Intend Ä‘á»ƒ má»Ÿ HomeActivity
				Intent intent = new Intent (HomeActivity.this, WalletActivity.class);
				//Ä�Æ°a Bundle vÃ o Intent
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_statistic) {
				//Intent intent = new Intent (HomeActivity.this, StatisticActivity.class);
				//startActivity(intent);
				Toast.makeText(HomeActivity.this, "ChÆ°a cÃ³ code ðŸ˜„", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
