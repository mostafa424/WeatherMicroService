package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Random;

public class ChannelAdapter {
    public static double[] openMeteoToWeatherStation(String stationid) throws Exception{
        String data = OpenMetoeAPI.getAPI(stationid);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(data);
        JSONObject weather = (JSONObject) json.get("current_weather");
        JSONObject hourly = (JSONObject) json.get("hourly");
        JSONArray humidityList = (JSONArray) hourly.get("relativehumidity_2m");
        double averageHumidity = 0.0;
        for(int i = 0; i < humidityList.size(); i++)
        {
            averageHumidity +=  (long)humidityList.get(i);
        }
        averageHumidity /=  humidityList.size();
        double temperature = Double.parseDouble(String.valueOf(weather.get("temperature")));
        double windSpeed = Double.parseDouble(String.valueOf(weather.get("windspeed")));
        double humidity = averageHumidity;
        double drop_rate = 7;
        double[] result = new double[4];
        result[0] = humidity;
        result[1] = temperature;
        result[2] = windSpeed;
        result[3] = drop_rate;
        return result;
    }
}
