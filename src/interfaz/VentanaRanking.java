package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import util.RankingManager;
import negocio.Jugador;
import java.util.List;

 // Ventana que muestra el ranking de mejores jugadores
 
public class VentanaRanking extends JFrame {

    private MenuPrincipal menuPrincipal;
    private RankingManager rankingManager;

    public VentanaRanking(MenuPrincipal menu, RankingManager rm) {
        this.menuPrincipal = menu;
        this.rankingManager = rm;

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {
        setTitle("🏆 Ranking de Jugadores");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("TOP 10 MEJORES JUGADORES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(25, 25, 112));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Tabla de ranking
        JTable tabla = crearTablaRanking();
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("✓ CERRAR");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setBackground(new Color(70, 130, 180));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);

        btnCerrar.addActionListener(e -> dispose());

        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(240, 248, 255));
        panelSur.add(btnCerrar);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JTable crearTablaRanking() {
        // Columnas de la tabla
        String[] columnas = {"Posición", "Jugador", "ID", "Mejor Puntaje"};

        // Obtener top 10 jugadores
        List<Jugador> topJugadores = rankingManager.obtenerTopN(10);

        // Crear modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        // Llenar tabla con datos
        int posicion = 1;
        for (Jugador j : topJugadores) {
            int mejorPuntaje = rankingManager.obtenerMejorPuntaje(j.getIdJugador());

            Object[] fila = {
                    posicion + "º",
                    j.getNombre(),
                    j.getIdJugador(),
                    mejorPuntaje
            };

            modelo.addRow(fila);
            posicion++;
        }

        // Si no hay jugadores, mostrar mensaje
        if (topJugadores.isEmpty()) {
            Object[] filaMensaje = {"", "No hay jugadores registrados aún", "", ""};
            modelo.addRow(filaMensaje);
        }

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(70, 130, 180));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(173, 216, 230));

        // Centrar contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // Posición
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Jugador
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120); // Puntaje

        // Renderizador personalizado para la columna de puntaje (con colores)
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (value instanceof Integer) {
                    int puntaje = (Integer) value;

                    if (puntaje >= 90) {
                        c.setForeground(new Color(0, 128, 0)); // Verde
                    } else if (puntaje >= 60) {
                        c.setForeground(new Color(255, 140, 0)); // Naranja
                    } else if (puntaje > 0) {
                        c.setForeground(new Color(205, 127, 50)); // Bronce
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Renderizador para las primeras 3 posiciones (medallas)
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (row == 0) {
                    c.setForeground(new Color(255, 215, 0)); // Oro
                    setFont(new Font("Arial", Font.BOLD, 16));
                } else if (row == 1) {
                    c.setForeground(new Color(192, 192, 192)); // Plata
                    setFont(new Font("Arial", Font.BOLD, 16));
                } else if (row == 2) {
                    c.setForeground(new Color(205, 127, 50)); // Bronce
                    setFont(new Font("Arial", Font.BOLD, 16));
                } else {
                    c.setForeground(Color.BLACK);
                    setFont(new Font("Arial", Font.PLAIN, 14));
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        return tabla;
    }
}