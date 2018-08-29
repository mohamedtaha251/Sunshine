package com.tutorialspoint.sunshine.Adapters;

/**
 * Created by Taha on 14/05/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorialspoint.sunshine.R;
import com.tutorialspoint.sunshine.Utility.Utility;
import com.tutorialspoint.sunshine.data.Weather;

import java.util.ArrayList;
import java.util.Calendar;

public class WeatherAdapter extends CursorAdapter {
    ArrayList<Weather> weatherDaysList;
    Context mContext;
    LayoutInflater inflater;
    private int resId;

    private SparseBooleanArray mSelectedItemsIds;

    //to set day Name
    String WEEK_DAYS[] = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;


    private TextView DayNameTextView;
    private TextView weatherStateTextView;
    private TextView weatherMinTempTextView;
    private TextView weatherMaxTempTextView;
    private ImageView weatherImageView;

    public WeatherAdapter(Context context, ArrayList<Weather> weatherList, int resource) {
        super(context,null);
        this.weatherDaysList = weatherList;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
        this.resId = resource;


    }

    @Override
    public int getCount() {
        return weatherDaysList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherDaysList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return weatherDaysList.get(position).currentCondition.getWeatherId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(this.resId, null);

        DayNameTextView = (TextView) convertView.findViewById(R.id.list_item_date_textview);
        weatherStateTextView = (TextView) convertView.findViewById(R.id.list_item_forecast_textview);
        weatherMinTempTextView = (TextView) convertView.findViewById(R.id.list_item_low_textview);
        weatherMaxTempTextView = (TextView) convertView.findViewById(R.id.list_item_high_textview);
        weatherImageView = (ImageView) convertView.findViewById(R.id.list_item_icon);

        Weather weather = weatherDaysList.get(position);
        DayNameTextView.setText(weather.getDayName());
        weatherStateTextView.setText(weather.currentCondition.getDescr());
        weatherMinTempTextView.setText((int) weather.temperature.getMinTemp()+"\u00b0");
        weatherMaxTempTextView.setText((int) weather.temperature.getMaxTemp()+"\u00b0");
        weatherImageView.setImageResource(Utility.getIconResourceForWeatherCondition(weather.currentCondition.getWeatherId()));
        return convertView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();

    }

}
