//Jakub Szulwinski S1627131

package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);
        Boolean isInitalized = settings.getBoolean("updaterInitialized", false);

        if (!isInitalized) {

            Boolean scheduled = false;

            Date current = new Date();

            Calendar AM8 = new GregorianCalendar();
            AM8.set(Calendar.HOUR_OF_DAY, 8);
            AM8.set(Calendar.MINUTE, 0);
            AM8.set(Calendar.SECOND, 0);
            AM8.set(Calendar.MILLISECOND, 0);

            if (current.before(AM8.getTime()) && !scheduled) {
                long am8L = AM8.getTimeInMillis() / 1000;
                setAlarm(am8L);
                scheduled = true;
            }

            Calendar PM8 = new GregorianCalendar();
            PM8.set(Calendar.HOUR_OF_DAY, 8);
            PM8.set(Calendar.MINUTE, 0);
            PM8.set(Calendar.SECOND, 0);
            PM8.set(Calendar.MILLISECOND, 0);

            if (current.before(PM8.getTime()) && !scheduled) {
                long pm8L = PM8.getTimeInMillis() / 1000;
                setAlarm(pm8L);
                scheduled = true;
            }

            AM8.add(Calendar.DAY_OF_MONTH, 1);

            if (current.before(AM8.getTime()) && !scheduled) {
                long am8L = AM8.getTimeInMillis() / 1000;
                setAlarm(am8L);
                scheduled = true;
            }

            PM8.add(Calendar.DAY_OF_MONTH, 1);

            if (current.before(PM8.getTime()) && !scheduled) {
                long pm8L = PM8.getTimeInMillis() / 1000;
                setAlarm(pm8L);
                scheduled = true;
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("updaterInitialized", scheduled);
            editor.apply();
        }

    }

    private void setAlarm(long time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Updater.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC, time, pending);
    }


}