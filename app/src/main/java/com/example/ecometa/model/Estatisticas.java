package com.example.ecometa.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Camada: Model
 * Armazena os totais acumulados de distância por tipo de transporte.
 */
@IgnoreExtraProperties
public class Estatisticas {
    private String user_id;
    private double total_caminhada_km;
    private double total_bicicleta_km;
    private double total_onibus_km;
    private double total_metro_km;

    public Estatisticas() {}

    public Estatisticas(String user_id) {
        this.user_id = user_id;
        this.total_caminhada_km = 0;
        this.total_bicicleta_km = 0;
        this.total_onibus_km = 0;
        this.total_metro_km = 0;
    }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public double getTotal_caminhada_km() { return total_caminhada_km; }
    public void setTotal_caminhada_km(double total_caminhada_km) { this.total_caminhada_km = total_caminhada_km; }

    public double getTotal_bicicleta_km() { return total_bicicleta_km; }
    public void setTotal_bicicleta_km(double total_bicicleta_km) { this.total_bicicleta_km = total_bicicleta_km; }

    public double getTotal_onibus_km() { return total_onibus_km; }
    public void setTotal_onibus_km(double total_onibus_km) { this.total_onibus_km = total_onibus_km; }

    public double getTotal_metro_km() { return total_metro_km; }
    public void setTotal_metro_km(double total_metro_km) { this.total_metro_km = total_metro_km; }
}
