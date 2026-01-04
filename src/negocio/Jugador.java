package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa al usuario en el sistema.
 */
public class Jugador {
    private int idJugador;
    private String nombre;
    private List<Intento> historialIntentos; // Relación 1 a muchos con Intento

    // Constructor
    public Jugador(int idJugador, String nombre) throws Exception {
        this.setIdJugador(idJugador);
        this.setNombre(nombre);
        this.historialIntentos = new ArrayList<>();
    }

    // Getters y Setters con Validaciones

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) throws Exception {
        if (idJugador <= 0) {
            throw new Exception("El ID del jugador debe ser un número positivo.");
        }
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del jugador no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public List<Intento> getHistorialIntentos() {
        return new ArrayList<>(historialIntentos);
    }

    // Métodos

    /**
     * Registra un nuevo intento de nivel para el jugador.
     */
    public void registrarIntento(Intento nuevoIntento) {
        if (nuevoIntento != null) {
            this.historialIntentos.add(nuevoIntento);
        }
    }

    @Override
    public String toString() {
        return "Jugador{" + "id=" + idJugador + ", nombre='" + nombre + '\'' + '}';
    }
}