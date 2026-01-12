package util;

import interfaz.MenuPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

 //Clase principal - Punto de entrada del programa
public class MainGUI {

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usar el Look and Feel por defecto
            System.out.println("No se pudo establecer el Look and Feel del sistema");
        }

        // Ejecutar la interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}