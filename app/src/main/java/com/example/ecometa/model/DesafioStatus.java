package com.example.ecometa.model;

public class DesafioStatus {
    private Desafio desafio;
    private boolean conquistado;

    public DesafioStatus(Desafio desafio, boolean conquistado) {
        this.desafio = desafio;
        this.conquistado = conquistado;
    }

    public Desafio getDesafio() { return desafio; }
    public boolean isConquistado() { return conquistado; }
}