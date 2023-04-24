package vttp2022.ssf.miniproject.models;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class TrainCrowd {
    
    private String stationName;
    private String startTime;
    private String endTime;
    private String crowdLevel;

    private static Map<String, String> trainLineMap = new HashMap<>() {{
        put("CCL", "Circle Line - CCL");
        put("CEL", "Circle Line Extension - Bayfront, Marina Bay - CEL");
        put("CGL", "Changi Extension - Expo, Changi Airport - CGL");
        put("DTL", "Downtown Line - DTL");
        put("EWL", "East West Line - EWL");
        put("NEL", "North East Line - NEL");
        put("NSL", "North South Line - NSL");
        put("BPL", "Bukit Panjang LRT - BPL");
    }};

    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getCrowdLevel() {
        return crowdLevel;
    }
    public void setCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public static TrainCrowd create(JsonObject jo) {
        TrainCrowd tc = new TrainCrowd();
        tc.setStationName(jo.getString("Station"));
        tc.setStartTime(formatTime(jo.getString("StartTime")));
        tc.setEndTime(formatTime(jo.getString("EndTime")));
        tc.setCrowdLevel(jo.getString("CrowdLevel"));
        return tc;
    }

    public static String formatTime(String dateTime) {
        String substringTime = dateTime.substring(0, 19);
        substringTime = substringTime.replaceAll("T", " ");
        return substringTime;
    }

    public static String getTrainLine(String trainCode) {
        return trainLineMap.get(trainCode);
    }

    public static String getStationName(String trainCode) {
        return trainLineName.get(trainCode);
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                    .add("stationName", stationName)
                    .add("crowdLevel", crowdLevel)
                    .build();
    }

    private static Map<String, String> trainLineName = new HashMap<>() {{
        put("NS1", "Jurong East");
        put("NS2", "Bukit Batok");
        put("NS3", "Bukit Gombak");
        put("NS4", "Choa Chu Kang");
        put("NS5", "Yew Tee");
        put("NS7", "Kranji");
        put("NS8", "Marsiling");
        put("NS9", "Woodlands");
        put("NS10", "Admiralty");
        put("NS11", "Sembawang");
        put("NS12", "Canberra");
        put("NS13", "Yishun");
        put("NS14", "Khatib");
        put("NS15", "Yio Chu Kang");
        put("NS16", "Ang Mo Kio");
        put("NS17", "Bishan");
        put("NS18", "Braddell");
        put("NS19", "Toa Payoh");
        put("NS20", "Novena");
        put("NS21", "Newton");
        put("NS22", "Orchard");
        put("NS23", "Somerset");
        put("NS24", "Dhoby Ghaut");
        put("NS25", "City Hall");
        put("NS26", "Raffles Place");
        put("NS27", "Marina Bay");
        put("NS28", "Marina South Pier");

        put("EW1", "Pasir Ris");
        put("EW2", "Tampines");
        put("EW3", "Simei");
        put("EW4", "Tanah Merah");
        put("EW5", "Bedok");
        put("EW6", "Kembangan");
        put("EW7", "Eunos");
        put("EW8", "Paya Lebar");
        put("EW9", "Aljunied");
        put("EW10", "Kallang");
        put("EW11", "Lavender");
        put("EW12", "Bugis");
        put("EW13", "City Hall");
        put("EW14", "Raffles Place");
        put("EW15", "Tanjong Pagar");
        put("EW16", "Outram Park");
        put("EW17", "Tiong Bahru");
        put("EW18", "Redhill");
        put("EW19", "Queenstown");
        put("EW20", "Commonwealth");
        put("EW21", "Buona Vista");
        put("EW22", "Dover");
        put("EW23", "Clementi");
        put("EW24", "Jurong East");
        put("EW25", "Chinese Garden");
        put("EW26", "Lakeside");
        put("EW27", "Boon Lay");
        put("EW28", "Pioneer");
        put("EW29", "Joo Koon");
        put("EW30", "Gul Circle");
        put("EW31", "Tuas Crescent");
        put("EW32", "Tuas West Road");
        put("EW33", "Tuas Link");

        put("CG1", "Expo");
        put("CG2", "Changi Airport");

        put("CE1", "Bayfront");
        put("CE2", "Marina Bay");

        put("NE1", "HarbourFront");
        put("NE3", "Outram Park");
        put("NE4", "Chinatown");
        put("NE5", "Clarke Quay");
        put("NE6", "Dhoby Ghaut");
        put("NE7", "Little India");
        put("NE8", "Farrer Park");
        put("NE9", "Boon Keng");
        put("NE10", "Potong Pasir");
        put("NE11", "Woodleigh");
        put("NE12", "Serangoon");
        put("NE13", "Kovan");
        put("NE14", "Hougang");
        put("NE15", "Buangkok");
        put("NE16", "Sengkang");
        put("NE17", "Punggol");

        put("CC1", "Dhoby Ghaut");
        put("CC2", "Bras Basah");
        put("CC3", "Esplanade");
        put("CC4", "Promenade");
        put("CC5", "Nicoll Highway");
        put("CC6", "Stadium");
        put("CC7", "Mountbatten");
        put("CC8", "Dakota");
        put("CC9", "Paya Lebar");
        put("CC10", "MacPherson");
        put("CC11", "Tai Seng");
        put("CC12", "Bartley");
        put("CC13", "Serangoon");
        put("CC14", "Lorong Chuan");
        put("CC15", "Bishan");
        put("CC16", "Marymount");
        put("CC17", "Caldecott");
        put("CC19", "Botanic Gardens");
        put("CC20", "Farrer Road");
        put("CC21", "Holland Village");
        put("CC22", "Buona Vista");
        put("CC23", "one-north");
        put("CC24", "Kent Ridge");
        put("CC25", "Haw Par Villa");
        put("CC26", "Pasir Panjang");
        put("CC27", "Labrador Park");
        put("CC28", "Telok Blangah");
        put("CC29", "HarbourFront");

        put("DT1", "Bukit Panjang");
        put("DT2", "Cashew");
        put("DT3", "Hillview");
        put("DT5", "Beauty World");
        put("DT6", "King Albert Park");
        put("DT7", "Sixth Avenue");
        put("DT8", "Tan Kah Kee");
        put("DT9", "Botanic Gardens");
        put("DT10", "Stevens");
        put("DT11", "Newton");
        put("DT12", "Little India");
        put("DT13", "Rochor");
        put("DT14", "Bugis");
        put("DT15", "Promenade");
        put("DT16", "Bayfront");
        put("DT17", "Downtown");
        put("DT18", "Telok Ayer");
        put("DT19", "Chinatown");
        put("DT20", "Fort Canning");
        put("DT21", "Bencoolen");
        put("DT22", "Jalan Besar");
        put("DT23", "Bendemeer");
        put("DT24", "Geylang Bahru");
        put("DT25", "Mattar");
        put("DT26", "MacPherson");
        put("DT27", "Ubi");
        put("DT28", "Kaki Bukit");
        put("DT29", "Bedok North");
        put("DT30", "Bedok Reservoir");
        put("DT31", "Tampines West");
        put("DT32", "Tampines");
        put("DT33", "Tampines East");
        put("DT34", "Upper Changi");
        put("DT35", "Expo");

        put("BP1", "Choa Chu Kang");
        put("BP2", "South View");
        put("BP3", "Keat Hong");
        put("BP4", "Teck Whye");
        put("BP5", "Phoenix");
        put("BP6", "Bukit Panjang");
        put("BP7", "Petir");
        put("BP8", "Pending");
        put("BP9", "Bangkit");
        put("BP10", "Fajar");
        put("BP11", "Segar");
        put("BP12", "Jelapang");
        put("BP13", "Senja");
    }};
}
