package com.mycompany.martes13;

public class Carta {
    // Atributos
    int num;
    String tipo;
    int valor;

    // Constructor
    public Carta(int num, String tipo, int valor) {
        this.num = num;
        this.tipo = tipo;
        this.valor = valor;
    }

    // Getters
    public int getNum() {
        return num;
    }
    public String getTipo() {
        return tipo;
    }
    public int getValor() {
        return valor;
    }

    // Setters
    public void setNum(int num) {
        this.num = num;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }

    // Puntos al final de la partida: Martes 13 vale 2, el resto 1
    public int getPuntos() {
        return tipo.equals(Main.MARTES_13) ? 2 : 1;
    }

    @Override
    public String toString() {
        if (tipo.equals(Main.MARTES_13)) {
            return "Martes 13";
        }
        return tipo + " " + num;
    }
}
