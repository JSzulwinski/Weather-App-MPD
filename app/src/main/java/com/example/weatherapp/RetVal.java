//Jakub Szulwinski S1627131

package com.example.weatherapp;

import java.util.List;

public class RetVal {
    public Boolean success;
    public Integer type;
    public List<BBCWeather> bbcWeatherList;
    public Integer id;
    public Integer updateUI;

    public RetVal(Boolean success, Integer type, List<BBCWeather> bbcWeatherlist, Integer id, Integer updateUI) {
        this.success = success;
        this.type = type;
        this.bbcWeatherList = bbcWeatherlist;
        this.id = id;
        this.updateUI = updateUI;
    }
}
