package negocio;

public class RielRecto extends Rieles {
    private boolean esVertical; // true: N-S, false: E-O

    public RielRecto(int posX, int posY, boolean esVertical) throws Exception {
        super(posX, posY);
        this.esVertical = esVertical;
    }

    public boolean isEsVertical() {
        return esVertical;
    }

    @Override
    public String obtenerSalida(String entrada) {
        if (!esVertical) { // Riel Horizontal
            if (entrada.equals(OESTE)) return ESTE;
            if (entrada.equals(ESTE)) return OESTE;
        } else { // Riel Vertical
            if (entrada.equals(NORTE)) return SUR;
            if (entrada.equals(SUR)) return NORTE;
        }
        return "DESCARRILAMIENTO";
    }
}