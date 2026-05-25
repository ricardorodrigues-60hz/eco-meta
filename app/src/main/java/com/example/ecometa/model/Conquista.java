package com.example.ecometa.model;

import java.util.Date;

/**
 * Modelo representativo da coleção 'conquistas_usuario' no Firestore.
 * Requisito P2: Classe Java distinta.
 */
public class Conquista {
    private String id_conquista;
    private String user_id;
    private String titulo_conquista;
    private Date data_desbloqueio;
    private String icone_url; // Mapeamento para os ícones Material 3 vistos nos prints

    public Conquista() {}

    public String getId_conquista() { return id_conquista; }
    public void setId_conquista(String id_conquista) { this.id_conquista = id_conquista; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getTitulo_conquista() { return titulo_conquista; }
    public void setTitulo_conquista(String titulo_conquista) { this.titulo_conquista = titulo_conquista; }

    public Date getData_desbloqueio() { return data_desbloqueio; }
    public void setData_desbloqueio(Date data_desbloqueio) { this.data_desbloqueio = data_desbloqueio; }

    public String getIcone_url() { return icone_url; }
    public void setIcone_url(String icone_url) { this.icone_url = icone_url; }
}