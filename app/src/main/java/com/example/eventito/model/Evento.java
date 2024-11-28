package com.example.eventito.model;

public class Evento {
        private String foto; // ID do recurso da imagem
        private String nome;
        private String descricao;

        public Evento(String  foto, String nome, String descricao) {
            this.foto = foto;
            this.nome = nome;
            this.descricao = descricao;
        }

        public String getFoto() {
            return foto;
        }

        public String getNome() {
            return nome;
        }

        public String getDescricao() {
            return descricao;
        }
}
