package negocio;

public class RielRecto extends Rieles {
    public RielRecto(int posX, int posY, int orientacion) throws Exception {
        super(posX, posY, orientacion);
    }

    @Override
    public String obtenerSalida(String entrada) {
        //interpretación según la orientación: orientacion 0/180 -> vertical (NORTE<->SUR) orientacion 90/270 -> horizontal (ESTE<->OESTE)
        int orient = getOrientacion() % 360;
        if (orient < 0) orient += 360;

        if (orient == 0 || orient == 180) {
            //riel vertical: Arriba-Abajo
            if (entrada.equals("NORTE")) return "SUR";
            if (entrada.equals("SUR")) return "NORTE";
            return "DESCARRILAMIENTO";
        } else {
            //riel horizontal: Izquierda-Derecha (OESTE<->ESTE)
            if (entrada.equals("ESTE")) return "OESTE";
            if (entrada.equals("OESTE")) return "ESTE";
            return "DESCARRILAMIENTO";
        }
    }
}