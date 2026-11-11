/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */
public class Vehiculo {
    
     private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private String color;

    // Cliente propietario del vehículo.
    private cliente propietario;

    // Constructor para crear un vehículo con todos sus datos.
    public Vehiculo(String placa, String marca, String modelo,
                    int anio, String color, cliente propietario) {

        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.propietario = propietario;
    }

    // Devuelve la placa del vehículo.
    public String getPlaca() {
        return placa;
    }

    // Modifica la placa.
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    // Devuelve la marca.
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Devuelve el modelo.
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // Devuelve el año.
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    // Devuelve el color.
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Devuelve el propietario.
    public cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(cliente propietario) {
        this.propietario = propietario;
    }

    // Representación del vehículo como texto.
    @Override
    public String toString() {
        return placa + " - " + marca + " " + modelo;
    }
    
}
