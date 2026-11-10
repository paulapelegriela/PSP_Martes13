package com.mycompany.martes13;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano = new ArrayList<>();
    private ArrayList<Carta> monton = new ArrayList<>();   // cartas recogidas
    private int[] puntosPartida;

    public Jugador(String nombre, int numPartidas) {
        this.nombre = nombre;
        this.puntosPartida = new int[numPartidas];
    }

    public String getNombre() {
        return nombre;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }
    public ArrayList<Carta> getMonton() {
        return monton;
    }

    public void setPuntosPartida(int partida, int puntos) {
        puntosPartida[partida] = puntos;
    }
    public int getPuntosPartida(int partida) {
        return puntosPartida[partida];
    }

    public int getTotal() {
        int total = 0;
        for (int p : puntosPartida) {
            total += p;
        }
        return total;
    }

    // Cuantas cartas de un tipo hay en el monton
    public int contarTipo(String tipo) {
        int n = 0;
        for (Carta c : monton) {
            if (c.getTipo().equals(tipo)) {
                n++;
            }
        }
        return n;
    }

    // Descarta del monton todas las cartas de un tipo
    public void descartarTipo(String tipo) {
        monton.removeIf(c -> c.getTipo().equals(tipo));
    }

    // Puntos de las cartas que conserva en el monton
    public int calcularPuntos() {
        int pts = 0;
        for (Carta c : monton) {
            pts += c.getPuntos();
        }
        return pts;
    }
}
