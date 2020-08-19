//Jakub Szulwinski S1627131

package com.example.weatherapp;

import java.util.Objects;

public class BBCWeather {
    public String title;
    public String description;

    public BBCWeather(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BBCWeather that = (BBCWeather) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
