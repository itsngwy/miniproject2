package vttp2022.ssf.miniproject.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static vttp2022.ssf.miniproject.repositories.Queries.*;

@Repository
public class AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Integer updateFavWeather(String email, String favArea) {
        return jdbcTemplate.update(SQL_UPDATE_FAV_AREA, favArea, email);
    }

    public String getFavWeather(String email) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_FAV_BY_EMAIL, email);

        rs.next();
        System.out.println(rs.getString("fav_location"));

        return rs.getString("fav_location");
    }

    public List<Document> getBusCodeUsingName(String busName) {
        Criteria criteria = Criteria.where("Description").is(busName);

        Query query = Query.query(criteria);

        return mongoTemplate.find(query, Document.class, "busstopnames");
    }

    public List<Document> getBusStopNameUsingCode(String busStopCode) {
        Criteria criteria = Criteria.where("BusStopCode").is(busStopCode);

        Query query = Query.query(criteria);

        return mongoTemplate.find(query, Document.class, "busstopnames");
    }

}
