/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class cliente {
    
    private String codigo;
    private String nombreCompleto;
    private String identidad;
    private String telefono;
    private String direccion;
    
    
    public cliente(String codigo,
                    String nombreCompleto,
                    String identidad,
                    String telefono,
                    String direccion) {

        this.codigo = codigo;
        this.nombreCompleto = nombreCompleto;
        this.identidad = identidad;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Métodos Get: permiten consultar los atributos.

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getIdentidad() {
        return identidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
    
    
}
