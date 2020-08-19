//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final EditText hour1 = findViewById(R.id.chooseHour1);
        final EditText hour2 = findViewById(R.id.chooseHour2);

        final SharedPreferences settings = getApplicationContext().getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);

        Integer updateHour1 = settings.getInt("updateHour1", 8);
        Integer updateHour2 = settings.getInt("updateHour2", 20);

        hour1.setText(""+updateHour1);
        hour2.setText(""+updateHour2);

        hour1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String entered = hour1.getText().toString();
                if(!entered.isEmpty()) {
                    Integer number;
                    try {
                        number = Integer.parseInt(entered);
                    }
                    catch (NumberFormatException e) {
                        number = -1;
                    }
                    if (!(number >= 0 && number <= 23)) {
                        hour1.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String entered = hour1.getText().toString();
                if (!entered.isEmpty()) {
                    Integer number;
                    try {
                        number = Integer.parseInt(entered);
                    }
                    catch (NumberFormatException e) {
                        number = -1;
                    }
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("updateHour1", number);
                    editor.apply();
                }
            }

        });

        hour2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String entered = hour2.getText().toString();
                if(!entered.isEmpty()) {
                    Integer number;
                    try {
                        number = Integer.parseInt(entered);
                    }
                    catch (NumberFormatException e) {
                        number = -1;
                    }
                    if (!(number >= 0 && number <= 23)) {
                        hour2.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String entered = hour2.getText().toString();
                if (!entered.isEmpty()) {
                    Integer number;
                    try {
                        number = Integer.parseInt(entered);
                    }
                    catch (NumberFormatException e) {
                        number = -1;
                    }
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("updateHour2", number);
                    editor.apply();
                }
            }
        });



    }


}

