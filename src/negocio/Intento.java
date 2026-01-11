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

    public void setTiempoSegundos(double tiempoSegundos) throws IllegalArgumentException {
        if (tiempoSegundos < 0) {
            throw new IllegalArgumentException("El tiempo de ejecución no puede ser negativo. Valor: " + tiempoSegundos);
        }
        this.tiempoSegundos = tiempoSegundos;
    }

    public int getCantidadRielesUsados() {
        return cantidadRielesUsados;
    }

    public void setCantidadRielesUsados(int cantidadRielesUsados) throws IllegalArgumentException {
        if (cantidadRielesUsados < 0) {
            throw new IllegalArgumentException("La cantidad de rieles no puede ser negativa. Valor: " + cantidadRielesUsados);
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