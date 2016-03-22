//package com.example.admin.offlineapplication.Activity;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.support.design.widget.TabLayout;
//
//import com.example.admin.offlineapplication.Utils.PrintLog;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * Created by Admin on 3/17/2016.
// */
//public class DbHelper extends SQLiteOpenHelper {
//
//    public String path="";
//    private Context mContext;
//    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) throws IOException {
//        super(context, name, factory, version);
//        mContext = context;
//        path = context.getExternalCacheDir().getPath()+"/";
//        PrintLog.print("Cachepath=>"+path);
//        createDatabase();
//    }
//
//    public void createDatabase() throws IOException {
//        boolean fileExists=checkDatabaseExists();
//        if (!fileExists){
//            copyDatabase();
//        }
//    }
//
//    private void copyDatabase() throws IOException {
//        InputStream inputStream = mContext.getAssets().open(TableConstants.DB_NAME);
//        String db_name = path+TableConstants.DB_NAME;
//        OutputStream outputStream = new FileOutputStream(db_name);
//        byte[] buffer= new byte[1024];
//        int len = buffer.length;
//        if ((len=inputStream.read(buffer))>0)
//        {
//            outputStream.write(buffer,0,len);
//        }
//        outputStream.flush();
//        outputStream.close();
//    }
//
//    public boolean checkDatabaseExists(){
//        File file = new File(path+TableConstants.DB_NAME);
//        return file.exists();
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        String query="Create Table "+TableConstants.TAB_NAME+"("+TableConstants.TAB_COL_NAME+" text,"+TableConstants.TAB_COL_STATUS+" text"
////                +")";
//////                +","+ TableConstants.TAB_COL_IMAGE+" BLOB"+")";
////        PrintLog.print("query:" + query);
////        db.execSQL(query);
////        PrintLog.print("DbPath:" + db.getPath());
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////        db.execSQL("drop table if exists "+TableConstants.TAB_NAME);
////        onCreate(db);
//    }
//
//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////        db.execSQL("drop table if exists " + TableConstants.TAB_NAME);
////        onCreate(db);
////        super.onDowngrade(db, oldVersion, newVersion);
//    }
//}
