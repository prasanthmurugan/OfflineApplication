//package com.example.admin.offlineapplication.Activity;
//
//import android.annotation.TargetApi;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.primaximmo.R;
//import com.android.primaximmo.base.BaseActivity;
//import com.android.primaximmo.common.DataBaseHelper;
//import com.android.primaximmo.preferences.OfflinePreference;
//import com.android.primaximmo.constants.Constants;
//import com.android.primaximmo.home.HomeActivity2;
//import com.android.primaximmo.menu1.SubscribeActivity;
//import com.android.primaximmo.menu1.home_listing_details.DetailsMenuActivity;
//import com.android.primaximmo.menu1.home_listing_details.my_alerts.MyAlertActivity;
//import com.android.primaximmo.menu1.home_listing_details.my_annonces.MyAnnoncesActivity;
//import com.android.primaximmo.menu1.home_listing_details.my_messages.MyMessageActivity;
//import com.android.primaximmo.menu1.post_ad.PostFreeAddActivity;
//import com.android.primaximmo.utils.AlertUtils;
//import com.android.primaximmo.utils.DeviceUtils;
//import com.android.primaximmo.utils.FileUtils;
//import com.android.primaximmo.utils.PrintLog;
//import com.android.primaximmo.utils.TextUtils;
//import com.android.primaximmo.webservice.PmoHttpClient;
//import com.android.primaximmo.webservice.PmoRequestHandler;
//import com.android.primaximmo.webservice.Webservice;
//import com.github.clans.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionMenu;
//import com.google.gson.Gson;
//import com.loopj.android.http.RequestParams;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//
//public class HomeListingActivity extends BaseActivity implements Constants {
//    public static String ANNOUNCE_ITEM = "com.android.primaximmo.menu1.home_listing.ANNOUNCE_ITEM";
//
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private PmoHttpClient pmoHttpClient;
//    private RecyclerView mRecyclerView;
//    private HomeListingAdapter_recyler1 adapter;
//    private ArrayList<NearBySearchParser.NearbySearchListing> nearbySearchListings;
//    private boolean mLastItemVisible = false;
//    private int mTotalCount = 0, mCurrentCount = 0;
//    private boolean isWebserviceRunning;
//    private RelativeLayout pmoProgressDialog;
//    private ProgressBar mLoadMoreProgressBar;
//    private int firstVisibleItem, visibleItemCount, totalItemCount;
//    private  LinearLayoutManager mLayoutManager;
//    private TextView layoutNoResultFound;
//
//    RelativeLayout rootLayout;
//    EditText search;
//    boolean isEdtSearch = false;
//    FloatingActionMenu floatingMenu;
//    private FloatingActionButton fabMyAnnonces, fabMesMessage ,fabMesAlerts,fabPostAdd,
//            fabSubscribe;
//    private OfflinePreference saveDataInFile;
//    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){
//         if(requestCode == REQUEST_POSTAD_SCREEN) {
//             floatingMenu.toggle(true);
//            HomeActivity2.setLoginVauesinHomePage();
//         }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_property_view_layout_recycler);
//        init();
//        setupDefault();
//        setupEvent();
//    }
//
//    private void init() {
//        DataBaseHelper temp=new DataBaseHelper(this);
//        try{
//            temp.createDatabase();
//        }catch(Exception e)
//        {
//            Toast.makeText(getApplicationContext(), "Error db" + e, Toast.LENGTH_LONG).show();
//        }
//
//
//        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
//
//        layoutNoResultFound = (TextView)findViewById(R.id.txt_no_results_found);
//        pmoProgressDialog = (RelativeLayout) findViewById(R.id.progress_bar_dim_layout);
//        DeviceUtils.setAnimatingProgressDialog(HomeListingActivity.this, pmoProgressDialog);
//        mLoadMoreProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setRefreshing(false);
//        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
//        mLoadMoreProgressBar.setVisibility(View.GONE);
//        search = (EditText)findViewById(R.id.search);
//
//        floatingMenu = (FloatingActionMenu)findViewById(R.id.floating_menu);
//        fabMyAnnonces = (FloatingActionButton) findViewById(R.id.fab_annonces);
//        fabMesMessage = (FloatingActionButton) findViewById(R.id.fab_my_messages);
//        fabMesAlerts = (FloatingActionButton) findViewById(R.id.fab_my_alerts);
//        fabPostAdd = (FloatingActionButton) findViewById(R.id.fab_post_ad);
//
//        fabSubscribe = (FloatingActionButton) findViewById(R.id.fab_subscribe);
//        saveDataInFile = new OfflinePreference(HomeListingActivity.this);
//
//    }
//
//    private void setupDefault() {
//        floatingMenu.setClosedOnTouchOutside(true);
//        pmoHttpClient=new PmoHttpClient(HomeListingActivity.this);
//        nearbySearchListings = new ArrayList<>();
//        adapter = new HomeListingAdapter_recyler1(this,nearbySearchListings,homeListItemClickListener);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
//
//
//      /*  if (((( saveDataInFile.readFromFile(getResources().getString(R.string.offline_storage_file_home_listing), Constants.OFFLINE_HOME_SCREEN))) != null) && (!( saveDataInFile.readFromFile(getResources().getString(R.string.offline_storage_file_home_listing), Constants.OFFLINE_HOME_SCREEN)).equals(""))) {
//            try {
//                nearbySearchListings.addAll((ArrayList<NearBySearchParser.NearbySearchListing>) saveDataInFile.readFromFile(getResources().getString(R.string.offline_storage_file_home_listing), Constants.OFFLINE_HOME_SCREEN));
//                adapter.updateList(nearbySearchListings);
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        } else {
//            PrintLog.print("Db Latest Questions null");
//        }*/
//
//        String DB_PATH="/data/data/com.android.primaximmo/databases/primaximmo_offline_db";
//        final SQLiteDatabase mydb1 = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
//        final Cursor c1 = mydb1.rawQuery("select * from offline_screen", null);
//        c1.moveToFirst();
//        boolean is_db_has_offline_home_lising = false;
//        if (c1.getCount() != 0) {
//            while (!c1.isAfterLast()) {
//                if(!TextUtils.isNullOrEmpty(String.valueOf(c1.getBlob(c1.getColumnIndex("home_listing")).toString().trim())))
//                { is_db_has_offline_home_lising = true;
//                    PrintLog.print("DB :" + String.valueOf(c1.getBlob(c1.getColumnIndex("home_listing"))));
//                    try {
//                        Object obj = TextUtils.toObject(c1.getBlob(c1.getColumnIndex("home_listing")));
//                        nearbySearchListings.addAll((ArrayList<NearBySearchParser.NearbySearchListing>)obj);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//                c1.moveToNext();
//            }
//        }
//        c1.close();
//        mydb1.close();
//        adapter.updateList(nearbySearchListings);
//        if(AlertUtils.isInternetConnected(HomeListingActivity.this)) {
//            nearbySearchListings.clear();
//            rootLayout.setVisibility(View.GONE);
//            getNearBySearch(true, false,false);
//        }else{
//            if(true)
//            AlertUtils.showToast(this, getResources().getString(R.string.no_internet_connection));
//            else{
//                AlertUtils.showAlert(this,getResources().getString(R.string.no_internet_connection));
//                setResult(RESULT_CANCELED);
//                forceFinish();
//            }
//        }
//        if(!getApp().getUserPreference().getISLoggedIn()){
//            fabMesAlerts.setVisibility(View.GONE);
//            fabMesMessage.setVisibility(View.GONE);
//            fabMyAnnonces.setVisibility(View.GONE);
//            fabPostAdd.setVisibility(View.VISIBLE);
//            fabSubscribe.setVisibility(View.VISIBLE);
//        }else{
//            fabMesAlerts.setVisibility(View.VISIBLE);
//            fabMesMessage.setVisibility(View.VISIBLE);
//            fabMyAnnonces.setVisibility(View.VISIBLE);
//            fabPostAdd.setVisibility(View.VISIBLE);
//            fabSubscribe.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    private void loadMore() {
//
//        if (mCurrentCount < mTotalCount) {
//            mLoadMoreProgressBar.setVisibility(View.VISIBLE);
//            if(AlertUtils.isInternetConnected(HomeListingActivity.this)) {
//                if(search.getText().toString().trim().length()>0){
//                    isEdtSearch = true;
//                } else {
//                    isEdtSearch = false;
//                }
//                getNearBySearch(false, true,isEdtSearch);
//
//            }else{
//                AlertUtils.showToast(this, getResources().getString(R.string.no_internet_connection));
//            }
//        } else {
//            mLoadMoreProgressBar.setVisibility(View.GONE);
//        }
//    }
//
//    private View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent mIntent;
//            switch (v.getId()) {
//                case R.id.fab_post_ad:
//                     mIntent = new Intent(HomeListingActivity.this, PostFreeAddActivity.class);
//                    if(getApp().getUserPreference().getISLoggedIn()) {
//                        if(getApp().getUserPreference().getProfileType()== 1)
//                            mIntent.putExtra("login_type", HomeActivity2.LOGIN_PARTICULAR);
//                        else if(getApp().getUserPreference().getProfileType()== 2)
//                            mIntent.putExtra("login_type", HomeActivity2.LOGIN_PROFESSIONAL);
//                    }
//
//                    else
//                        mIntent.putExtra("login_type",HomeActivity2. LOGOUT);
//                    startActivityForResult(mIntent,HomeActivity2. REQUEST_POSTAD_SCREEN);
//                    break;
//                case R.id.fab_annonces:
//                    mIntent = new Intent(HomeListingActivity.this, MyAnnoncesActivity.class);
//                    startActivity(mIntent);
//                    break;
//                case R.id.fab_my_messages:
//                    mIntent = new Intent(HomeListingActivity.this, MyMessageActivity.class);
//                    startActivity(mIntent);
//                    break;
//                case R.id.fab_my_alerts:
//                    mIntent = new Intent(HomeListingActivity.this, MyAlertActivity.class);
//                    startActivity(mIntent);
//                    break;
//                case R.id.fab_subscribe:
//                   mIntent = new Intent(HomeListingActivity.this, SubscribeActivity.class);
//                    startActivity(mIntent);
//                    break;
//            }
//        }
//    };
//
//    private void setupEvent() {
//        fabMyAnnonces.setOnClickListener(clickListener);
//        fabMesMessage.setOnClickListener(clickListener);
//        fabMesAlerts.setOnClickListener(clickListener);
//        fabPostAdd.setOnClickListener(clickListener);
//        fabSubscribe.setOnClickListener(clickListener);
//
//
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                isEdtSearch = true;
//                getNearBySearch(false, false, isEdtSearch);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshItems();
//            }
//        });
//        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                setResult(RESULT_CANCELED);
//                forceFinish();
//            }
//        });
//
//
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                visibleItemCount = mRecyclerView.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
//                mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
//
//                if (!isWebserviceRunning && mLastItemVisible) {
//                    loadMore();
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            }
//        });
//    }
//
//
//    private void checkForLoadMore() {
//        if (mCurrentCount >= mTotalCount) {
//            mLoadMoreProgressBar.setVisibility(View.GONE);
//        }
//    }
//
//    public void getNearBySearch(final boolean showProgressBar,final boolean isLoadMore,boolean isEdtSearch){
//
//        if(showProgressBar){
//            pmoProgressDialog.setVisibility(View.VISIBLE);
//        }
//        RequestParams params  = new RequestParams();
//        if(isEdtSearch && !TextUtils.isNullOrEmpty(search.getText().toString().trim())){
//            params.put("search",search.getText().toString().trim());
//            params.put("isSearch",1);
//        }
//        if (isLoadMore) {
//            params.put("start", mCurrentCount);
//        }
//       /* params.put("latitude",47.0000);
//        params.put("longitude",2.0000);*/
//        isWebserviceRunning = true;
//        pmoHttpClient.performRequest(Webservice.NEARBY, PmoHttpClient.HttpMethod.GET, params, new PmoRequestHandler() {
//            @Override
//            public void onSuccess(String content) {
//                pmoProgressDialog.setVisibility(View.GONE);
//                rootLayout.setVisibility(View.VISIBLE);
//                floatingMenu.setVisibility(View.VISIBLE);
//
//                Gson gson = new Gson();
//                NearBySearchParser nearBySearchParser = gson.fromJson(content, NearBySearchParser.class);
//                PrintLog.print("Response: " + content);
//                if (nearBySearchParser.nearbySearchListing.size() > 0) {
//                    mTotalCount = Integer.parseInt(nearBySearchParser.meta.totalCount);
//                    if (isLoadMore) {
//                        mLoadMoreProgressBar.setVisibility(View.GONE);
//                        mCurrentCount += nearbySearchListings.size();
//
//                        // for (NearBySearchParser.NearbySearchListing item : nearBySearchParser.nearbySearchListing) {
//                        nearbySearchListings.addAll(nearBySearchParser.nearbySearchListing);
//                        // }
//                        adapter.updateList(nearbySearchListings);
//                    } else {
//                        mCurrentCount = nearbySearchListings.size();
//                        nearbySearchListings = nearBySearchParser.nearbySearchListing;
//                        adapter.updateList(nearbySearchListings);
//                    }
//
//
//                /*  FileUtils.deleteTempOfflineStorage(HomeListingActivity.this, getResources().getString(R.string.offline_storage_file_home_listing));//delete old offine storage history
//                    saveDataInFile.storeOnFile(getResources().getString(R.string.offline_storage_file_home_listing), Constants.OFFLINE_HOME_SCREEN, nearbySearchListings);*/
//                            mRecyclerView.setVisibility(View.VISIBLE);
//
//                    dataBaseHelper.insertOfflineContent(getResources().getString(R.string.offline_storage_file_home_listing),(Object)nearbySearchListings);
//                    layoutNoResultFound.setVisibility(View.GONE);
//                } else {
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    layoutNoResultFound.setVisibility(View.VISIBLE);
//                }
//                checkForLoadMore();
//                isWebserviceRunning = false;
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error, String message) {
//                pmoProgressDialog.setVisibility(View.GONE);
//
//                isWebserviceRunning = false;
//                AlertUtils.showAlert(HomeListingActivity.this, message);
//            }
//        });
//    }
//
//
//    HomeListItemClickListener homeListItemClickListener = new HomeListItemClickListener() {
//        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void homeListItemClickListener(NearBySearchParser.NearbySearchListing item, View itemView) {
//            Intent intent = new Intent(new Intent(HomeListingActivity.this, DetailsMenuActivity.class));
//            intent.putExtra(HomeListingActivity.ANNOUNCE_ITEM,item);
//             startActivity(intent);
//
//
//
//        }
//    };
//
//    void refreshItems() {
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (AlertUtils.isInternetConnected(HomeListingActivity.this)) {
//                    getNearBySearch(false, false, isEdtSearch);
//                } else {
//                    AlertUtils.showToast(HomeListingActivity.this, getResources().getString(R.string.no_internet_connection));
//                }
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(floatingMenu.isOpened())
//            floatingMenu.toggle(true);
//    }
//
//    @Override
//    public void onBackPressed() {
//      //  super.onBackPressed();
//        if(floatingMenu.isOpened())
//           floatingMenu.toggle(true);
//        else {
//            setResult(RESULT_CANCELED  );
//            forceFinish();
//
//        }
//    }
//
//}
