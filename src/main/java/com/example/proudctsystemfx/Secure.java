package com.example.proudctsystemfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Secure {
    private static final String PRODUCTS_TABLE_NAME = "productstbl_ammar";
    private static final String url = "jdbc:mysql://localhost:3306/productsdb_alharbi";
    private static final String user = "root";
    private static final String password = "123456";

    public Connection secureConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);// return connection

    }

    public String getTableName() { //get table name
        return PRODUCTS_TABLE_NAME;
    }//return table name


}
