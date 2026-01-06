package util;

import negocio.*;

 // Demo simple para ejecutar la lógica del dominio sin interfaz gráfica

public class Main {
    public static void main(String[] args) throws Exception {
        // Inicializar managers
        NivelManager nivelManager = new NivelManager();
        RankingManager rankingManager = new RankingManager();
        Juego juego = new Juego();
        GameManager gameManager = new GameManager(juego, rankingManager);

        // Crear exactamente 3 niveles (FÁCIL, MEDIO, DIFICIL) desde plantillas
        nivelManager.crearNivelesPorDefecto();

        Nivel nivel1 = nivelManager.obtenerNivelPorNumero(1);
        Nivel nivel2 = nivelManager.obtenerNivelPorNumero(2);
        Nivel nivel3 = nivelManager.obtenerNivelPorNumero(3);

        // Crear jugador e iniciar partida
        Jugador jugador = new Jugador(1, "Ana");
        gameManager.iniciarPartida(jugador);

        // --- Modo construcción + play para el nivel 1 ---
        // Definir entrada/salida en columnas izquierda/derecha y colocar rieles que crean una ruta simple
        // Usamos la fila 2 (centro) en un mapa 5x5: inicio en (2,0) y fin en (2,4)
        nivelManager.setEstacionInicio(nivel1, 2, 0, "SUR");
        nivelManager.setEstacionFin(nivel1, 2, 4);
        // Orientación 90 para rieles horizontales (salida ESTE/OESTE)
        nivelManager.agregarRiel(nivel1, new RielRecto(2, 1, 90));
        nivelManager.agregarRiel(nivel1, new RielRecto(2, 2, 90));
        nivelManager.agregarRiel(nivel1, new RielRecto(2, 3, 90));

        // Intentos inválidos: solapamiento, colocar sobre estación, y colocar sobre obstáculo
        try {
            // Solapamiento: ya hay riel en (2,2)
            nivelManager.agregarRiel(nivel1, new RielRecto(2, 2, 90));
        } catch (Exception ex) {
            System.out.println("Error al colocar riel (solapamiento): " + ex.getMessage());
        }
        try {
            // Sobre estación inicio (2,0)
            nivelManager.agregarRiel(nivel1, new RielRecto(2, 0, 90));
        } catch (Exception ex) {
            System.out.println("Error al colocar riel sobre estación: " + ex.getMessage());
        }
        try {
            // Poner un obstáculo y luego intentar colocar riel ahí
            nivel1.agregarObstaculo(new Piedra(0, 1));
            nivelManager.agregarRiel(nivel1, new RielRecto(0, 1, 90));
        } catch (Exception ex) {
            System.out.println("Error al colocar riel sobre obstáculo: " + ex.getMessage());
        }

        // Iniciar construcción (empieza el temporizador), simular que el jugador tarda 1.5s en armar la ruta
        gameManager.iniciarConstruccion(nivel1);
        Thread.sleep(1500);

        // Presiona Play: se simula el tren y se calcula el puntaje usando el tiempo de construcción y rieles usados
        Puntaje p1 = gameManager.playNivel();
        System.out.println("Intento 1 (construcción): puntaje=" + p1.getValorNumerico() + ", estrellas=" + p1.getEstrellas());

        // Para los otros niveles, mostramos cómo se podría seguir usando el flujo antiguo (crear Intento manualmente)
        Intento intento2 = new Intento(8);
        intento2.setEsExitoso(true);
        intento2.setTiempoSegundos(80.2);
        Puntaje p2 = gameManager.finalizarIntento(nivel2, intento2);
        System.out.println("Intento 2: puntaje=" + p2.getValorNumerico() + ", estrellas=" + p2.getEstrellas());

        // Simular intento en nivel 3 (DIFICIL)
        Intento intento3 = new Intento(7);
        intento3.setEsExitoso(true);
        intento3.setTiempoSegundos(30.0);
        Puntaje p3 = gameManager.finalizarIntento(nivel3, intento3);
        System.out.println("Intento 3: puntaje=" + p3.getValorNumerico() + ", estrellas=" + p3.getEstrellas());

        // Mostrar ranking
        System.out.println("Top 10:");
        for (Jugador j : rankingManager.obtenerTopN(10)) {
            int mejor = rankingManager.obtenerMejorPuntaje(j.getIdJugador());
            System.out.println(j + " - Mejor puntaje: " + mejor);
        }

        System.out.println("Demo de lógica concluido.");
    }
}