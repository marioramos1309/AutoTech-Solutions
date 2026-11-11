/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class DetalleServicio {

    private Servicio servicio;
    private double precioUnitario;


    public DetalleServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un servicio."
            );
        }

        this.servicio = servicio;

        
        this.precioUnitario = servicio.getPrecio();
    }

    
    public Servicio getServicio() {
        return servicio;
    }

    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
}