package negocio;

public class Piedra extends Obstaculo {

    public Piedra(int posX, int posY) throws Exception {
        super(posX, posY, "Piedra de granito");
    }

    @Override
    public String getTipoEfecto() {
        return "BLOQUEO TOTAL"; // El tren choca inmediatamente
    }
}