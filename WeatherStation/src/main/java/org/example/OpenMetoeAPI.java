package org.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class OpenMetoeAPI {
    public static String getAPI()throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("https://api.open-meteo.com/v1/forecast?" +
                    "latitude=31.20" +
                    "&longitude=29.92" +
                    "&hourly=temperature_2m,");

            //System.out.println("Executing GET request...");
            HttpResponse response = httpclient.execute(httpget);
            String responseBody = new BasicResponseHandler().handleResponse(response);

            //System.out.println("Response: " + responseBody);
            return responseBody;
        }
    }
}
