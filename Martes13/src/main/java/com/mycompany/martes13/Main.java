package com.mycompany.martes13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // ====== CONFIGURACION (ajusta lo que las reglas no especifican) ======
    static final int LIMITE = 13;
    static final int CARTAS_POR_JUGADOR = 8;
    static final int NUM_PARTIDAS = 4;
    static final int NUM_MAX = 10;          // numeros 1..NUM_MAX por cada tipo
    static final int NUM_MARTES13 = 3;      // cartas comodin en la baraja
    static final int VALOR_MARTES13 = 13;   // valor del comodin en la suma de la fila
    // =====================================================================

    static final String ESCALERAS = "Escaleras";
    static final String ESPEJOS = "Espejos";
    static final String GATOS = "Gatos";
    static final String MARTES_13 = "Martes 13";
    static final String[] TIPOS = {ESCALERAS, ESPEJOS, GATOS};

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== MARTES 13 ===");
        int n = leerEntero("Numero de jugadores (2-4): ", 2, 4);

        ArrayList<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = sc.nextLine().trim();
            if (nombre.isEmpty()) {
                nombre = "Jugador " + i;
            }
            jugadores.add(new Jugador(nombre, NUM_PARTIDAS));
        }

        for (int partida = 0; partida < NUM_PARTIDAS; partida++) {
            System.out.println("\n########## PARTIDA " + (partida + 1) + " de " + NUM_PARTIDAS + " ##########");
            jugarPartida(jugadores, partida);
            mostrarMarcador(jugadores, partida);
        }

        mostrarGanador(jugadores);
    }

    // ---------- Baraja ----------
    static ArrayList<Carta> crearBaraja() {
        ArrayList<Carta> baraja = new ArrayList<>();
        for (String tipo : TIPOS) {
            for (int num = 1; num <= NUM_MAX; num++) {
                baraja.add(new Carta(num, tipo, num));   // valor = numero de la carta
            }
        }
        for (int i = 0; i < NUM_MARTES13; i++) {
            baraja.add(new Carta(13, MARTES_13, VALOR_MARTES13));
        }
        return baraja;
    }

    // ---------- Una partida ----------
    static void jugarPartida(ArrayList<Jugador> jugadores, int partida) {
        // Preparacion: barajar y repartir 8 cartas a cada uno
        ArrayList<Carta> baraja = crearBaraja();
        Collections.shuffle(baraja);
        for (Jugador j : jugadores) {
            j.getMano().clear();
            j.getMonton().clear();
            for (int i = 0; i < CARTAS_POR_JUGADOR; i++) {
                j.getMano().add(baraja.remove(0));
            }
        }
        // Las cartas que sobran en 'baraja' no se usan

        // 3 filas: 0=Escaleras, 1=Espejos, 2=Gatos
        ArrayList<ArrayList<Carta>> filas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            filas.add(new ArrayList<>());
        }

        for (int ronda = 0; ronda < CARTAS_POR_JUGADOR; ronda++) {
            for (Jugador j : jugadores) {
                turno(j, filas);
            }
        }

        // Recuento
        System.out.println("\n--- Recuento de la partida ---");
        aplicarMayoria(jugadores);
        for (Jugador j : jugadores) {
            j.setPuntosPartida(partida, j.calcularPuntos());
        }
    }

    // ---------- Un turno ----------
    static void turno(Jugador j, ArrayList<ArrayList<Carta>> filas) {
        for (int i = 0; i < 25; i++) {
            System.out.println();   // "limpia" la pantalla para no ver la mano del anterior
        }
        System.out.println(">>> Turno de " + j.getNombre() + " (pulsa Enter cuando estes listo/a)");
        sc.nextLine();

        mostrarFilas(filas);
        System.out.println("Tu monton: " + j.getMonton().size() + " cartas");
        System.out.println("Tu mano:");
        for (int i = 0; i < j.getMano().size(); i++) {
            System.out.println("  " + (i + 1) + ") " + j.getMano().get(i));
        }

        int idx = leerEntero("Elige carta: ", 1, j.getMano().size()) - 1;
        Carta carta = j.getMano().remove(idx);

        int fila;
        if (carta.getTipo().equals(MARTES_13)) {
            fila = leerEntero("Martes 13 es comodin. Fila (1=Escaleras, 2=Espejos, 3=Gatos): ", 1, 3) - 1;
        } else {
            fila = indiceFila(carta.getTipo());
        }

        ArrayList<Carta> f = filas.get(fila);
        f.add(carta);

        int suma = sumaFila(f);
        System.out.println("La fila de " + TIPOS[fila] + " suma " + suma);

        // Solo se recoge si la suma es MAYOR que 13 (13 exacto no recoge)
        if (suma > LIMITE) {
            int recogidas = f.size() - 1;
            for (int i = 0; i < recogidas; i++) {
                j.getMonton().add(f.get(i));
            }
            Carta ultima = f.get(f.size() - 1);
            f.clear();
            f.add(ultima);   // la carta recien jugada se queda en la fila
            System.out.println("¡Supera " + LIMITE + "! " + j.getNombre() + " recoge " + recogidas + " cartas.");
        }
        System.out.println("(Pulsa Enter para continuar)");
        sc.nextLine();
    }

    static int indiceFila(String tipo) {
        for (int i = 0; i < TIPOS.length; i++) {
            if (TIPOS[i].equals(tipo)) {
                return i;
            }
        }
        return -1;
    }

    static int sumaFila(ArrayList<Carta> fila) {
        int suma = 0;
        for (Carta c : fila) {
            suma += c.getValor();
        }
        return suma;
    }

    // ---------- Descarte por mayoria ----------
    static void aplicarMayoria(ArrayList<Jugador> jugadores) {
        for (String tipo : TIPOS) {
            int max = 0;
            for (Jugador j : jugadores) {
                max = Math.max(max, j.contarTipo(tipo));
            }
            if (max == 0) {
                continue;
            }

            ArrayList<Jugador> conMax = new ArrayList<>();
            for (Jugador j : jugadores) {
                if (j.contarTipo(tipo) == max) {
                    conMax.add(j);
                }
            }

            if (conMax.size() == 1) {
                Jugador j = conMax.get(0);
                j.descartarTipo(tipo);
                System.out.println(j.getNombre() + " tiene la mayoria de " + tipo + " (" + max + ") y las descarta.");
            } else {
                System.out.println(tipo + ": empate, nadie descarta.");
            }
        }
    }

    // ---------- Pantallas ----------
    static void mostrarFilas(ArrayList<ArrayList<Carta>> filas) {
        System.out.println("--- Filas ---");
        for (int i = 0; i < 3; i++) {
            System.out.println(TIPOS[i] + " (suma " + sumaFila(filas.get(i)) + "): " + filas.get(i));
        }
    }

    static void mostrarMarcador(ArrayList<Jugador> jugadores, int partida) {
        System.out.println("\n--- Marcador tras la partida " + (partida + 1) + " ---");
        for (Jugador j : jugadores) {
            System.out.println(j.getNombre() + ": esta partida " + j.getPuntosPartida(partida)
                    + " | total " + j.getTotal());
        }
    }

    static void mostrarGanador(ArrayList<Jugador> jugadores) {
        int min = Integer.MAX_VALUE;
        for (Jugador j : jugadores) {
            min = Math.min(min, j.getTotal());
        }
        System.out.println("\n=== FIN DEL JUEGO ===");
        for (Jugador j : jugadores) {
            if (j.getTotal() == min) {
                System.out.println("Ganador/a: " + j.getNombre() + " con " + min + " puntos");
            }
        }
    }

    // ---------- Entrada segura ----------
    static int leerEntero(String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) {
                    return v;
                }
            } catch (NumberFormatException e) {
                // se repite la pregunta
            }
            System.out.println("Introduce un numero entre " + min + " y " + max + ".");
        }
    }
}
