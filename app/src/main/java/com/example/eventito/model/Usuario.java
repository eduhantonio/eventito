package com.example.eventito.model;

public class Usuario {
    private int idUsuario;
    private String tipoUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String qrCodeUsuario;
    private String conquistasUsuario;

    public Usuario(int idUsuario, String tipoUsuario, String nomeUsuario, String emailUsuario, String qrCodeUsuario, String conquistasUsuario) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.qrCodeUsuario = qrCodeUsuario;
        this.conquistasUsuario = conquistasUsuario;
    }

    public Usuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getQrCodeUsuario() {
        return qrCodeUsuario;
    }

    public void setQrCodeUsuario(String qrCodeUsuario) {
        this.qrCodeUsuario = qrCodeUsuario;
    }

    public String getConquistasUsuario() {
        return conquistasUsuario;
    }

    public void setConquistasUsuario(String conquistasUsuario) {
        this.conquistasUsuario = conquistasUsuario;
    }
}
