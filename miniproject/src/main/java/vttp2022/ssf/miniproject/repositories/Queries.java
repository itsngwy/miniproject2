package vttp2022.ssf.miniproject.repositories;

public class Queries {
    public static String SQL_INSERT_USER = "insert into user(email, firstname, lastname, password, fav_location) value(?, ?, ?, sha1(?), ?)";
    public static String SQL_FIND_USER_BY_EMAIL = "select * from user where email=?";

    public static String SQL_UPDATE_FAV_AREA = "update user set fav_location = ? where email=?";
    public static String SQL_FIND_FAV_BY_EMAIL = "select * from user where email=?";
    
    public static String SQL_INSERT_TRAFFIC_IMAGES = "insert into traffic_images(email, url, title, description, date) values (?, ?, ?, ?, NOW())";
    public static String SQL_SELECT_USER_UPLOADED_IMAGES_WITH_TIME_LIMIT = "select * from user user join traffic_images traff on user.email = traff.email where date > date_sub(now(), interval 2 hour) order by date desc";

    public static String SQL_FIND_USER_ACCOUNT = "select * from user where email=? and password=sha1(?)";
}
