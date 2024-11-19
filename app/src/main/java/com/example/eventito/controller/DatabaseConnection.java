package com.example.eventito.controller;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://eventitobd.cviuae62yy1f.us-east-1.rds.amazonaws.com:3306/eventitobd?useSSL=false";
    private static final String USER = "admin";
    private static final String PASSWORD = "eventito*2024";

    public static class ConnectTask extends AsyncTask<Void, Void, Connection> {

        @Override
        protected Connection doInBackground(Void... voids) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }

        @Override
        protected void onPostExecute(Connection connection) {
            if (connection != null) {
                System.out.println("Conexão com MySQL bem sucedida!");
            } else {
                System.err.println("Erro ao conectar com o MySQL");
            }
        }
    }
}
