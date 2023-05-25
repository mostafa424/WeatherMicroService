package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {
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
                buffer.add(record);
                if(buffer.size()==10000){
                    putAsParquet(buffer);buffer.clear();
                }
            }
        }
    }
    public static void putAsParquet(List<ConsumerRecord<String,String>>buffer){

    }
}