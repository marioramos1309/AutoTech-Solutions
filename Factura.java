/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */

import java.time.LocalDate;
import excepciones.PagoInsuficienteException;


public class Factura {

   
    private String codigo;
    private Orden orden;
    private LocalDate fecha;
    private double subtotal;
    private double impuestoPorcentaje;
    private double montoImpuesto;
    private double total;
    private double pagoRecibido;
    private double cambio;

    
    public Factura(String codigo, Orden orden, double impuestoPorcentaje) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de factura es obligatorio.");
        }

        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede estar vacía.");
        }

        if (impuestoPorcentaje < 0) {
            throw new IllegalArgumentException("El impuesto no puede ser negativo.");
        }

        this.codigo = codigo;
        this.orden = orden;
        this.fecha = LocalDate.now();
        this.impuestoPorcentaje = impuestoPorcentaje;

        
        this.subtotal = orden.getTotal();

        
        calcularTotales();
    }

    // Calcula el impuesto y el total de la factura.
    public void calcularTotales() {

        montoImpuesto = subtotal * (impuestoPorcentaje / 100);

        total = subtotal + montoImpuesto;

        
        montoImpuesto = Math.round(montoImpuesto * 100.0) / 100.0;
        total = Math.round(total * 100.0) / 100.0;
    }

    
    public void registrarPago(double pagoRecibido)
            throws PagoInsuficienteException {

        if (pagoRecibido < total) {
            throw new PagoInsuficienteException(
                "Pago insuficiente. El total es L. "
                + total + " y el cliente entregó L. " + pagoRecibido
            );
        }

        if (pagoRecibido < 0) {
            throw new IllegalArgumentException("El pago no puede ser negativo.");
        }

        this.pagoRecibido = pagoRecibido;

        // Calculamos el cambio que debemos entregar.
        this.cambio = pagoRecibido - total;

        this.cambio = Math.round(cambio * 100.0) / 100.0;
    }

    // Métodos para consultar los datos de la factura.
    public String getCodigo() {
        return codigo;
    }

    public Orden getOrden() {
        return orden;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestoPorcentaje() {
        return impuestoPorcentaje;
    }

    public double getMontoImpuesto() {
        return montoImpuesto;
    }

    public double getTotal() {
        return total;
    }

    public double getPagoRecibido() {
        return pagoRecibido;
    }

    public double getCambio() {
        return cambio;
    }
}
