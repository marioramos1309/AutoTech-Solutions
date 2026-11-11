/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Mario Ramos
 */

import modelo.Factura;
import modelo.Orden;
import modelo.ConfiguracionFacturacion;
import repositorio.RepositorioGenerico;
import excepciones.PagoInsuficienteException;

import java.util.ArrayList;


public class FacturaControlador {

    
    private final RepositorioGenerico<Factura> repositorioFacturas;

    
    private final ConfiguracionFacturacion configuracion;

    
    public FacturaControlador() {

        repositorioFacturas = new RepositorioGenerico<>(
            Factura::getCodigo
        );

        configuracion = new ConfiguracionFacturacion();
    }

    // Genera la factura y procesa el pago del cliente.
    public Factura procesarFactura(
            String codigo,
            Orden orden,
            double efectivo
    ) throws PagoInsuficienteException {

        if (orden == null) {
            throw new IllegalArgumentException(
                "La orden no puede estar vacía."
            );
        }

        
        if (!"Finalizada".equalsIgnoreCase(orden.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden facturar órdenes finalizadas."
            );
        }

        
        for (Factura registrada : listarFacturas()) {

            if (registrada.getOrden().getCodigo()
                    .equalsIgnoreCase(orden.getCodigo())) {

                throw new IllegalArgumentException(
                    "Esta orden ya tiene una factura registrada."
                );
            }
        }

        
        Factura factura = new Factura(
            codigo,
            orden,
            configuracion.getImpuestoPorcentaje()
        );

        
        factura.registrarPago(efectivo);

        
        repositorioFacturas.agregar(factura);

        return factura;
    }

   
    public double getImpuestoPorcentaje() {
        return configuracion.getImpuestoPorcentaje();
    }

    
    public void setImpuestoPorcentaje(double porcentaje) {
        configuracion.setImpuestoPorcentaje(porcentaje);
    }

    
    public Factura buscarFactura(String codigo) {
        return repositorioFacturas.buscarPorCodigo(codigo);
    }

    
    public ArrayList<Factura> listarFacturas() {
        return repositorioFacturas.listarTodos();
    }

    
    public int cantidadFacturas() {
        return repositorioFacturas.cantidad();
    }
}