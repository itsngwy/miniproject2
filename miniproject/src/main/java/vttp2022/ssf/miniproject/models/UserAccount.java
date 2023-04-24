package vttp2022.ssf.miniproject.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.JsonObject;

public class UserAccount {
    
    private String firstName;
    private String lastName;
    private String email;
    private String favArea;
    
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFavArea() {
        return favArea;
    }
    public void setFavArea(String favArea) {
        this.favArea = favArea;
    }

    public static UserAccount create(SqlRowSet rs) {
        UserAccount u = new UserAccount();
        u.setEmail(rs.getString("email"));
        u.setFirstName(rs.getString("firstname"));
        u.setLastName(rs.getString("lastname"));
        u.setFavArea(rs.getString("fav_location"));
        return u;
    }
    
}
