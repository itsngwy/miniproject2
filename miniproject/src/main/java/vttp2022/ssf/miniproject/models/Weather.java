package vttp2022.ssf.miniproject.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Weather {
    
    private String updatedTimeStamp;
    private String validStartTime;
    private String validEndTime;
    private List<WeatherArea> listOfWeatherInAreas = new LinkedList<>();
    private Map<String, String> weatherMap = new HashMap<>();

    public String getUpdatedTimeStamp() {
        return updatedTimeStamp;
    }
    public void setUpdatedTimeStamp(String updatedTimeStamp) {
        this.updatedTimeStamp = updatedTimeStamp;
    }
    public String getValidStartTime() {
        return validStartTime;
    }
    public void setValidStartTime(String validStartTime) {
        this.validStartTime = validStartTime;
    }
    public String getValidEndTime() {
        return validEndTime;
    }
    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }
    public List<WeatherArea> getListOfWeatherInAreas() {
        return listOfWeatherInAreas;
    }
    public void setListOfWeatherInAreas(List<WeatherArea> listOfWeatherInAreas) {
        this.listOfWeatherInAreas = listOfWeatherInAreas;
    }
    public Map<String, String> getWeatherMap() {
        return weatherMap;
    }
    
    public void create(JsonObject jo) {
        // this.setUpdatedTimeStamp(jo.getString("update_timestamp"));
        // this.setValidStartTime(jo.getJsonObject("valid_period").getString("start"));
        // this.setValidEndTime(jo.getJsonObject("valid_period").getString("end"));
        this.setUpdatedTimeStamp(formatTime(jo.getString("update_timestamp")));
        this.setValidStartTime(formatTime(jo.getJsonObject("valid_period").getString("start")));
        this.setValidEndTime(formatTime(jo.getJsonObject("valid_period").getString("end")));
        System.out.println(this.getUpdatedTimeStamp());
        System.out.println(this.getValidStartTime());
        System.out.println(this.getValidEndTime());

        JsonArray ja = jo.getJsonArray("forecasts");

        for (int i = 0; i < ja.size(); i++) {
            WeatherArea wl = new WeatherArea();
            JsonObject joWL = ja.get(i).asJsonObject();
            wl.setArea(joWL.getString("area"));
            wl.setForecast(joWL.getString("forecast"));
            weatherMap.put(joWL.getString("area"), joWL.getString("forecast"));
            listOfWeatherInAreas.add(wl);
        }
    }

    public String formatTime(String dateTime) {
        String substringTime = dateTime.substring(0, 19);
        substringTime = substringTime.replaceAll("T", " ");
        return substringTime;
    }

    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        listOfWeatherInAreas.stream()
                .forEach(wa -> {
                    jab.add(
                        Json.createObjectBuilder()
                        .add("area", wa.getArea())
                        .add("forecast", wa.getForecast())
                        .build()
                    );
        });    

        return Json.createObjectBuilder()
                    .add("updatedTimeStamp", updatedTimeStamp)
                    .add("validStartTime", validStartTime)
                    .add("validEndTime", validEndTime)
                    .add("listOfWeatherInAreas", jab.build())
                    .build();
    }
    
}
