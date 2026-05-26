package com.example.ecometa.model;

/**
 * Modelo de dados para o Usuário.
 * Representa a estrutura do documento na coleção 'usuarios' do Firestore.
 */
public class Usuario {
    private String id_user;
    private String nome;
    private String email;
    private int eco_points;
    private double total_co2_poupado;
    private String nivel;

    // Construtor vazio necessário para o Firebase Firestore
    public Usuario() {
    }

    public Usuario(String id_user, String nome, String email) {
        this.id_user = id_user;
        this.nome = nome;
        this.email = email;
        this.eco_points = 0;
        this.total_co2_poupado = 0.0;
        this.nivel = "Brotinho"; // Nível inicial conforme design
    }

    // Getters e Setters
    public String getId_user() { return id_user; }
    public void setId_user(String id_user) { this.id_user = id_user; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getEco_points() { return eco_points; }
    public void setEco_points(int eco_points) { this.eco_points = eco_points; }

    public double getTotal_co2_poupado() { return total_co2_poupado; }
    public void setTotal_co2_poupado(double total_co2_poupado) { this.total_co2_poupado = total_co2_poupado; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}
