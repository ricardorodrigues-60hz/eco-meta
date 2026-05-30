package com.example.ecometa.model;

/**
 * Camada: Model
 * Classe auxiliar para agrupar o desafio e o progresso do usuário para a UI.
 */
public class DesafioStatus {
    private Desafio desafio;
    private double km_atual;
    private boolean concluido;

    public DesafioStatus(Desafio desafio, double km_atual, boolean concluido) {
        this.desafio = desafio;
        this.km_atual = km_atual;
        this.concluido = concluido;
    }

    public Desafio getDesafio() { return desafio; }
    public double getKm_atual() { return km_atual; }
    public boolean isConcluido() { return concluido; }

    public int getPorcentagemProgresso() {
        if (desafio.getMeta_km() <= 0) return 0;
        int progresso = (int) ((km_atual / desafio.getMeta_km()) * 100);
        return Math.min(progresso, 100);
    }
}
