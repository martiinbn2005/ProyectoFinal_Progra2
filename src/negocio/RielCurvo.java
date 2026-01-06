package negocio;

public class RielCurvo extends Rieles {

    // Representamos los 4 tipos de curvas posibles
    public static final String SUP_DER = "SUPERIOR_DERECHA"; // Conecta Norte y Este
    public static final String SUP_IZQ = "SUPERIOR_IZQUIERDA"; // Conecta Norte y Oeste
    public static final String INF_DER = "INFERIOR_DERECHA"; // Conecta Sur y Este
    public static final String INF_IZQ = "INFERIOR_IZQUIERDA"; // Conecta Sur y Oeste

    private String tipoCurva;

    public RielCurvo(int posX, int posY, int orientacion, String tipoCurva) throws Exception{
        super(posX, posY, orientacion);
        this.setTipoCurva(tipoCurva);
    }

    public String getTipoCurva() {
        return tipoCurva;
    }

    public void setTipoCurva(String tipoCurva) throws Exception{
        if (tipoCurva == null) {
            throw new Exception("El tipo de curva no puede ser nulo.");
        }
        this.tipoCurva = tipoCurva;
    }

    @Override
    public String obtenerSalida(String entrada) {
        if (entrada == null) return "DESCARRILAMIENTO";

        switch (this.tipoCurva) {
            case SUP_DER: // Conecta NORTE y ESTE
                if (entrada.equals(NORTE)) return ESTE;
                if (entrada.equals(ESTE)) return NORTE;
                break;

            case SUP_IZQ: // Conecta NORTE y OESTE
                if (entrada.equals(NORTE)) return OESTE;
                if (entrada.equals(OESTE)) return NORTE;
                break;

            case INF_DER: // Conecta SUR y ESTE
                if (entrada.equals(SUR)) return ESTE;
                if (entrada.equals(ESTE)) return SUR;
                break;

            case INF_IZQ: // Conecta SUR y OESTE
                if (entrada.equals(SUR)) return OESTE;
                if (entrada.equals(OESTE)) return SUR;
                break;
        }

        return "DESCARRILAMIENTO";
    }
}