package com.tutorialspoint.sunshine.Intefraces;

import com.tutorialspoint.sunshine.data.Weather;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<Weather> weathers);
}