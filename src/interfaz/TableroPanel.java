package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import negocio.*;

//panel que muestra el tablero del juego y permite colocar rieles
public class TableroPanel extends JPanel {

    private Nivel nivel;
    private JButton[][] botonesCeldas;
    private int filas;
    private int columnas;

    //colores exactos como aparecen en la leyenda
    private final Color COLOR_VACIO = new Color(245, 245, 245);
    private final Color COLOR_INICIO = new Color(46, 125, 50);      //verde de la leyenda
    private final Color COLOR_FIN = new Color(198, 40, 40);         //rojo de la leyenda
    private final Color COLOR_OBSTACULO = new Color(97, 97, 97);    //gris de la leyenda
    private final Color COLOR_RIEL = new Color(25, 118, 210);       //azul de la leyenda

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
        gridPanel.setLayout(new GridLayout(filas, columnas, 3, 3));
        gridPanel.setBackground(new Color(100, 100, 100));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

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

        // Panel de leyenda
        JPanel leyenda = crearLeyenda();
        add(leyenda, BorderLayout.SOUTH);
    }

    private JButton crearCelda(int fila, int col, int tipoCelda) {
        JButton celda = new JButton();
        celda.setFocusPainted(false);
        celda.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        celda.setOpaque(true);
        celda.setBorderPainted(true);
        celda.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        celda.setPreferredSize(new Dimension(70, 70));

        // Configurar según el tipo de celda
        switch (tipoCelda) {
            case 0: // Vacío - puede colocar rieles
                celda.setBackground(COLOR_VACIO);
                celda.setForeground(Color.BLACK);
                celda.setText("");
                celda.setEnabled(true);

                // Acción: abrir menú de rieles
                final int f = fila;
                final int c = col;
                celda.addActionListener(e -> mostrarMenuRieles(f, c, celda));
                break;

            case 1: // Estación inicio
                celda.setBackground(COLOR_INICIO);
                celda.setForeground(Color.WHITE);
                celda.setText("🚂");
                celda.setEnabled(false);
                celda.setDisabledIcon(null);
                break;

            case 2: // Estación fin
                celda.setBackground(COLOR_FIN);
                celda.setForeground(Color.WHITE);
                celda.setText("🏁");
                celda.setEnabled(false);
                celda.setDisabledIcon(null);
                break;

            case 3: // Obstáculo
                celda.setBackground(COLOR_OBSTACULO);
                celda.setForeground(Color.WHITE);
                celda.setText("🪨");
                celda.setEnabled(false);
                celda.setDisabledIcon(null);
                break;
        }

        return celda;
    }

    private void mostrarMenuRieles(int fila, int col, JButton celda) {
        // Verificar si ya hay un riel colocado
        Rieles rielExistente = nivel.obtenerRielEn(fila, col);
        if (rielExistente != null) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Ya hay un riel en esta posición.\n¿Deseas eliminarlo?",
                    "Riel existente", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                eliminarRiel(fila, col, celda);
            }
            return;
        }

        // Mostrar menú de opciones de rieles
        String[] opciones = {
                "━ Riel Recto Horizontal",
                "┃ Riel Recto Vertical",
                "┐ Curva Superior Derecha",
                "┌ Curva Superior Izquierda",
                "└ Curva Inferior Derecha",
                "┘ Curva Inferior Izquierda",
                "Cancelar"
        };

        int seleccion = JOptionPane.showOptionDialog(this,
                "Selecciona el tipo de riel:",
                "Colocar Riel en (" + fila + "," + col + ")",
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
                case 0: // Horizontal
                    nuevoRiel = new RielRecto(fila, col, 90);
                    simbolo = "━";
                    break;
                case 1: // Vertical
                    nuevoRiel = new RielRecto(fila, col, 0);
                    simbolo = "┃";
                    break;
                case 2: // Curva Superior Derecha
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.SUP_DER);
                    simbolo = "┐";
                    break;
                case 3: // Curva Superior Izquierda
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.SUP_IZQ);
                    simbolo = "┌";
                    break;
                case 4: // Curva Inferior Derecha
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.INF_DER);
                    simbolo = "└";
                    break;
                case 5: // Curva Inferior Izquierda
                    nuevoRiel = new RielCurvo(fila, col, 0, RielCurvo.INF_IZQ);
                    simbolo = "┘";
                    break;
            }

            // Intentar agregar el riel al nivel
            nivel.agregarRiel(nuevoRiel);

            // Actualizar visualmente la celda
            celda.setBackground(COLOR_RIEL);
            celda.setForeground(Color.WHITE);
            celda.setText(simbolo);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se puede colocar el riel:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRiel(int fila, int col, JButton celda) {
        // Usar el nuevo método eliminarRiel() de Nivel
        boolean eliminado = nivel.eliminarRiel(fila, col);

        if (eliminado) {
            // Restaurar visualmente la celda
            celda.setBackground(COLOR_VACIO);
            celda.setForeground(Color.BLACK);
            celda.setText("");
        }
    }

    public void limpiarRieles() {
        // Usar el nuevo método limpiarRieles() de Nivel
        nivel.limpiarRieles();

        // Restaurar visualmente todas las celdas vacías
        int[][] mapa = nivel.getMapaMatriz();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] == 0) { // Solo celdas vacías
                    botonesCeldas[i][j].setBackground(COLOR_VACIO);
                    botonesCeldas[i][j].setForeground(Color.BLACK);
                    botonesCeldas[i][j].setText("");
                }
            }
        }
    }

    private JPanel crearLeyenda() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel.setBackground(new Color(240, 248, 255));

        panel.add(crearItemLeyenda("Inicio", COLOR_INICIO));
        panel.add(crearItemLeyenda("Meta", COLOR_FIN));
        panel.add(crearItemLeyenda("Obstáculo", COLOR_OBSTACULO));
        panel.add(crearItemLeyenda("Riel", COLOR_RIEL));

        return panel;
    }

    private JPanel crearItemLeyenda(String texto, Color color) {
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setBackground(new Color(240, 248, 255));

        JPanel cuadro = new JPanel();
        cuadro.setPreferredSize(new Dimension(30, 30));
        cuadro.setBackground(color);
        cuadro.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cuadro.setOpaque(true);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        etiqueta.setForeground(Color.BLACK);

        item.add(cuadro);
        item.add(etiqueta);

        return item;
    }
}