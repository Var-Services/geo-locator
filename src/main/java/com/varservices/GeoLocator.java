package com.varservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoLocator {

    public static String getGeoLocation(String ipAddress, String apiKey) {
        String urlString = "http://api.ipstack.com/" + ipAddress + "?access_key=" + apiKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // parsing json response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.toString());
                String country = rootNode.path("country_name").asText();
                String region = rootNode.path("region_name").asText();
                String city = rootNode.path("city").asText();

                return String.format("IP Address: %s\nCountry: %s\nRegion: %s\nCity: %s",
                        ipAddress, country, region, city);
            } else {
                return "GET request not worked";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }
}