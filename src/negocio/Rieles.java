package negocio;

/**
 * Clase base para todos los tipos de vías.
 */
public abstract class Rieles {
    private int posX;
    private int posY;
    private int orientacion; // 0, 90, 180, 270 grados

    public Rieles(int posX, int posY, int orientacion) throws Exception {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setOrientacion(orientacion);
    }

    // Getters y Setters con Validaciones
    public int getPosX() { return posX; }
    public void setPosX(int posX) throws Exception {
        if (posX < 0) throw new Exception("Coordenada X inválida.");
        this.posX = posX;
    }

    public int getPosY() { return posY; }
    public void setPosY(int posY) throws Exception {
        if (posY < 0) throw new Exception("Coordenada Y inválida.");
        this.posY = posY;
    }

    public int getOrientacion() { return orientacion; }
    public void setOrientacion(int orientacion) {
        this.orientacion = (orientacion % 360); // Asegura que esté en el rango circular
    }

    // Métodos

    /**
     * Cada riel decide hacia dónde sale el tren según por qué lado entró.
     */
    public abstract String obtenerSalida(String entrada);
}