package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class PublicationLoader extends AsyncTaskLoader<List<Publication>> {

    /** Tag for log messages */
    private static final String LOG_TAG = PublicationLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public PublicationLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, " start loading");
    }

    @Override
    public List<Publication> loadInBackground() {
        // Perform the network request, parse the response, and extract a list of publications.
        List<Publication> publications= QueryUtils.fetchPublicationData(mUrl);

        Log.v(LOG_TAG, " load in background");
        return publications;
    }
}