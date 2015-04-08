package com.study.raphaelpontes.sunshine.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raphaelpontes on 06/04/2015
 */
public class WeatherDataParser {

    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex) throws JSONException {

        JSONObject json = new JSONObject(weatherJsonStr);
        JSONArray days = json.getJSONArray("list");
        JSONObject daysInfo = new JSONObject(days.get(dayIndex).toString());
        daysInfo = daysInfo.getJSONObject("temp");

        return daysInfo.getDouble("max");
    }
}
