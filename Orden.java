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
import java.util.ArrayList;
import java.util.List;

public class Orden {

   
    private String codigo;
    private cliente cliente;
    private Vehiculo vehiculo;

    
    private List<DetalleServicio> servicios;
    private List<DetalleRepuesto> repuestos;

    
    private LocalDate fecha;
    private String estado;
    private double total;

    
    public Orden(String codigo, cliente cliente, Vehiculo vehiculo) {

        this.codigo = codigo;
        this.cliente = cliente;
        this.vehiculo = vehiculo;

        
        this.servicios = new ArrayList<>();
        this.repuestos = new ArrayList<>();

        // Registramos automáticamente la fecha.
        this.fecha = LocalDate.now();

       
        this.estado = "Pendiente";

        this.total = 0;
    }

    
    public String getCodigo() {
        return codigo;
    }

    
    public cliente getCliente() {
        return cliente;
    }

    
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    
    public List<DetalleServicio> getServicios() {
        return servicios;
    }

    
    public List<DetalleRepuesto> getRepuestos() {
        return repuestos;
    }

    
    public LocalDate getFecha() {
        return fecha;
    }

    
    public String getEstado() {
        return estado;
    }

    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    public void agregarServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede estar vacío."
            );
        }

        
        DetalleServicio detalle = new DetalleServicio(servicio);

        servicios.add(detalle);

        calcularTotal();
    }

    
    public void agregarRepuesto(DetalleRepuesto detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle del repuesto no puede estar vacío."
            );
        }

        repuestos.add(detalle);

        calcularTotal();
    }

   
    public void calcularTotal() {

        total = 0;

        
        for (DetalleServicio detalle : servicios) {
            total += detalle.getPrecioUnitario();
        }

        
        for (DetalleRepuesto detalle : repuestos) {
            total += detalle.calcularSubtotal();
        }
    }

    
    public double getTotal() {
        return total;
    }
}