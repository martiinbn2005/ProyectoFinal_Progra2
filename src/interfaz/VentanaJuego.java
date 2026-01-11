package interfaz;

import javax.swing.*;
import java.awt.*;
import util.GameManager;
import negocio.*;

/**
 * Ventana principal donde se juega - contiene el tablero y controles
 */
public class VentanaJuego extends JFrame {

    private MenuPrincipal menuPrincipal;
    private GameManager gameManager;
    private Nivel nivel;
    private Jugador jugador;

    private TableroPanel tableroPanel;
    private JLabel lblTiempo;
    private JLabel lblRieles;
    private JButton btnPlay;
    private JButton btnLimpiar;
    private JButton btnVolver;

    private Timer timerCronometro;
    private long tiempoInicio;

    public VentanaJuego(MenuPrincipal menu, GameManager gm, Nivel nivel, Jugador jugador) {
        this.menuPrincipal = menu;
        this.gameManager = gm;
        this.nivel = nivel;
        this.jugador = jugador;

        configurarVentana();
        crearComponentes();
        iniciarCronometro();
    }

    private void configurarVentana() {
        setTitle("🚂 Train Track Builder - Nivel " + nivel.getNumeroNivel());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                volverAlMenu();
            }
        });
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Panel superior - Información y controles
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central - Tablero de juego
        tableroPanel = new TableroPanel(nivel);
        add(tableroPanel, BorderLayout.CENTER);

        // Panel inferior - Botones de acción
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Información del nivel (izquierda)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(25, 25, 112));

        JLabel lblNivel = new JLabel("Nivel " + nivel.getNumeroNivel() + " - " + nivel.getDificultad().toUpperCase());
        lblNivel.setFont(new Font("Arial", Font.BOLD, 20));
        lblNivel.setForeground(Color.WHITE);

        JLabel lblJugador = new JLabel("Jugador: " + jugador.getNombre());
        lblJugador.setFont(new Font("Arial", Font.PLAIN, 14));
        lblJugador.setForeground(Color.LIGHT_GRAY);

        panelInfo.add(lblNivel);
        panelInfo.add(lblJugador);

        // Estadísticas (derecha)
        JPanel panelStats = new JPanel();
        panelStats.setLayout(new GridLayout(2, 1, 5, 5));
        panelStats.setBackground(new Color(25, 25, 112));

        lblTiempo = new JLabel("⏱ Tiempo: 00:00");
        lblTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiempo.setForeground(Color.YELLOW);

        lblRieles = new JLabel("🛤️ Rieles: 0");
        lblRieles.setFont(new Font("Arial", Font.BOLD, 16));
        lblRieles.setForeground(Color.CYAN);

        panelStats.add(lblTiempo);
        panelStats.add(lblRieles);

        panel.add(panelInfo, BorderLayout.WEST);
        panel.add(panelStats, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón PLAY
        btnPlay = new JButton("▶ PLAY");
        btnPlay.setFont(new Font("Arial", Font.BOLD, 20));
        btnPlay.setBackground(new Color(34, 139, 34));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);
        btnPlay.setPreferredSize(new Dimension(150, 50));
        btnPlay.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPlay.addActionListener(e -> ejecutarSimulacion());

        // Botón LIMPIAR
        btnLimpiar = new JButton("🗑️ LIMPIAR");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 16));
        btnLimpiar.setBackground(new Color(255, 140, 0));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(150, 50));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLimpiar.addActionListener(e -> limpiarTablero());

        // Botón VOLVER
        btnVolver = new JButton("⬅ VOLVER");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
        btnVolver.setBackground(new Color(128, 128, 128));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setPreferredSize(new Dimension(150, 50));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver.addActionListener(e -> volverAlMenu());

        panel.add(btnPlay);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private void iniciarCronometro() {
        tiempoInicio = System.currentTimeMillis();

        timerCronometro = new Timer(100, e -> {
            long transcurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
            int minutos = (int) (transcurrido / 60);
            int segundos = (int) (transcurrido % 60);
            lblTiempo.setText(String.format("⏱ Tiempo: %02d:%02d", minutos, segundos));

            // Actualizar contador de rieles
            lblRieles.setText("🛤️ Rieles: " + nivel.getRieles().size());
        });

        timerCronometro.start();
    }

    private void ejecutarSimulacion() {
        try {
            // Validar que haya rieles colocados
            if (nivel.getRieles().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debes colocar al menos un riel antes de jugar",
                        "Sin rieles", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Detener cronómetro
            if (timerCronometro != null) {
                timerCronometro.stop();
            }

            // Deshabilitar botones durante la simulación
            btnPlay.setEnabled(false);
            btnLimpiar.setEnabled(false);
            btnVolver.setEnabled(false);

            // Ejecutar simulación (playNivel calcula todo internamente)
            Puntaje puntaje = gameManager.playNivel();

            // Mostrar resultado
            new VentanaResultado(this, menuPrincipal, puntaje, nivel).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error durante la simulación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

            // Reactivar botones
            btnPlay.setEnabled(true);
            btnLimpiar.setEnabled(true);
            btnVolver.setEnabled(true);
        }
    }

    private void limpiarTablero() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar todos los rieles colocados?",
                "Confirmar limpieza", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            tableroPanel.limpiarRieles();
            lblRieles.setText("🛤️ Rieles: 0");
        }
    }

    private void volverAlMenu() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Deseas volver al menú principal?\nSe perderá el progreso del nivel actual.",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            if (timerCronometro != null) {
                timerCronometro.stop();
            }
            menuPrincipal.setVisible(true);
            dispose();
        }
    }
}