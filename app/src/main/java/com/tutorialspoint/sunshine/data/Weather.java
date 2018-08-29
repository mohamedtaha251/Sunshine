package com.tutorialspoint.sunshine.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.tutorialspoint.sunshine.View.MainActivity;
import com.tutorialspoint.sunshine.sync.JSONWeatherTask;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by Taha on 17/05/2018.
 */
public class Weather implements Serializable {

    private int id;
    private String dayName;
    private String daydate;
    public CurrentCondition currentCondition;
    public Temperature temperature;
    public Wind wind;
    public Rain rain;
    public Snow snow;
    public Clouds clouds;

    public static String WEEK_DAYS[] = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return daydate;
    }

    public void setDate(String date) {
        this.daydate = date;
    }

    public Weather() {
        dayName = "undefined";
        daydate = "0/0/2000";
        id = 0;
        currentCondition = new CurrentCondition();
        temperature = new Temperature();
        wind = new Wind();
        rain = new Rain();
        snow = new Snow();
        clouds = new Clouds();
    }


    public Uri getIconURI() {
        Uri iconUri = Uri.parse("http://openweathermap.org/img/w/" + currentCondition.getIcon() + ".png");
        return iconUri;
    }

    public Bitmap getIconBitmap() {
        //convert url image to Bitmap image
        String urldisplay = "http://openweathermap.org/img/w/" + currentCondition.getIcon() + ".png";
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    public class CurrentCondition implements Serializable {
        private int weatherId;
        private String condition;
        private String descr;
        private String icon;
        private float pressure;
        private float humidity;

        public CurrentCondition() {
            weatherId = 0;
            condition = "";
            descr = "";
            icon = "";
            pressure = 0;
            humidity = 0;
        }

        public int getWeatherId() {
            return weatherId;
        }

        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public float getHumidity() {
            return humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }


    }

    public class Temperature implements Serializable {
        private float temp;
        private float minTemp;
        private float maxTemp;

        public Temperature() {
            temp = 0;
            minTemp = 0;
            maxTemp = 0;
        }

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

    public class Wind implements Serializable {
        private float speed;
        private float deg;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDeg() {
            return deg;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }


    }

    public class Rain implements Serializable {
        private String time;
        private float ammount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }


    }

    public class Snow implements Serializable {
        private String time;
        private float ammount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }


    }

    public class Clouds implements Serializable {
        private int perc;

        public int getPerc() {
            return perc;
        }

        public void setPerc(int perc) {
            this.perc = perc;
        }


    }

    class Columns_Index {
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_DAYName = 1;
        public static final int COLUMN_DATE = 2;
        public static final int COLUMN_STATE = 3;
        public static final int COLUMN_HIGH = 4;
        public static final int COLUMN_LOW = 5;
        public static final int COLUMN_ICONID = 6;
        public static final int COLUMN_HUMIDITY = 7;
        public static final int COLUMN_PRESSURE = 8;
        public static final int COLUMN_WIND = 9;
    }

    public static ArrayList<Weather> getWeathersInAsyncTask(MainActivity mainActivity) {
        ArrayList<Weather> weathers = new ArrayList<Weather>();
        try {
            weathers = new JSONWeatherTask(mainActivity).execute().get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
        return weathers;
    }

    public static String calculateDayName(int index) {
        //this complex function get day name based on index of loop
        int TodayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currentDayIndex = TodayIndex + index - 1;
        currentDayIndex = currentDayIndex > 6 ? (currentDayIndex - 7) : currentDayIndex;
        return WEEK_DAYS[currentDayIndex];

    }

    public static ArrayList<Weather> getWeathersFromCursor(Cursor cursor) {
        ArrayList<Weather> weathers = new ArrayList<Weather>();

        if (cursor.moveToFirst()) {

            do {
                Weather weather = new Weather();
                weather.setId(cursor.getInt(Columns_Index.COLUMN_ID));
                weather.setDayName(cursor.getString(Columns_Index.COLUMN_DAYName));
                weather.setDate(cursor.getString(Columns_Index.COLUMN_DATE));
                weather.currentCondition.setDescr(cursor.getString(Columns_Index.COLUMN_STATE));
                weather.currentCondition.setIcon(cursor.getInt(Columns_Index.COLUMN_ICONID) + "");
                weather.currentCondition.setWeatherId(cursor.getInt(Columns_Index.COLUMN_ICONID));
                weather.temperature.setMaxTemp(cursor.getInt(Columns_Index.COLUMN_HIGH));
                weather.temperature.setMinTemp(cursor.getInt(Columns_Index.COLUMN_LOW));
                weather.currentCondition.setHumidity(cursor.getInt(Columns_Index.COLUMN_HUMIDITY));
                weather.currentCondition.setPressure(cursor.getInt(Columns_Index.COLUMN_PRESSURE));
                weather.wind.setSpeed(cursor.getInt(Columns_Index.COLUMN_WIND));
                weathers.add(weather);
            } while (cursor.moveToNext());

        }
        return weathers;
    }

    public static ArrayList<Weather> getWeathersFromSQLite(Context context) {
        WeatherSQLite weatherSQLite = new WeatherSQLite(context);
        Cursor cursor = weatherSQLite.getAll();
        return Weather.getWeathersFromCursor(cursor);
    }
}