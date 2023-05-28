package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String bootstrapServers="my-kafka:9092";
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application");
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> weatherStream = builder.stream(
                "WeatherStationData", /* input topic */
                Consumed.with(
                        Serdes.String(), /* key serde */
                        Serdes.String()   /* value serde */
                ));
        KStream<String, String> rainingStream = weatherStream.filter((key, value) -> {
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(value);
                String StationId = (String) json.get("station_id");
                JSONObject weather = (JSONObject) json.get("weather");
                String humidity = String.valueOf(weather.get("humidity"));
                int humidity_number = Integer.parseInt(humidity);
                if(humidity_number>=70){
                    System.out.println("humidity = "+humidity_number+"It is raining in "+StationId);
                    return true;
                }
                else{
                    return false;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            });
        rainingStream.to("RainingTopics", Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
        }
    }
