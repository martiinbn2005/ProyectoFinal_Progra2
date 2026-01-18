package negocio;

public class RielCurvo extends Rieles {

    //representamos los 4 tipos de curvas posibles
    public static final String SUP_DER = "SUPERIOR_DERECHA"; //conecta norte y este
    public static final String SUP_IZQ = "SUPERIOR_IZQUIERDA"; //conecta norte y oeste
    public static final String INF_DER = "INFERIOR_DERECHA"; //conecta sur y este
    public static final String INF_IZQ = "INFERIOR_IZQUIERDA"; //conecta sur y oeste

    private String tipoCurva;

    public RielCurvo(int posX, int posY, int orientacion, String tipoCurva) throws Exception{
        super(posX, posY, orientacion);
        this.setTipoCurva(tipoCurva);
    }

    public String getTipoCurva() {
        return tipoCurva;
    }

    public void setTipoCurva(String tipoCurva) throws IllegalArgumentException {
        if (tipoCurva == null || tipoCurva.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de curva no puede ser nulo o vacío.");
        }
        this.tipoCurva = tipoCurva;
    }

    @Override
    public String obtenerSalida(String entrada) {
        if (entrada == null) return "DESCARRILAMIENTO";

        switch (this.tipoCurva) {
            case SUP_DER: //superior derecha (┐): conecta ARRIBA-DERECHA
                if (entrada.equals(NORTE)) return ESTE;   // Arriba -> Derecha
                if (entrada.equals(ESTE)) return NORTE;   // Derecha -> Arriba
                break;

            case SUP_IZQ: //superior izquierda (┌): conecta ARRIBA-IZQUIERDA
                if (entrada.equals(NORTE)) return OESTE;  // Arriba -> Izquierda
                if (entrada.equals(OESTE)) return NORTE;  // Izquierda -> Arriba
                break;

            case INF_DER: //inferior derecha (┘): conecta ABAJO-DERECHA
                if (entrada.equals(SUR)) return ESTE;     // Abajo -> Derecha
                if (entrada.equals(ESTE)) return SUR;     // Derecha -> Abajo
                break;

            case INF_IZQ: //inferior izquierda (└): conecta ABAJO-IZQUIERDA
                if (entrada.equals(SUR)) return OESTE;    // Abajo -> Izquierda
                if (entrada.equals(OESTE)) return SUR;    // Izquierda -> Abajo
                break;
        }

        return "DESCARRILAMIENTO";
    }
}