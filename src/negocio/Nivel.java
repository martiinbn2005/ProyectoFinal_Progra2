package negocio;

import java.util.ArrayList;
import java.util.List;

 //clase que define el escenario del juego.
 
public class Nivel {
    private int numeroNivel;
    private String dificultad;
    private int filas;
    private int columnas;
    private int[][] mapaMatriz; //0: vacío, 1: estación inicio, 2: estación fin, 3: obstáculo
    private List<Obstaculo> obstaculos;

    //nuevos campos para rieles y estaciones (entrada/salida)
    private List<Rieles> rieles;
    private int entradaX = -1;
    private int entradaY = -1;
    private String entradaDireccion;
    private int salidaX = -1;
    private int salidaY = -1;

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
        this.rieles = new ArrayList<>();
    }

    //getters y setters con validaciones

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

    //métodos

    /**
    Permite agregar un obstáculo al nivel y marcarlo en la matriz.
    @param obstaculo el obstáculo a agregar
    @throws Exception si el obstáculo está fuera de límites
    @throws IllegalArgumentException si el obstáculo es nulo
     */
    public void agregarObstaculo(Obstaculo obstaculo) throws Exception {
        if (obstaculo == null) {
            throw new IllegalArgumentException("Obstáculo no puede ser nulo");
        }
        int x = obstaculo.getPosX();
        int y = obstaculo.getPosY();

        if (x < 0 || x >= filas || y < 0 || y >= columnas) {
            throw new Exception("El obstáculo está fuera de los límites del mapa. Coordenadas: (" + x + "," + y + "), Tamaño mapa: " + filas + "x" + columnas);
        }

        this.obstaculos.add(obstaculo);
        this.mapaMatriz[x][y] = 3; //se marca como obstáculo en la matriz
    }

    //métodos para rieles y estaciones (entrada/salida)
    public void setEstacionInicio(int x, int y, String direccion) throws Exception {
        //la estación de inicio debe ubicarse en la columna izquierda (y == 0)
        if (x < 0 || x >= filas || y < 0 || y >= columnas) throw new Exception("Coordenadas fuera de rango para entrada.");
        if (y != 0) throw new Exception("La estación inicio debe estar en la columna izquierda (y=0) para iniciar desde la izquierda.");
        this.entradaX = x;
        this.entradaY = y;
        //forzamos que la estación de inicio siempre salga hacia la derecha (ESTE)
        this.entradaDireccion = "ESTE";
        this.mapaMatriz[x][y] = 1;
    }

    public void setEstacionFin(int x, int y) throws Exception {
        //la estación de fin debe ubicarse en la columna derecha (y == columnas - 1)
        if (x < 0 || x >= filas || y < 0 || y >= columnas) throw new Exception("Coordenadas fuera de rango para salida.");
        if (y != (columnas - 1)) throw new Exception("La estación fin debe estar en la columna derecha para terminar en la derecha.");
        this.salidaX = x;
        this.salidaY = y;
        this.mapaMatriz[x][y] = 2;
    }

    public int getEntradaX() { return entradaX; }
    public int getEntradaY() { return entradaY; }
    public String getEntradaDireccion() { return entradaDireccion; }
    public int getSalidaX() { return salidaX; }
    public int getSalidaY() { return salidaY; }

    /**
    Verifica si es posible colocar un riel en (x,y) y devuelve
    Optional.empty() si se puede, o Optional con la razón en caso contrario.
     */
    public java.util.Optional<String> puedeColocarRiel(int x, int y, Rieles riel) {
        if (riel == null) return java.util.Optional.of("riel nulo");
        if (x < 0 || x >= filas || y < 0 || y >= columnas) return java.util.Optional.of("coordenadas fuera de rango para riel.");
        //si hay obstáculo en la matriz
        if (this.mapaMatriz[x][y] == 3) return java.util.Optional.of("celda ocupada por obstáculo.");
        //no permitir colocar sobre estaciones
        if (this.mapaMatriz[x][y] == 1) return java.util.Optional.of("no se puede colocar riel sobre estación inicio.");
        if (this.mapaMatriz[x][y] == 2) return java.util.Optional.of("no se puede colocar riel sobre estación fin.");
        //evitar solapamiento de rieles
        if (obtenerRielEn(x, y) != null) return java.util.Optional.of("Ya existe un riel en la celda.");
        return java.util.Optional.empty();
    }

    /**
    Agrega un riel al nivel con validaciones.
    @param riel el riel a agregar
    @throws Exception si el riel no se puede colocar en la posición indicada
    @throws IllegalArgumentException si el riel es nulo
     */
    public void agregarRiel(Rieles riel) throws Exception {
        if (riel == null) {
            throw new IllegalArgumentException("Riel no puede ser nulo");
        }
        java.util.Optional<String> motivo = puedeColocarRiel(riel.getPosX(), riel.getPosY(), riel);
        if (motivo.isPresent()) {
            throw new Exception("Imposible colocar riel en posición (" + riel.getPosX() + "," + riel.getPosY() + "): " + motivo.get());
        }
        this.rieles.add(riel);
    }

    public Rieles obtenerRielEn(int x, int y) {
        for (Rieles r : rieles) {
            if (r.getPosX() == x && r.getPosY() == y) return r;
        }
        return null;
    }

    public java.util.List<Rieles> getRieles() { return new java.util.ArrayList<>(rieles); }

    public int[][] getMapaMatriz() {
        return mapaMatriz;
    }
}