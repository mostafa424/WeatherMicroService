package org.example;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws ParseException {
        Logger logger = LoggerFactory.getLogger(Main.class.getName());
        String bootstrapServers="127.0.0.1:9092";
        String grp_id="g1";
        String topic="WeatherStationData";
        //Creating consumer properties
        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //creating consumer
        KafkaConsumer<String,String> consumer= new KafkaConsumer<String,String>(properties);
        //Subscribing
        consumer.subscribe(Arrays.asList(topic));
        //polling
        List<ConsumerRecord<String,String>>buffer = new ArrayList<>();
        while(true){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String,String> record: records){
                //logger.info("Key: "+ record.key() + ", Value:" +record.value());
                //logger.info("Partition:" + record.partition()+",Offset:"+record.offset());
                System.out.println(record.value());
                buffer.add(record);
                if(buffer.size()==10){
                    putAsParquet(buffer);buffer.clear();
                }
            }
        }
    }
    public static void putAsParquet(List<ConsumerRecord<String,String>> buffer) throws ParseException {
        ArrayList<ArrayList<GenericData.Record>> recordList = new ArrayList<ArrayList<GenericData.Record>>();
        for(int i=0; i <= 10; i++)
            recordList.add(new ArrayList<>());

        for(ConsumerRecord<String, String> record: buffer){
            JSONParser parser = new JSONParser();
            try{
                JSONObject recordJson = (JSONObject) parser.parse(record.value());

                long station_id = Long.parseLong((String) recordJson.get("station_id"));
                long s_no = (long) recordJson.get("s_no");
                String battery_status = (String) recordJson.get("battery_status");
                long status_timestamp = (long) recordJson.get("status_timestamp");
                JSONObject weatherJson = (JSONObject) recordJson.get("weather");
                int humidity = Integer.parseInt(String.valueOf(weatherJson.get("humidity")));
                int temperature = Integer.parseInt(String.valueOf(weatherJson.get("temperature")));
                int wind_speed = Integer.parseInt(String.valueOf(weatherJson.get("wind_speed")));

                GenericData.Record genericRecord = new GenericData.Record(ParquetWriter.parseSchema());
                genericRecord.put("station_id", station_id);
                genericRecord.put("s_no", s_no);
                genericRecord.put("battery_status", battery_status);
                genericRecord.put("status_timestamp", status_timestamp);
                genericRecord.put("humidity", humidity);
                genericRecord.put("temperature", temperature);
                genericRecord.put("wind_speed", wind_speed);

                recordList.get((int) station_id).add(genericRecord);
            } catch(Exception ignored){}
        }
        for(int i = 1; i <= 10; i++){
            if(recordList.get(i).size() > 0)
                ParquetWriter.writeToParquetFile(recordList.get(i), i);
        }
    }
}