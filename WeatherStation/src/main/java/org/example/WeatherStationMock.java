package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.Random;

public class WeatherStationMock {
    String station_id;
    long s_no ;

    String status ;
    long status_timestamp ;

    int humidity ;
    int temperature ;
    int wind_speed ;
    int drop_rate ;
    public WeatherStationMock(String station_id,boolean fromAPI) throws Exception{
        Random random = new Random();
        this.station_id = station_id;
        this.s_no = 1;
        this.status = this.updateBatteryStatus();
        this.status_timestamp = System.currentTimeMillis() / 1000L;
        this.humidity = random.nextInt(100);
        this.temperature = random.nextInt(134);
        this.wind_speed = random.nextInt(408);
        this.drop_rate = random.nextInt(10)+1;

        if(fromAPI){
            int[] new_data = ChannelAdapter.openMeteoToWeatherStation();
            this.humidity = new_data[0];
            this.temperature = new_data[1];
            this.wind_speed = new_data[2];
            this.drop_rate = new_data[3];
        }

    }

    public  String updateBatteryStatus(){
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;
        if (randomNumber <= 3) {
            return "low";
        } else if (randomNumber <= 7) {
            return "med";
        } else {
            return "high";
        }
    }
    public String generate_new_msg(){
        Random random = new Random();
        this.s_no ++;
        this.status = this.updateBatteryStatus();
        this.status_timestamp = System.currentTimeMillis() / 1000L;
        this.humidity = random.nextInt(100);
        this.temperature = random.nextInt(134);
        this.wind_speed = random.nextInt(408);
        this.drop_rate = random.nextInt(10)+1;
        return outputMessage();
    }
    public String generate_new_msg(boolean fromAPI){
        Random random = new Random();
        this.s_no ++;
        this.status = this.updateBatteryStatus();
        this.status_timestamp = System.currentTimeMillis() / 1000L;
        if(fromAPI){

        }
        else{
            this.humidity = random.nextInt(100);
            this.temperature = random.nextInt(134);
            this.wind_speed = random.nextInt(408);
            this.drop_rate = random.nextInt(10)+1;
        }

        return outputMessage();
    }
    public  String outputMessage(){
        String message="";
        if(drop_rate>1) {
            JSONObject JsonMsg = new JSONObject();
            JsonMsg.put("station_id", station_id);
            JsonMsg.put("s_no", s_no);
            JsonMsg.put("battery_status", status);
            JsonMsg.put("status_timestamp", status_timestamp);

            JSONObject weather = new JSONObject();

            weather.put("humidity", humidity);
            weather.put("temperature", temperature);
            weather.put("wind_speed", wind_speed);

            JsonMsg.put("weather", weather);
            message = JsonMsg.toString();
        }
        return message;
    }
}
