package com.tutorialspoint.sunshine.Utility;

import android.util.Log;

import com.tutorialspoint.sunshine.data.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Taha on 17/05/2018.
 */
public class JSONWeatherParser {

    public static ArrayList<Weather> getWeathers(String data) throws JSONException {
        ArrayList<Weather> weathers = new ArrayList<>();

        //get the entire json response
        JSONObject entireObject = new JSONObject(data);

        //get the 7 days from list array
        JSONArray weatherDaysJSONArray = entireObject.getJSONArray("list");

        for (int i = 0; i < 7; i++) {
            Weather weather = new Weather();
            JSONObject JSONWeatherDay = weatherDaysJSONArray.getJSONObject(i);

            //get the sub  object called weather
            JSONArray weatherObj = JSONWeatherDay.getJSONArray("weather");
            JSONObject subWeather = weatherObj.getJSONObject(0);

            weather.setId(i);
            weather.setDayName(Weather.calculateDayName(i));
            weather.currentCondition.setWeatherId(getInt("id", subWeather));
            weather.currentCondition.setDescr(getString("description", subWeather));
            weather.currentCondition.setCondition(getString("main", subWeather));
            weather.currentCondition.setIcon(getString("icon", subWeather));

            //get the sub  object called main
            JSONObject mainObj = getObject("main", JSONWeatherDay);
            weather.currentCondition.setHumidity(getInt("humidity", mainObj));
            weather.currentCondition.setPressure(getInt("pressure", mainObj));
            weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
            weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
            weather.temperature.setTemp(getFloat("temp", mainObj));

            weathers.add(weather);

        }
        return weathers;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
