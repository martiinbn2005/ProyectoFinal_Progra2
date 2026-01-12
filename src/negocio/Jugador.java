package negocio;

import java.util.ArrayList;
import java.util.List;

 // Clase que representa al usuario en el sistema.
 
public class Jugador {
    private int idJugador;
    private String nombre;
    private List<Intento> historialIntentos; //relación 1 a muchos con intento

    //constructor
    public Jugador(int idJugador, String nombre) throws Exception {
        this.setIdJugador(idJugador);
        this.setNombre(nombre);
        this.historialIntentos = new ArrayList<>();
    }

    //getters y setters con validaciones

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

    //métodos

    /**
    Registra un nuevo intento de nivel para el jugador.
    @param nuevoIntento el intento a registrar
    @throws IllegalArgumentException si el intento es nulo
     */
    public void registrarIntento(Intento nuevoIntento) throws IllegalArgumentException {
        if (nuevoIntento == null) {
            throw new IllegalArgumentException("No se puede registrar un intento nulo");
        }
        this.historialIntentos.add(nuevoIntento);
    }

    @Override
    public String toString() {
        return "Jugador{" + "id=" + idJugador + ", nombre='" + nombre + '\'' + '}';
    }
}