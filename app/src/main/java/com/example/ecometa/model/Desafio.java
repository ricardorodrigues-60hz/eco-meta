package com.example.ecometa.model;

import java.util.Date;

/**
 * Modelo representativo da coleção 'desafios' no Firestore.
 * Requisito P2: Classe Java distinta.
 */
public class Desafio {
    private String id_desafio;
    private String titulo;
    private String descricao;
    private int pontos_recompensa;
    private Date data_limite;
    private double progresso_atual; // Para as barras de progresso na UI
    private double meta_objetivo;

    public Desafio() {}

    public String getId_desafio() { return id_desafio; }
    public void setId_desafio(String id_desafio) { this.id_desafio = id_desafio; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getPontos_recompensa() { return pontos_recompensa; }
    public void setPontos_recompensa(int pontos_recompensa) { this.pontos_recompensa = pontos_recompensa; }

    public Date getData_limite() { return data_limite; }
    public void setData_limite(Date data_limite) { this.data_limite = data_limite; }

    public double getProgresso_atual() { return progresso_atual; }
    public void setProgresso_atual(double progresso_atual) { this.progresso_atual = progresso_atual; }

    public double getMeta_objetivo() { return meta_objetivo; }
    public void setMeta_objetivo(double meta_objetivo) { this.meta_objetivo = meta_objetivo; }
}