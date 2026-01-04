package negocio;

/**
 * Clase abstracta que representa un obstáculo en el mapa.
 */
public abstract class Obstaculo {
    private int posX;
    private int posY;
    private String nombre;

    public Obstaculo(int posX, int posY, String nombre) throws Exception {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setNombre(nombre);
    }

    // Getters y Setters con Validaciones

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) throws Exception {
        if (posX < 0) throw new Exception("La posición X no puede ser negativa.");
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) throws Exception {
        if (posY < 0) throw new Exception("La posición Y no puede ser negativa.");
        this.posY = posY;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del obstáculo es obligatorio.");
        }
        this.nombre = nombre;
    }

    /**
     * Método abstracto que definirá cómo afecta el obstáculo al tren.
     */
    public abstract String getTipoEfecto();
}