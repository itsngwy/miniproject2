package vttp2022.ssf.miniproject.models;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import jakarta.json.JsonObject;

public class NextBus {
    
    private String eta;
    private String latitude;
    private String longtitude;
    private String load;
    private String feature;
    private String type;

    public String getEta() {
        return eta;
    }
    public void setEta(String eta) {
        this.eta = eta;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongtitude() {
        return longtitude;
    }
    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
    public String getLoad() {
        return load;
    }
    public void setLoad(String load) {
        this.load = load;
    }
    public String getFeature() {
        return feature;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public static NextBus nextBusCreate(JsonObject jo) {
        NextBus nb = new NextBus();
        //nb.setEta(jo.getString("EstimatedArrival"));
        nb.setEta(calculateEta(jo.getString("EstimatedArrival")));
        nb.setLatitude(jo.getString("Latitude"));
        nb.setLongtitude(jo.getString("Longitude"));
        nb.setLoad(jo.getString("Load"));
        nb.setFeature(jo.getString("Feature"));
        nb.setType(jo.getString("Type"));
        return nb;
    }

    public static String calculateEta(String eta) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");

        // To make sure the timing we get is always Singapore timing!
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String currentTime = formatter.format(date).toString();

        // If no timing given by the API, return -
        if (eta.isEmpty()) {
            return "-";
        }

        eta = eta.substring(11, 19);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long difference = 0;

        try {
            Date date1 = format.parse(eta);
            Date date2 = format.parse(currentTime);
            System.out.println(date1);
            System.out.println(date2);
            difference = (date1.getTime() - date2.getTime())/60000;
            System.out.println(difference);
            //System.out.printf("start %s, end %s\n", date1, date2);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
        }
        
        String busEta = Long.toString(difference);
        //System.out.println(busEta);

        if (difference < 1) {
            return "Arr";
        }
        return busEta;
    }
}
