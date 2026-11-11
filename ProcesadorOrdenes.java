/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hilos;

/**
 *
 * @author Mario Ramos
 */

import controlador.OrdenControlador;
import javax.swing.SwingWorker;


public class ProcesadorOrdenes extends SwingWorker<Void, Void> {

    
    private final OrdenControlador ordenControlador;

    
    private final String codigoOrden;

    
    public ProcesadorOrdenes(
            OrdenControlador ordenControlador,
            String codigoOrden) {

        this.ordenControlador = ordenControlador;
        this.codigoOrden = codigoOrden;
    }

    
    @Override
    protected Void doInBackground() throws Exception {

        // Simulamos un procesamiento de 5 segundos.
        // El progreso se actualiza del 0 al 100.
        for (int progreso = 0; progreso <= 100; progreso++) {

            setProgress(progreso);

            // Pausa de 50 milisegundos.
            Thread.sleep(50);
        }

        // Al terminar el procesamiento, finalizamos la orden.
         ordenControlador.finalizarOrden(codigoOrden);

        return null;
    }
}
