package com.tutorialspoint.sunshine.View;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class ForecastFragment extends Fragment implements AsyncResponse {
    private ListView mListView;
    ArrayList<Weather> weathers;
    SwipeRefreshLayout pullToRefresh;
    WeatherSQLite weatherSQLite;


    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_forecast);
        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);


        //get data from sqlite
        weatherSQLite = new WeatherSQLite(getContext());
        Cursor cursor = weatherSQLite.getAll();
        weathers = Weather.getWeathersFromCursor(cursor);
        WeatherAdapter forcastAdapter = new WeatherAdapter(getActivity(), weathers, R.layout.list_item_forcast);
        mListView.setAdapter(forcastAdapter);

        //on click on of item to open detail activity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get DayName from listview
                View itemView = Utility.getViewByPosition(position, mListView);
                TextView tvDayName = (TextView) itemView.findViewById(R.id.list_item_date_textview);
                String dayName = (String) tvDayName.getText();


                //get selected weather from weathers ArrayList which updated after on async task finished
                Weather selectedWeather = weathers.get(position);

                //move to Detail activity with data of selected item
                Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                intent.putExtra("Weather", selectedWeather);
                intent.putExtra("DayName", dayName);
                startActivity(intent);
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

    private void refreshData() {
        //call async task
        JSONWeatherTask asyncTask = new JSONWeatherTask(getActivity());
        asyncTask.delegateMainActivity = this;
        asyncTask.execute();
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

        weatherSQLite.deleteAll();
        for (Weather weather : weathers) {
            weatherSQLite.insert(weather);
        }
    }


}
