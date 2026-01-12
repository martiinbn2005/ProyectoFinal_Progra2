package negocio;

/*
Clase abstracta que representa un tramo de vía en el mapa.
Define la estructura base y las constantes de dirección.
 */
public abstract class Rieles {

    //constantes de dirección
    public static final String NORTE = "NORTE";
    public static final String SUR = "SUR";
    public static final String ESTE = "ESTE";   //derecha
    public static final String OESTE = "OESTE"; //izquierda

    //atributos
    private int posX;
    private int posY;
    private int orientacion; //0, 90, 180, 270 grados

    //constructor
    public Rieles(int posX, int posY, int orientacion) throws Exception {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setOrientacion(orientacion);
    }

    //métodos abstractos

    //Determina por dónde sale el tren dado un punto de entrada.
    public abstract String obtenerSalida(String entrada);

    //getters y setters con validaciones

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) throws IllegalArgumentException {
        if (posX < 0) {
            throw new IllegalArgumentException("La coordenada X del riel no puede ser negativa. Valor: " + posX);
        }
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) throws IllegalArgumentException {
        if (posY < 0) {
            throw new IllegalArgumentException("La coordenada Y del riel no puede ser negativa. Valor: " + posY);
        }
        this.posY = posY;
    }

    public int getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(int orientacion) {
        this.orientacion = (orientacion % 360);
        if (this.orientacion < 0) this.orientacion += 360;
    }
}