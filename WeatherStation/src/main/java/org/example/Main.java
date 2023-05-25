package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.Properties;


//@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception{
        String bootstrapServers="127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        WeatherStationMock weatherStation = new WeatherStationMock(args[0],false);

        while(true) {
             String msg = weatherStation.generate_new_msg();

             KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
             ProducerRecord<String, String> record = new ProducerRecord<>("WeatherStationData", msg);
             producer.send(record);
             producer.flush();
             producer.close();

             Thread.sleep(1000);
        }

    }
}