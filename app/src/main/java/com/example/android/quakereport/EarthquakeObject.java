package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pitrs on 15.04.2018.
 */

public class EarthquakeObject {

    private double mRichterScale;
    private String mCityName;
    private long mDate;
    private String simpleDateToDisplay;
    private String simpleTimeToDisplay;
    private String mURLdata;

    public EarthquakeObject(Double richterScale, String cityName, long date, String urlData) {
        mRichterScale = richterScale;
        mCityName = cityName;
        mDate = date;
        Date dateObjectDate = new Date(mDate);
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateToDisplay = simpleDate.format(dateObjectDate);
        Date dateObjectTime = new Date(mDate);
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm:ss z");
        simpleTimeToDisplay = simpleTime.format(dateObjectTime);
        mURLdata = urlData;
    }

    public double getRichterScale() {
        return mRichterScale;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getDate() {
        return simpleDateToDisplay;
    }

    public String getTime() {
        return simpleTimeToDisplay;
    }

    public String getURL() { return mURLdata; }
}
