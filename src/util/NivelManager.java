package util;

import negocio.*;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
/**
 * Gestor de niveles del juego.
 * Crea y administra los niveles predefinidos.
 */
=======

// Responsable de crear y validar niveles (uso del dominio sin interfaz gráfica)

>>>>>>> 59b6db44a5257da67ceb97f6962bd6be270423a6
public class NivelManager {

    private List<Nivel> niveles;

    public NivelManager() {
        this.niveles = new ArrayList<>();
    }

    /**
     * Crea los 3 niveles por defecto del juego: Fácil, Medio y Difícil
     */
    public void crearNivelesPorDefecto() throws Exception {
<<<<<<< HEAD
        niveles.clear();
=======
        this.niveles.clear();
        //cargar los 3 niveles desde plantillas predefinidas
        try {
            this.cargarDesdePlantilla("resources/levels/level1.lvl");
            this.cargarDesdePlantilla("resources/levels/level2.lvl");
            this.cargarDesdePlantilla("resources/levels/level3.lvl");
        } catch (Exception ex) {
            //si falla la carga de plantillas, crear niveles vacíos como fallback
            System.err.println("Advertencia: no se pudieron cargar plantillas, creando niveles vacíos: " + ex.getMessage());
            this.niveles.add(new Nivel(1, "FACIL", 5, 5));
            this.niveles.add(new Nivel(2, "MEDIO", 7, 7));
            this.niveles.add(new Nivel(3, "DIFICIL", 9, 9));
        }
    }
>>>>>>> 59b6db44a5257da67ceb97f6962bd6be270423a6

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

<<<<<<< HEAD
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
=======
        nivel.agregarObstaculo(obstaculo);
    }

    //nuevos helpers para rieles y estaciones
    public void agregarRiel(Nivel nivel, Rieles riel) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        if (riel == null) throw new Exception("Riel nulo");
        nivel.agregarRiel(riel);
    }

    public void setEstacionInicio(Nivel nivel, int x, int y, String direccion) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        nivel.setEstacionInicio(x, y, direccion);
    }

    public void setEstacionFin(Nivel nivel, int x, int y) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        nivel.setEstacionFin(x, y);
    }

    public Nivel obtenerNivelPorNumero(int numero) {
        for (Nivel n : niveles) {
            try {
                if (n.getNumeroNivel() == numero) return n;
            } catch (Exception ex) {
                //ignoramos
            }
        }
        return null;
    }

    /*
    Carga un nivel a partir de una plantilla (formato simple definido en resources/levels).
    El archivo usa líneas "clave:valor" y una sección "obstaculos:" seguida de líneas "x,y[,TIPO]".
    devuelve el Nivel creado y lo añade a la lista interna de niveles.
>>>>>>> 59b6db44a5257da67ceb97f6962bd6be270423a6
     */
    public Nivel obtenerNivelPorNumero(int numeroNivel) throws Exception {
        for (Nivel n : niveles) {
            if (n.getNumeroNivel() == numeroNivel) {
                return n;
            }
<<<<<<< HEAD
=======

            if (numero == null || dificultad == null || filas == null || columnas == null) {
                throw new Exception("plantilla incompleta: faltan campos obligatorios (numero, dificultad, filas, columnas)");
            }

            Nivel nivel = new Nivel(numero, dificultad, filas, columnas);

            //procesar estaciones
            if (entradaStr != null) {
                try {
                    String[] p = entradaStr.split(",");
                    if (p.length < 3) throw new Exception("entrada inválida en plantilla: " + entradaStr);
                    int ex = Integer.parseInt(p[0].trim());
                    int ey = Integer.parseInt(p[1].trim());
                    String dir = p[2].trim();
                    nivel.setEstacionInicio(ex, ey, dir);
                } catch (NumberFormatException e) {
                    throw new Exception("formato numérico inválido en entrada de plantilla '" + entradaStr + "': " + e.getMessage());
                }
            }
            if (salidaStr != null) {
                try {
                    String[] p = salidaStr.split(",");
                    if (p.length < 2) throw new Exception("salida inválida en plantilla: " + salidaStr);
                    int sx = Integer.parseInt(p[0].trim());
                    int sy = Integer.parseInt(p[1].trim());
                    nivel.setEstacionFin(sx, sy);
                } catch (NumberFormatException e) {
                    throw new Exception("formato numérico inválido en salida de plantilla '" + salidaStr + "': " + e.getMessage());
                }
            }

            //procesar obstaculos
            for (String o : obstLines) {
                try {
                    String[] p = o.split(",");
                    if (p.length < 2) continue;
                    int ox = Integer.parseInt(p[0].trim());
                    int oy = Integer.parseInt(p[1].trim());
                    String tipo = p.length >= 3 ? p[2].trim().toUpperCase() : "PIEDRA";
                    //por ahora solo se soporta piedra
                    if ("PIEDRA".equals(tipo)) {
                        nivel.agregarObstaculo(new negocio.Piedra(ox, oy));
                    } else {
                        //fallback a piedra si tipo desconocido
                        nivel.agregarObstaculo(new negocio.Piedra(ox, oy));
                    }
                } catch (NumberFormatException e) {
                    throw new Exception("formato numérico inválido en obstáculo '" + o + "': " + e.getMessage());
                }
            }

            this.niveles.add(nivel);
            return nivel;

        } catch (java.io.IOException e) {
            throw new Exception("Error leyendo plantilla: " + e.getMessage());
>>>>>>> 59b6db44a5257da67ceb97f6962bd6be270423a6
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