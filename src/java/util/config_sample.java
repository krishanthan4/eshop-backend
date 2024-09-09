package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class config_sample {
// add the correct details and save this file as config.java

    public static final String CLIENT_URL = "http://localhost:3000";
    public static final String CLIENT_URL_SECURED = "https://localhost:3000";
    public static final String SERVER_URL = "http://localhost:8080/eshop-backend";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "password";
    public static final String DB_NAME = "database";
    public static final String DB_PORT = "3306";
    public static final String DB_HOST = "localhost";
    //create a app password from this link  -> https://myaccount.google.com/apppasswords
    public static final String APP_PASSWORD = "password";
    public static final String APP_EMAIL = "email";
    public static final String SECRET_KEY = "secret-key";

    public static List<String> getConfigList() {
        return new ArrayList<>(Arrays.asList(
                CLIENT_URL,
                CLIENT_URL_SECURED,
                SERVER_URL,
                DB_USERNAME,
                DB_PASSWORD,
                DB_NAME,
                DB_PORT,
                DB_HOST,
                APP_PASSWORD,
                APP_EMAIL,
                SECRET_KEY
        ));
    }
}
