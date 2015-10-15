package com.mililu.moneypower;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAdapter {
	static final String DATABASE_NAME = "DARFTMONEYPOWER.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE_ACCOUNT = "create table tbl_ACCOUNT (ID_ACCOUNT integer primary key autoincrement, USERNAME  text,PASSWORD text, FULLNAME text); ";
	static final String DATABASE_CREATE_WALLET = "create table tbl_WALLET (ID_WALLET integer primary key autoincrement, NAME_WALLET  text,ID_ACCOUNT integer, MONEY numeric, DESCRIPTION text); ";
	static final String DATABASE_CREATE_INCOME = "create table tbl_INCOME (ID_INC integer primary key autoincrement, NAME_INCOME  text); ";
	static final String DATABASE_CREATE_EXPENDITURE = "create table tbl_EXPENDITURE (ID_EXP integer primary key autoincrement, NAME_EXP text); ";
	static final String DATABASE_CREATE_EXP_DETAIL = "create table tbl_EXP_DETAIL (ID_EXP_DET integer primary key autoincrement, ID_EXP  integer, NAME_EXP_DET text); ";
	//static final String DATABASE_CREATE_DIARY_INC = "create table tbl_DIARY_INC (ID_DIA_INC integer primary key autoincrement, ID_INC integer, ID_WALLET integer, DATE text, HOUR text, MONEY numeric, NOTICE text); ";
	//static final String DATABASE_CREATE_DIARY_EXP = "create table tbl_DIARY_EXP (ID_DIA_EXP integer primary key autoincrement, ID_EXP_DET integer, ID_WALLET integer, DATE text, HOUR text, MONEY numeric, NOTICE text); ";
	static final String DATABASE_CREATE_DIARY = "CREATE TABLE `tbl_DIARY` (`ID_DIARY` INTEGER PRIMARY KEY AUTOINCREMENT, `ID_CATEGORY` INTEGER, `ID_WALLET` INTEGER, `AMOUNT` NUMERIC, `DAY` INTEGER, `MONTH` INTEGER, `YEAR` INTEGER, `TIME` TEXT, `TYPE` INTEGER, `NOTICE` TEXT);";
	static final String DATABASE_INSERT_EXPENDITURE = "insert into tbl_EXPENDITURE (ID_EXP, NAME_EXP) values (1, 'Ăn uống'), (2, 'Đi lại'), (3, 'Dịch vụ sinh hoạt'), (4, 'Hưởng thụ'); ";
	static final String DATABASE_INSERT_EXP_DET = "insert into tbl_EXP_DETAIL (ID_EXP_DET, ID_EXP, NAME_EXP_DET) values (1, 1, 'Đi chợ/siêu thị'), (2, 1, 'Cafe'), (3, 1, 'Cơm tiệm'), (4, 2, 'Gửi xe'), (5, 2, 'Xăng xe'), (6, 2, 'Rửa xe'), (7, 3, 'Điện thoại'), (8, 3, 'Điện'), (9, 3, 'Nước'), (10, 4, 'Du lịch'), (11, 4, 'Xem phim'); ";
	static final String DATABASE_INSERT_INCOME = "insert into tbl_INCOME (ID_INC, NAME_INCOME) values (1, 'Lương'), (2, 'Thưởng'), (3, 'Lãi'), (4, 'Lãi tiết kiệm'), (5, 'Được cho/tặng'), (6, 'Khác'); ";
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
	/**
	 * Insert account into database
	 * @param username
	 * @param password
	 */
	public void InsertAccount(String username,String password, String fullname)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("USERNAME", username);
		newValues.put("PASSWORD",password);
		newValues.put("FULLNAME", fullname);

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
	
	/**
	 * Get password of account from database
	 * @param username
	 * @return
	 */
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
	/**
	 * Get ID of account from table ACCOUNT 
	 * @param username
	 * @return
	 */
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
	
    public Cursor getAccountInfor(String username) {
        // Select All Query
    	// String selectQuery = "SELECT ID_ACCOUNT AS _id, * FROM tbl_ACCOUNT WHERE USERNAME = " + username;
    	// db = dbHelper.getReadableDatabase();
        //return db.rawQuery(selectQuery, null);
        
        Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        return cursor;
    }
	
	/**
	 * Check username is exit in table ACCOUNT or not 
	 * @param username
	 * @return
	 */
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
	/**
	 * Insert wallet into table WALLET
	 * @param namewallet
	 * @param money
	 * @param id_user
	 */
	public void insertWallet(String namewallet, int money, int id_user, String description){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", namewallet);
		newValues.put("ID_ACCOUNT",id_user);
		newValues.put("MONEY", money);
		newValues.put("DESCRIPTION", description);

		// Insert the row into your table
		db.insert("tbl_WALLET", null, newValues);
	}
	
	/**
	 * Update wallet from table WALLET
	 * @param id_wallet
	 * @param namewallet
	 * @param money
	 */
	public void updateWallet(int id_wallet, String namewallet, int money){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", namewallet);
		//newValues.put("ID_ACCOUNT",id_user);
		newValues.put("MONEY", money);
		
		db.update("tbl_WALLET", newValues, "ID_WALLET =? ", new String[] {String.valueOf(id_wallet)});
	}
	/**
	 * Delete wallet from table WALLET
	 * @param id_wallet
	 */
	public void deleteWallet(int id_wallet){
		db.delete("tbl_WALLET", "ID_WALLET =?", new String[] {String.valueOf(id_wallet)});
	}
	
	public int getAmountOfWallet(String id_wallet){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_WALLET=?", new String[]{id_wallet}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return -1;
        }
	    cursor.moveToFirst();
		int amount = cursor.getInt(cursor.getColumnIndex("MONEY"));
		cursor.close();
		return amount;
	}
	
	public int getTotalAmount(int id_user){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_ACCOUNT=?", new String[]{String.valueOf(id_user)}, null, null, null);
        int amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	int money = cursor.getInt(cursor.getColumnIndex("MONEY"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	   
    public Cursor getListWalletOfUser(int id_user) {
    	// Select All Query
        String selectQuery = "SELECT ID_WALLET AS _id, * FROM tbl_WALLET WHERE ID_ACCOUNT=" + id_user;
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    public Cursor getListWalletOfUser2(int id_user) {
    	Cursor cursor=db.query("tbl_WALLET", null, " ID_ACCOUNT=?", new String[]{String.valueOf(id_user)}, null, null, null);
    	return cursor;
	}
    
    public Cursor getCategoryIncomeCursor() {
        // Select All Query
        String selectQuery = "SELECT ID_INC AS _id, * FROM tbl_INCOME";
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
    public Cursor getCategoryExpenCursor() {
        // Select All Query
        String selectQuery = "SELECT ID_EXP AS _id, * FROM tbl_EXPENDITURE";
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
    public Cursor getCategoryExpenDetail(int id_exp) {
        // Select All Query
        String selectQuery = "SELECT ID_EXP_DET AS _id, * FROM tbl_EXP_DETAIL WHERE ID_EXP =" + id_exp;
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
	
    public void insertDiary(Diary diary){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("ID_CATEGORY", diary.getId_category());
		newValues.put("ID_WALLET",diary.getId_wallet());
		newValues.put("AMOUNT", diary.getAmount());
		newValues.put("DAY", diary.getDay());
		newValues.put("MONTH", diary.getMonth());
		newValues.put("YEAR", diary.getYear());
		newValues.put("TIME", diary.getTime().toString());
		newValues.put("TYPE", diary.getType());
		newValues.put("NOTICE", diary.getNotice().toString());
		// Insert the row into your table
		db.insert("tbl_DIARY", null, newValues);
	}
    
	public Cursor getDiaryByWallet(int id_wallet){
		String selectQuery = "SELECT tbl_DIARY.AMOUNT, tbl_INCOME.NAME_INCOME, tbl_EXP_DETAIL.NAME_EXP_DET, tbl_DIARY.DAY, tbl_DIARY.MONTH, tbl_DIARY.YEAR, tbl_DIARY.TIME, tbl_DIARY.TYPE, tbl_DIARY.NOTICE FROM tbl_DIARY, tbl_EXP_DETAIL, tbl_INCOME WHERE (((tbl_DIARY.TYPE = 1 AND tbl_DIARY.ID_CATEGORY = tbl_INCOME.ID_INC) OR (tbl_DIARY.TYPE = 2 AND tbl_EXP_DETAIL.ID_EXP_DET = tbl_DIARY.ID_CATEGORY)) AND (tbl_DIARY.ID_WALLET = " + id_wallet +")) GROUP BY tbl_DIARY.ID_DIARY ORDER BY tbl_DIARY.YEAR DESC, tbl_DIARY.MONTH DESC, tbl_DIARY.DAY DESC, tbl_DIARY.TIME;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
}

