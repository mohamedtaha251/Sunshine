package com.tutorialspoint.sunshine.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.tutorialspoint.sunshine.Intefraces.AsyncResponse;
import com.tutorialspoint.sunshine.Utility.JSONWeatherParser;
import com.tutorialspoint.sunshine.Utility.WeatherHttpClient;
import com.tutorialspoint.sunshine.data.Weather;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Taha on 17/05/2018.
 */
public class JSONWeatherTask extends AsyncTask<Void, Void, ArrayList<Weather>> {
    public ArrayList<Weather> weathers;
    WeatherHttpClient weatherHttpClient = new WeatherHttpClient();
    String jsonResponse = null;
    private WeakReference<Context> contextReference;
    public AsyncResponse delegateMainActivity =null;



    public JSONWeatherTask(Context context) {
        contextReference = new WeakReference<>(context);

    }


    @Override
    protected ArrayList<Weather> doInBackground(Void... params) {
        jsonResponse = weatherHttpClient.getWeatherData();


        //terminate AsyncTask if response is null
        if (jsonResponse == null) {
            this.cancel(true);
        }

        try {
            weathers = JSONWeatherParser.getWeathers(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return weathers;

        }

    }


    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);

        /*update UI after execution of asynctask*/
        AppCompatActivity mainActivityContext = (AppCompatActivity) contextReference.get();

        if (mainActivityContext == null)
            return;

        //this function set adapter and pass the result to  main activity
        delegateMainActivity.processFinish(weathers);

    }
}