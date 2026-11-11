/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class DetalleRepuesto {
    
    private Repuesto repuesto;
    private int cantidad;
    private double precioUnitario;

 
    public DetalleRepuesto(Repuesto repuesto, int cantidad) {

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un repuesto."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.repuesto = repuesto;
        this.cantidad = cantidad;

        
        this.precioUnitario = repuesto.getPrecioConDescuento();
    }

    // Devuelve el repuesto seleccionado.
    public Repuesto getRepuesto() {
        return repuesto;
    }

   
    public int getCantidad() {
        return cantidad;
    }

    
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    
    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }
    
}
