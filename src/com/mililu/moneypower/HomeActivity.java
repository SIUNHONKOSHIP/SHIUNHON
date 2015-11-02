package com.mililu.moneypower;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HomeActivity extends Activity{

	ListView mDrawerList;
	RelativeLayout mDrawerPane;
	//private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	 
	ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
	
	Button btnMenu, btnWallet, btnIncome, btnExpenditure, btnStatistic;
	TextView lableFullname, lableUsername;
	DataBaseAdapter dbAdapter;
	int id_curent_user;
	String username_current_user, fullname_current_user;
	
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
	    fullname_current_user = bundle.getString("FULLNAME_ACCOUNT");
	    username_current_user = bundle.getString("USERNAME_ACCOUNT");
	    
	    // Get The Reference Of Buttons and Edit Text
	    btnMenu = (Button)findViewById(R.id.btn_home_menu);
	    
	    btnWallet=(Button)findViewById(R.id.btn_home_wallet);
	    Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    btnWallet.setTypeface(font);
        
	    btnIncome=(Button)findViewById(R.id.btn_home_income);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    btnIncome.setTypeface(font);
	    
	    btnExpenditure=(Button)findViewById(R.id.btn_home_expenditure);	 
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    btnExpenditure.setTypeface(font);
	    
	    btnStatistic=(Button)findViewById(R.id.btn_home_statistic);
	    font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLDITALIC.TTF");
	    btnStatistic.setTypeface(font);
	    
	    lableFullname = (TextView)findViewById(R.id.tv_home_fullname) ;
	    lableUsername = (TextView)findViewById(R.id.tv_home_username);
	    lableFullname.setText(fullname_current_user);
	    lableUsername.setText(username_current_user);
	    
	    // Set OnClick Listener
	    
	    btnIncome.setOnClickListener(new MyEvent());
	    btnExpenditure.setOnClickListener(new MyEvent());
	    btnWallet.setOnClickListener(new MyEvent());
	    btnStatistic.setOnClickListener(new MyEvent());
	    btnMenu.setOnClickListener(new MyEvent());
	    
	    // Add list menu
	    mNavItems.add(new NavItem("Wallet", "", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Income", "", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Expenture", "", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Category", "Category of Income & Expenditure", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Diary", "Everything you have wrote", R.drawable.ic_launcher));
		mNavItems.add(new NavItem("Report", "", R.drawable.ic_launcher));
	    mNavItems.add(new NavItem("About us", "Information about this app", R.drawable.icon_info));
	    mNavItems.add(new NavItem("Logout", "", R.drawable.icon_info));
	 
	    // DrawerLayout
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
	 
	    // Populate the Navigtion Drawer with options
	    mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
	    mDrawerList = (ListView) findViewById(R.id.lv_home_navList);
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
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId()==R.id.btn_home_income)
			{
				Bundle bundle=new Bundle();
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				Intent intent = new Intent (HomeActivity.this, IncomeActivity.class);
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_home_expenditure) {
				Bundle bundle=new Bundle();
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				Intent intent = new Intent (HomeActivity.this, ExpenditureActivity.class);
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_home_wallet) {
				Bundle bundle=new Bundle();
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				Intent intent = new Intent (HomeActivity.this, WalletActivity.class);
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_home_statistic) {
				Bundle bundle=new Bundle();
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				Intent intent = new Intent (HomeActivity.this, StatisticbyMonthActivity.class);
				intent.putExtra("DATA_ACCOUNT", bundle);
				startActivity(intent);
			}
			else if (v.getId()==R.id.btn_home_menu){
				mDrawerLayout.openDrawer(mDrawerPane);
			}
		}
	}
	
	/**
	* Called when a particular item from the navigation drawer
	* is selected.
	**/
	private void selectItemFromDrawer(int position) {
	 
		if (position == 0){ /// selected wallet
			Bundle bundle=new Bundle();
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			Intent intent = new Intent (HomeActivity.this, WalletActivity.class);
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 1){  /// selected income
			Bundle bundle=new Bundle();
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			Intent intent = new Intent (HomeActivity.this, IncomeActivity.class);
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 2){ ///selected expenditure
			Bundle bundle=new Bundle();
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			Intent intent = new Intent (HomeActivity.this, ExpenditureActivity.class);
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 3){ ///selected category
			Toast.makeText(HomeActivity.this, "You have just select Category", Toast.LENGTH_LONG).show();
		}
		else if (position == 4){ ///selected Diary
			Bundle bundle=new Bundle();
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			Intent intent = new Intent (HomeActivity.this, DiaryActivity.class);
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 5){ ///selected report
			Bundle bundle=new Bundle();
			bundle.putInt("ID_ACCOUNT", id_curent_user);
			Intent intent = new Intent (HomeActivity.this, StatisticbyMonthActivity.class);
			intent.putExtra("DATA_ACCOUNT", bundle);
			startActivity(intent);
		}
		else if (position == 6){ ///selected about us
			Toast.makeText(HomeActivity.this, "You have just select About us", Toast.LENGTH_LONG).show();
		}
		else if (position == 7){ ///selected logout
			HomeActivity.this.finish();
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
