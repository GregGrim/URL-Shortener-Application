package com.tpo.tpo_10.Services;

import java.sql.*;

public class H2Service {
    public void createDB() {
        String url = "jdbc:h2:./linkDB";
        try (Connection connection = DriverManager.getConnection(url)) {

            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet tables = dbMetaData.getTables(null, null, "LINK", null);

            if (!tables.next()) {
                try (Statement statement = connection.createStatement()) {
                    String sqlCreate = "CREATE TABLE IF NOT EXISTS Link (" +
                            "id VARCHAR(255) PRIMARY KEY," +
                            "name VARCHAR(255)," +
                            "password VARCHAR(255)," +
                            "targetUrl VARCHAR(255)," +
                            "redirectUrl VARCHAR(255)," +
                            "visits INT)";
                    statement.execute(sqlCreate);
                }
            } else System.out.println("Database already initialized.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
