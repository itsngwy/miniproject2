package vttp2022.ssf.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vttp2022.ssf.miniproject.models.UserAccount;
import vttp2022.ssf.miniproject.models.UserRegisterForm;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import static vttp2022.ssf.miniproject.repositories.Queries.*;

import java.util.Optional;

@Repository
public class LoginRegisterRepository {
    
    @Autowired
    private JdbcTemplate template;

    public Boolean emailExists(String email) {

        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_EMAIL, email);

        if (rs.next()) {
            System.out.println("Email already exists");
            System.out.println(rs);
            return true;
        }

        return false;
    }

    public void register(UserRegisterForm userDetails) {
        Integer count = template.update(SQL_INSERT_USER, userDetails.getEmail(), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getPassword(), "-");

        System.out.println("Number of rows affected, " + count);
    }

    public Optional<UserAccount> login(String email, String password) {
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_ACCOUNT, email, password);
        if (!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(UserAccount.create(rs));
    }
}
