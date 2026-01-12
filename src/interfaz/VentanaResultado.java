package interfaz;

import javax.swing.*;
import java.awt.*;
import negocio.Puntaje;
import negocio.Nivel;

/**
 * Ventana que muestra el resultado del intento
 */
public class VentanaResultado extends JFrame {

    private VentanaJuego ventanaJuego;
    private MenuPrincipal menuPrincipal;
    private Puntaje puntaje;
    private Nivel nivel;

    public VentanaResultado(VentanaJuego vj, MenuPrincipal menu, Puntaje p, Nivel n) {
        this.ventanaJuego = vj;
        this.menuPrincipal = menu;
        this.puntaje = p;
        this.nivel = n;

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {
        setTitle("🎉 Resultado del Nivel");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                menuPrincipal.setVisible(true);
            }
        });
    }

    private void crearComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Determinar color de fondo según el puntaje
        Color colorFondo;
        String mensaje;

        if (puntaje.getValorNumerico() == 0) {
            colorFondo = new Color(255, 200, 200); // Rojo claro (falló)
            mensaje = "❌ ¡NIVEL FALLIDO!";
        } else if (puntaje.getEstrellas() == 3) {
            colorFondo = new Color(255, 215, 0, 50); // Dorado claro
            mensaje = "🏆 ¡NIVEL COMPLETADO!";
        } else if (puntaje.getEstrellas() == 2) {
            colorFondo = new Color(192, 192, 192, 50); // Plateado claro
            mensaje = "⭐ ¡NIVEL COMPLETADO!";
        } else {
            colorFondo = new Color(205, 127, 50, 50); // Bronce claro
            mensaje = "✓ ¡NIVEL COMPLETADO!";
        }

        panelPrincipal.setBackground(colorFondo);

        // Título del resultado
        JLabel lblTitulo = new JLabel(mensaje, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (puntaje.getValorNumerico() == 0) {
            lblTitulo.setForeground(new Color(139, 0, 0));
        } else {
            lblTitulo.setForeground(new Color(25, 25, 112));
        }

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(30));

        // Mostrar estrellas
        if (puntaje.getValorNumerico() > 0) {
            JPanel panelEstrellas = crearPanelEstrellas();
            panelPrincipal.add(panelEstrellas);
            panelPrincipal.add(Box.createVerticalStrut(20));
        }

        // Puntaje numérico
        JLabel lblPuntaje = new JLabel("Puntaje: " + puntaje.getValorNumerico() + " / 100",
                SwingConstants.CENTER);
        lblPuntaje.setFont(new Font("Arial", Font.BOLD, 32));
        lblPuntaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (puntaje.getValorNumerico() >= 90) {
            lblPuntaje.setForeground(new Color(0, 128, 0));
        } else if (puntaje.getValorNumerico() >= 60) {
            lblPuntaje.setForeground(new Color(255, 140, 0));
        } else if (puntaje.getValorNumerico() > 0) {
            lblPuntaje.setForeground(new Color(205, 127, 50));
        } else {
            lblPuntaje.setForeground(new Color(139, 0, 0));
        }

        panelPrincipal.add(lblPuntaje);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Descripción del desempeño
        if (puntaje.getValorNumerico() > 0) {
            JLabel lblDescripcion = new JLabel("<html><center>" + puntaje.getDescripcionDesempeño() + "</center></html>");
            lblDescripcion.setFont(new Font("Arial", Font.ITALIC, 16));
            lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblDescripcion.setForeground(new Color(60, 60, 60));

            panelPrincipal.add(lblDescripcion);
        } else {
            JLabel lblFallo = new JLabel("<html><center>El tren no llegó a la meta.<br>¡Revisa tu ruta e inténtalo de nuevo!</center></html>");
            lblFallo.setFont(new Font("Arial", Font.ITALIC, 14));
            lblFallo.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblFallo.setForeground(new Color(139, 0, 0));

            panelPrincipal.add(lblFallo);
        }

        panelPrincipal.add(Box.createVerticalStrut(40));

        // Botones de acción
        JButton btnMenu = crearBoton("🏠 MENÚ PRINCIPAL", new Color(70, 130, 180));

        btnMenu.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        panelPrincipal.add(btnMenu);

        add(panelPrincipal);
    }

    private JPanel crearPanelEstrellas() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int estrellas = puntaje.getEstrellas();

        for (int i = 0; i < 3; i++) {
            JLabel lblEstrella = new JLabel();
            lblEstrella.setFont(new Font("Arial", Font.PLAIN, 50));

            if (i < estrellas) {
                lblEstrella.setText("⭐");
            } else {
                lblEstrella.setText("☆");
                lblEstrella.setForeground(Color.LIGHT_GRAY);
            }

            panel.add(lblEstrella);
        }

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 50));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);

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
}