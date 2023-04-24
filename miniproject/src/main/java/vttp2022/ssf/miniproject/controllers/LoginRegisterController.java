package vttp2022.ssf.miniproject.controllers;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.ssf.miniproject.models.UserAccount;
import vttp2022.ssf.miniproject.models.UserRegisterForm;
import vttp2022.ssf.miniproject.repositories.LoginRegisterRepository;
import vttp2022.ssf.miniproject.services.LoginRegisterService;
import vttp2022.ssf.miniproject.services.MailService;

@Controller
@RequestMapping(path="/api")
public class LoginRegisterController {

    @Autowired
    private LoginRegisterService loginRegSvc;

    @Autowired
    private MailService mailService;

    @GetMapping
    public String getLogin() {
        return "login";
    }

    @GetMapping(path="/login")
    @ResponseBody
    public ResponseEntity<String> authenticate(@RequestParam String email, @RequestParam String password, HttpSession sess, Model model) {

        System.out.println(email);
        System.out.println(password);
        
        Optional<UserAccount> ua = loginRegSvc.authenticateLogin(email, password);
        
        JsonObjectBuilder job = Json.createObjectBuilder();

        if (ua.isPresent()) {
            System.out.println("Login successful");
            job.add("email", email)
                .add("authentication", true)
                .add("firstName", ua.get().getFirstName())
                .add("lastName", ua.get().getLastName())
                .add("favLocation", ua.get().getFavArea());

        } else {
            System.out.println("Login failed, please check email and password");
            job.add("email", email)
                .add("authentication", false);
        }
        return ResponseEntity.ok(job.build().toString());
    }
    
    // @GetMapping(path="/signup")
    // public String getSignup() {
    //     return "signup";
    // }

    @GetMapping(path="/registerPage")
    public String map() {
        return "register";
    }

    @PostMapping(path="/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody MultiValueMap<String, String> form) throws IOException {
        
        String fName = form.getFirst("firstName");
        String lName = form.getFirst("lastName");
        String email = form.getFirst("email");
        String password = form.getFirst("password");
        String repeatPw = form.getFirst("repeatpw");

        System.out.printf("%s %s %s %s %s\n", fName, lName, email, password, repeatPw);

        JsonObjectBuilder job = Json.createObjectBuilder();

        if (!password.equals(repeatPw)) {
            job.add("Response", "Both passwords are not the same, please try again");
            return ResponseEntity.ok(job.build().toString());
        }

        if (loginRegSvc.checkEmailExists(email)) {
            job.add("Response", "Email is already used, please try other email!");
            return ResponseEntity.ok(job.build().toString());
        }

        UserRegisterForm newUser = new UserRegisterForm(fName, lName, email, password);
        loginRegSvc.createAcc(newUser);
        mailService.send(email, fName);
        job.add("Response", "Your account has registered successfully!");
        
        return ResponseEntity.ok(job.build().toString());
    }

    @GetMapping(path="/homepage")
    public String home() {
        return "busHome";
    }
}
