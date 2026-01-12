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
            case SUP_DER: //conecta norte y este
                if (entrada.equals(NORTE)) return ESTE;
                if (entrada.equals(ESTE)) return NORTE;
                break;

            case SUP_IZQ: //conecta norte y oeste
                if (entrada.equals(NORTE)) return OESTE;
                if (entrada.equals(OESTE)) return NORTE;
                break;

            case INF_DER: //conecta sur y este
                if (entrada.equals(SUR)) return ESTE;
                if (entrada.equals(ESTE)) return SUR;
                break;

            case INF_IZQ: //conecta sur y oeste
                if (entrada.equals(SUR)) return OESTE;
                if (entrada.equals(OESTE)) return SUR;
                break;
        }

        return "DESCARRILAMIENTO";
    }
}