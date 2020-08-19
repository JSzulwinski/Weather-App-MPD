//Jakub Szulwinski S1627131

package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherInfoActivity extends AppCompatActivity implements ShowData {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Integer> locationsMap = new HashMap<>();
        locationsMap.put("Glasgow", 2648579);
        locationsMap.put("London", 2643743);
        locationsMap.put("New York", 5128581);
        locationsMap.put("Oman", 287286);
        locationsMap.put("Mauritius", 934154);
        locationsMap.put("Bangladesh", 1185241);

        setContentView(R.layout.activity_weather_info);
        String locationName = getIntent().getStringExtra("locationName");
        TextView tVLocationName = findViewById(R.id.location_name);
        tVLocationName.setText(locationName);
        Integer locationId = locationsMap.get(locationName);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);
        String savedObservationTitle = settings.getString("observation_title_" + locationId, "");
        String savedObservationDesc = settings.getString("observation_desc_" + locationId, "");
        String savedForecastTitle1 = settings.getString("forecast_1_title_" + locationId, "");
        String savedForecastDesc1 = settings.getString("forecast_1_desc_" + locationId, "");
        String savedForecastTitle2 = settings.getString("forecast_2_title_" + locationId, "");
        String savedForecastDesc2 = settings.getString("forecast_2_desc_" + locationId, "");
        String savedForecastTitle3 = settings.getString("forecast_3_title_" + locationId, "");
        String savedForecastDesc3 = settings.getString("forecast_3_desc_" + locationId, "");

        boolean loadedObservation = true;
        if (savedObservationTitle.isEmpty() || savedObservationDesc.isEmpty()) {
            new FetchFeedTask(getBaseContext(), this).execute(locationId, 1, 1);
            loadedObservation = false;
        }

        boolean loadedForecast = true;
        if (savedForecastTitle1.isEmpty() || savedForecastDesc1.isEmpty()
                || savedForecastTitle2.isEmpty() || savedForecastDesc2.isEmpty()
                || savedForecastTitle3.isEmpty() || savedForecastDesc3.isEmpty()) {
            new FetchFeedTask(getBaseContext(), this).execute(locationId, 2, 1);
            loadedForecast = false;
        }

        if (loadedObservation && loadedForecast) {
            List<BBCWeather> observationList = new ArrayList<>(Arrays.asList(new BBCWeather(savedObservationTitle, savedObservationDesc)));
            showData(new RetVal(true, 1, observationList, locationId, 1));
            List<BBCWeather> forecastList = new ArrayList<>(Arrays.asList(
                    new BBCWeather(savedForecastTitle1, savedForecastDesc1),
                    new BBCWeather(savedForecastTitle2, savedForecastDesc2),
                    new BBCWeather(savedForecastTitle3, savedForecastDesc3)));
            showData(new RetVal(true, 2, forecastList, locationId, 1));
        }
    }

    @Override
    public void showData(RetVal retVal) {
        LinearLayout linearLayout = findViewById(R.id.activity_weather_info_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(linearLayout.getLayoutParams());
        for (BBCWeather bbcWeather : retVal.bbcWeatherList) {
            bbcWeather.title = bbcWeather.title.replaceAll(",", "\n");
            bbcWeather.description = bbcWeather.description.replaceAll(",", "\n");
            TextView textView = new TextView(getBaseContext());
            textView.setText(bbcWeather.title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            linearLayout.addView(textView, params);

            TextView textView2 = new TextView(getBaseContext());
            textView2.setText(bbcWeather.description + "\n\n");
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_PT, 0);
            final int currentId = View.generateViewId();
            textView2.setId(currentId);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView2 = findViewById(currentId);
                    if (textView2.getTextSize() == 0) {
                        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                    }
                    else {
                        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PT, 0);
                    }

                }
            });
            linearLayout.addView(textView2, params);
        }
    }
}

