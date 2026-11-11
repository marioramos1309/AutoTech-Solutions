/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.autotechsoluctions.autotechsolutions;

/**
 *
 * @author Mario Ramos
 */

import vista.VentanaPrincipal;

public class AutoTechSolutions {

    public static void main(String[] args) {
        
        // Ejecutamos la interfaz gráfica en el hilo de Swing.
        javax.swing.SwingUtilities.invokeLater(() -> {

            // Creamos la ventana principal del ERP.
            VentanaPrincipal ventana = new VentanaPrincipal();

            // Hacemos visible nuestra ventana.
            ventana.setVisible(true);
            
        });
    }
}
