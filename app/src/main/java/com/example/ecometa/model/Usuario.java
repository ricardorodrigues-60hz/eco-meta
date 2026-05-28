package com.example.ecometa.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {
    private String id_user;
    private String nome;
    private String email;
    private int eco_points;
    private double total_co2_poupado;
    private String nivel;

    // Construtor Vazio
    public Usuario() {
    }

    public Usuario(String id_user, String nome, int eco_points, double total_co2_poupado) {
        this.id_user = id_user;
        this.nome = nome;
        this.eco_points = eco_points;
        this.total_co2_poupado = total_co2_poupado;
        this.nivel = "Brotinho";
        this.email = "";
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEco_points() {
        return eco_points;
    }

    public void setEco_points(int eco_points) {
        this.eco_points = eco_points;
    }

    public double getTotal_co2_poupado() {
        return total_co2_poupado;
    }

    public void setTotal_co2_poupado(double total_co2_poupado) {
        this.total_co2_poupado = total_co2_poupado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}