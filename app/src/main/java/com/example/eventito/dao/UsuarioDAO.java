package com.example.eventito.dao;

import com.example.eventito.controller.DatabaseConnection;
import com.example.eventito.model.Usuario;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public List<Usuario> usuarios = new ArrayList<>();

    String query = "SELECT * FROM Usuarios";

}
