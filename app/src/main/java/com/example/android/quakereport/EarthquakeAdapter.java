package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by pitrs on 15.04.2018.
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EarthquakeObject> models = new ArrayList<>();

    /**
     * Constructor for EarthquakeAdapter
     * @param viewModels
     */
    public EarthquakeAdapter(final List<EarthquakeObject> viewModels) {
        if (viewModels != null) {
            this.models.addAll(viewModels);
        }
    }

    class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        public TextView viewRichterScale;
        public TextView viewNearTo;
        public TextView viewCityName;
        public TextView viewDate;
        public TextView viewTime;
        public LinearLayout earthquakeBox;
        private int magnitudeColorResID = R.color.magnitude1;
        private double currentRichterScale;
        private GradientDrawable magnitudeDrawable;

        /**
         * Parsing variables views to views in earthquake_list.xml and constructing ViewHolder
         * @param itemView
         */
        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            viewRichterScale = itemView.findViewById(R.id.view_rychter_scale);
            viewNearTo = itemView.findViewById(R.id.view_near_to);
            viewCityName = itemView.findViewById(R.id.view_city_name);
            viewDate = itemView.findViewById(R.id.view_date);
            viewTime = itemView.findViewById(R.id.view_time);
            earthquakeBox = itemView.findViewById(R.id.earthquake_box);
        }

        /**
         * Binding data to variable views
         * @param itemModel
         */
        public void bindData(final EarthquakeObject itemModel) {
            currentRichterScale = itemModel.getRichterScale();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            viewRichterScale.setText(decimalFormat.format(currentRichterScale));
            magnitudeDrawable = (GradientDrawable) viewRichterScale.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            getMagnitudeColor();
            magnitudeDrawable.setColor(ContextCompat.getColor(viewRichterScale.getContext(), magnitudeColorResID));
            splitCityName(itemModel);
            viewDate.setText(itemModel.getDate());
            viewTime.setText(itemModel.getTime());
            // Giving each item onclick listener to open in intent attached URL
            earthquakeBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openUrl = new Intent(Intent.ACTION_VIEW);
                    openUrl.setData(Uri.parse(itemModel.getURL()));
                    itemView.getContext().startActivity(openUrl);
                }
            });
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

        /**
         *  Aqcuiring string with data of place and formating for 2 views
         * @param itemModel
         */
        private void splitCityName(final EarthquakeObject itemModel) {
            String stringToSplit = itemModel.getCityName();
            Log.i("City Name", stringToSplit);
            if (stringToSplit.contains("of")) {
                String parts[] = stringToSplit.split("of ");
                String part1 = parts[0];
                String part2 = parts[1];
                viewNearTo.setText(part1 + "of");
                viewCityName.setText(part2);
            } else {
                viewNearTo.setText("Near the");
                viewCityName.setText(itemModel.getCityName());
            }

        }
    }

    /**
     * Inflating RecyclerView with ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new EarthquakeViewHolder(view);
    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((EarthquakeViewHolder) holder).bindData(models.get(position));
    }

    public int getItemCount() {
        return models.size();
    }

    public int getItemViewType(final int position) {
        return R.layout.earthquake_list;
    }


}
