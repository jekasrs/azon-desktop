package seller.group.azon.backend;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DBconnection {
    String url;
    String username;
    String password;

    private DBconnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Success: connection to DB");
        return con;
    }

    public static DBconnection getInstance() throws IOException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("/Users/evgenijsmirnov/Desktop/base/app/src/main/resources/database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return new DBconnection(url, username, password);
    }
}