package com.tutorialspoint.sunshine.View;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tutorialspoint.sunshine.Adapters.WeatherAdapter;
import com.tutorialspoint.sunshine.Intefraces.AsyncResponse;
import com.tutorialspoint.sunshine.R;
import com.tutorialspoint.sunshine.Utility.Utility;
import com.tutorialspoint.sunshine.data.Weather;
import com.tutorialspoint.sunshine.data.WeatherSQLite;
import com.tutorialspoint.sunshine.sync.JSONWeatherTask;

import java.util.ArrayList;

public class LandscapeFragment extends Fragment implements AsyncResponse {
    private ListView mListView;
    ArrayList<Weather> weathers;
    SwipeRefreshLayout pullToRefresh;
    WeatherSQLite weatherSQLite;

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

    public LandscapeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.landscape_fragement, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_forecast);
        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);

        //get data from sqlite
        weatherSQLite = new WeatherSQLite(getContext());
        Cursor cursor = weatherSQLite.getAll();
        weathers = Weather.getWeathersFromCursor(cursor);
        WeatherAdapter forcastAdapter = new WeatherAdapter(getActivity(), weathers, R.layout.list_item_forcast);
        mListView.setAdapter(forcastAdapter);

        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);

        //on click on of item to open detail activity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateRightFragment(position);

            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });
        return rootView;
    }


    @Override
    public void processFinish(ArrayList<Weather> weathers) {
    /*
    * * @call in asyncTask in postExecute() function.
    * * @purpose 1-update the adapter After asyncFinished.
    * *          2-enable mainActivity to have the result of asyncTask(weathers).
    *
    * */

        //update adapter after async task finish
        this.weathers = weathers;
        WeatherAdapter forcastAdapter = new WeatherAdapter(getActivity(), weathers, R.layout.list_item_forcast);
        mListView.setAdapter(forcastAdapter);

        //set right fragement with first day in list
        updateRightFragment(0);
    }



    private void updateRightFragment(int position)
    {
        weather = weathers.get(position);

        //get DayName from listview
        View itemView = Utility.getViewByPosition(position, mListView);
        TextView tvDayName = (TextView) itemView.findViewById(R.id.list_item_date_textview);
        dayName = (String) tvDayName.getText();


        //set view based on selected item
        mIconView.setImageResource(Utility.getIconResourceForWeatherCondition(weather.currentCondition.getWeatherId()));
        mFriendlyDateView.setText(dayName);
        mDescriptionView.setText(weather.currentCondition.getDescr());
        mHighTempView.setText((int) weather.temperature.getMaxTemp() + "\u00b0");
        mLowTempView.setText((int) weather.temperature.getMinTemp() + "\u00b0");
        mHumidityView.setText((int) weather.currentCondition.getHumidity() + "%");
        mPressureView.setText((int) weather.currentCondition.getPressure() + " km/h NW");
        mWindView.setText((int) weather.wind.getSpeed() + "hpa");
    }

    private void refreshData() {
        //call async task
        JSONWeatherTask asyncTask = new JSONWeatherTask(getActivity());
        asyncTask.delegateMainActivity = this;
        asyncTask.execute();
    }

}
