package vttp2022.ssf.miniproject.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Bus {

    private String serviceNo;
    private String operator;
    private NextBus nextBus1;
    private NextBus nextBus2;
    private NextBus nextBus3;

    public String getServiceNo() {
        return serviceNo;
    }
    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public NextBus getNextBus1() {
        return nextBus1;
    }
    public void setNextBus1(NextBus nextBus1) {
        this.nextBus1 = nextBus1;
    }
    public NextBus getNextBus2() {
        return nextBus2;
    }
    public void setNextBus2(NextBus nextBus2) {
        this.nextBus2 = nextBus2;
    }
    public NextBus getNextBus3() {
        return nextBus3;
    }
    public void setNextBus3(NextBus nextBus3) {
        this.nextBus3 = nextBus3;
    }

    public static Bus create(JsonObject jo) {
        Bus b = new Bus();
        //b.setServiceNo(Integer.parseInt(jo.getString("ServiceNo")));
        b.setServiceNo(jo.getString("ServiceNo"));
        b.setOperator(jo.getString("Operator"));
        b.setNextBus1(NextBus.nextBusCreate(jo.getJsonObject("NextBus")));
        b.setNextBus2(NextBus.nextBusCreate(jo.getJsonObject("NextBus2")));
        b.setNextBus3(NextBus.nextBusCreate(jo.getJsonObject("NextBus3")));
        return b;
    }

    public JsonObject toJson() {
        JsonObject nBus1 = Json.createObjectBuilder()
                    .add("eta", nextBus1.getEta())
                    .add("latitude", nextBus1.getLatitude())
                    .add("longtitude", nextBus1.getLongtitude())
                    .add("load", nextBus1.getLoad())
                    .add("feature", nextBus1.getFeature())
                    .add("type", nextBus1.getType())
                    .build();
        JsonObject nBus2 = Json.createObjectBuilder()
                    .add("eta", nextBus2.getEta())
                    .add("latitude", nextBus2.getLatitude())
                    .add("longtitude", nextBus2.getLongtitude())
                    .add("load", nextBus2.getLoad())
                    .add("feature", nextBus2.getFeature())
                    .add("type", nextBus2.getType())
                    .build();
        JsonObject nBus3 = Json.createObjectBuilder()
                    .add("eta", nextBus3.getEta())
                    .add("latitude", nextBus3.getLatitude())
                    .add("longtitude", nextBus3.getLongtitude())
                    .add("load", nextBus3.getLoad())
                    .add("feature", nextBus3.getFeature())
                    .add("type", nextBus3.getType())
                    .build();

        return Json.createObjectBuilder()
                    .add("serviceNo", serviceNo)
                    .add("operator", operator)
                    .add("nextBus1", nBus1)
                    .add("nextBus2", nBus2)
                    .add("nextBus3", nBus3)
                    .build();
    }
    
}
