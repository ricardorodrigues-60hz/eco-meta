package com.example.ecometa.model;

public class Desafio {
    private String id_desafio;
    private String titulo;
    private String descricao;
    private String tipo_transporte;
    private double meta_km;
    private int eco_points_recompensa;

    public Desafio() {} // Obrigatório para o Firestore

    public String getId_desafio() {
        return id_desafio;
    }

    public void setId_desafio(String id_desafio) {
        this.id_desafio = id_desafio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo_transporte() {
        return tipo_transporte;
    }

    public void setTipo_transporte(String tipo_transporte) {
        this.tipo_transporte = tipo_transporte;
    }

    public double getMeta_km() {
        return meta_km;
    }

    public void setMeta_km(double meta_km) {
        this.meta_km = meta_km;
    }

    public int getEco_points_recompensa() {
        return eco_points_recompensa;
    }

    public void setEco_points_recompensa(int eco_points_recompensa) {
        this.eco_points_recompensa = eco_points_recompensa;
    }
}