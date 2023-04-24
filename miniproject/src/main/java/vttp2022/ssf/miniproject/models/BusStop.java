package vttp2022.ssf.miniproject.models;

import org.bson.Document;

public class BusStop {
    private String busStopCode;
    private String roadName;
    private String Description;
    private Double latitude;
    private Double longitude;

    public String getBusStopCode() {
        return busStopCode;
    }
    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }
    public String getRoadName() {
        return roadName;
    }
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static BusStop create(Document d) {
        BusStop bs = new BusStop();
        bs.setBusStopCode(d.getString("BusStopCode"));
        bs.setRoadName(d.getString("RoadName"));
        bs.setDescription(d.getString("Description"));
        bs.setLatitude(d.getDouble("Latitude"));
        bs.setLongitude(d.getDouble("Longitude"));
        return bs;
    }
}
