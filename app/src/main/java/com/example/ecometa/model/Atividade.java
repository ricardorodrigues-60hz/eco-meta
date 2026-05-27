package com.example.ecometa.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Camada: Model
 * Representa uma atividade de transporte sustentável no Firestore.
 */
@IgnoreExtraProperties
public class Atividade {
    private String id_atividade;
    private String user_id;
    private String tipo_transporte;
    private double distancia_km;
    private double co2_evitado;
    private int points_earned; // 💡
    private Timestamp data;


    public Atividade() {}


     // Construtor completo para inicialização local no app
    public Atividade(String id_atividade, String user_id, String tipo_transporte, double distancia_km, double co2_evitado, int points_earned, Timestamp data) {
        this.id_atividade = id_atividade;
        this.user_id = user_id;
        this.tipo_transporte = tipo_transporte;
        this.distancia_km = distancia_km;
        this.co2_evitado = co2_evitado;
        this.points_earned = points_earned;
        this.data = data;
    }

    public String getId_atividade() {
        return id_atividade;
    }
    public void setId_atividade(String id_atividade) {
        this.id_atividade = id_atividade;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTipo_transporte() {
        return tipo_transporte;
    }
    public void setTipo_transporte(String tipo_transporte) {
        this.tipo_transporte = tipo_transporte;
    }

    public double getDistancia_km() {
        return distancia_km;
    }
    public void setDistancia_km(double distancia_km) {
        this.distancia_km = distancia_km;
    }

    public double getCo2_evitado() {
        return co2_evitado;
    }
    public void setCo2_evitado(double co2_evitado) {
        this.co2_evitado = co2_evitado;
    }

    public int getPoints_earned() {
        return points_earned;
    }
    public void setPoints_earned(int points_earned) {
        this.points_earned = points_earned;
    }

    public Timestamp getData() {
        return data;
    }
    public void setData(Timestamp data) {
        this.data = data;
    }
}