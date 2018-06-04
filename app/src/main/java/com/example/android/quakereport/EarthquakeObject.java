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

    /**
     * Constructor of EarthquakeObject for EarthquakeAdapter (ReyclerView)
     * @param richterScale - parsing magnitude from JSON
     * @param cityName - parsing place from JSON
     * @param date - parsing time from JSON and giving it proper and more understandable format
     * @param urlData - parsin URL from JSON and provide it for intent to open original source of
     * data in web browser
     */
    public EarthquakeObject(Double richterScale, String cityName, long date, String urlData) {
        mRichterScale = richterScale;
        mCityName = cityName;
        mDate = date;
        //taking from timestamp only time
        Date dateObjectDate = new Date(mDate);
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateToDisplay = simpleDate.format(dateObjectDate);
        //taking from timestamp only the date
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
