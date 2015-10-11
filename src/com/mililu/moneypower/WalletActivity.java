package com.mililu.moneypower;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class WalletActivity extends Activity{
	
	// Khai bao bien
	SQLiteDatabase db = null;
	MyWalletArrayAdapter arrayadapter = null;
	Button btnBack, btnCreateWallet;
	DataBaseAdapter dbAdapter;
	Wallet wl=null;
	List<Wallet>list_wallet=new ArrayList<Wallet>();
	int id_curent_user;
	TextView txtTittle, txtBalance, txtAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_wallet);
		
		// Set font
		txtTittle=(TextView)findViewById(R.id.textView1);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        txtTittle.setTypeface(font);
        
        txtBalance=(TextView)findViewById(R.id.textView2);
        font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        txtBalance.setTypeface(font);
        
        txtAmount=(TextView)findViewById(R.id.textView3);
        font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
        txtAmount.setTypeface(font);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Buttons
	    btnBack=(Button)findViewById(R.id.btn_wallettohome);
	    btnCreateWallet=(Button)findViewById(R.id.btn_createwallet);
	    
	    // Set OnClick Listener on SignUp button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnCreateWallet.setOnClickListener(new MyEvent());
	    
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
	    // Load Data
	    
	}
	@Override
	protected void onStart(){
		super.onStart();
		ShowWallet();
		getTotalAmount(id_curent_user);
	}
	
	/** HÃ m xÃ©t sá»± kiá»‡n nháº¥n nÃºt 
	 * 
	 * @author Thanh LiÃªm
	 *
	 */
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_wallettohome)
			{
				WalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createwallet)
			{
				Bundle bundle=new Bundle();
				//Ä‘Æ°a dá»¯ liá»‡u riÃªng láº» vÃ o Bundle
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				// Táº¡o Intend Ä‘á»ƒ má»Ÿ HomeActivity
				Intent intent = new Intent (WalletActivity.this, CreateWalletActivity.class);
				//Ä�Æ°a Bundle vÃ o Intent
				intent.putExtra("DATA", bundle);
				startActivity(intent);
				//ShowWallet();
			}
		}
	}
	private void getTotalAmount(int id_user){
		int mBalance = dbAdapter.getTotalAmount(id_user);
		txtAmount.setText(String.valueOf(mBalance));
	}
	
	private void ShowWallet(){
		db=openOrCreateDatabase("DARFTMONEYPOWER.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		if(db!=null){
			Cursor cursor=db.query("tbl_WALLET", null, "ID_ACCOUNT=?", new String[]{String.valueOf(id_curent_user)}, null, null, null);
			if (cursor.getCount()<1){
				Toast.makeText(WalletActivity.this, "You don't have any wallet !!", Toast.LENGTH_LONG).show();
			}
			else{
				list_wallet.clear();
				//startManagingCursor(cursor);
				//Wallet header=new Wallet();
				//header.setName("TÃªn VÃ­");
				//header.setMoney("Sá»‘ Tiá»�n");
				//list.add(header);
				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					Wallet data=new Wallet();
					data.setId_wallet(cursor.getInt(0));
					data.setName(cursor.getString(1));
					data.setMoney(cursor.getString(3));
					list_wallet.add(data);
					cursor.moveToNext();
			 	}
				cursor.close();
				arrayadapter=new MyWalletArrayAdapter(WalletActivity.this, R.layout.layout_for_show_list, list_wallet);
				final ListView lv= (ListView) findViewById(R.id.listView1);
				lv.setAdapter(arrayadapter);
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						final Wallet data=list_wallet.get(arg2);
						final int pos=arg2;
						Toast.makeText(WalletActivity.this, data.toString(), Toast.LENGTH_LONG).show();
						AlertDialog.Builder b = new Builder(WalletActivity.this);
						b.setTitle("Remove Wallet");
						b.setMessage("Do you wanna delete ["+data.getName() +"]?");
						b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								int n=db.delete("tbl_WALLET", "ID_WALLET=?", new String[]{String.valueOf(data.getId_wallet())});
								if(n>0){
									Toast.makeText(WalletActivity.this, "Delete Success", Toast.LENGTH_LONG).show();
									list_wallet.remove(pos);
									arrayadapter.notifyDataSetChanged();
								}
								else{
									Toast.makeText(WalletActivity.this, "Delete not success", Toast.LENGTH_LONG).show();
								}
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
						return false;
					}
				}); // ket thÃºc hÃ m xÃ©t event cháº¡m lÃ¢u trÃªn 1 Ä‘á»‘i tÆ°á»£ng wallet
				lv.setOnItemClickListener(new OnItemClickListener() {
					 @Override
					 public void onItemClick(AdapterView<?> arg0, View arg1,
					 int arg2, long arg3) {
					 // TODO Auto-generated method stub
					 Toast.makeText(WalletActivity.this,"View :"+ list_wallet.get(arg2).toString(), Toast.LENGTH_LONG).show();
					 /*Intent intent=new Intent(WalletActivity.this, CreateWalletActivity.class);
					 Bundle bundle=new Bundle();
					 bundle.putInt("KEY", 2);
					 bundle.putString("getNameWallet", list.get(arg2).getName().toString());
					 bundle.putString("getMoneyWallet", list.get(arg2).getMoney().toString());
					 bundle.putInt("getIdWallet", list.get(arg2).getId_wallet());
					 intent.putExtra("DATA", bundle);
					 //wl=list.get(arg2);
					 startActivity(intent);
					 */
					 }
					 });
			}
		}
	}
}
