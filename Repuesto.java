/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class Repuesto {
        
    private String codigo;
    private String nombre;
    private String descripcion;
    
    private int cantidadStock;
    private int stockMinimo;

    private double precioUnitario;
    private double descuentoPorcentaje;

    
    public Repuesto(String codigo, String nombre, String descripcion,
                    int cantidadStock, int stockMinimo,
                    double precioUnitario, double descuentoPorcentaje) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
        this.stockMinimo = stockMinimo;
        this.precioUnitario = precioUnitario;
        this.descuentoPorcentaje = descuentoPorcentaje;
    }
    
      /*Constructor de copia.
     Permite crear un repuesto independiente con los mismos datos.*/
    public Repuesto(Repuesto otro) {

        this.codigo = otro.codigo;
        this.nombre = otro.nombre;
        this.descripcion = otro.descripcion;
        this.cantidadStock = otro.cantidadStock;
        this.stockMinimo = otro.stockMinimo;
        this.precioUnitario = otro.precioUnitario;
        this.descuentoPorcentaje = otro.descuentoPorcentaje;
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

    
    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    
    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    
    public double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje(double descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    
    public double getPrecioConDescuento() {

        return precioUnitario -
                (precioUnitario * descuentoPorcentaje / 100);
    }

    
    public boolean necesitaReposicion() {

        return cantidadStock <= stockMinimo;
    }

    
    @Override
    public String toString() {
        return nombre;
    }
    
}
