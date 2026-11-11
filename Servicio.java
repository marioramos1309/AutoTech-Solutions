/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class Servicio {

    
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int duracionMinutos;

    
    public Servicio(String codigo, String nombre, String descripcion,
                    double precio, int duracionMinutos) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }

   
    public String getCodigo() {
        return codigo;
    }

    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    public String getNombre() {
        return nombre;
    }

    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    public String getDescripcion() {
        return descripcion;
    }

    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve el precio del servicio.
    public double getPrecio() {
        return precio;
    }

   
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    
    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    
    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

   
    @Override
    public String toString() {
        return nombre;
    }
}
