package com.example.eventito.model;

public class Conquista {
    private String nome;
    private String descricao;
    private int pontosAtuais;
    private int pontosTotais;

    // Construtor
    public Conquista(String nome, String descricao, int pontosAtuais, int pontosTotais) {
        this.nome = nome;
        this.descricao = descricao;
        this.pontosAtuais = pontosAtuais;
        this.pontosTotais = pontosTotais;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPontosAtuais() {
        return pontosAtuais;
    }

    public int getPontosTotais() {
        return pontosTotais;
    }
}

