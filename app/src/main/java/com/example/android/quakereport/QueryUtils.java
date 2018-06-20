package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by pitrs on 01.05.2018.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    public static ArrayList<EarthquakeObject> fetchEarthquakes(String requestURL) {

        URL url = createURL(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with closin inputStream", e);
        }
        ArrayList<EarthquakeObject> earthquake = extractEarthquakes(jsonResponse);
        return earthquake;
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (jsonResponse == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*miliseconds*/);
            urlConnection.setConnectTimeout(15000/*miliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            System.out.println("Response code:" + String.valueOf(urlConnection.getResponseCode()));

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving JSON data", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilderOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilderOutput.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilderOutput.toString();
    }

    /**
     * Reading through JSON response
     *
     * @return
     */
    public static ArrayList<EarthquakeObject> extractEarthquakes(String jsonResponse) {
        ArrayList<EarthquakeObject> earthquake = new ArrayList<>();
        try {
            //Reading through response from level "features"
            JSONObject reader = new JSONObject(jsonResponse);
            JSONArray feature = reader.getJSONArray("features");
            // Loop to read through all "features" nodes
            for (int i = 0; i < feature.length(); i++) {
                JSONObject o = feature.getJSONObject(i);
                //Will acquire object from what we will extract data
                JSONObject p = o.getJSONObject("properties");
                // Data for magnitude
                Double richterScale = p.getDouble("mag");
                // Data for place of earthquake
                String cityName = p.getString("place");
                // Data for time of earthquake
                Long date = p.getLong("time");
                // Data for URL of earthquake on web of ****
                String urlData = p.getString("url");
                // Parsing data to EarthquakeObject class
                earthquake.add(new EarthquakeObject(richterScale, cityName, date, urlData));
                Log.i("queryUtils", earthquake.toString());
            }
        } catch (JSONException e) {
            //In case of error Log will be printed
            Log.e("queryUtils", "Problemy with parsing the earthquake JSON result", e);
        }
        return earthquake;
    }
}
