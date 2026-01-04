package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que define el escenario del juego.
 */
public class Nivel {
    private int numeroNivel;
    private String dificultad;
    private int filas;
    private int columnas;
    private int[][] mapaMatriz; // 0: Vacío, 1: Estación Inicio, 2: Estación Fin, 3: Obstáculo
    private List<Obstaculo> obstaculos;

    public Nivel(int numeroNivel, String dificultad, int filas, int columnas) throws Exception {
        this.setNumeroNivel(numeroNivel);
        this.setDificultad(dificultad);
        if (filas <= 0 || columnas <= 0) {
            throw new Exception("Las dimensiones del mapa deben ser mayores a cero.");
        }
        this.filas = filas;
        this.columnas = columnas;
        this.mapaMatriz = new int[filas][columnas];
        this.obstaculos = new ArrayList<>();
    }

    // Getters y Setters con Validaciones

    public int getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(int numeroNivel) throws Exception {
        if (numeroNivel <= 0) {
            throw new Exception("El número de nivel debe ser positivo.");
        }
        this.numeroNivel = numeroNivel;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) throws Exception {
        if (dificultad == null || dificultad.trim().isEmpty()) {
            throw new Exception("La dificultad no puede estar vacía.");
        }
        this.dificultad = dificultad;
    }

    // Métodos

    /**
     * Permite agregar un obstáculo al nivel y marcarlo en la matriz.
     */
    public void agregarObstaculo(Obstaculo obstaculo) throws Exception {
        int x = obstaculo.getPosX();
        int y = obstaculo.getPosY();

        if (x < 0 || x >= filas || y < 0 || y >= columnas) {
            throw new Exception("El obstáculo está fuera de los límites del mapa.");
        }

        this.obstaculos.add(obstaculo);
        this.mapaMatriz[x][y] = 3; // Se marca como obstáculo en la matriz
    }

    public int[][] getMapaMatriz() {
        return mapaMatriz;
    }
}