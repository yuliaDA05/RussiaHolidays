package com.mirea.kt.ribo.russiaholidays;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HolidaysAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        adapter = new HolidaysAdapter();
        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        Calendar calendar = new GregorianCalendar();
        int currentMouth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        adapter.setCurrentMonthHolidays(loadMonth(currentMouth, currentYear));
        calendar.add(Calendar.MONTH, 1);
        int nextMouth = calendar.get(Calendar.MONTH) + 1;
        int nextYear = calendar.get(Calendar.YEAR);
        adapter.setNextMonthHolidays(loadMonth(nextMouth, nextYear));
    }

    private List<HolidayData> loadMonth(int month, int year) {
        String address = "https://calendarific.com/api/v2/holidays";
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", "riHsDa6SmS0p700SJxGYXMDN5fMVaj2g");
        map.put("country", "ru");
        map.put("year", String.valueOf(year));
        map.put("month", String.valueOf(month));

        HttpRunnable httpRunnable = new HttpRunnable(address, map, false);
        Thread thread = new Thread(httpRunnable);
        thread.start();

        ArrayList<HolidayData> result = new ArrayList<>();

        try {
            thread.join();
            String body = httpRunnable.getResponseBody();
            JSONObject root = new JSONObject(body);
            JSONObject response = root.getJSONObject("response");
            JSONArray holidays = response.getJSONArray("holidays");
            for (int i = 0; i < holidays.length(); i++) {
                try {
                    HolidayData data = new HolidayData(holidays.getJSONObject(i));
                    result.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}