package com.example.admin.offlineapplication.Activity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 3/11/2016.
 */
public class ListFeedParser implements Serializable {
    ArrayList<Feed> feed;
    public class Feed implements Serializable {
        String name,image,status;
    }
}
