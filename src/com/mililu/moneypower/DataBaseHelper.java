package com.mililu.moneypower;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	public DataBaseHelper(Context context, String name,CursorFactory factory, int version) 
    {
	           super(context, name, factory, version);
	}
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase _db) 
	{
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_ACCOUNT);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_WALLET);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_INCOME);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_EXPENDITURE);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_EXP_DETAIL);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_DIARY_INC);
			_db.execSQL(DataBaseAdapter.DATABASE_CREATE_DIARY_EXP);
			_db.execSQL(DataBaseAdapter.DATABASE_INSERT_INCOME);
			_db.execSQL(DataBaseAdapter.DATABASE_INSERT_EXPENDITURE);
			_db.execSQL(DataBaseAdapter.DATABASE_INSERT_EXP_DET);
	}
	// Called when there is a database version mismatch meaning that the version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) 
	{
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
 
			// Upgrade the existing database to conform to the new version. Multiple
			// previous versions can be handled by comparing _oldVersion and _newVersion
			// values.
			// The simplest case is to drop the old table and create a new one.
			_db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
			// Create a new one.
			onCreate(_db);
	}

}
