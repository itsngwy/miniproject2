package vttp2022.ssf.miniproject.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.miniproject.models.TrafficImages;
import vttp2022.ssf.miniproject.repositories.ImageException;
import vttp2022.ssf.miniproject.repositories.TrafficUpdatesRepository;

@Service
public class TrafficService {
    
    private static final String TRAFFICIMG = "http://datamall2.mytransport.sg/ltaodataservice/Traffic-Imagesv2";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private TrafficUpdatesRepository traffRepo;
    
    public JsonObject getTrafficImages() {
        String payload;

        String url = UriComponentsBuilder.fromUriString(TRAFFICIMG)
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

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject trafficResults = jsonReader.readObject();
        //System.out.println("payload: " + payload);
        return trafficResults;
    }

    @Transactional(rollbackFor = ImageException.class)
    public Integer uploadImages(MultipartFile file, String title, String description, String email) throws ImageException {

        String imageUrl = traffRepo.uploadImagesToS3(file, title, description);
        Integer imageInsertCount = 0;

        imageInsertCount = traffRepo.updateImageToMySql(imageUrl, title, description, email);
        System.out.println("Rows affected while updating traffic image repo: " + imageInsertCount);

        if (imageInsertCount == 0) {
            throw new ImageException("Nothing is added to MySQL and hence image will not be added into Digital Ocean");
        }
        
        return imageInsertCount;
    }

    public List<TrafficImages> getUserTrafficImages() {
        return traffRepo.queryAllImages();
    }

}
