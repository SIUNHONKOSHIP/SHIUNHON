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
	Cursor cursorWallet;
	ListView lvWallet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_wallet);
		
		// Get The Reference Of View
	    txtTittle=(TextView)findViewById(R.id.textView1);
	    txtBalance=(TextView)findViewById(R.id.textView2);
	    txtAmount=(TextView)findViewById(R.id.textView3);
	    btnBack=(Button)findViewById(R.id.btn_wallettohome);
	    btnCreateWallet=(Button)findViewById(R.id.btn_createwallet);
	    lvWallet = (ListView)findViewById(R.id.listView1);
		
	    // Set font
        Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
        txtTittle.setTypeface(font_light);
        txtBalance.setTypeface(font_light);
        txtAmount.setTypeface(font_bold);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnCreateWallet.setOnClickListener(new MyEvent());
	    
	    // Set OnItemClick Listener on listview
	    lvWallet.setOnItemClickListener(new MyEventItemOnClick());
	    
	    // Get data of Bundle
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
	    
	}
	@Override
	protected void onStart(){
		super.onStart();
		ShowWallet();
		getTotalAmount(id_curent_user);
	}
	
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
				bundle.putInt("ID_ACCOUNT", id_curent_user);
				Intent intent = new Intent (WalletActivity.this, CreateWalletActivity.class);
				intent.putExtra("DATA", bundle);
				startActivity(intent);
			}
		}
	}
	private void getTotalAmount(int id_user){
		int mBalance = dbAdapter.getTotalAmount(id_user);
		txtAmount.setText(String.valueOf(mBalance));
	}
	
	private void ShowWallet(){
		cursorWallet=dbAdapter.getListWalletOfUser(id_curent_user);
		if (cursorWallet.getCount()<1){
			Toast.makeText(WalletActivity.this, "You don't have any wallet !!", Toast.LENGTH_LONG).show();
		}
		else{
			list_wallet.clear();
			cursorWallet.moveToFirst();
			while(!cursorWallet.isAfterLast()){
				Wallet data=new Wallet();
				data.setId_wallet(cursorWallet.getInt(cursorWallet.getColumnIndexOrThrow("ID_WALLET")));
				data.setName(cursorWallet.getString(cursorWallet.getColumnIndexOrThrow("NAME_WALLET")));
				data.setMoney(cursorWallet.getString(cursorWallet.getColumnIndexOrThrow("MONEY")));
				list_wallet.add(data);
				cursorWallet.moveToNext();
		 	}
			cursorWallet.close();
			arrayadapter = new MyWalletArrayAdapter(WalletActivity.this, R.layout.layout_for_show_list, list_wallet);
			lvWallet.setAdapter(arrayadapter);
				lvWallet.setOnItemLongClickListener(new OnItemLongClickListener() {
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
				});
			}
	}
	
	private class MyEventItemOnClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			//Toast.makeText(WalletActivity.this,"View :"+ list_wallet.get(position).toString(), Toast.LENGTH_LONG).show();
			
		}
	}
	
	private class MyEventItemOnLongClick implements OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
