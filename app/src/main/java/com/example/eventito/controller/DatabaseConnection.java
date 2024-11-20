package com.example.eventito.controller;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://mysql.freehostia.com/sabdoe_eventitobd";
    private static final String USER = "sabdoe";
    private static final String PASSWORD = "eventito*2024";

    public static void connect() {
        new ConnectTask().execute();
    }

    private static class ConnectTask extends AsyncTask<Void, Void, Connection> {

        @Override
        protected Connection doInBackground(Void... voids) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Log.d("DatabaseConnection", "Conexão com MySQL bem-sucedida!");

            } catch (SQLException e) {
                Log.e("DatabaseConnection", "Erro ao conectar com o banco de dados", e);
            }
            return connection;
        }

        @Override
        protected void onPostExecute(Connection connection) {
            if (connection != null) {
                Log.d("DatabaseConnection", "Conexão com o banco de dados estabelecida!");
            } else {
                Log.d("DatabaseConnection", "Falha na conexão com o banco de dados");
            }
        }
    }
}
