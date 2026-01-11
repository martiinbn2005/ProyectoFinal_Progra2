package util;

import negocio.Nivel;
import negocio.Obstaculo;
import negocio.Rieles;

import java.util.ArrayList;
import java.util.List;


//Responsable de crear y validar niveles (uso del dominio sin interfaz gráfica)

public class NivelManager {

    private List<Nivel> niveles;

    public NivelManager() {
        this.niveles = new ArrayList<>();
    }

    public void crearNivelesPorDefecto() throws Exception {
        this.niveles.clear();
        // Cargar los 3 niveles desde plantillas predefinidas
        try {
            this.cargarDesdePlantilla("resources/levels/level1.lvl");
            this.cargarDesdePlantilla("resources/levels/level2.lvl");
            this.cargarDesdePlantilla("resources/levels/level3.lvl");
        } catch (Exception ex) {
            // Si falla la carga de plantillas, crear niveles vacíos como fallback
            System.err.println("Advertencia: no se pudieron cargar plantillas, creando niveles vacíos: " + ex.getMessage());
            this.niveles.add(new Nivel(1, "FACIL", 5, 5));
            this.niveles.add(new Nivel(2, "MEDIO", 7, 7));
            this.niveles.add(new Nivel(3, "DIFICIL", 9, 9));
        }
    }

    public List<Nivel> listarNiveles() {
        return new ArrayList<>(niveles);
    }

    public void agregarObstaculo(Nivel nivel, Obstaculo obstaculo) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        if (obstaculo == null) throw new Exception("Obstáculo nulo");

        nivel.agregarObstaculo(obstaculo);
    }

    // Nuevos helpers para rieles y estaciones
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
                // ignoramos
            }
        }
        return null;
    }

    /**
     * Carga un nivel a partir de una plantilla en disco (formato simple definido en resources/levels).
     * El archivo usa líneas "clave:valor" y una sección "obstaculos:" seguida de líneas "x,y[,TIPO]".
     * Devuelve el Nivel creado y lo añade a la lista interna de niveles.
     */
    public Nivel cargarDesdePlantilla(String path) throws Exception {
        java.io.File f = new java.io.File(path);
        if (!f.exists()) throw new Exception("Plantilla no encontrada: " + path);

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
            String line;
            Integer numero = null;
            String dificultad = null;
            Integer filas = null;
            Integer columnas = null;
            String entradaStr = null;
            String salidaStr = null;
            java.util.List<String> obstLines = new java.util.ArrayList<>();
            boolean inObst = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (inObst) {
                    obstLines.add(line);
                    continue;
                }
                if (line.endsWith(":")) {
                    String key = line.substring(0, line.length() - 1).trim().toLowerCase();
                    if ("obstaculos".equals(key)) {
                        inObst = true;
                    }
                    continue;
                }

                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    switch (key) {
                        case "numero": numero = Integer.parseInt(val); break;
                        case "dificultad": dificultad = val; break;
                        case "filas": filas = Integer.parseInt(val); break;
                        case "columnas": columnas = Integer.parseInt(val); break;
                        case "entrada": entradaStr = val; break;
                        case "salida": salidaStr = val; break;
                        default: break;
                    }
                }
            }

            if (numero == null || dificultad == null || filas == null || columnas == null) {
                throw new Exception("Plantilla incompleta: faltan campos obligatorios (numero, dificultad, filas, columnas)");
            }

            Nivel nivel = new Nivel(numero, dificultad, filas, columnas);

            // procesar estaciones
            if (entradaStr != null) {
                try {
                    String[] p = entradaStr.split(",");
                    if (p.length < 3) throw new Exception("Entrada inválida en plantilla: " + entradaStr);
                    int ex = Integer.parseInt(p[0].trim());
                    int ey = Integer.parseInt(p[1].trim());
                    String dir = p[2].trim();
                    nivel.setEstacionInicio(ex, ey, dir);
                } catch (NumberFormatException e) {
                    throw new Exception("Formato numérico inválido en entrada de plantilla '" + entradaStr + "': " + e.getMessage());
                }
            }
            if (salidaStr != null) {
                try {
                    String[] p = salidaStr.split(",");
                    if (p.length < 2) throw new Exception("Salida inválida en plantilla: " + salidaStr);
                    int sx = Integer.parseInt(p[0].trim());
                    int sy = Integer.parseInt(p[1].trim());
                    nivel.setEstacionFin(sx, sy);
                } catch (NumberFormatException e) {
                    throw new Exception("Formato numérico inválido en salida de plantilla '" + salidaStr + "': " + e.getMessage());
                }
            }

            // procesar obstaculos
            for (String o : obstLines) {
                try {
                    String[] p = o.split(",");
                    if (p.length < 2) continue;
                    int ox = Integer.parseInt(p[0].trim());
                    int oy = Integer.parseInt(p[1].trim());
                    String tipo = p.length >= 3 ? p[2].trim().toUpperCase() : "PIEDRA";
                    // Por ahora solo se soporta PIEDRA
                    if ("PIEDRA".equals(tipo)) {
                        nivel.agregarObstaculo(new negocio.Piedra(ox, oy));
                    } else {
                        // fallback a piedra si tipo desconocido
                        nivel.agregarObstaculo(new negocio.Piedra(ox, oy));
                    }
                } catch (NumberFormatException e) {
                    throw new Exception("Formato numérico inválido en obstáculo '" + o + "': " + e.getMessage());
                }
            }

            this.niveles.add(nivel);
            return nivel;

        } catch (java.io.IOException e) {
            throw new Exception("Error leyendo plantilla: " + e.getMessage());
        }
    }
}