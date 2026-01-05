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

        // Crear exactamente 3 niveles (FÁCIL, MEDIO, DIFICIL)
        nivelManager.crearNivelesPorDefecto();
        Nivel nivel1 = nivelManager.obtenerNivelPorNumero(1);
        Nivel nivel2 = nivelManager.obtenerNivelPorNumero(2);
        Nivel nivel3 = nivelManager.obtenerNivelPorNumero(3);

        // Crear jugador e iniciar partida
        Jugador jugador = new Jugador(1, "Ana");
        gameManager.iniciarPartida(jugador);

        // Simular intentos
        Intento intento1 = new Intento(6);
        intento1.setEsExitoso(true);
        intento1.setTiempoSegundos(42.5);
        Puntaje p1 = gameManager.finalizarIntento(nivel1, intento1);
        System.out.println("Intento 1: puntaje=" + p1.getValorNumerico() + ", estrellas=" + p1.getEstrellas());

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