package com.example.ecometa.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ConquistaUsuario {
    private String id_conquista;
    private String user_id;
    private String id_desafio;
    private Timestamp data_conquista;

    public ConquistaUsuario() {}

    public ConquistaUsuario(String id_conquista, String user_id, String id_desafio, Timestamp data_conquista) {
        this.id_conquista = id_conquista;
        this.user_id = user_id;
        this.id_desafio = id_desafio;
        this.data_conquista = data_conquista;
    }

    // Getters e Setters
    public String getId_conquista() {
        return id_conquista;
    }
    public void setId_conquista(String id_conquista) {
        this.id_conquista = id_conquista;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getId_desafio() {
        return id_desafio;
    }
    public void setId_desafio(String id_desafio) {
        this.id_desafio = id_desafio;
    }

    public Timestamp getData_conquista() {
        return data_conquista;
    }
    public void setData_conquista(Timestamp data_conquista) {
        this.data_conquista = data_conquista;
    }
}