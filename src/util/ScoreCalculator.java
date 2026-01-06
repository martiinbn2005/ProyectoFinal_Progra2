package util;

import negocio.Intento;
import negocio.Nivel;
import negocio.Puntaje;


public class ScoreCalculator {

    /*
     * Calcula y devuelve un objeto Puntaje basado en el intento y el nivel.
     * Reglas simples:
        - Si el intento no fue exitoso, puntaje = 0
        - Puntaje base 100 para intentos exitosos, se aplican penalizaciones por tiempo y rieles usados.
        - Los primeros 10 segundos de construcción NO se penalizan.
        - Dificultad ajusta puntaje: "FACIL" +0, "MEDIO" -10, "DIFICIL" -20 (case-insensitive)
     */
    public static Puntaje calcularPuntaje(Intento intento, Nivel nivel) throws Exception {
        if (intento == null || nivel == null) throw new Exception("Intento y Nivel son obligatorios para calcular puntaje.");

        if (!intento.isEsExitoso()) {
            return new Puntaje(0);
        }

        double tiempo = intento.getTiempoSegundos();
        int rieles = intento.getCantidadRielesUsados();

        int base = 100;

        // Penalizaciones
        // Primeros 10 segundos no se penalizan
        double tiempoPenalizable = Math.max(0.0, tiempo - 10.0);
        int penalizacionTiempo = (int) Math.round(tiempoPenalizable * 1.0); // 1 punto por segundo sobre el tiempo penalizable
        int penalizacionRieles = Math.max(0, (rieles - 5) * 2); // si usó más de 5 rieles, 2 puntos por extra

        int ajusteDificultad = 0;
        String d = nivel.getDificultad().toUpperCase();
        if (d.contains("DIFIC")) ajusteDificultad = -20;
        else if (d.contains("MED")) ajusteDificultad = -10;
        // else fácil  0

        int calculado = base - penalizacionTiempo - penalizacionRieles + ajusteDificultad;
        if (calculado < 0) calculado = 0;
        if (calculado > 100) calculado = 100;

        return new Puntaje(calculado);
    }
}
