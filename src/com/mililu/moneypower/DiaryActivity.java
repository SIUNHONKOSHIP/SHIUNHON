package com.mililu.moneypower;

import java.util.ArrayList;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DiaryActivity extends ExpandableListActivity{
	
	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	int id_curent_user;
	ArrayList<Diary> child; 
	
	ExpandableListView expandableList;
	
	DataBaseAdapter dbAdapter;
	AdapterDiary adapter;
	Cursor cursorDiaryDate, cursorDiaryData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		//setContentView(R.layout.main);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
		expandableList = getExpandableListView(); // you can use (ExpandableListView) findViewById(R.id.list)
		
		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		
		
		adapter = new AdapterDiary(parentItems, childItems);
		
		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(this);
		
		Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA_ACCOUNT");
	    id_curent_user = bundle.getInt("ID_ACCOUNT");
		
		
		
		setDATA();
		/// ham mo toan bo group
		for (int i = 0; i < adapter.getGroupCount(); i++){
			expandableList.expandGroup(i);
		}
	}

	public void setDATA(){
		cursorDiaryDate=dbAdapter.getListDateOfDiary(id_curent_user);
		if (cursorDiaryDate.getCount()<1){
			Toast.makeText(DiaryActivity.this, "You don't have any history !!", Toast.LENGTH_SHORT).show();
		}
		else {
			cursorDiaryDate.moveToFirst();
			while(!cursorDiaryDate.isAfterLast()){
				int day = cursorDiaryDate.getInt(cursorDiaryDate.getColumnIndexOrThrow("DAY"));
				int month = cursorDiaryDate.getInt(cursorDiaryDate.getColumnIndexOrThrow("MONTH"));
				int year = cursorDiaryDate.getInt(cursorDiaryDate.getColumnIndexOrThrow("YEAR"));
				
				parentItems.add(day +"/"+ month +"/"+ year);
				
				cursorDiaryData = dbAdapter.getDiaryByDate(day, month, year, id_curent_user );
				cursorDiaryData.moveToFirst();
				
				child = new ArrayList<Diary>();
				while(!cursorDiaryData.isAfterLast()){
					Diary itemData = new Diary();
					itemData.setId_diary(cursorDiaryData.getInt(cursorDiaryData.getColumnIndexOrThrow("ID_DIARY")));
					itemData.setType(cursorDiaryData.getInt(cursorDiaryData.getColumnIndexOrThrow("TYPE")));
					itemData.setName_income(cursorDiaryData.getString(cursorDiaryData.getColumnIndexOrThrow("NAME_INCOME")));
					itemData.setName_expen(cursorDiaryData.getString(cursorDiaryData.getColumnIndexOrThrow("NAME_EXP_DET")));
					itemData.setName_wallet(cursorDiaryData.getString(cursorDiaryData.getColumnIndexOrThrow("NAME_WALLET")));
					itemData.setAmount(cursorDiaryData.getInt(cursorDiaryData.getColumnIndexOrThrow("AMOUNT")));
					itemData.setNotice(cursorDiaryData.getString(cursorDiaryData.getColumnIndexOrThrow("NOTICE")));
					child.add(itemData);
					cursorDiaryData.moveToNext();
				}
				childItems.add(child);
				cursorDiaryDate.moveToNext();
			}
		}
	}
	


	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

		//Toast.makeText(DiaryActivity.this, String.valueOf(((Diary)adapter.getChild(groupPosition, childPosition)).getId_diary()), Toast.LENGTH_SHORT).show();
		Toast.makeText(DiaryActivity.this, "Meo maow meow mao", Toast.LENGTH_SHORT).show();
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
}
