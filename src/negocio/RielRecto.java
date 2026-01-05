package negocio;

public class RielRecto extends Rieles {
    public RielRecto(int posX, int posY, int orientacion) throws Exception {
        super(posX, posY, orientacion);
    }

    @Override
    public String obtenerSalida(String entrada) {
        // Lógica: Si entra por el NORTE, sale por el SUR (y viceversa)
        if (entrada.equals("NORTE")) return "SUR";
        if (entrada.equals("SUR")) return "NORTE";
        return "DESCARRILAMIENTO";
    }
}