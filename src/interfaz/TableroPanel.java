package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import negocio.*;

/**
 * Panel que muestra el tablero del juego y permite colocar rieles
 */
public class TableroPanel extends JPanel {

    private Nivel nivel;
    private JButton[][] botonesCeldas;
    private int filas;
    private int columnas;

    //colores
    private final Color COLOR_VACIO = Color.WHITE;
    private final Color COLOR_INICIO = new Color(34, 139, 34);
    private final Color COLOR_FIN = new Color(220, 20, 60);
    private final Color COLOR_OBSTACULO = new Color(128, 128, 128);
    private final Color COLOR_RIEL = new Color(70, 130, 180);

    public TableroPanel(Nivel nivel) {
        this.nivel = nivel;
        this.filas = nivel.getMapaMatriz().length;
        this.columnas = nivel.getMapaMatriz()[0].length;

        configurarPanel();
        crearTablero();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void crearTablero() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(filas, columnas, 2, 2));
        gridPanel.setBackground(new Color(200, 200, 200));

        botonesCeldas = new JButton[filas][columnas];
        int[][] mapa = nivel.getMapaMatriz();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JButton celda = crearCelda(i, j, mapa[i][j]);
                botonesCeldas[i][j] = celda;
                gridPanel.add(celda);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        //panel de leyenda
        JPanel leyenda = crearLeyenda();
        add(leyenda, BorderLayout.SOUTH);
    }

    private JButton crearCelda(int fila, int col, int tipoCelda) {
        JButton celda = new JButton();
        celda.setFocusPainted(false);
        celda.setFont(new Font("Arial", Font.BOLD, 20));

        //configurar según el tipo de celda
        switch (tipoCelda) {
            case 0: //vacío - puede colocar rieles
                celda.setBackground(COLOR_VACIO);
                celda.setText("");
                celda.setEnabled(true);

                //acción: abrir menú de rieles
                final int f = fila;
                final int c = col;
                celda.addActionListener(e -> mostrarMenuRieles(f, c, celda));
                break;

            case 1: //estación inicio
                celda.setBackground(COLOR_INICIO);
                celda.setText("🚂");
                celda.setEnabled(false);
                break;

            case 2: //estación fin
                celda.setBackground(COLOR_FIN);
                celda.setText("🏁");
                celda.setEnabled(false);
                break;

            case 3: //obstáculo
                celda.setBackground(COLOR_OBSTACULO);
                celda.setText("🪨");
                celda.setEnabled(false);
                break;
        }

        return celda;
    }

    private void mostrarMenuRieles(int fila, int col, JButton celda) {
        //verificar si ya hay un riel colocado
        Rieles rielExistente = nivel.obtenerRielEn(fila, col);
        if (rielExistente != null) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "ya hay un riel en esta posición.\n¿deseas eliminarlo?",
                    "riel existente", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                eliminarRiel(fila, col, celda);
            }
            return;
        }

        //mostrar menú de opciones de rieles
        String[] opciones = {
                "━ riel recto horizontal",
                "┃ riel recto vertical",
                "┐ curva superior derecha",
                "┌ curva superior izquierda",
                "└ curva inferior derecha",
                "┘ curva inferior izquierda",
                "cancelar"
        };

        int seleccion = JOptionPane.showOptionDialog(this,
                "selecciona el tipo de riel:",
                "colocar riel en (" + fila + "," + col + ")",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion >= 0 && seleccion < 6) {
            colocarRiel(fila, col, seleccion, celda);
        }
    }

    private void colocarRiel(int fila, int col, int tipoRiel, JButton celda) {
        try {
            Rieles nuevoRiel = null;
            String simbolo = "";

            switch (tipoRiel) {
                case 0: //horizontal
                    nuevoRiel = new RielRecto(fila, col, 90);
                    simbolo = "━";
                    break;
                case 1: //vertical
                    nuevoRiel = new RielRecto(fila, col, 0);
                    simbolo = "┃";
                    break;
                case 2: //curva superior derecha
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.SUP_DER);
                    simbolo = "┐";
                    break;
                case 3: //curva superior izquierda
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.SUP_IZQ);
                    simbolo = "┌";
                    break;
                case 4: //curva inferior derecha
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.INF_DER);
                    simbolo = "└";
                    break;
                case 5: //curva inferior izquierda
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.INF_IZQ);
                    simbolo = "┘";
                    break;
            }

            //intentar agregar el riel al nivel
            nivel.agregarRiel(nuevoRiel);

            //actualizar visualmente la celda
            celda.setBackground(COLOR_RIEL);
            celda.setText(simbolo);
            celda.setForeground(Color.WHITE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "no se puede colocar el riel:\n" + ex.getMessage(),
                    "error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRiel(int fila, int col, JButton celda) {
        //buscar y eliminar el riel de la lista
        Rieles rielAEliminar = null;
        for (Rieles r : nivel.getRieles()) {
            if (r.getPosX() == fila && r.getPosY() == col) {
                rielAEliminar = r;
                break;
            }
        }

        if (rielAEliminar != null) {
            nivel.getRieles().remove(rielAEliminar);

            //restaurar visualmente la celda
            celda.setBackground(COLOR_VACIO);
            celda.setText("");
        }
    }

    public void limpiarRieles() {
        // Eliminar todos los rieles del nivel
        nivel.getRieles().clear();

        // Restaurar visualmente todas las celdas vacías
        int[][] mapa = nivel.getMapaMatriz();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] == 0) { // Solo celdas vacías
                    botonesCeldas[i][j].setBackground(COLOR_VACIO);
                    botonesCeldas[i][j].setText("");
                }
            }
        }
    }

    private JPanel crearLeyenda() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panel.setBackground(new Color(240, 248, 255));

        panel.add(crearItemLeyenda("🚂 Inicio", COLOR_INICIO));
        panel.add(crearItemLeyenda("🏁 Meta", COLOR_FIN));
        panel.add(crearItemLeyenda("🪨 Obstáculo", COLOR_OBSTACULO));
        panel.add(crearItemLeyenda("🛤️ Riel", COLOR_RIEL));

        return panel;
    }

    private JPanel crearItemLeyenda(String texto, Color color) {
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(new Color(240, 248, 255));

        JPanel cuadro = new JPanel();
        cuadro.setPreferredSize(new Dimension(20, 20));
        cuadro.setBackground(color);
        cuadro.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 12));

        item.add(cuadro);
        item.add(etiqueta);

        return item;
    }
}