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

import android.content.Context;

import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.LoaderManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthquakeObject>> {

    // URL to get JSON data for ArrayList
    final String urls = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    private ArrayList<EarthquakeObject> earthquakeList;
    ListView earthquakeListView;

    private EarthquakeAdapter earthquakeAdapter;

    ArrayList<EarthquakeObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.earthquake_activity);

        earthquakeAdapter = new EarthquakeAdapter(this,new ArrayList<EarthquakeObject>());

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this).forceLoad();

        earthquakeListView = (ListView) findViewById(R.id.list);

    }

    @Override
    public Loader<ArrayList<EarthquakeObject>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, urls);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthquakeObject>> loader, ArrayList<EarthquakeObject> earthquakeList) {
        earthquakeAdapter.clear();
        if (earthquakeList != null && !earthquakeList.isEmpty()) {
            earthquakeAdapter.addAll(earthquakeList);
            earthquakeListView.setAdapter(earthquakeAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthquakeObject>> loader) {
        earthquakeAdapter.clear();
    }
}


