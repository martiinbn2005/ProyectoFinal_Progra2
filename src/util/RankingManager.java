package util;

import negocio.Jugador;
import negocio.Intento;

import java.util.*;

public class RankingManager {

    private Map<Integer, Integer> mejoresPuntajes;
    private Map<Integer, Jugador> jugadores;

    public RankingManager() {
        this.mejoresPuntajes = new HashMap<>();
        this.jugadores = new HashMap<>();
    }

    public void registrarJugador(Jugador j) {
        if (j == null) return;
        jugadores.put(j.getIdJugador(), j);
        mejoresPuntajes.putIfAbsent(j.getIdJugador(), 0);
    }

    public void actualizarConIntento(Jugador j, Intento intento) {
        if (j == null || intento == null || intento.getPuntajeObtenido() == null) return;
        int valor = intento.getPuntajeObtenido().getValorNumerico();
        int id = j.getIdJugador();
        mejoresPuntajes.putIfAbsent(id, 0);
        if (valor > mejoresPuntajes.get(id)) {
            mejoresPuntajes.put(id, valor);
        }
    }

    // jugadores ordenados por puntaje descendente.
    public List<Jugador> obtenerTopN(int n) {
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(mejoresPuntajes.entrySet());
        list.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<Jugador> out = new ArrayList<>();
        for (int i = 0; i < Math.min(n, list.size()); i++) {
            Jugador j = jugadores.get(list.get(i).getKey());
            if (j != null) out.add(j);
        }
        return out;
    }

    /**
     * Devuelve el mejor puntaje conocido para un jugador (0 si no existe).
     */
    public int obtenerMejorPuntaje(int idJugador) {
        return this.mejoresPuntajes.getOrDefault(idJugador, 0);
    }
}
