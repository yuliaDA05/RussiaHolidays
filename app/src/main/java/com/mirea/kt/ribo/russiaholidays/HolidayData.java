package com.mirea.kt.ribo.russiaholidays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HolidayData {

    String name;
    String description;
    int month;
    int day;
    List<String> type;

    public HolidayData(JSONObject object) throws JSONException {
        name = object.getString("name");
        description = object.getString("description");

        JSONObject datetime = object.getJSONObject("date").getJSONObject("datetime");
        month = datetime.getInt("month");
        day = datetime.getInt("day");

        ArrayList<String> type = new ArrayList<>();
        JSONArray typeArray = object.getJSONArray("type");
        for (int i = 0; i < typeArray.length(); i++) {
            type.add(typeArray.getString(i));
        }
        this.type = type;
    }
}
