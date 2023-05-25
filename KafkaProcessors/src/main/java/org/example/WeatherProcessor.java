package org.example;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.Duration;

public class WeatherProcessor implements Processor<String,String> {
    @Override
    public void init(ProcessorContext processorContext) {
        processorContext.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {

        });
    }

    @Override
    public void process(String s, String s2) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(s2);
            String StationId = (String) json.get("station_id");
            JSONObject weather = (JSONObject) json.get("weather");
            String humidiy = (String)weather.get("humidity");
            int humidity_number = Integer.parseInt(humidiy);
            if(humidity_number>=70){
                System.out.println("It is raining in "+StationId);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {

    }
}
