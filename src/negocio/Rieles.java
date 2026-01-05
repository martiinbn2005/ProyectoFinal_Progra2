package negocio;

/**
 * Clase abstracta que representa un tramo de vía en el mapa.
 * Define la estructura base y las constantes de dirección.
 */
public abstract class Rieles {

    // Constantes de Dirección
    public static final String NORTE = "NORTE";
    public static final String SUR = "SUR";
    public static final String ESTE = "ESTE";   // Derecha
    public static final String OESTE = "OESTE"; // Izquierda

    //  Atributos
    private int posX;
    private int posY;

    // Constructor
    public Rieles(int posX, int posY) throws Exception {
        this.setPosX(posX);
        this.setPosY(posY);
    }

    // Métodos Abstractos

    /**
     * Determina por dónde sale el tren dado un punto de entrada.
     */
    public abstract String obtenerSalida(String entrada);

    // Getters y Setters con Validaciones

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) throws Exception{
        if (posX < 0) {
            throw new Exception("La coordenada X del riel no puede ser negativa.");
        }
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) throws Exception{
        if (posY < 0) {
            throw new Exception("La coordenada Y del riel no puede ser negativa.");
        }
        this.posY = posY;
    }
}