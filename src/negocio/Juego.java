package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal.
 * Actúa como el contenedor global de la partida.
 */
public class Juego {
    private Jugador jugadorActual;
    private List<Nivel> niveles;
    private Nivel nivelActivo;

    public Juego() throws Exception {
        this.niveles = new ArrayList<>();
        if (this.niveles == null) {
            throw new Exception("Error al inicializar lista de niveles");
        }
    }

    // Getters y Setters con Validaciones

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(Jugador jugadorActual) throws Exception {
        if (jugadorActual == null) {
            throw new Exception("El juego debe tener un jugador asignado.");
        }
        this.jugadorActual = jugadorActual;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void agregarNivel(Nivel nivel) {
        this.niveles.add(nivel);
    }

    public Nivel getNivelActivo() {
        return nivelActivo;
    }

    public void setNivelActivo(Nivel nivelActivo) {
        this.nivelActivo = nivelActivo;
    }
}