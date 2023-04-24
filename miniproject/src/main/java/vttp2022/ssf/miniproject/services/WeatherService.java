package vttp2022.ssf.miniproject.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.ssf.miniproject.models.UserAccount;
import vttp2022.ssf.miniproject.models.Weather;
import vttp2022.ssf.miniproject.models.WeatherArea;
import vttp2022.ssf.miniproject.repositories.AccountRepository;

@Service
public class WeatherService {

    @Autowired
    private AccountRepository accRepo;
    
    private static final String URL = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";

    public Optional<Weather> getWeather() {
        System.out.println("Getting weather from https://data.gov.sg/dataset/weather-forecast");

        // Get date time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
        System.out.println(formatter.format(date).toString());

        // This is for the date_time request method
        String url = URL + "?date_time" + URLEncoder.encode(formatter.format(date).toString(), StandardCharsets.US_ASCII);
        System.out.println(url);

        // Create the GET request, the GET url
        // RequestEntity<Void> is always void inside
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        // Try catch errors while exchanging for response
        try {
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return null;
        }

        String payload = resp.getBody();
        //System.out.println("payload: " + payload);

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject weatherResult = jsonReader.readObject();
        JsonArray weather = weatherResult.getJsonArray("items");
        JsonObject jo = weather.get((weather.size())-1).asJsonObject();

        Weather weathers = new Weather();
        // if (jo.getJsonArray("area_metadata") == null) {
        //     return Optional.empty();
        // }
        weathers.create(jo);

        List<WeatherArea> weatherList = weathers.getListOfWeatherInAreas();

        // for (int i = 0; i < weatherList.size(); i++) {
        //     System.out.println(weatherList.get(i).getArea());
        //     System.out.println(weatherList.get(i).getForecast());
        // }

        return Optional.of(weathers);
    }

    public Boolean addFavArea(String email, String favArea) {

        // Update Fav Location
        Integer count = accRepo.updateFavWeather(email, favArea);
        System.out.println("Trying to update fav area");
        if (count > 0) {
            System.out.println("Fav Area updated successfully");
            return true;
        }
        
        return false;
    }

    public String getFavArea(String email) {
        return accRepo.getFavWeather(email);
    }

    public JsonObject getWeatherInJson() {
        System.out.println("Getting weather from https://data.gov.sg/dataset/weather-forecast");

        // Get date time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
        System.out.println(formatter.format(date).toString());

        // This is for the date_time request method
        String url = URL + "?date_time" + URLEncoder.encode(formatter.format(date).toString(), StandardCharsets.US_ASCII);
        System.out.println(url);

        // Create the GET request, the GET url
        // RequestEntity<Void> is always void inside
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        // Try catch errors while exchanging for response
        try {
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return null;
        }

        String payload = resp.getBody();
        //System.out.println("payload: " + payload);

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject weatherResult = jsonReader.readObject();

        return weatherResult;
    }

}
