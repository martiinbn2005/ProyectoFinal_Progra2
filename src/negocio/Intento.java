package negocio;

import java.time.LocalDateTime;

/**
 * Representa una ejecución de un nivel por parte de un jugador.
 */
public class Intento {
    private LocalDateTime fechaHora;
    private boolean esExitoso;
    private double tiempoSegundos;
    private int cantidadRielesUsados;
    private Puntaje puntajeObtenido; // Relación 1 a 1 con Puntaje

    public Intento(int cantidadRielesUsados) throws Exception {
        this.fechaHora = LocalDateTime.now();
        this.setCantidadRielesUsados(cantidadRielesUsados);
        this.esExitoso = false; // Por defecto inicia en falso hasta que el tren llegue a la meta
    }

    // Getters y Setters con Validaciones

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isEsExitoso() {
        return esExitoso;
    }

    public void setEsExitoso(boolean esExitoso) {
        this.esExitoso = esExitoso;
    }

    public double getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(double tiempoSegundos) throws Exception {
        if (tiempoSegundos < 0) {
            throw new Exception("El tiempo de ejecución no puede ser negativo.");
        }
        this.tiempoSegundos = tiempoSegundos;
    }

    public int getCantidadRielesUsados() {
        return cantidadRielesUsados;
    }

    public void setCantidadRielesUsados(int cantidadRielesUsados) throws Exception {
        if (cantidadRielesUsados < 0) {
            throw new Exception("La cantidad de rieles no puede ser negativa.");
        }
        this.cantidadRielesUsados = cantidadRielesUsados;
    }

    public Puntaje getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(Puntaje puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }
}