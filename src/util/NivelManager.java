package util;

import negocio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de niveles del juego.
 * Crea y administra los niveles predefinidos.
 */
public class NivelManager {

    private List<Nivel> niveles;

    public NivelManager() {
        this.niveles = new ArrayList<>();
    }

    /**
     * Crea los 3 niveles por defecto del juego: Fácil, Medio y Difícil
     */
    public void crearNivelesPorDefecto() throws Exception {
        niveles.clear();

        // Nivel 1: FÁCIL (5x5)
        Nivel nivel1 = new Nivel(1, "FACIL", 5, 5);
        nivel1.setEstacionInicio(2, 0, "ESTE");
        nivel1.setEstacionFin(2, 4);
        nivel1.agregarObstaculo(new Piedra(0, 2));
        nivel1.agregarObstaculo(new Piedra(4, 2));
        niveles.add(nivel1);

        // Nivel 2: MEDIO (6x7)
        Nivel nivel2 = new Nivel(2, "MEDIO", 6, 7);
        nivel2.setEstacionInicio(2, 0, "ESTE");
        nivel2.setEstacionFin(4, 6);
        nivel2.agregarObstaculo(new Piedra(1, 2));
        nivel2.agregarObstaculo(new Piedra(2, 3));
        nivel2.agregarObstaculo(new Piedra(3, 2));
        nivel2.agregarObstaculo(new Piedra(4, 4));
        niveles.add(nivel2);

        // Nivel 3: DIFÍCIL (7x8)
        Nivel nivel3 = new Nivel(3, "DIFICIL", 7, 8);
        nivel3.setEstacionInicio(3, 0, "ESTE");
        nivel3.setEstacionFin(5, 7);
        nivel3.agregarObstaculo(new Piedra(1, 2));
        nivel3.agregarObstaculo(new Piedra(2, 3));
        nivel3.agregarObstaculo(new Piedra(3, 2));
        nivel3.agregarObstaculo(new Piedra(3, 4));
        nivel3.agregarObstaculo(new Piedra(4, 3));
        nivel3.agregarObstaculo(new Piedra(5, 4));
        nivel3.agregarObstaculo(new Piedra(5, 2));
        niveles.add(nivel3);
    }

    /**
     * Obtiene un nivel por su número
     */
    public Nivel obtenerNivelPorNumero(int numeroNivel) throws Exception {
        for (Nivel n : niveles) {
            if (n.getNumeroNivel() == numeroNivel) {
                return n;
            }
        }
        throw new Exception("Nivel no encontrado: " + numeroNivel);
    }

    /**
     * Obtiene todos los niveles
     */
    public List<Nivel> obtenerTodosLosNiveles() {
        return new ArrayList<>(niveles);
    }

    /**
     * Agrega un riel a un nivel con validación
     */
    public void agregarRiel(Nivel nivel, Rieles riel) throws Exception {
        if (nivel == null) {
            throw new IllegalArgumentException("Nivel no puede ser nulo");
        }
        if (riel == null) {
            throw new IllegalArgumentException("Riel no puede ser nulo");
        }

        nivel.agregarRiel(riel);
    }
}