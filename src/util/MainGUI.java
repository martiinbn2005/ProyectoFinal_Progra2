package util;

import interfaz.MenuPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//clase principal punto de entrada del programa

public class MainGUI {

    public static void main(String[] args) {
        //configurar look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //si falla, usar el look and feel por defecto
            System.out.println("No se pudo establecer el Look and Feel del sistema");
        }

        //ejecutar la interfaz gráfica en el event dispatch thread
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