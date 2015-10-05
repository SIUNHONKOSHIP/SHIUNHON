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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class WalletActivity extends Activity{
	// Khai bÃ¡o biáº¿n
	SQLiteDatabase db = null;
	MySimpleArrayAdapter arrayadapter = null;
	Button btnBack, btnCreateWallet;
	DataBaseAdapter dbAdapter;
	Wallet wl=null;
	List<Wallet>list=new ArrayList<Wallet>();
	int id_curent_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		
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
	
	private void ShowWallet(){
		db=openOrCreateDatabase("DARFTMONEYPOWER.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		if(db!=null){
			Cursor cursor=db.query("tbl_WALLET", null, "ID_ACCOUNT=?", new String[]{String.valueOf(id_curent_user)}, null, null, null);
			if (cursor.getCount()<1){
				Toast.makeText(WalletActivity.this, "ChÆ°a cÃ³ vÃ­ !!! hÃ£y táº¡o má»›i ðŸ˜Š", Toast.LENGTH_LONG).show();
			}
			else{
				list.clear();
				//startManagingCursor(cursor);
				Wallet header=new Wallet();
				header.setName("TÃªn VÃ­");
				header.setMoney("Sá»‘ Tiá»�n");
				list.add(header);
				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					Wallet data=new Wallet();
					data.setId_wallet(cursor.getInt(0));
					data.setName(cursor.getString(1));
					data.setMoney(cursor.getString(3));
					list.add(data);
					cursor.moveToNext();
			 	}
				cursor.close();
				arrayadapter=new MySimpleArrayAdapter(WalletActivity.this, R.layout.layout_for_show_list, list);
				final ListView lv= (ListView) findViewById(R.id.listView1);
				lv.setAdapter(arrayadapter);
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						final Wallet data=list.get(arg2);
						final int pos=arg2;
						Toast.makeText(WalletActivity.this, "Edit-->"+data.toString(), Toast.LENGTH_LONG).show();
						AlertDialog.Builder b = new Builder(WalletActivity.this);
						b.setTitle("Remove");
						b.setMessage("Báº¡n cÃ³ muá»‘n xÃ³a ["+data.getName() +"] khÃ´ng?");
						b.setPositiveButton("CÃ³", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								int n=db.delete("tbl_WALLET", "ID_WALLET=?", new String[]{String.valueOf(data.getId_wallet())});
								if(n>0){
									Toast.makeText(WalletActivity.this, "XÃ³a thÃ nh cÃ´ng ", Toast.LENGTH_LONG).show();
									list.remove(pos);
									arrayadapter.notifyDataSetChanged();
								}
								else{
									Toast.makeText(WalletActivity.this, "XÃ³a tháº¥t báº¡i", Toast.LENGTH_LONG).show();
								}
							}
						});
						b.setNegativeButton("KhÃ´ng", new DialogInterface.OnClickListener() {
							
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
					 Toast.makeText(WalletActivity.this,"View -->"+ list.get(arg2).toString(), Toast.LENGTH_LONG).show();
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
