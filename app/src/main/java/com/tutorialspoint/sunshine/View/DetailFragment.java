package com.tutorialspoint.sunshine.View;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorialspoint.sunshine.R;
import com.tutorialspoint.sunshine.Utility.Utility;
import com.tutorialspoint.sunshine.data.Weather;

public class DetailFragment extends Fragment {


    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    Weather weather;
    String dayName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //recieve data from intent which coming from main activity
        weather = (Weather) getActivity().getIntent().getSerializableExtra("Weather");
        dayName = getActivity().getIntent().getStringExtra("DayName");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //set view based on selected item
        mIconView.setImageResource(Utility.getIconResourceForWeatherCondition(weather.currentCondition.getWeatherId()));
        mFriendlyDateView.setText(this.dayName);
        mDescriptionView.setText(weather.currentCondition.getDescr());
        mHighTempView.setText((int) weather.temperature.getMaxTemp()+"\u00b0");
        mLowTempView.setText((int) weather.temperature.getMinTemp()+"\u00b0");
        mHumidityView.setText((int) weather.currentCondition.getHumidity() + "%");
        mPressureView.setText((int) weather.currentCondition.getPressure() + " km/h NW");
        mWindView.setText((int) weather.wind.getSpeed()+"hpa");


    }


}
