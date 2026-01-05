package util;

import negocio.Intento;
import negocio.Juego;
import negocio.Jugador;
import negocio.Nivel;
import negocio.Puntaje;

public class GameManager {

    private Juego juego;
    private ScoreCalculator scoreCalculator;
    private RankingManager rankingManager;

    public GameManager(Juego juego, RankingManager rankingManager) {
        this.juego = juego;
        this.rankingManager = rankingManager;
    }

    public void iniciarPartida(Jugador jugador) throws Exception {
        if (jugador == null) throw new Exception("Jugador nulo");
        juego.setJugadorActual(jugador);
        rankingManager.registrarJugador(jugador);
    }

    public Puntaje finalizarIntento(Nivel nivel, Intento intento) throws Exception {
        if (nivel == null || intento == null) throw new Exception("Nivel e Intento requeridos");

        // Calcular puntaje
        Puntaje puntaje = ScoreCalculator.calcularPuntaje(intento, nivel);
        intento.setPuntajeObtenido(puntaje);

        // Registrar intento para el jugador actual
        Jugador j = juego.getJugadorActual();
        if (j != null) j.registrarIntento(intento);

        // Actualizar ranking
        rankingManager.actualizarConIntento(j, intento);

        return puntaje;
    }
}
