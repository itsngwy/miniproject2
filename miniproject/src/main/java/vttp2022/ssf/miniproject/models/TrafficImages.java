package vttp2022.ssf.miniproject.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class TrafficImages {
    private String email;
    private String firstName;
    private String url;
    private String title;
    private String description;
    private String date;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String decription) {
        this.description = decription;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public static TrafficImages create(SqlRowSet rs) {
        String date = rs.getString("date");
        date = date.substring(0, 10) + " " + date.substring(11);
        TrafficImages ti = new TrafficImages();
        ti.setEmail(rs.getString("email"));
        ti.setFirstName(rs.getString("firstname"));
        ti.setUrl(rs.getString("url"));
        ti.setTitle(rs.getString("title"));
        ti.setDescription(rs.getString("description"));
        ti.setDate(date);
        return ti;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                    .add("email", email)
                    .add("firstName", firstName)
                    .add("url", url)
                    .add("title", title)
                    .add("description", description)
                    .add("date", date)
                    .build();
    }

}
