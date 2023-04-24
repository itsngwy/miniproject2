package vttp2022.ssf.miniproject.models;

import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class UserRegisterForm {
    
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRegisterForm() {}

    public UserRegisterForm(String fName, String lName, String email, String pw) {
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.password = pw;
    }

    public String getId() {
        return id;
    }
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public JsonObject toJson() {    
        return Json.createObjectBuilder()
            .add("id", id)
            .add("firstName", firstName)
            .add("lastName", lastName)
            .add("email", email)
            .add("password", password)
            .build();
    }
}
