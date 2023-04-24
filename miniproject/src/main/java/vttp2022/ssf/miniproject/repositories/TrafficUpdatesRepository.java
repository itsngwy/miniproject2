package vttp2022.ssf.miniproject.repositories;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp2022.ssf.miniproject.models.TrafficImages;

import static vttp2022.ssf.miniproject.repositories.Queries.*;

@Repository
public class TrafficUpdatesRepository {

    private Logger logger = Logger.getLogger(TrafficUpdatesRepository.class.getName());

    @Value("${spaces.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;
    
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String uploadImagesToS3(MultipartFile file, String title, String description) {

        // User data for UserMetadata
        // Can put anything we want cause it is just for metadata
        Map<String, String> userData = new HashMap<>();
        //userData.put("name", "wy");
        userData.put("uploadTime", (new Date()).toString());
        userData.put("originalFilename", file.getOriginalFilename());

        // Metadata of the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        // Set the media type. Must!! or else it doesnt know what to save it as and will result in weird output
        metadata.setContentType(file.getContentType());
        // Set User data that we created earlier
        metadata.setUserMetadata(userData);

        // For image id 
        String key = UUID.randomUUID().toString().substring(0, 8);

        try {
            // Create a put request
            // Standard format
            // file.getInputStream will cause IOException so we either throws IOException or we try catch here
            PutObjectRequest putReq = new PutObjectRequest(
                spacesBucket, // bucket name, same as the bucket name in digital ocean
                key, // key
                file.getInputStream(), //inputstream
                metadata); // metadata

            // Allow public access
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);

            // Upload the file to the S3 bucket
            s3Client.putObject(putReq);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Put S3", ex);
        }

        // Set imageUrl so that when we want to display the image from digital ocean, we can just call it
		String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, key);

        System.out.println(imageUrl);

        return imageUrl;
    }

    public Integer updateImageToMySql(String imageUrl, String title, String description, String email) {
        System.out.println(ZonedDateTime.now(ZoneId.of("GMT+08:00")));
        return jdbcTemplate.update(SQL_INSERT_TRAFFIC_IMAGES, email, imageUrl, title, description); //LocalDate.now() + " " + LocalTime.now()
    }

    public List<TrafficImages> queryAllImages() {
        SqlRowSet rs =jdbcTemplate.queryForRowSet(SQL_SELECT_USER_UPLOADED_IMAGES_WITH_TIME_LIMIT);
        
        List<TrafficImages> ls = new LinkedList<>();

        while(rs.next()) {
            ls.add(TrafficImages.create(rs));
            System.out.println(rs.getString("url"));
        }

        return ls;
    }

}
