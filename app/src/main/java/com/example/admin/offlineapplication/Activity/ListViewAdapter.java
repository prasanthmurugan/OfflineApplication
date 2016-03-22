package com.example.admin.offlineapplication.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.offlineapplication.R;
import com.example.admin.offlineapplication.Utils.PrintLog;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.SimpleTimeZone;
import java.util.concurrent.Executor;

/**
 * Created by Admin on 3/11/2016.
 */

public class ListViewAdapter extends BaseAdapter {
    private ListFeedParser listFeedParser;
    private  Context mContext;
    private  ImageLoaderConfiguration configuration;
    private  DisplayImageOptions displayImageOptions;
    private  ImageLoader imageLoader;
    public ListViewAdapter(Context context,ListFeedParser listFeedParser) {
        this.mContext = context;
        this.listFeedParser = listFeedParser;
         imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedVignetteBitmapDisplayer(5,10))
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return listFeedParser.feed.size();
    }

    @Override
    public Object getItem(int position) {
        return listFeedParser.feed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeedHolder feedHolder = null;
        if (convertView==null) {
            feedHolder = new FeedHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.feed_list_item_layout,null);
//            feedHolder.feedImage = (SimpleDraweeView) convertView.findViewById(R.id.feed_image);
            feedHolder.feedImage = (ImageView) convertView.findViewById(R.id.feed_image);
            feedHolder.feedName = (TextView) convertView.findViewById(R.id.feed_name);
            feedHolder.feedStatus = (TextView) convertView.findViewById(R.id.feed_status);
            feedHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progress);
            convertView.setTag(feedHolder);
        }
        else {
            feedHolder = (FeedHolder) convertView.getTag();
        }
        feedHolder.feedName.setText(listFeedParser.feed.get(position).name);
        feedHolder.feedStatus.setText(listFeedParser.feed.get(position).status);
        if(listFeedParser.feed.get(position).image!=null) {

//            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(listFeedParser.feed.get(position).image))
//                    .setProgressiveRenderingEnabled(true)
//                    .build();
//
//            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                    .setImageRequest(request)
//                    .setTapToRetryEnabled(true)
//                    .build();
//
////            ImagePipeline  imagePipeline = Fresco.getImagePipeline();
////            imagePipeline.cache(request);
//
////            DataSource<Boolean> isCached = imagePipeline.isInDiskCache(request);
////            DataSubscriber<Boolean> subscriber = new BaseDataSubscriber<Boolean>() {
////                @Override
////                protected void onNewResultImpl(DataSource<Boolean> dataSource) {
////                    if (!dataSource.isFinished()){
////                        return;
////                    }
////                    PrintLog.print("isCached=>"+dataSource.getResult());
////
////                }
////
////                @Override
////                protected void onFailureImpl(DataSource<Boolean> dataSource) {
////
////                }
////            };
////            isCached.subscribe(subscribe);
//
//            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                    .getEncodedCacheKey(request);
//            PrintLog.print("isCached=>" + ImagePipelineFactory.getInstance()
//                    .getMainDiskStorageCache().hasKey(cacheKey));
//
//            feedHolder.feedImage.setController(draweeController);

//Universal Image Loader
            final FeedHolder finalFeedHolder = feedHolder;
            imageLoader.displayImage(listFeedParser.feed.get(position).image, feedHolder.feedImage, displayImageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    finalFeedHolder.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    finalFeedHolder.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    finalFeedHolder.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    finalFeedHolder.progressBar.setVisibility(View.GONE);

                }
            });
        }else{
            feedHolder.feedImage.setImageResource(R.drawable.sample_feed_image);
        }

        return convertView;
    }

    public class FeedHolder {
//        SimpleDraweeView feedImage;
        ImageView feedImage;
        TextView feedName,feedStatus;
        ProgressBar progressBar;
    }
}
