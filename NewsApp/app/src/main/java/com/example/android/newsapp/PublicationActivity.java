package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PublicationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Publication>> {

    public static final String LOG_TAG = PublicationActivity.class.getName();
        private static final String BASE_QUERY_URL = "http://content.guardianapis.com/search?";

    /**
     * Adapter for the list of publications
     */
    private PublicationAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private static final int PUBLICATION_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView publicationListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty);

        publicationListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of publications
        mAdapter = new PublicationAdapter(this, new ArrayList<Publication>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        publicationListView.setAdapter(mAdapter);

        publicationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Publication currentPublication = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentPublication.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PUBLICATION_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View circle = findViewById(R.id.loading_spinner);
            circle.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Publication>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String fields = "trailText,thumbnail,shortUrl";
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        String searchContent = sharedPrefs.getString(
                getString(R.string.settings_search_content_key),
                getString(R.string.settings_search_content_default));

        Uri baseUri = Uri.parse(BASE_QUERY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-fields", fields);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("q", searchContent);
        uriBuilder.appendQueryParameter("api-key", "test");
        Log.e(LOG_TAG, uriBuilder.toString());
        return new PublicationLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Publication>> loader, List<Publication> publications) {

        // Hide loading indicator because the data has been loaded
        View circle = findViewById(R.id.loading_spinner);
        circle.setVisibility(View.GONE);

        // Set empty state text to display "No News found."
        mEmptyStateTextView.setText(R.string.empty_state);

        // Clear the adapter of previous earthquake data
        Log.e(LOG_TAG, " onLoadFinished");
        mAdapter.clear();

        // If there is a valid list of {@link Publication}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (publications != null && !publications.isEmpty()) {
            mAdapter.addAll(publications);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Publication>> loader) {
        Log.e(LOG_TAG, " onLoaderReset");
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}