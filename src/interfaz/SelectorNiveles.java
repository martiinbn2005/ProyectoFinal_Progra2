package interfaz;

import javax.swing.*;
import java.awt.*;
import util.GameManager;
import util.NivelManager;
import negocio.Jugador;
import negocio.Nivel;

/**
 * Ventana para seleccionar el nivel a jugar
 */
public class SelectorNiveles extends JFrame {

    private MenuPrincipal menuPrincipal;
    private GameManager gameManager;
    private NivelManager nivelManager;
    private Jugador jugador;

    public SelectorNiveles(MenuPrincipal menu, GameManager gm, NivelManager nm, Jugador j) {
        this.menuPrincipal = menu;
        this.gameManager = gm;
        this.nivelManager = nm;
        this.jugador = j;

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {
        setTitle("🎮 Seleccionar Nivel");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Al cerrar, volver al menú principal
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                menuPrincipal.setVisible(true);
            }
        });
    }

    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Selecciona un Nivel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(25, 25, 112));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Panel central con los 3 niveles
        JPanel panelNiveles = new JPanel();
        panelNiveles.setLayout(new GridLayout(1, 3, 20, 0));
        panelNiveles.setBackground(new Color(240, 248, 255));

        try {
            Nivel nivel1 = nivelManager.obtenerNivelPorNumero(1);
            Nivel nivel2 = nivelManager.obtenerNivelPorNumero(2);
            Nivel nivel3 = nivelManager.obtenerNivelPorNumero(3);

            panelNiveles.add(crearTarjetaNivel(nivel1, "🟢 FÁCIL",
                    "Ideal para principiantes", new Color(34, 139, 34)));
            panelNiveles.add(crearTarjetaNivel(nivel2, "🟡 MEDIO",
                    "Un desafío moderado", new Color(255, 165, 0)));
            panelNiveles.add(crearTarjetaNivel(nivel3, "🔴 DIFÍCIL",
                    "Para expertos", new Color(220, 20, 60)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar niveles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        panelPrincipal.add(panelNiveles, BorderLayout.CENTER);

        // Botón volver
        JButton btnVolver = new JButton("⬅ Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
        btnVolver.setBackground(new Color(128, 128, 128));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);

        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(240, 248, 255));
        panelSur.add(btnVolver);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearTarjetaNivel(Nivel nivel, String titulo, String descripcion, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Título del nivel
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDescripcion.setForeground(Color.GRAY);

        // Información del nivel
        JLabel lblInfo = new JLabel("<html><center>Tamaño: " +
                nivel.getMapaMatriz().length + "x" + nivel.getMapaMatriz()[0].length +
                "</center></html>");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón jugar
        JButton btnJugar = new JButton("JUGAR");
        btnJugar.setFont(new Font("Arial", Font.BOLD, 16));
        btnJugar.setBackground(color);
        btnJugar.setForeground(Color.WHITE);
        btnJugar.setFocusPainted(false);
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.setMaximumSize(new Dimension(150, 40));
        btnJugar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnJugar.setOpaque(true);
        btnJugar.setBorderPainted(false);

        btnJugar.addActionListener(e -> {
            try {
                gameManager.iniciarConstruccion(nivel);
                new VentanaJuego(menuPrincipal, gameManager, nivel, jugador).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al iniciar nivel: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agregar componentes
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(lblDescripcion);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblInfo);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(btnJugar);

        return tarjeta;
    }
}