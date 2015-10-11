package com.mililu.moneypower;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HomeActivity extends Activity{

	ListView mDrawerList;
	RelativeLayout mDrawerPane;
	//private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	 
	ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
	
	Button btnWallet, btnIncome, btnExpenditure, btnStatistic;
	TextView lableFullname, lableUsername;
	DataBaseAdapter dbAdapter;
	int id_curent_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
	    
	    // Get The Reference Of Buttons and Edit Text
	    btnWallet=(Button)findViewById(R.id.btn_wallet);
	    Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    btnWallet.setTypeface(font);
        
	    btnIncome=(Button)findViewById(R.id.btn_income);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    btnIncome.setTypeface(font);
	    
	    btnExpenditure=(Button)findViewById(R.id.btn_expenditure);	 
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    btnExpenditure.setTypeface(font);
	    
	    btnStatistic=(Button)findViewById(R.id.btn_statistic);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLDITALIC.TTF");
	    btnStatistic.setTypeface(font);
	    
	    lableFullname = (TextView)findViewById(R.id.lable_fullname) ;
	    lableUsername = (TextView)findViewById(R.id.lable_username);
	    lableFullname.setText("Unknow Name");
	    lableUsername.setText(String.valueOf(id_curent_user));
	    
	    
	    // Set OnClick Listener on SignUp button 
	    btnIncome.setOnClickListener(new MyEvent());
	    btnExpenditure.setOnClickListener(new MyEvent());
	    btnWallet.setOnClickListener(new MyEvent());
	    btnStatistic.setOnClickListener(new MyEvent());
	    
	    
	    
	    mNavItems.add(new NavItem("Wallet", "You have 0 VND", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Income", "", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Expenture", "", R.drawable.ic_launcher));
	    mNavItems.add(new NavItem("About us", "Information about this app", R.drawable.icon_info));
	    mNavItems.add(new NavItem("Logout", "", R.drawable.icon_info));
	    mNavItems.add(new NavItem("Reset Database", "⚠ delete and create Database again 💀", R.drawable.icon_info));
	 
	    // DrawerLayout
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
	 
	    // Populate the Navigtion Drawer with options
	    mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
	    mDrawerList = (ListView) findViewById(R.id.navList);
	    DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
	    mDrawerList.setAdapter(adapter);
	 
	    // Drawer Item click listeners
	    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectItemFromDrawer(position);
	        }
	    });
	}
	
	/**
	 * Delete and create Database again
	 */
	public void ResetDatabase() {
		/// Delete Database
		String msg = "";
		if (deleteDatabase("DARFTMONEYPOWER.db")==true){
			msg = "Delete database successful!";
		}
		else{
			msg = "Failed!";
		}
		Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
		
		/// Create Database
		dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    /// Thong bao thanh cong
		Toast.makeText(HomeActivity.this, "Database has been reset 😎", Toast.LENGTH_LONG).show();
	}
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId()==R.id.btn_income)
			{
				Bundle bundle=new Bundle();
				//Ä‘Æ°a dá»¯ liá»‡u riÃªng láº» vÃ o Bundle
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				// Táº¡o Intend Ä‘á»ƒ má»Ÿ HomeActivity
				Intent intent = new Intent (HomeActivity.this, IncomeActivity.class);
				//Ä�Æ°a Bundle vÃ o Intent
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_expenditure) {
				//Intent intent = new Intent (HomeActivity.this, ExpenditureActivity.class);
				//startActivity(intent);
				Toast.makeText(HomeActivity.this, "Under deverlopment.", Toast.LENGTH_LONG).show();
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
				Toast.makeText(HomeActivity.this, "Under deverlopment.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/*
	* Called when a particular item from the navigation drawer
	* is selected.
	* */
	private void selectItemFromDrawer(int position) {
	    //Fragment fragment = new PreferencesFragment();
	 
	    //FragmentManager fragmentManager = getFragmentManager();
	    //fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
	 
		if (position == 0){
			Bundle bundle=new Bundle();
			//Ä‘Æ°a dá»¯ liá»‡u riÃªng láº» vÃ o Bundle
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			// Táº¡o Intend Ä‘á»ƒ má»Ÿ HomeActivity
			Intent intent = new Intent (HomeActivity.this, WalletActivity.class);
			//Ä�Æ°a Bundle vÃ o Intent
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 1){
			Toast.makeText(HomeActivity.this, "You have just select Income", Toast.LENGTH_LONG).show();
		}
		else if (position == 2){
			Toast.makeText(HomeActivity.this, "You have just select Expenture", Toast.LENGTH_LONG).show();
		}
		else if (position == 3){
			Toast.makeText(HomeActivity.this, "You have just select Infor", Toast.LENGTH_LONG).show();
		}
		else if (position == 4){
			HomeActivity.this.finish();
		}
		else if (position == 5){
			AlertDialog.Builder b = new Builder(HomeActivity.this);
			b.setTitle("Remove Wallet");
			b.setMessage("Do you wanna Reset Database?");
			b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ResetDatabase();
					HomeActivity.this.finish();
				}
			});
			b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			b.show();
		}
		
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mNavItems.get(position).mTitle);
	 
	    // Close the drawer
	    mDrawerLayout.closeDrawer(mDrawerPane);
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
