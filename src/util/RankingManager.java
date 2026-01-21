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

    //registra un jugador en el ranking
    public void registrarJugador(Jugador j) throws IllegalArgumentException {
        if (j == null) {
            throw new IllegalArgumentException("No se puede registrar un jugador nulo");
        }
        jugadores.put(j.getIdJugador(), j);
        mejoresPuntajes.putIfAbsent(j.getIdJugador(), 0);
    }

    //actualiza el ranking con un nuevo intento
    public void actualizarConIntento(Jugador j, Intento intento) throws IllegalArgumentException {
        if (j == null) {
            throw new IllegalArgumentException("Jugador no puede ser nulo para actualizar ranking");
        }
        if (intento == null) {
            throw new IllegalArgumentException("Intento no puede ser nulo para actualizar ranking");
        }
        if (intento.getPuntajeObtenido() == null) {
            throw new IllegalStateException("El intento no tiene puntaje calculado");
        }
        int valor = intento.getPuntajeObtenido().getValorNumerico();
        int id = j.getIdJugador();
        mejoresPuntajes.putIfAbsent(id, 0);
        if (valor > mejoresPuntajes.get(id)) {
            mejoresPuntajes.put(id, valor);
        }
    }

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

    public int obtenerMejorPuntaje(int idJugador) {
        return this.mejoresPuntajes.getOrDefault(idJugador, 0);
    }
}