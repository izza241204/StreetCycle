package com.example.streetcycle.Connection;

import java.sql.*;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public DBConnect() {
        try {
            String url = "jdbc:sqlserver://localhost;databaseName=Bengkel;user=sa;password=polman";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Error saat connect database: " + e);
        }
    }

    public static void main(String[] args) {
        DBConnect connection = new DBConnect();
        System.out.println("Connection berhasil");
    }
}


