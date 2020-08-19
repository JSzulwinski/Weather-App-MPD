//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class FetchFeedTask extends AsyncTask<Integer, Void, RetVal> {
    Context context;
    WeatherInfoActivity weatherInfoActivity;

    public FetchFeedTask(Context context, WeatherInfoActivity weatherInfoActivity) {
        this.context = context;
        this.weatherInfoActivity = weatherInfoActivity;
    }
    @Override
    protected RetVal doInBackground(Integer... ids) {
        List<BBCWeather> bbcWeatherList = new ArrayList<>();
        List<Integer> idList = Arrays.asList(ids);
        int id = idList.get(0);
        int type = idList.get(1);
        int updateUI = idList.get(2);
        try {
            String urlLink;
            switch (type) {
                case 1:
                    urlLink = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
                    break;
                case 2:
                    urlLink = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/";
                    break;
                default:
                    throw new RuntimeException();
            }
            URL url = new URL(urlLink + id);
            InputStream inputStream = url.openConnection().getInputStream();
            bbcWeatherList = RSSParser.parseRSS(inputStream);
        } catch (IOException | XmlPullParserException e) {
            return new RetVal(false, type, bbcWeatherList, id, updateUI);
        }

        return new RetVal(true, type, bbcWeatherList, id, updateUI);
    }


    @Override
    protected void onPostExecute(final RetVal retVal) {
        if (retVal.success) {
            SharedPreferences settings = context.getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            switch (retVal.type) {
                case 1:
                    editor.putString("observation_title_" + retVal.id, retVal.bbcWeatherList.get(0).title);
                    editor.putString("observation_desc_" + retVal.id, retVal.bbcWeatherList.get(0).description);
                    break;
                case 2:
                    editor.putString("forecast_1_title_" + retVal.id, retVal.bbcWeatherList.get(0).title);
                    editor.putString("forecast_2_title_" + retVal.id, retVal.bbcWeatherList.get(1).title);
                    editor.putString("forecast_3_title_" + retVal.id, retVal.bbcWeatherList.get(2).title);
                    editor.putString("forecast_1_desc_" + retVal.id, retVal.bbcWeatherList.get(0).description);
                    editor.putString("forecast_2_desc_" + retVal.id, retVal.bbcWeatherList.get(1).description);
                    editor.putString("forecast_3_desc_" + retVal.id, retVal.bbcWeatherList.get(2).description);
                    break;
                default:
                    throw new RuntimeException();
            }

            if(retVal.updateUI > 0) {
                weatherInfoActivity.showData(retVal);
            }

            editor.apply();

        } else {
            Timer timer = new Timer();
            final Integer id = retVal.id;
            final Integer type = retVal.type;
            final Integer updateUI = retVal.updateUI;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    new FetchFeedTask(context, weatherInfoActivity).execute(id, type, updateUI);
                }
            };
            timer.schedule(timerTask, 2000L);
        }
    }


}
