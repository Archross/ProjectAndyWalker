package com.nattachai;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter {
	public static final String MYDATABASE_NAME = "MY_DATABASE2";
	public static final String MYDATABASE_TABLE = "MY_TABLE";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "TaskName";
	public static final String KEY_CATEGORY = "Category";
	public static final String KEY_COMMENT = "Comment";
	public static final String KEY_DATE = "Date";


	//create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE =
	"create table " + MYDATABASE_TABLE + " ("
	+ KEY_ID + " integer primary key autoincrement, " 
	+ KEY_NAME + " text not null, "
	+ KEY_CATEGORY + " text not null, "
	+ KEY_COMMENT + " text not null, "
	+ KEY_DATE + " long not null);";

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public SQLiteAdapter(Context c){
	context = c;
	}

	public SQLiteAdapter openToRead() throws android.database.SQLException {
	sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
	sqLiteDatabase = sqLiteHelper.getReadableDatabase();
	return this;
	}

	public SQLiteAdapter openToWrite() throws android.database.SQLException {
	sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
	sqLiteDatabase = sqLiteHelper.getWritableDatabase();
	return this;
	}

	public void close(){
	sqLiteHelper.close();
	}
/*
	public long insert(String content){

	ContentValues contentValues = new ContentValues();
	contentValues.put(KEY_CONTENT, content);
	return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
	}
	*/
	
	public ArrayList<Task> getTasKList(){
		String[] columns = new String[]{KEY_ID, KEY_NAME,KEY_CATEGORY,KEY_COMMENT,KEY_DATE};
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
		  null, null, null, null, null);
		int iRow = cursor.getColumnIndex(KEY_ID);
		int iName = cursor.getColumnIndex(KEY_NAME);
		int iCategory = cursor.getColumnIndex(KEY_CATEGORY);
		int iComment = cursor.getColumnIndex(KEY_COMMENT);
		int iDate = cursor.getColumnIndex(KEY_DATE);
		ArrayList<Task> aTasks = new ArrayList<Task>(10);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			Date d = new Date( cursor.getLong(iDate));;
			Task temp = new Task(cursor.getInt(iRow),cursor.getString(iName), cursor.getString(iCategory), cursor.getString(iComment), d);
			aTasks.add(temp);
		}
		
		return aTasks;
	}
	public long insert(Task content){

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_NAME, content.getName());
		contentValues.put(KEY_CATEGORY, content.getCategory());
		contentValues.put(KEY_COMMENT, content.getComment());
		contentValues.put(KEY_DATE, content.getDate().getTime());
		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
		}

	public int deleteAll(){
	return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}

	public Cursor queueAll(){
	String[] columns = new String[]{KEY_ID, KEY_NAME,KEY_CATEGORY,KEY_COMMENT,KEY_DATE};
	Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
	  null, null, null, null, null);

	return cursor;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name,
	  CursorFactory factory, int version) {
	 super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	 // TODO Auto-generated method stub
	 db.execSQL(SCRIPT_CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 // TODO Auto-generated method stub

	}

	}

	}