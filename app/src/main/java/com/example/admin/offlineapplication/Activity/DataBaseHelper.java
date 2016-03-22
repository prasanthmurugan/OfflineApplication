package com.example.admin.offlineapplication.Activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.admin.offlineapplication.Utils.PrintLog;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressLint("SdCardPath")


public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH="/data/data/com.example.admin.offlineapplication/databases/";
	private static String DB_NAME="offlineDatabase";
	private static String FIELD_NAME="object";
	private static String TABLE_NAME="Feed";
	private final Context myContext;
	Gson gson;

	public DataBaseHelper(Context context)
	{
		super(context,DB_NAME,null,2);
		this.myContext = context;
		gson = new Gson();
	}

	public void createDatabase()throws IOException {
		boolean dbExist=checkDataBase();
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

		PrintLog.print("checkDatabase=>" + dbExist);
		if(dbExist){

		}
		else
		{
			this.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put(FIELD_NAME, "");

//			try{
				copyDataBase();
//			}
//			catch(Exception e){
//				throw new Error("Error Copying Database");
//			}
			sqLiteDatabase.insert(TABLE_NAME, null, values);
			this.close();
		}
	}

	public void insertOfflineContent(Object content){
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READWRITE);

		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_NAME,gson.toJson(content).getBytes());

//		if(activityName.equalsIgnoreCase(myContext.getResources().getString(R.string.offline_storage_file_home_listing))) {
//			try {
//				contentValues.put("home_listing", TextUtils.toByteArray(content));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

//		sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

		sqLiteDatabase.update(TABLE_NAME, contentValues,null,null);
	}

	public ListFeedParser getBlobObject() {
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		String selectQuery = "select * from " + TABLE_NAME;
		PrintLog.print("selectQuery=>" + selectQuery);
		Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
		PrintLog.print("cursor_count=>" + cursor.getCount());
		ListFeedParser feedParser = null;

		if (cursor.moveToFirst()) {
			do {
				byte[] bytes = cursor.getBlob(cursor.getColumnIndex(FIELD_NAME));
				String jason = new String(bytes);
				feedParser = gson.fromJson(jason, ListFeedParser.class);
			} while (cursor.moveToNext());
		}

		return feedParser;
	}

	public void updateData(String address1, String address2, String city, String state, String phone_number, String email, String pincode) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("address1", address1);
		contentValues.put("address2", address2);
		contentValues.put("pincode", pincode);
		contentValues.put("city",city);
		contentValues.put("state", state);
		contentValues.put("mobile", phone_number);
		sqLiteDatabase.update("register", contentValues, "email=?", new String[]{email});
	}

	public void copyDataBase()throws IOException {
		InputStream mydb = myContext.getAssets().open(DB_NAME);
		String dbname = DB_PATH + DB_NAME;
		PrintLog.print("dbname=>"+dbname);
		OutputStream newdb=new FileOutputStream(dbname);
		byte[] buff=new byte[1024];
		int len=buff.length;
		while((len=mydb.read(buff))>0){
			newdb.write(buff,0,len);
		}
		newdb.flush();
		mydb.close();
		newdb.close();
	}

	public boolean checkDataBase(){
		File dbname=new File(DB_PATH+DB_NAME);
		PrintLog.print("FilePath=>"+dbname.getPath());
		return dbname.exists();
	}

	@Override
	public void onCreate (SQLiteDatabase db){

	}

	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

	}
}
