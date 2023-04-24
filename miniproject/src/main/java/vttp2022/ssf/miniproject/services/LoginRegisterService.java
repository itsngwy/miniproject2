package vttp2022.ssf.miniproject.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.miniproject.models.UserAccount;
import vttp2022.ssf.miniproject.models.UserRegisterForm;
import vttp2022.ssf.miniproject.repositories.AccountRepository;
import vttp2022.ssf.miniproject.repositories.LoginRegisterRepository;

@Service
public class LoginRegisterService {
    
    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private LoginRegisterRepository logRegRepo;

    private JsonObject accResult;

    public boolean checkEmailExists(String email) {
        return logRegRepo.emailExists(email);
    }

    public void createAcc(UserRegisterForm userDetails) {
        logRegRepo.register(userDetails);
    }

    public Optional<UserAccount> authenticateLogin(String email, String pw) {

        Optional<UserAccount> ua = logRegRepo.login(email, pw);
        return ua;
        // String payload = opt.get();
        // System.out.println(payload);

        // accResult = stringToJson(payload);
        // String pwInRepo = accResult.getString("password");

        // // Compare password
        // if (pw.equals(pwInRepo)) {
        //     return true;
        // } else {
        //     return false;
        // }
    }

    // public UserAccount getUserInfoAfterSuccessfulLogin(String email) {
    //     return UserAccount.create(accResult);
    // }

    public JsonObject stringToJson(String payload) {
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from Reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read the whole payload as Json object
        JsonObject accResult = jsonReader.readObject();

        return accResult;
    }

}
