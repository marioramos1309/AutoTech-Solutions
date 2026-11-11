/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Mario Ramos
 */

import excepciones.ClienteNoEncontradoException;
import modelo.cliente;
import modelo.Vehiculo;
import repositorio.RepositorioGenerico;

import java.util.ArrayList;

public class VehiculoControlador {

   
    private final RepositorioGenerico<Vehiculo> repositorio =
            new RepositorioGenerico<>(Vehiculo::getPlaca);

     private final ClienteControlador clienteControlador;

   
    public VehiculoControlador(ClienteControlador clienteControlador) {
        this.clienteControlador = clienteControlador;
    }

    // Registra un vehículo nuevo.
    public boolean registrarVehiculo(Vehiculo vehiculo)
            throws ClienteNoEncontradoException {

        validarVehiculo(vehiculo);

                cliente propietario = clienteControlador.buscarCliente(
                vehiculo.getPropietario().getCodigo()
        );

      
        vehiculo.setPropietario(propietario);

        
        return repositorio.agregar(vehiculo);
    }

    // buscamos un vehiculo por su placaa
    public Vehiculo buscarVehiculo(String placa) {

        Vehiculo vehiculo = repositorio.buscarPorCodigo(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "No se encontró ningún vehículo con la placa: " + placa
            );
        }

        return vehiculo;
    }

    
    public ArrayList<Vehiculo> listarVehiculos() {
        return repositorio.listarTodos();
    }

    
    public boolean actualizarVehiculo(String placa, Vehiculo nuevoVehiculo)
            throws ClienteNoEncontradoException {

        
        buscarVehiculo(placa);

        validarVehiculo(nuevoVehiculo);

        
        cliente propietario = clienteControlador.buscarCliente(
                nuevoVehiculo.getPropietario().getCodigo()
        );

        nuevoVehiculo.setPropietario(propietario);

        return repositorio.actualizar(placa, nuevoVehiculo);
    }

    // Elimina un vehículo por su placa.
    public boolean eliminarVehiculo(String placa) {

        
        buscarVehiculo(placa);

        return repositorio.eliminar(placa);
    }

   
    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    
    public int cantidadVehiculos() {
        return repositorio.cantidad();
    }

    
    private void validarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null
                || vehiculo.getPlaca() == null
                || vehiculo.getPlaca().isBlank()
                || vehiculo.getMarca() == null
                || vehiculo.getMarca().isBlank()
                || vehiculo.getModelo() == null
                || vehiculo.getModelo().isBlank()
                || vehiculo.getColor() == null
                || vehiculo.getColor().isBlank()
                || vehiculo.getAnio() <= 0
                || vehiculo.getPropietario() == null
                || vehiculo.getPropietario().getCodigo() == null
                || vehiculo.getPropietario().getCodigo().isBlank()) {

            throw new IllegalArgumentException(
                    "Todos los datos del vehículo son obligatorios."
            );
        }
    }
}
