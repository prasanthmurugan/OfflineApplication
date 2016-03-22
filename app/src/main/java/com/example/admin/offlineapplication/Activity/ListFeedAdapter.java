package com.example.admin.offlineapplication.Activity;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.offlineapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Admin on 3/11/2016.
 */
//public class ListFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    Context mContext;
//    ListFeedParser listFeedParser=new ListFeedParser();
//    public ListFeedAdapter(Context context, ListFeedParser listFeedParser) {
//        mContext = context;
//        this.listFeedParser = listFeedParser;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_list_item_layout,null);
//        return new FeedViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            FeedViewHolder feedViewHolder = (FeedViewHolder)holder;
//            feedViewHolder.feedStatus.setText(listFeedParser.feed.get(position).status);
//            feedViewHolder.feedName.setText(listFeedParser.feed.get(position).name);
//            if (listFeedParser.feed.get(position).image!=null){
//                feedViewHolder.feedImage.setImageURI(Uri.parse(listFeedParser.feed.get(position).image));
//            }
//    }
//
//    @Override
//    public int getItemCount() {
//        return listFeedParser.feed.size();
//    }
//
//    public class FeedViewHolder extends RecyclerView.ViewHolder {
//        SimpleDraweeView feedImage;
//        TextView feedName,feedStatus;
//        public FeedViewHolder(View itemView) {
//            super(itemView);
//            feedImage = (SimpleDraweeView)itemView.findViewById(R.id.feed_image);
//            feedName = (TextView)itemView.findViewById(R.id.feed_name);
//            feedStatus  = (TextView)itemView.findViewById(R.id.feed_status);
//        }
//    }
//
//    public void upDateParser(ListFeedParser listFeedParser)
//    {
//        this.listFeedParser = listFeedParser;
//    }
//}
