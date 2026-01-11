package interfaz;

import javax.swing.*;
import java.awt.*;
import util.GameManager;
import util.NivelManager;
import util.RankingManager;
import negocio.Juego;
import negocio.Jugador;

/**
 * Ventana principal del juego - Menú de inicio
 */
public class MenuPrincipal extends JFrame {

    private GameManager gameManager;
    private NivelManager nivelManager;
    private RankingManager rankingManager;
    private Juego juego;
    private Jugador jugadorActual;

    public MenuPrincipal() {
        inicializarJuego();
        configurarVentana();
        crearComponentes();
    }

    private void inicializarJuego() {
        try {
            nivelManager = new NivelManager();
            rankingManager = new RankingManager();
            juego = new Juego();
            gameManager = new GameManager(juego, rankingManager);

            // Crear los 3 niveles por defecto
            nivelManager.crearNivelesPorDefecto();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar el juego: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarVentana() {
        setTitle("🚂 Train Track Builder - Menú Principal");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Título
        JLabel titulo = new JLabel("🚂 TRAIN TRACK BUILDER");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(new Color(25, 25, 112));

        // Subtítulo
        JLabel subtitulo = new JLabel("Construye la ruta perfecta");
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setForeground(new Color(70, 130, 180));

        // Espacio
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(subtitulo);
        panelPrincipal.add(Box.createVerticalStrut(40));

        // Panel para nombre del jugador
        JPanel panelNombre = new JPanel();
        panelNombre.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelNombre.setBackground(new Color(240, 248, 255));
        panelNombre.setMaximumSize(new Dimension(500, 50));

        JLabel lblNombre = new JLabel("Tu nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField txtNombre = new JTextField(15);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));

        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);

        panelPrincipal.add(panelNombre);
        panelPrincipal.add(Box.createVerticalStrut(30));

        // Botones del menú
        JButton btnJugar = crearBoton("▶ JUGAR", new Color(34, 139, 34));
        JButton btnInstrucciones = crearBoton("📖 INSTRUCCIONES", new Color(70, 130, 180));
        JButton btnRanking = crearBoton("🏆 RANKING", new Color(255, 140, 0));
        JButton btnSalir = crearBoton("❌ SALIR", new Color(220, 20, 60));

        // Acciones de los botones
        btnJugar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, ingresa tu nombre para continuar",
                        "Nombre requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Crear jugador con ID único basado en timestamp
                int id = (int) (System.currentTimeMillis() % 100000);
                jugadorActual = new Jugador(id, nombre);
                gameManager.iniciarPartida(jugadorActual);

                // Abrir selector de niveles
                new SelectorNiveles(this, gameManager, nivelManager, jugadorActual).setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al crear jugador: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnInstrucciones.addActionListener(e -> mostrarInstrucciones());

        btnRanking.addActionListener(e -> {
            new VentanaRanking(this, rankingManager).setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas salir?",
                    "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Agregar botones al panel
        panelPrincipal.add(btnJugar);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnInstrucciones);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnRanking);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnSalir);

        add(panelPrincipal);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(350, 50));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    private void mostrarInstrucciones() {
        String instrucciones =
                "🎯 OBJETIVO DEL JUEGO\n\n" +
                        "Construye una ruta de rieles desde la estación de INICIO (izquierda)\n" +
                        "hasta la estación de FIN (derecha) para que el tren llegue a salvo.\n\n" +
                        "🛤️ CÓMO JUGAR\n\n" +
                        "1. Selecciona un nivel (Fácil, Medio o Difícil)\n" +
                        "2. Haz clic en una celda vacía del tablero\n" +
                        "3. Elige el tipo de riel que deseas colocar:\n" +
                        "   • Riel Recto Horizontal ━\n" +
                        "   • Riel Recto Vertical ┃\n" +
                        "   • Rieles Curvos ┐ ┌ └ ┘\n" +
                        "4. Conecta la entrada con la salida evitando obstáculos 🪨\n" +
                        "5. Presiona el botón PLAY ▶ para ver si tu ruta funciona\n\n" +
                        "⭐ PUNTUACIÓN\n\n" +
                        "• Usa MENOS rieles → Mejor puntaje\n" +
                        "• Construye MÁS RÁPIDO → Mejor puntaje\n" +
                        "• Los primeros 10 segundos no se penalizan\n" +
                        "• Obtén hasta 3 estrellas según tu desempeño\n\n" +
                        "❌ RESTRICCIONES\n\n" +
                        "• No puedes colocar rieles sobre obstáculos\n" +
                        "• No puedes colocar rieles sobre estaciones\n" +
                        "• No puedes solapar rieles\n\n" +
                        "¡Buena suerte, ingeniero! 🚂";

        JTextArea textArea = new JTextArea(instrucciones);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "📖 Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }
}