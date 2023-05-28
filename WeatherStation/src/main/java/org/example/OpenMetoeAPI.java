package org.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class OpenMetoeAPI {
    public static String getAPI(String stationid)throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String[] latitude = {"31.20", "21.43", "49.25", "47.37", "37.77",
                    "60.39", "52.37", "55.68", "-33.87", "48.21"};
            String[] longitude = {"29.92", "39.83", "-123.12", "8.55", "-122.42",
                    "5.32", "4.89", "12.57", "151.21", "16.37"};
            String url = "https://api.open-meteo.com/v1/forecast?latitude="+
                    latitude[Integer.parseInt(stationid)]+"&longitude="+longitude[Integer.parseInt(stationid)]+
                    "&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&timeformat=unixtime";
            HttpGet httpget = new HttpGet(url);

            //System.out.println("Executing GET request...");
            HttpResponse response = httpclient.execute(httpget);
            String responseBody = new BasicResponseHandler().handleResponse(response);

            //System.out.println("Response: " + responseBody);
            return responseBody;
        }
    }
}
