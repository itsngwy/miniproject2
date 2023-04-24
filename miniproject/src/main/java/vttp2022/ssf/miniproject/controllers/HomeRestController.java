package vttp2022.ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp2022.ssf.miniproject.services.TransportService;
import vttp2022.ssf.miniproject.services.WeatherService;

@RestController
@RequestMapping(path="/json")
public class HomeRestController {

    @Autowired
    private TransportService transportSvc;

    @Autowired
    private WeatherService weatherSvc;
    
    @GetMapping(value="{busStopCode}")
    public ResponseEntity<String> getJsonBusTiming(@PathVariable String busStopCode) {
        JsonObject myBus = transportSvc.getBusTimingInJson(busStopCode);
        JsonArray busData = myBus.getJsonArray("Services");

        //System.out.println(myBus);

        if (busData.isEmpty()) {
            JsonObject err = Json.createObjectBuilder()
                .add("error", "Bus stop code %s not found".formatted(busStopCode))
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(err.toString());
        }

        return ResponseEntity.ok(myBus.toString());
    }

    @GetMapping(path="/weather")
    public ResponseEntity<String> getJsonWeather() {

        JsonObject myWeather = weatherSvc.getWeatherInJson();
        JsonArray weatherData = myWeather.getJsonArray("items");

        //System.out.println(myWeather);

        if (weatherData.isEmpty()) {
            JsonObject err = Json.createObjectBuilder()
                .add("error", "Weather currently not available")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(err.toString());
        }

        return ResponseEntity.ok(myWeather.toString());
    }

}
