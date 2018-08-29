package com.tutorialspoint.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherSQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    public static final String TABLE_NAME = "weatherday";
    public static final int DATABASE_VERSION = 1;


    //columns Name
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAYName = "day_name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_HIGH = "high";
    public static final String COLUMN_LOW = "low";
    public static final String COLUMN_ICONID = "icon_id";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_WIND = "wind";


    public WeatherSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create WeahterDay table
        final String SQL_CREATE_WEATHERDAY_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_DAYName + " TEXT NOT NULL," +
                COLUMN_DATE + " TEXT NOT NULL," +
                COLUMN_STATE + " TEXT NOT NULL," +
                COLUMN_HIGH + " INTEGER NOT NULL," +
                COLUMN_LOW + " INTEGER NOT NULL," +
                COLUMN_ICONID + " INTEGER NOT NULL," +
                COLUMN_HUMIDITY + " INTEGER NOT NULL," +
                COLUMN_PRESSURE + " INTEGER NOT NULL," +
                COLUMN_WIND + " INTEGER NOT NULL" +
                " );";
        db.execSQL(SQL_CREATE_WEATHERDAY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insert(Weather weather) {
        SQLiteDatabase db = super.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, weather.getId());
        values.put(COLUMN_DAYName, weather.getDayName());
        values.put(COLUMN_DATE, weather.getDate());
        values.put(COLUMN_STATE, weather.currentCondition.getDescr());
        values.put(COLUMN_HIGH, weather.temperature.getMaxTemp());
        values.put(COLUMN_LOW, weather.temperature.getMinTemp());
        values.put(COLUMN_ICONID, weather.currentCondition.getWeatherId());
        values.put(COLUMN_HUMIDITY, (int) weather.currentCondition.getHumidity());
        values.put(COLUMN_PRESSURE, (int) weather.currentCondition.getPressure());
        values.put(COLUMN_WIND, (int) weather.wind.getSpeed());

        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAll() {
        SQLiteDatabase db = super.getReadableDatabase();
        String[] columns = {COLUMN_ID,
                COLUMN_DAYName,
                COLUMN_DATE,
                COLUMN_STATE,
                COLUMN_HIGH,
                COLUMN_LOW,
                COLUMN_ICONID,
                COLUMN_HUMIDITY,
                COLUMN_PRESSURE,
                COLUMN_WIND};

        return db.query(TABLE_NAME, columns, null, null, null, null, null);
    }

    public boolean deleteAll() {
        SQLiteDatabase db = super.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null) > 0;
    }
}
