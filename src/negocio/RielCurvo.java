package negocio;

public class RielCurvo extends Rieles {
    public RielCurvo(int posX, int posY, int orientacion) throws Exception {
        super(posX, posY, orientacion);
    }

    @Override
    public String obtenerSalida(String entrada) {
        // Lógica: Si entra por el NORTE, sale por el ESTE (ejemplo de curva a 90°)
        if (entrada.equals("NORTE")) return "ESTE";
        if (entrada.equals("ESTE")) return "NORTE";
        return "DESCARRILAMIENTO";
    }
}
