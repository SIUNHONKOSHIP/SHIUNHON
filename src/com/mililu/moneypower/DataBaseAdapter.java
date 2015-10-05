package com.mililu.moneypower;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {
	static final String DATABASE_NAME = "DARFTMONEYPOWER.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE_ACCOUNT = "create table tbl_ACCOUNT (ID_ACCOUNT integer primary key autoincrement, USERNAME  text,PASSWORD text); ";
	static final String DATABASE_CREATE_WALLET = "create table tbl_WALLET (ID_WALLET integer primary key autoincrement, NAME_WALLET  text,ID_ACCOUNT integer, MONEY numeric); ";
	static final String DATABASE_CREATE_INCOME = "create table tbl_INCOME (ID_INC integer primary key autoincrement, NAME_INCOME  text); ";
	static final String DATABASE_CREATE_INCOME_DETAIL = "create table tbl_INC_DETAIL (ID_INC_DET integer primary key autoincrement, ID_INC integer, NAME_INC_DET text); ";
	static final String DATABASE_CREATE_EXPENDITURE = "create table tbl_EXPENDITURE (ID_EXP integer primary key autoincrement, NAME_EXP text); ";
	static final String DATABASE_CREATE_EXP_DETAIL = "create table tbl_EXP_DETAIL (ID_EXP_DET integer primary key autoincrement, ID_EXP  integer, NAME_EXP_DET text); ";
	static final String DATABASE_CREATE_DIARY_INC = "create table tbl_DIARY_INC (ID_DIA_INC integer primary key autoincrement, ID_INC_DET integer, ID_WALLET integer, DATE text, MONEY numeric); ";
	static final String DATABASE_CREATE_DIARY_EXP = "create table tbl_DIARY_EXP (ID_DIA_EXP integer primary key autoincrement, ID_EXP_DET integer, ID_WALLET integer, DATE text, MONEY numeric); ";
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  DataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public  DataBaseAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close() 
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	public void InsertAccount(String username,String password)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("USERNAME", username);
		newValues.put("PASSWORD",password);

		// Insert the row into your table
		db.insert("tbl_ACCOUNT", null, newValues);
	}
	public int deleteEntry(String UserName)
	{
		//String id=String.valueOf(ID);
	    String where="USERNAME=?";
	    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
       // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	
	public String getAccountPassword(String username)
	{
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return "NOT EXIST";
        }
	    cursor.moveToFirst();
		String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
		cursor.close();
		return password;				
	}
	
	public int getAccountId(String username)
	{
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return -1;
        }
	    cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex("ID_ACCOUNT"));
		cursor.close();
		return id;				
	}
	
	public boolean isAccountExit(String username){
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return false;
        }
        return true;
	}
	
	public void  updateEntry(String userName,String password){
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
	    db.update("LOGIN",updatedValues, where, new String[]{userName});			   
	}	
	
	public void insertWallet(String namewallet, int money, int id_user){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", namewallet);
		newValues.put("ID_ACCOUNT",id_user);
		newValues.put("MONEY", money);

		// Insert the row into your table
		db.insert("tbl_WALLET", null, newValues);
		db.close();
	}
	
	public void updateWallet(int id_wallet, String namewallet, int money){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", namewallet);
		//newValues.put("ID_ACCOUNT",id_user);
		newValues.put("MONEY", money);
		
		db.update("tbl_WALLET", newValues, "ID_WALLET =? ", new String[] {String.valueOf(id_wallet)});
	}
}
