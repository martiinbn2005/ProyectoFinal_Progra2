package util;

import negocio.Intento;
import negocio.Juego;
import negocio.Jugador;
import negocio.Nivel;
import negocio.Puntaje;
import negocio.Rieles;

public class GameManager {

    private Juego juego;
    private RankingManager rankingManager;

    //campos para el modo construcción
    private long construccionStartMillis;
    private Nivel nivelEnConstruccion;

    public GameManager(Juego juego, RankingManager rankingManager) {
        this.juego = juego;
        this.rankingManager = rankingManager;
    }

    public void iniciarPartida(Jugador jugador) throws Exception {
        if (jugador == null) throw new Exception("Jugador nulo");
        juego.setJugadorActual(jugador);
        rankingManager.registrarJugador(jugador);
    }

    //marca el inicio del tiempo de construcción para un nivel
    public void iniciarConstruccion(Nivel nivel) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        this.nivelEnConstruccion = nivel;
        this.construccionStartMillis = System.currentTimeMillis();
    }

    //el jugador presiona "play": se calcula el tiempo de construcción, se crea el intento, se simula la ejecución del tren y se finaliza el intento (cálculo de puntaje y registro)
    public Puntaje playNivel() throws Exception {
        if (nivelEnConstruccion == null) {
            throw new IllegalStateException("No hay un nivel en construcción. Debe llamar a iniciarConstruccion() primero.");
        }

        try {
            double tiempoSegundos = (System.currentTimeMillis() - this.construccionStartMillis) / 1000.0;
            int rielesUsados = nivelEnConstruccion.getRieles().size();

            Intento intento = new Intento(rielesUsados);
            intento.setTiempoSegundos(tiempoSegundos); //tiempo usado para construir la ruta

            //simular salida del tren
            boolean exito = simularTren(nivelEnConstruccion);
            intento.setEsExitoso(exito);

            //finalizar intento (calcular puntaje y registrar)
            Puntaje puntaje = finalizarIntento(nivelEnConstruccion, intento);

            return puntaje;
        } catch (Exception e) {
            throw new Exception("Error al reproducir nivel: " + e.getMessage(), e);
        } finally {
            //reset estado de construcción incluso si hay error
            this.nivelEnConstruccion = null;
        }
    }

    //simula el recorrido del tren por la matriz de rieles del nivel. retorna true si alcanza la estación de fin, false si choca o descarrila
    private boolean simularTren(Nivel nivel) throws Exception {
        if (nivel == null) {
            throw new IllegalArgumentException("Nivel no puede ser nulo para simular tren");
        }

        int[][] mapa = nivel.getMapaMatriz();
        if (mapa == null || mapa.length == 0) {
            throw new IllegalStateException("Mapa del nivel no configurado o vacío");
        }
        int rows = mapa.length;
        int cols = (rows > 0) ? mapa[0].length : 0;
        if (cols == 0) {
            throw new IllegalStateException("Mapa del nivel sin columnas válidas");
        }

        int x = nivel.getEntradaX();
        int y = nivel.getEntradaY();
        String dir = nivel.getEntradaDireccion();
        if (x < 0 || y < 0 || dir == null) return false;

        int maxSteps = rows * cols * 4; //para evitar loops infinitos
        for (int step = 0; step < maxSteps; step++) {
            int nx = x, ny = y;
            if ("NORTE".equals(dir)) nx--;
            else if ("SUR".equals(dir)) nx++;
            else if ("OESTE".equals(dir)) ny--;
            else if ("ESTE".equals(dir)) ny++;
            else return false;

            if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) return false; //sale del mapa

            //si llega a estación fin, solo es éxito si entra desde la izquierda (moviendo hacia este)
            if (nx == nivel.getSalidaX() && ny == nivel.getSalidaY()) {
                if ("ESTE".equals(dir)) return true;
                else return false;
            }

            //si choca con obstáculo
            if (mapa[nx][ny] == 3) return false;

            //debe haber un riel en la celda
            Rieles r = nivel.obtenerRielEn(nx, ny);
            if (r == null) return false;

            //determinar por qué lado entra (opuesto a la dirección de movimiento)
            String entradaSide = oppositeDirection(dir);
            String salidaSide = r.obtenerSalida(entradaSide);
            if ("DESCARRILAMIENTO".equals(salidaSide)) return false;

            //actualizar dirección y posición
            dir = salidaSide;
            x = nx;
            y = ny;
        }

        return false;
    }

    private String oppositeDirection(String dir) {
        if ("NORTE".equals(dir)) return "SUR";
        if ("SUR".equals(dir)) return "NORTE";
        if ("ESTE".equals(dir)) return "OESTE";
        if ("OESTE".equals(dir)) return "ESTE";
        return dir;
    }

    public Puntaje finalizarIntento(Nivel nivel, Intento intento) throws Exception {
        if (nivel == null) {
            throw new IllegalArgumentException("Nivel no puede ser nulo para finalizar intento");
        }
        if (intento == null) {
            throw new IllegalArgumentException("Intento no puede ser nulo para finalizar");
        }

        try {
            //calcular puntaje
            Puntaje puntaje = ScoreCalculator.calcularPuntaje(intento, nivel);
            intento.setPuntajeObtenido(puntaje);

            //registrar intento para el jugador actual
            Jugador j = juego.getJugadorActual();
            if (j != null) {
                j.registrarIntento(intento);
            }

            //actualizar ranking
            if (j != null) {
                rankingManager.actualizarConIntento(j, intento);
            }

            return puntaje;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error al finalizar intento: " + e.getMessage(), e);
        }
    }
}