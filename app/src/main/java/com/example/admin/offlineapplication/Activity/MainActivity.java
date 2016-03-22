package com.example.admin.offlineapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.admin.offlineapplication.R;
import com.example.admin.offlineapplication.Utils.PrintLog;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int NET_REQ_CODE = 0;
    private  static final String FEED_URL = "http://api.androidhive.info/feed/feed.json";
    private static final String FILE_NAME = "newFile";
    private static final String FILE_OBJ_NAME = "objFile";
    private boolean isNetEnabled = false;
    private ListView feedRecyclerView;
    private ListViewAdapter listFeedAdapter;
    //    private RecyclerView feedRecyclerView;
    private ListFeedParser listFeedParser=new ListFeedParser();
//    ListFeedAdapter listFeedAdapter;
//    private  DbHandler dbHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == NET_REQ_CODE){
                PrintLog.print("data=>"+data.getData());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Set<RequestListener> requestListeners = new HashSet<>();
//        Supplier<MemoryCacheParams> supplier = new Supplier<MemoryCacheParams>() {
//            @Override
//            public MemoryCacheParams get() {
//                return null;
//            }
//        };
//        DiskCacheConfig diskCacheConfig;
//        File file = new File(this.getExternalCacheDir(),FILE_NAME);
//        if(!file.exists()){
//            file.getParentFile().mkdir();
//        }
////        try {
////            FileOutputStream fileOutputStream = MainActivity.this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
////        PrintLog.print("file=>"+file.getAbsolutePath());
////        PrintLog.print("file_exists"+file.exists());
//            diskCacheConfig = DiskCacheConfig.newBuilder(this)
//                    .setBaseDirectoryName(FILE_NAME)
//                    .setBaseDirectoryPath(this.getFilesDir())
//                    .build();
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//
//
//        requestListeners.add(new RequestLoggingListener());
//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
//                .setRequestListeners(requestListeners)
//                .setMainDiskCacheConfig(diskCacheConfig)
//                .setImageCacheStatsTracker(new ImageCacheStatsTracker() {
//                    @Override
//                    public void onBitmapCachePut() {
//
//                    }
//
//                    @Override
//                    public void onBitmapCacheHit() {
//
//                    }
//
//                    @Override
//                    public void onBitmapCacheMiss() {
//
//                    }
//
//                    @Override
//                    public void onMemoryCachePut() {
//
//                    }
//
//                    @Override
//                    public void onMemoryCacheHit() {
//
//                    }
//
//                    @Override
//                    public void onMemoryCacheMiss() {
//
//                    }
//
//                    @Override
//                    public void onStagingAreaHit() {
//
//                    }
//
//                    @Override
//                    public void onStagingAreaMiss() {
//
//                    }
//
//                    @Override
//                    public void onDiskCacheHit() {
//                        PrintLog.print("came=>onDiskCacheHit");
//                    }
//
//                    @Override
//                    public void onDiskCacheMiss() {
//                        PrintLog.print("came=>onDiskCacheMiss");
//                    }
//
//                    @Override
//                    public void onDiskCacheGetFail() {
//                        PrintLog.print("came=>onDiskCacheGetFail");
//                    }
//
//                    @Override
//                    public void registerBitmapMemoryCache(CountingMemoryCache<?, ?> bitmapMemoryCache) {
//
//                    }
//
//                    @Override
//                    public void registerEncodedMemoryCache(CountingMemoryCache<?, ?> encodedMemoryCache) {
//
//                    }
//                })
//                .build();
//        FLog.setMinimumLoggingLevel(FLog.ERROR);
//        Fresco.initialize(this, config);

//***Universal Image Loader***
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(displayImageOptions)
                .build();

        ImageLoader.getInstance().init(configuration);
        init();
        setUpDefaults();
        setUpEvents();
    }



    private void init() {
//        feedRecyclerView = (RecyclerView)findViewById(R.id.feed_list);
        feedRecyclerView = (ListView)findViewById(R.id.feed_list);
    }

    private void setUpDefaults() {

//
        if (networkAvailable()) {
            new FeedTask().execute(FEED_URL);
        }
        else {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            if (dataBaseHelper.checkDataBase()) {
                ListFeedParser listFeedParser = dataBaseHelper.getBlobObject();

                if (listFeedParser != null) {
                    listFeedAdapter = new ListViewAdapter(this, listFeedParser);
                    feedRecyclerView.setAdapter(listFeedAdapter);

                }
            }
            else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage(getResources().getString(R.string.data_cleared));
                alert.setNeutralButton(getResources().getString(R.string.enable_net), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent networkIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivityForResult(networkIntent,NET_REQ_CODE);
                    }
                });
                alert.show();
            }

//           if(getFromFile()!=null) {
//               listFeedParser = getFromFile();
//////For Recycler view
//////            listFeedAdapter = new ListFeedAdapter(MainActivity.this,listFeedParser);
//////            feedRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//////            feedRecyclerView.setAdapter(listFeedAdapter);
//
//                listFeedAdapter =new ListViewAdapter(MainActivity.this,listFeedParser);
//               feedRecyclerView.setAdapter(listFeedAdapter);
//
//           }
//           else {
//              AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//               alert.setMessage(getResources().getString(R.string.data_cleared));
//                alert.setNeutralButton(getResources().getString(R.string.enable_net), new DialogInterface.OnClickListener() {
//                    @Override
//                   public void onClick(DialogInterface dialog, int which) {
//                       Intent networkIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                       startActivityForResult(networkIntent,NET_REQ_CODE);
//                    }
//                });
//                alert.show();
//            }
        }
    }


    private boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        PrintLog.print("network=>"+networkInfo);
        return networkInfo!=null;
    }

    private void setUpEvents() {

    }

    public class FeedTask extends AsyncTask<String,Void,String>{
        String jasonData="",data="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                InputStream inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((data=bufferedReader.readLine())!=null){
                    jasonData+=data+"\n";
                }
                PrintLog.print("jasonData=>"+jasonData);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jasonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            listFeedParser = gson.fromJson(s,ListFeedParser.class);
            listFeedParser.feed.add(11,listFeedParser.feed.get(1));
            listFeedParser.feed.add(12,listFeedParser.feed.get(2));
            listFeedParser.feed.add(13,listFeedParser.feed.get(3));
            listFeedParser.feed.add(14,listFeedParser.feed.get(4));
            listFeedParser.feed.add(15,listFeedParser.feed.get(5));
            listFeedParser.feed.add(16,listFeedParser.feed.get(6));
            listFeedParser.feed.add(17,listFeedParser.feed.get(7));
            listFeedParser.feed.add(18,listFeedParser.feed.get(8));
            listFeedParser.feed.add(19,listFeedParser.feed.get(9));
            listFeedParser.feed.add(20,listFeedParser.feed.get(10));
            PrintLog.print("parserSize=>" + listFeedParser.feed.size());

//            listFeedAdapter = new ListFeedAdapter(MainActivity.this,listFeedParser);
//            saveToFile(listFeedParser);
//            feedRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            feedRecyclerView.setAdapter(listFeedAdapter);

            listFeedAdapter = new ListViewAdapter(MainActivity.this,listFeedParser);
//            saveToFile(listFeedParser);
            saveToDataBase(listFeedParser);
            feedRecyclerView.setAdapter(listFeedAdapter);
        }
    }

    private void saveToFile(ListFeedParser listFeedParser) {
        try {
            File file = new File(this.getExternalCacheDir(),FILE_OBJ_NAME);
//            if (file.exists())
//            {
            file.getParentFile().mkdirs();
//            }
//            FileOutputStream fileOutputStream = MainActivity.this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            PrintLog.print("fileDir=>"+this.getExternalCacheDir());
            PrintLog.print("file-path=>" + file.getPath());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(listFeedParser);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    private ListFeedParser getFromFile(){
        ListFeedParser listParser = null;
        try {
            FileInputStream fileInputStream = new  FileInputStream(FILE_OBJ_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            listParser = (ListFeedParser) objectInputStream.readObject();
            objectInputStream.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return listParser;
    }

    private void saveToDataBase(ListFeedParser listFeedParser) {

        DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
        try {
            helper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i=0;i<listFeedParser.feed.size();i++){
        helper.insertOfflineContent(listFeedParser);

//        }
//            dbHandler.get();
    }


}
