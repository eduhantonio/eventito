package com.example.eventito;

import com.example.eventito.model.Usuario;

public class UsuarioManager {
    private  static Usuario usuarioAtual;

    public static Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public static void setUsuarioAtual(Usuario usuario) {
        usuarioAtual = usuario;
    }
}
