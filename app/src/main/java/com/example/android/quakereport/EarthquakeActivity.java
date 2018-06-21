/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthquakeObject>> {

    // URL to get JSON data for ArrayList
    final String urls = "https://earthquake.usgs.gov/fdsnws/event/1/query?";

    private ArrayList<EarthquakeObject> earthquakeList;
    private ListView earthquakeListView;

    private EarthquakeAdapter earthquakeAdapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    private TextView emptyView;

    private ProgressBar loadingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        loadingCircle = findViewById(R.id.loading_spinner);
        loadingCircle.setVisibility(View.VISIBLE);

        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<EarthquakeObject>());

        earthquakeListView = (ListView) findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();

        if (nInfo != null && nInfo.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this).forceLoad();
        } else {
            emptyView.setText(R.string.no_internet);
            loadingCircle.setVisibility(View.GONE);
        }

    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
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

    @Override
    public Loader<ArrayList<EarthquakeObject>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(urls);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new EarthquakeLoader(this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthquakeObject>> loader, ArrayList<EarthquakeObject> earthquakeList) {
        Log.i("Loader", "finished");
        earthquakeAdapter.clear();
        if (earthquakeList != null && !earthquakeList.isEmpty()) {
            earthquakeAdapter.addAll(earthquakeList);
            earthquakeListView.setAdapter(earthquakeAdapter);
            loadingCircle.setVisibility(View.GONE);
        } else {
            loadingCircle.setVisibility(View.GONE);
            emptyView.setText(R.string.no_content);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthquakeObject>> loader) {
        Log.i("Loader", "reseted");
        earthquakeAdapter.clear();
    }
}


