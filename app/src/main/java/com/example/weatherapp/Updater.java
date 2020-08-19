//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Updater extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<Integer> ids = Arrays.asList(
        2648579,
       2643743,
        5128581,
        287286,
        934154,
        1185241);

        for (Integer id : ids) {
            new FetchFeedTask(context, null).execute(id, 1, 0);
            new FetchFeedTask(context, null).execute(id, 2, 0);
        }

        SharedPreferences settings = context.getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);

        Integer updateHour1 = settings.getInt("updateHour1", 8);
        Integer updateHour2 = settings.getInt("updateHour2", 20);

        Integer earlierHour = (updateHour1 < updateHour2) ? updateHour1 : updateHour2;
        Integer laterHour = (updateHour1 > updateHour2) ? updateHour1 : updateHour2;

        Boolean scheduled = false;
        Date current = new Date();

        Calendar AM8 = new GregorianCalendar();
        AM8.set(Calendar.HOUR_OF_DAY, earlierHour);
        AM8.set(Calendar.MINUTE, 0);
        AM8.set(Calendar.SECOND, 0);
        AM8.set(Calendar.MILLISECOND, 0);

        if (current.before(AM8.getTime()) && !scheduled) {
            long am8L = AM8.getTimeInMillis() / 1000;
            setAlarm(am8L, context);
            scheduled = true;
        }

        Calendar PM8 = new GregorianCalendar();
        PM8.set(Calendar.HOUR_OF_DAY, laterHour);
        PM8.set(Calendar.MINUTE, 0);
        PM8.set(Calendar.SECOND, 0);
        PM8.set(Calendar.MILLISECOND, 0);

        if (current.before(PM8.getTime()) && !scheduled) {
            long pm8L = PM8.getTimeInMillis() / 1000;
            setAlarm(pm8L, context);
            scheduled = true;
        }

        AM8.add(Calendar.DAY_OF_MONTH, 1);

        if (current.before(AM8.getTime()) && !scheduled) {
            long am8L = AM8.getTimeInMillis() / 1000;
            setAlarm(am8L, context);
            scheduled = true;
        }

        PM8.add(Calendar.DAY_OF_MONTH, 1);

        if (current.before(PM8.getTime()) && !scheduled) {
            long pm8L = PM8.getTimeInMillis() / 1000;
            setAlarm(pm8L, context);
            scheduled = true;
        }

    }

    private void setAlarm(long time, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Updater.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC, time, pending);
    }

}
