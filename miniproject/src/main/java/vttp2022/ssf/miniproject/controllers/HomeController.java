package vttp2022.ssf.miniproject.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.ssf.miniproject.models.Bus;
import vttp2022.ssf.miniproject.models.TrafficImages;
import vttp2022.ssf.miniproject.models.TrainCrowd;
import vttp2022.ssf.miniproject.models.Weather;
import vttp2022.ssf.miniproject.services.FireBaseService;
import vttp2022.ssf.miniproject.services.TrafficService;
import vttp2022.ssf.miniproject.services.TransportService;
import vttp2022.ssf.miniproject.services.WeatherService;

@Controller
@RequestMapping(path="/api")
public class HomeController {
    
    @Autowired
    private TransportService transportSvc;

    @Autowired
    private WeatherService weatherSvc;

    @Autowired
    private TrafficService traffSvc;

    @Autowired
    private FireBaseService fireBaseSvc;
    
    @GetMapping(path="/getBusTiming")
    @ResponseBody
    public ResponseEntity<String> getBusTiming(@RequestParam String busCode, HttpSession sess) {
       
        System.out.println(busCode);
        String busString = busCode.strip();
        JsonObjectBuilder job = Json.createObjectBuilder();

        if (!busString.matches("\\d+")) {
            System.out.println("contains alphabets");
            String busCode2 = transportSvc.getBusCode(busString);
            job.add("busStopDetails", "%s - %s".formatted(busString, busCode2));
            busString = busCode2;
            System.out.println(busString);
        } else {
            String busStopName = transportSvc.getBusStopName(busCode);
            job.add("busStopDetails", "%s - %s".formatted(busStopName, busString));
        }

        List<Bus> myBus = transportSvc.getBusTiming(busString);
        JsonArrayBuilder jab = Json.createArrayBuilder();

        myBus.stream().forEach(b -> {
            jab.add(b.toJson());
        });

        job.add("busDetails", jab.build());

        return ResponseEntity.ok(job.build().toString());
    }

    @GetMapping(path="/weatherPage")
    @ResponseBody
    public ResponseEntity<String> weatherPage(HttpSession sess) {

        Optional<Weather> myWeathers = weatherSvc.getWeather();
        
        if (myWeathers.isEmpty()) {
            System.out.println("it is null");
            return ResponseEntity.ok(Json.createObjectBuilder().add("error", "error, weather api is currently on maintenance").build().toString());
        }

        // System.out.println(myWeathers.toJson().toString());
        return ResponseEntity.ok(myWeathers.get().toJson().toString());
    }

    @PutMapping(path="/favArea")
    @ResponseBody
    public ResponseEntity<String> getWeather(@RequestParam MultiValueMap<String, String> form, HttpSession sess) {

        String favArea = form.getFirst("favourite");
        String email = form.getFirst("email");
        System.out.println("From Angular: " + favArea);
        
        Boolean hasAdded = weatherSvc.addFavArea(email, favArea);
        
        // String favWeather = myWeathers.getWeatherMap().get(updatedUserInfo.getFavArea());
        // System.out.println(favWeather);

        return ResponseEntity.ok(Json.createObjectBuilder().add("hasAdded", hasAdded).build().toString());
    }

    @GetMapping(path="/getFavArea")
    @ResponseBody
    public ResponseEntity<String> getFavArea(@RequestParam String email) {

        String favLocation = weatherSvc.getFavArea(email);

        JsonObjectBuilder job = Json.createObjectBuilder();

        if (favLocation.equals("-")) {
            job.add("favWeather", "-");
            return ResponseEntity.ok(job.build().toString());
        }
        
        Weather myWeathers = weatherSvc.getWeather().get();
        String favWeather = myWeathers.getWeatherMap().get(favLocation);

        System.out.println("From SQL: " + favWeather);

        job.add("favWeather", "%s - %s".formatted(favLocation, favWeather));
        System.out.println();

        return ResponseEntity.ok(job.build().toString());
    }

    @GetMapping(path="/getTrainCrowd/{trainCode}")
    @ResponseBody
    public ResponseEntity<String> train(@PathVariable String trainCode, HttpSession sess) {

        System.out.println(trainCode);

        List<TrainCrowd> myTrainCrowd = transportSvc.getTrainCrowd(trainCode);

        // String trainStart = myTrainCrowd.get(0).getStartTime();
        // String trainEnd = myTrainCrowd.get(0).getEndTime();
        // String trainLineName = TrainCrowd.getTrainLine(trainCode);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        myTrainCrowd.stream()
                    .forEach(tc -> {
                        jab.add(tc.toJson());
                    });

        JsonObject jo = Json.createObjectBuilder()
                        .add("trainStart", myTrainCrowd.get(0).getStartTime())
                        .add("trainEnd", myTrainCrowd.get(0).getEndTime())
                        .add("trainLineName", TrainCrowd.getTrainLine(trainCode))
                        .add("trainDetails", jab.build())
                        .build();

        return ResponseEntity.ok(jo.toString());
    }
    
    @GetMapping(path="/getTrafficImages")
    @ResponseBody
    public ResponseEntity<String> trafficImages() {
        
        System.out.println("Hello");
        JsonObject payload = traffSvc.getTrafficImages();

        return ResponseEntity.ok(payload.toString());
    }

    @PostMapping(path="/uploadFile", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file, @RequestPart String title, @RequestPart String description, @RequestPart String email) {

        System.out.println(file);
        System.out.println(title);
        System.out.println(description);
        System.out.println(email);

        int count = 0;

        try {
            // key is the image key name in digitalocean
            count = traffSvc.uploadImages(file, title, description, email);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(">>>>> " + ex.getMessage());
        }

        if (count == 1) {
            fireBaseSvc.pushNotification(email);
        }

        return ResponseEntity.ok(Json.createObjectBuilder().add("fileUploaded", count).build().toString());
    }
    
    @GetMapping(path="/postIt")
    public void testPost() {
        fireBaseSvc.pushNotification("qwe@qwe");
    }

    @GetMapping(path="/getTrafficUpdates")
    @ResponseBody
    public ResponseEntity<String> getTrafficUpdates() {

        List<TrafficImages> ls = traffSvc.getUserTrafficImages();

        JsonArrayBuilder jab = Json.createArrayBuilder();

        ls.stream().forEach(t -> jab.add(t.toJson()));

        return ResponseEntity.ok(jab.build().toString());
    }

    @PostMapping(path="/postKey")
    @ResponseBody
    public ResponseEntity<String> saveKeyForFirebase(@RequestBody MultiValueMap<String, String> form) {

        String email = form.getFirst("email");
        String token = form.getFirst("token");

        System.out.println(form.getFirst("email"));
        System.out.println(form.getFirst("token"));

        fireBaseSvc.saveToken(email, token);

        return ResponseEntity.ok("");
    }

}
