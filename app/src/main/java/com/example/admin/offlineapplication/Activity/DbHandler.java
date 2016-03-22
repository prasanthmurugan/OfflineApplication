//package com.example.admin.offlineapplication.Activity;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.example.admin.offlineapplication.Utils.PrintLog;
//
//import java.io.IOException;
//
///**
// * Created by Admin on 3/17/2016.
// */
//public class DbHandler {
//
//    private DbHelper dbHelper;
//    private String dbPath;
//
//    public DbHandler(Context context) throws IOException {
//        dbHelper = new DbHelper(context,TableConstants.DB_NAME,null,TableConstants.DB_VERSION);
//        dbPath = ""+dbHelper.path+""+TableConstants.DB_NAME;
//    }
//
//    public void insert(ListFeedParser.Feed feed)
//    {
////        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
//        ContentValues values = new ContentValues();
//        values.put(TableConstants.TAB_COL_NAME,feed.name);
//        values.put(TableConstants.TAB_COL_STATUS,feed.status);
//        byte[] bytes = new byte[1024];
//
////        values.put(TableConstants.TAB_COL_IMAGE,feed.image);
//        database.insert(TableConstants.TAB_NAME,null,values);
////        PrintLog.print("Values=>"+values);
////        PrintLog.print("id=>"+row_id);
////        return (int)row_id;
//
//
//
//
//
//
//
//    }
//
//    public void get(){
////        SQLiteDatabase database = dbHelper.getReadableDatabase();
//        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
//        Cursor cursor = database.rawQuery("select * from " + TableConstants.TAB_NAME, null);
//        if (cursor.moveToFirst())
//            do {
//            PrintLog.print("cursor=>" + cursor.getString(cursor.getColumnIndex(TableConstants.TAB_COL_IMAGE)));
//        }while (cursor.moveToNext());
//        cursor.close();
//    }
//
//}
