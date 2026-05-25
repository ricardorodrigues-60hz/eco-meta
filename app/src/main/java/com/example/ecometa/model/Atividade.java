package com.example.ecometa.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Classe de modelo que representa uma Atividade de deslocamento no EcoMeta.
 */
@IgnoreExtraProperties
public class Atividade {
    private String id_atividade;
    private String user_id;
    private String tipo_transporte;
    private double distancia_km;
    private double co2_evitado;
    private Timestamp data;

    /**
     * Construtor padrão necessário para o Firebase.
     */
    public Atividade() {
    }

    /**
     * Construtor completo para registro de atividades.
     *
     * @param id_atividade    ID único da atividade.
     * @param user_id         ID do usuário que realizou a atividade.
     * @param tipo_transporte Tipo de transporte (Bicicleta, Caminhada, Ônibus, Metrô).
     * @param distancia_km    Distância percorrida em quilômetros.
     * @param co2_evitado     Quantidade de CO2 evitada (calculada).
     * @param data            Data e hora do registro.
     */
    public Atividade(String id_atividade, String user_id, String tipo_transporte, double distancia_km, double co2_evitado, Timestamp data) {
        this.id_atividade = id_atividade;
        this.user_id = user_id;
        this.tipo_transporte = tipo_transporte;
        this.distancia_km = distancia_km;
        this.co2_evitado = co2_evitado;
        this.data = data;
    }

    // Getters e Setters

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

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }
}
