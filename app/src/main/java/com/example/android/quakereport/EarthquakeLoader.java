package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthquakeObject>> {

    private String mUrl;

    public EarthquakeLoader (Context context, String urls) {
        super (context);
        mUrl = urls;
    }

    @Override
    public ArrayList<EarthquakeObject> loadInBackground() {
        Log.i("Loader","loaded");
        if (mUrl == null) {
            return null;
        }
        ArrayList<EarthquakeObject> earthquakeList = QueryUtils.fetchEarthquakes(mUrl);
        return earthquakeList;
    }
}


