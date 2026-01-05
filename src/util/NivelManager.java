package util;

import negocio.Nivel;
import negocio.Obstaculo;

import java.util.ArrayList;
import java.util.List;


//Responsable de crear y validar niveles (uso del dominio sin interfaz gráfica)

public class NivelManager {

    private List<Nivel> niveles;

    public NivelManager() {
        this.niveles = new ArrayList<>();
    }

    public void crearNivelesPorDefecto() throws Exception {
        this.niveles.clear();
        this.niveles.add(new Nivel(1, "FACIL", 5, 5));
        this.niveles.add(new Nivel(2, "MEDIO", 7, 7));
        this.niveles.add(new Nivel(3, "DIFICIL", 9, 9));
    }

    public List<Nivel> listarNiveles() {
        return new ArrayList<>(niveles);
    }

    public void agregarObstaculo(Nivel nivel, Obstaculo obstaculo) throws Exception {
        if (nivel == null) throw new Exception("Nivel nulo");
        if (obstaculo == null) throw new Exception("Obstáculo nulo");

        nivel.agregarObstaculo(obstaculo);
    }

    public Nivel obtenerNivelPorNumero(int numero) {
        for (Nivel n : niveles) {
            try {
                if (n.getNumeroNivel() == numero) return n;
            } catch (Exception ex) {
                // ignoramos
            }
        }
        return null;
    }
}
