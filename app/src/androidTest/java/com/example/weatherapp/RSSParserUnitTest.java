package com.example.weatherapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RSSParserUnitTest {

    private final String observation = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rss xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:georss=\"http://www.georss.org/georss\" version=\"2.0\">\n" +
            "  <channel>\n" +
            "    <title>BBC Weather - Observations for  Glasgow, GB</title>\n" +
            "    <link>https://www.bbc.co.uk/weather/2648579</link>\n" +
            "    <description>Latest observations for Glasgow from BBC Weather, including weather, temperature and wind information</description>\n" +
            "    <language>en</language>\n" +
            "    <copyright>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</copyright>\n" +
            "    <pubDate>Fri, 14 Aug 2020 13:00:00 GMT</pubDate>\n" +
            "    <dc:date>2020-08-14T13:00:00Z</dc:date>\n" +
            "    <dc:language>en</dc:language>\n" +
            "    <dc:rights>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</dc:rights>\n" +
            "    <atom:link href=\"https://weather-service-thunder-broker.api.bbci.co.uk/en/observation/rss/2648579\" type=\"application/rss+xml\" rel=\"self\" />\n" +
            "    <item>\n" +
            "      <title>Friday - 14:00 BST: Not available, 18Â°C (64Â°F)</title>\n" +
            "      <link>https://www.bbc.co.uk/weather/2648579</link>\n" +
            "      <description>Temperature: 18Â°C (64Â°F), Wind Direction: East North Easterly, Wind Speed: 6mph, Humidity: 83%, Pressure: 1018mb, Steady, Visibility: --</description>\n" +
            "      <pubDate>Fri, 14 Aug 2020 13:00:00 GMT</pubDate>\n" +
            "      <guid isPermaLink=\"false\">https://www.bbc.co.uk/weather/2648579-2020-08-14T14:00:00.000+01:00</guid>\n" +
            "      <dc:date>2020-08-14T13:00:00Z</dc:date>\n" +
            "      <georss:point>55.8652 -4.2576</georss:point>\n" +
            "    </item>\n" +
            "  </channel>\n" +
            "</rss>";

    private final String forecast = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rss xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:georss=\"http://www.georss.org/georss\" version=\"2.0\">\n" +
            "  <channel>\n" +
            "    <title>BBC Weather - Forecast for  Glasgow, GB</title>\n" +
            "    <link>https://www.bbc.co.uk/weather/2648579</link>\n" +
            "    <description>3-day forecast for Glasgow from BBC Weather, including weather, temperature and wind information</description>\n" +
            "    <language>en</language>\n" +
            "    <copyright>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</copyright>\n" +
            "    <pubDate>Fri, 14 Aug 2020 13:00:47 GMT</pubDate>\n" +
            "    <dc:date>2020-08-14T13:00:47Z</dc:date>\n" +
            "    <dc:language>en</dc:language>\n" +
            "    <dc:rights>Copyright: (C) British Broadcasting Corporation, see http://www.bbc.co.uk/terms/additional_rss.shtml for more details</dc:rights>\n" +
            "    <atom:link href=\"https://weather-broker-cdn.api.bbci.co.uk/%s/forecast/rss/3day/%s\" type=\"application/rss+xml\" rel=\"self\" />\n" +
            "    <image>\n" +
            "      <title>BBC Weather - Forecast for  Glasgow, GB</title>\n" +
            "      <url>http://static.bbci.co.uk/weather/0.3.203/images/icons/individual_57_icons/en_on_light_bg/7.gif</url>\n" +
            "      <link>https://www.bbc.co.uk/weather/2648579</link>\n" +
            "    </image>\n" +
            "    <item>\n" +
            "      <title>Today: Light Cloud, Minimum Temperature: 13Â°C (56Â°F) Maximum Temperature: 21Â°C (70Â°F)</title>\n" +
            "      <link>https://www.bbc.co.uk/weather/2648579?day=0</link>\n" +
            "      <description>Maximum Temperature: 21Â°C (70Â°F), Minimum Temperature: 13Â°C (56Â°F), Wind Direction: Easterly, Wind Speed: 11mph, Visibility: Good, Pressure: 1017mb, Humidity: 76%, UV Risk: 3, Pollution: Low, Sunrise: 05:48 BST, Sunset: 20:54 BST</description>\n" +
            "      <pubDate>Fri, 14 Aug 2020 13:00:47 GMT</pubDate>\n" +
            "      <guid isPermaLink=\"false\">https://www.bbc.co.uk/weather/2648579-0-2020-08-14T09:57:00.000+0000</guid>\n" +
            "      <dc:date>2020-08-14T13:00:47Z</dc:date>\n" +
            "      <georss:point>55.8652 -4.2576</georss:point>\n" +
            "    </item>\n" +
            "    <item>\n" +
            "      <title>Saturday: Light Cloud, Minimum Temperature: 13Â°C (55Â°F) Maximum Temperature: 22Â°C (71Â°F)</title>\n" +
            "      <link>https://www.bbc.co.uk/weather/2648579?day=1</link>\n" +
            "      <description>Maximum Temperature: 22Â°C (71Â°F), Minimum Temperature: 13Â°C (55Â°F), Wind Direction: Easterly, Wind Speed: 12mph, Visibility: Good, Pressure: 1018mb, Humidity: 75%, UV Risk: 3, Pollution: Low, Sunrise: 05:50 BST, Sunset: 20:52 BST</description>\n" +
            "      <pubDate>Fri, 14 Aug 2020 13:00:47 GMT</pubDate>\n" +
            "      <guid isPermaLink=\"false\">https://www.bbc.co.uk/weather/2648579-1-2020-08-14T09:57:00.000+0000</guid>\n" +
            "      <dc:date>2020-08-14T13:00:47Z</dc:date>\n" +
            "      <georss:point>55.8652 -4.2576</georss:point>\n" +
            "    </item>\n" +
            "    <item>\n" +
            "      <title>Sunday: Light Cloud, Minimum Temperature: 13Â°C (55Â°F) Maximum Temperature: 18Â°C (65Â°F)</title>\n" +
            "      <link>https://www.bbc.co.uk/weather/2648579?day=2</link>\n" +
            "      <description>Maximum Temperature: 18Â°C (65Â°F), Minimum Temperature: 13Â°C (55Â°F), Wind Direction: North Easterly, Wind Speed: 14mph, Visibility: Good, Pressure: 1015mb, Humidity: 80%, UV Risk: 2, Pollution: Low, Sunrise: 05:52 BST, Sunset: 20:50 BST</description>\n" +
            "      <pubDate>Fri, 14 Aug 2020 13:00:47 GMT</pubDate>\n" +
            "      <guid isPermaLink=\"false\">https://www.bbc.co.uk/weather/2648579-2-2020-08-14T09:57:00.000+0000</guid>\n" +
            "      <dc:date>2020-08-14T13:00:47Z</dc:date>\n" +
            "      <georss:point>55.8652 -4.2576</georss:point>\n" +
            "    </item>\n" +
            "  </channel>\n" +
            "</rss>";

    @Test
    public void parseObservation() {
        List<BBCWeather> bbcWeatherList = new ArrayList<>();
        try {
            bbcWeatherList = RSSParser.parseRSS(new ByteArrayInputStream(observation.getBytes()));
        } catch (IOException | XmlPullParserException e) {
            fail();
        }
        BBCWeather bbcWeather = new BBCWeather("Friday - 14:00 BST: Not available, 18Â°C (64Â°F)",
                "Temperature: 18Â°C (64Â°F), Wind Direction: East North Easterly, Wind Speed: 6mph, Humidity: 83%, Pressure: 1018mb, Steady, Visibility: --");
        assertEquals(bbcWeatherList.get(0), bbcWeather);
    }

    @Test
    public void parseForecast() {
        List<BBCWeather> bbcWeatherList = new ArrayList<>();
        try {
            bbcWeatherList = RSSParser.parseRSS(new ByteArrayInputStream(forecast.getBytes()));
        } catch (IOException | XmlPullParserException e) {
            fail();
        }

        BBCWeather bbcWeather1 = new BBCWeather("Today: Light Cloud, Minimum Temperature: 13Â°C (56Â°F) Maximum Temperature: 21Â°C (70Â°F)",
                "Maximum Temperature: 21Â°C (70Â°F), Minimum Temperature: 13Â°C (56Â°F), Wind Direction: Easterly, Wind Speed: 11mph, Visibility: Good, Pressure: 1017mb, Humidity: 76%, UV Risk: 3, Pollution: Low, Sunrise: 05:48 BST, Sunset: 20:54 BST");
        BBCWeather bbcWeather2 = new BBCWeather("Saturday: Light Cloud, Minimum Temperature: 13Â°C (55Â°F) Maximum Temperature: 22Â°C (71Â°F)",
                "Maximum Temperature: 22Â°C (71Â°F), Minimum Temperature: 13Â°C (55Â°F), Wind Direction: Easterly, Wind Speed: 12mph, Visibility: Good, Pressure: 1018mb, Humidity: 75%, UV Risk: 3, Pollution: Low, Sunrise: 05:50 BST, Sunset: 20:52 BST");
        BBCWeather bbcWeather3 = new BBCWeather("Sunday: Light Cloud, Minimum Temperature: 13Â°C (55Â°F) Maximum Temperature: 18Â°C (65Â°F)",
                "Maximum Temperature: 18Â°C (65Â°F), Minimum Temperature: 13Â°C (55Â°F), Wind Direction: North Easterly, Wind Speed: 14mph, Visibility: Good, Pressure: 1015mb, Humidity: 80%, UV Risk: 2, Pollution: Low, Sunrise: 05:52 BST, Sunset: 20:50 BST");

        assertEquals(bbcWeatherList.get(0), bbcWeather1);
        assertEquals(bbcWeatherList.get(1), bbcWeather2);
        assertEquals(bbcWeatherList.get(2), bbcWeather3);

    }


}