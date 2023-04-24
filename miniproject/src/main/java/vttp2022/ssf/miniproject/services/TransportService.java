package vttp2022.ssf.miniproject.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.ssf.miniproject.models.Bus;
import vttp2022.ssf.miniproject.models.BusStop;
import vttp2022.ssf.miniproject.models.TrainCrowd;
import vttp2022.ssf.miniproject.repositories.AccountRepository;

@Service
public class TransportService {
    
    private static final String BUSURL = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";
    private static final String TRAINURL = "http://datamall2.mytransport.sg/ltaodataservice/PCDRealTime";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private AccountRepository accRepo;
    
    public String getBusCode(String busName) {
        List<Document> ls = accRepo.getBusCodeUsingName(busName);

        List<BusStop> ls2 = ls.stream()
                    .map(d -> BusStop.create(d))
                    .toList();
        //System.out.println(ls2.get(0).getBusStopCode());
        return ls2.get(0).getBusStopCode();
    }

    public String getBusStopName(String busCode) {
        List<Document> ls = accRepo.getBusStopNameUsingCode(busCode);

        List<BusStop> ls2 = ls.stream()
                    .map(d -> BusStop.create(d))
                    .toList();

        return ls2.get(0).getDescription();
    }

    // Get train platform crowd
    public List<TrainCrowd> getTrainCrowd(String trainLine) {

        String payload;
        String url = UriComponentsBuilder.fromUriString(TRAINURL)
                .queryParam("TrainLine", trainLine)
                .queryParam("AccountKey", key)
                .toUriString();

        System.out.println(url);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("AccountKey", key);
            HttpEntity request = new HttpEntity(headers);

            // Throws an exception if status code not in between 200 - 399
            // Get response from the client with our request
            resp = template.exchange(url, HttpMethod.GET, request,String.class, 1);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return Collections.emptyList();
        }

        payload = resp.getBody();
        System.out.println("payload: " + payload);

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject trainCrowdResult = jsonReader.readObject();

        JsonArray trainCrowdData = trainCrowdResult.getJsonArray("value");

        List<TrainCrowd> trainCrowdList = new LinkedList<>();

        for (int i = 0; i <  trainCrowdData.size(); i++) {
            JsonObject jo = trainCrowdData.getJsonObject(i);
            trainCrowdList.add(TrainCrowd.create(jo));
        }

        trainCrowdList = trainCrowdList.stream()
            .sorted((b1, b2) -> {
                String b1New;
                String b2New;
                if (b1.getStationName().matches(".*[a-zA-Z]+.*") || b2.getStationName().matches(".*[a-zA-Z]+.*")) {
                    b1New = b1.getStationName().replaceAll("[^\\d.]", "");
                    b2New = b2.getStationName().replaceAll("[^\\d.]", "");
                } else {
                    b1New = b1.getStationName();
                    b2New = b2.getStationName();
                }
                if (Integer.parseInt(b1New) > Integer.parseInt(b2New))
                    return 1;
                else return -1;
            })
            .collect(Collectors.toList());

        getStationName(trainCrowdList);
        return trainCrowdList;
    }

    public void getStationName(List<TrainCrowd> trainCrowdList) {
        for (int i = 0; i < trainCrowdList.size(); i++) {
            String stationCode = trainCrowdList.get(i).getStationName();
            trainCrowdList.get(i).setStationName(stationCode + " - " + TrainCrowd.getStationName(stationCode));
        }
    }

    public List<Bus> getBusTiming(String busStopCode) {
        
        String payload;

        String url = UriComponentsBuilder.fromUriString(BUSURL)
                .queryParam("BusStopCode", busStopCode)
                .queryParam("AccountKey", key)
                .toUriString();

        System.out.println(url);
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("AccountKey", key);
            HttpEntity request = new HttpEntity(headers);

            // Throws an exception if status code not in between 200 - 399
            // Get response from the client with our request
            resp = template.exchange(url, HttpMethod.GET, request, String.class, 1);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return Collections.emptyList();
        }

        payload = resp.getBody();
        //System.out.println("payload: " + payload);

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject busResult = jsonReader.readObject();

        JsonArray busData = busResult.getJsonArray("Services");

        List<JsonObject> sortingList = new LinkedList<>();

        for (int i = 0; i <  busData.size(); i++) {
            sortingList.add(busData.getJsonObject(i));
        }

        List<Bus> list = new LinkedList<>();

        for (int i = 0; i < busData.size(); i++) {
            JsonObject jo = busData.getJsonObject(i);
            // Static create each bus object
            list.add(Bus.create(jo));
        }

        // list = list.stream()
        //         .sorted(Comparator.comparing(Bus::getServiceNo))
        //         .collect(Collectors.toList());

        list = list.stream()
            .sorted((b1, b2) -> {
                String b1New;
                String b2New;
                if (b1.getServiceNo().matches(".*[a-zA-Z]+.*") || b2.getServiceNo().matches(".*[a-zA-Z]+.*")) {
                    b1New = b1.getServiceNo().replaceAll("[^\\d.]", "");
                    b2New = b2.getServiceNo().replaceAll("[^\\d.]", "");
                } else {
                    b1New = b1.getServiceNo();
                    b2New = b2.getServiceNo();
                }
                if (Integer.parseInt(b1New) > Integer.parseInt(b2New))
                    return 1;
                else return -1;
            })
            .collect(Collectors.toList());
        
        for (int i = 0; i < list.size(); i++) {
            Bus myBus = list.get(i);
            System.out.println(myBus.getServiceNo());
            // System.out.println(myBus.getOperator());
            // System.out.println();

        //     System.out.println(myBus.getNextBus1().getEta());
        //     System.out.println(myBus.getNextBus1().getLatitude());
        //     System.out.println(myBus.getNextBus1().getLongtitude());
        //     System.out.println(myBus.getNextBus1().getLoad());
            System.out.println(myBus.getNextBus1().getFeature());
        //     System.out.println(myBus.getNextBus1().getType());
        //     System.out.println();

        //     System.out.println(myBus.getNextBus2().getEta());
        //     System.out.println(myBus.getNextBus2().getLatitude());
        //     System.out.println(myBus.getNextBus2().getLongtitude());
        //     System.out.println(myBus.getNextBus2().getLoad());
            System.out.println(myBus.getNextBus2().getFeature());
        //     System.out.println(myBus.getNextBus2().getType());
        //     System.out.println();

        //     System.out.println(myBus.getNextBus3().getEta());
        //     System.out.println(myBus.getNextBus3().getLatitude());
        //     System.out.println(myBus.getNextBus3().getLongtitude());
        //     System.out.println(myBus.getNextBus3().getLoad());
            System.out.println(myBus.getNextBus3().getFeature());
        //     System.out.println(myBus.getNextBus3().getType());
            System.out.println();
        }

        //System.out.println(busData.toString());

        return list;
    }

    public JsonObject getBusTimingInJson(String busStopCode) { 

        String payload;

        String url = UriComponentsBuilder.fromUriString(BUSURL)
                .queryParam("BusStopCode", busStopCode)
                .queryParam("AccountKey", key)
                .toUriString();

        System.out.println(url);
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("AccountKey", key);
            HttpEntity request = new HttpEntity(headers);

            // Throws an exception if status code not in between 200 - 399
            // Get response from the client with our request
            resp = template.exchange(url, HttpMethod.GET, request, String.class, 1);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return null;
        }

        payload = resp.getBody();
        //System.out.println("payload: " + payload);

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject busResult = jsonReader.readObject();

        return busResult;
    }

}
