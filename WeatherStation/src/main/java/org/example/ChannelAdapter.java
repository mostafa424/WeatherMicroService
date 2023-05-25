package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Random;

public class ChannelAdapter {
    public static int[] openMeteoToWeatherStation() throws Exception{
        Random random = new Random();
        String data = OpenMetoeAPI.getAPI();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(data);
        JSONObject weather = (JSONObject) json.get("current_weather");
        int temperature = Integer.parseInt(String.valueOf(weather.get("temperature")));
        int windSpeed = Integer.parseInt(String.valueOf(weather.get("windspeed")));
        //int humidity_number = Integer.parseInt(humidity);
        int humidity = random.nextInt(100);
        int drop_rate = 7;
        int[] result = new int[4];
        result[0] = humidity;
        result[1] = temperature;
        result[2] = windSpeed;
        result[3] = drop_rate;
        return result;
    }
}
