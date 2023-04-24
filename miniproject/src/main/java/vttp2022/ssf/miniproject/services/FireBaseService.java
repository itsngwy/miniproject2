package vttp2022.ssf.miniproject.services;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.ssf.miniproject.repositories.FireBaseRepository;

@Service
public class FireBaseService {
    
    @Autowired
    private FireBaseRepository fireBaseRepo;

    public void saveToken(String email, String token) {
        fireBaseRepo.saveKey(email, token);
    }

    public String pushNotification(String email) {
        System.out.println("All Redis Keys");
        Set<String> redisKeys = fireBaseRepo.getAllKeys();

        String payload = "";

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        for (String key: redisKeys) {

            System.out.println(key);

            if (key.equals(email)) {
                continue;
            }

            String token = fireBaseRepo.getValue(key);
            
            JsonObject job = Json.createObjectBuilder()
            .add("notification", Json.createObjectBuilder().add("title", "New Traffic Updates").add("body", "Refresh page to view"))
            .add("to", "%s".formatted(token))
            .build();

            System.out.println(job.toString());

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "key=AAAAdFFY6M8:APA91bHBVHbN4I4OD09Yp-ieydxIlFz1WsvKeCVCHTrK2OLYvbCqbQWNCqsXmB5NjoSSsBX39Y4wHgMlIbgQVzNKhED8zM9RsM9DND94lzNp16bt-KfZd9Id1aCCzywlICo_7hrdQZY3");
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                HttpEntity request = new HttpEntity(job.toString(), headers);
    
                // Throws an exception if status code not in between 200 - 399
                // Get response from the client with our request
                resp = template.postForEntity("https://fcm.googleapis.com/fcm/send", request, String.class);
    
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
            }
            
        }

        //payload = resp.getBody();

        //System.out.println("payload: " + payload);
        return payload;
    }
}
