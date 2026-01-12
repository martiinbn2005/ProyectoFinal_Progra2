package negocio;

 //Clase que representa la evaluación del desempeño del jugador en un intento.

public class Puntaje {
    private int valorNumerico;
    private int estrellas; //calificación de 1 a 3
    private String descripcionDesempeño;

    public Puntaje(int valorNumerico) throws Exception {
        this.setValorNumerico(valorNumerico);
        this.calcularEstrellas();
    }

    //getters y setters con validaciones

    public int getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(int valorNumerico) throws IllegalArgumentException {
        if (valorNumerico < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser un valor negativo. Valor: " + valorNumerico);
        }
        this.valorNumerico = valorNumerico;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public String getDescripcionDesempeño() {
        return descripcionDesempeño;
    }

    //métodos
    // Define el rango de estrellas basado en el puntaje obtenido.
    
    private void calcularEstrellas() {
        if (this.valorNumerico >= 90) {
            this.estrellas = 3;
            this.descripcionDesempeño = "¡Ingeniero Maestro! Ruta óptima.";
        } else if (this.valorNumerico >= 60) {
            this.estrellas = 2;
            this.descripcionDesempeño = "Buen trabajo. Se puede optimizar.";
        } else {
            this.estrellas = 1;
            this.descripcionDesempeño = "Nivel completado, pero hubo ineficiencias.";
        }
    }
}