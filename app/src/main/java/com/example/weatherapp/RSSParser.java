//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RSSParser {

    public static List<BBCWeather> parseRSS(InputStream inputStream) throws IOException, XmlPullParserException {
        try {
            List<BBCWeather> bbcWeatherList = new ArrayList<>();

            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            pullParser.setInput(inputStream, null);

            pullParser.nextTag();
            String title = null;
            String description = null;
            while (pullParser.next() != XmlPullParser.END_DOCUMENT) {
                String name = pullParser.getName();
                if (name == null) {
                    continue;
                }

                String text = "";
                if (pullParser.next() == XmlPullParser.TEXT) {
                    text = pullParser.getText();
                    pullParser.nextTag();
                }

                if (name.toLowerCase().equals("description")) {
                    description = text;
                }

                if (name.toLowerCase().equals("title")) {
                    title = text;
                }

                if (description != null && title != null) {
                    bbcWeatherList.add(new BBCWeather(title, description));
                    title = null;
                    description = null;
                }
            }
            bbcWeatherList.remove(0);
            return bbcWeatherList;
        }
        finally {
            inputStream.close();
        }

    }
}
