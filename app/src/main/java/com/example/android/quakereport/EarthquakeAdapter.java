package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by pitrs on 15.04.2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeObject> {

    private List<EarthquakeObject> models = new ArrayList<>();

    TextView viewNearTo;
    TextView viewCityName;
    double currentRichterScale;
    int magnitudeColorResID = R.color.magnitude1;

    public EarthquakeAdapter(Activity context, ArrayList<EarthquakeObject> earthquakes) {
        super (context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list, parent, false);
        }

         TextView viewRichterScale = listItemView.findViewById(R.id.view_rychter_scale);
         viewNearTo = listItemView.findViewById(R.id.view_near_to);
         viewCityName = listItemView.findViewById(R.id.view_city_name);
         TextView viewDate = listItemView.findViewById(R.id.view_date);
         TextView viewTime = listItemView.findViewById(R.id.view_time);
         LinearLayout earthquakeBox = listItemView.findViewById(R.id.earthquake_box);

         GradientDrawable magnitudeDrawable;


        /**
         * Binding data to variable views
         * @param itemModel
         */
        final EarthquakeObject currentEarthObject = getItem(position);

            currentRichterScale = currentEarthObject.getRichterScale();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            viewRichterScale.setText(decimalFormat.format(currentRichterScale));
            magnitudeDrawable = (GradientDrawable) viewRichterScale.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            getMagnitudeColor();
            magnitudeDrawable.setColor(ContextCompat.getColor(viewRichterScale.getContext(), magnitudeColorResID));
            splitCityName(currentEarthObject);
            viewDate.setText(currentEarthObject.getDate());
            viewTime.setText(currentEarthObject.getTime());
            // Giving each item onclick listener to open in intent attached URL
            earthquakeBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openUrl = new Intent(Intent.ACTION_VIEW);
                    openUrl.setData(Uri.parse(currentEarthObject.getURL()));
                    convertView.getContext().startActivity(openUrl);
                }
            });

            return listItemView;
        }

        /**
         * Changing background of magnitude view according to measured magnitude
         */
        private void getMagnitudeColor() {
            int magnitudeFloor = (int) Math.floor(currentRichterScale);
            switch (magnitudeFloor) {
                case 1:
                    magnitudeColorResID = R.color.magnitude1;
                    break;
                case 2:
                    magnitudeColorResID = R.color.magnitude2;
                    break;
                case 3:
                    magnitudeColorResID = R.color.magnitude3;
                    break;
                case 4:
                    magnitudeColorResID = R.color.magnitude4;
                    break;
                case 5:
                    magnitudeColorResID = R.color.magnitude5;
                    break;
                case 6:
                    magnitudeColorResID = R.color.magnitude6;
                    break;
                case 7:
                    magnitudeColorResID = R.color.magnitude7;
                    break;
                case 8:
                    magnitudeColorResID = R.color.magnitude8;
                    break;
                case 9:
                    magnitudeColorResID = R.color.magnitude9;
                    break;
                default:
                    magnitudeColorResID = R.color.magnitude10plus;
                    break;
            }
        }

        private void splitCityName(EarthquakeObject currentEarthObject) {
            String stringToSplit = currentEarthObject.getCityName();
            Log.i("City Name", stringToSplit);
            if (stringToSplit.contains("of")) {
                String parts[] = stringToSplit.split("of ");
                String part1 = parts[0];
                String part2 = parts[1];
                viewNearTo.setText(part1 + "of");
                viewCityName.setText(part2);
            } else {
                viewNearTo.setText("Near the");
                viewCityName.setText(currentEarthObject.getCityName());
            }

        }
    }


