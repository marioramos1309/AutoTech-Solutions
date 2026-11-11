/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Mario Ramos
 */

import modelo.Servicio;
import repositorio.RepositorioGenerico;

import java.util.ArrayList;

public class ServicioControlador {
    
     private final RepositorioGenerico<Servicio> repositorio =
            new RepositorioGenerico<>(Servicio::getCodigo);

    // Registra un servicio nuevo.
    public boolean registrarServicio(Servicio servicio) {

        // Evitamos registrar un servicio sin código.
        if (servicio.getCodigo() == null ||
                servicio.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código del servicio es obligatorio."
            );
        }

        // Evitamos registrar servicios sin nombre.
        if (servicio.getNombre() == null ||
                servicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del servicio es obligatorio."
            );
        }

        
        if (servicio.getPrecio() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero."
            );
        }

        
        if (servicio.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero."
            );
        }

        return repositorio.agregar(servicio);
    }

    
    public Servicio buscarServicio(String codigo) {

        Servicio servicio = repositorio.buscarPorCodigo(codigo);

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "No se encontró un servicio con ese código."
            );
        }

        return servicio;
    }

    
    public ArrayList<Servicio> listarServicios() {
        return repositorio.listarTodos();
    }

    
    public boolean actualizarServicio(
            String codigo, Servicio servicio) {

        
        if (repositorio.buscarPorCodigo(codigo) == null) {
            throw new IllegalArgumentException(
                    "No existe el servicio que deseas modificar."
            );
        }

        
        servicio.setCodigo(codigo);

        if (servicio.getNombre() == null ||
                servicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del servicio es obligatorio."
            );
        }

        if (servicio.getPrecio() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero."
            );
        }

        if (servicio.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero."
            );
        }

        return repositorio.actualizar(codigo, servicio);
    }

        public boolean eliminarServicio(String codigo) {

        if (repositorio.buscarPorCodigo(codigo) == null) {
            throw new IllegalArgumentException(
                    "No existe el servicio que deseas eliminar."
            );
        }

        return repositorio.eliminar(codigo);
    }

    
    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    
    public int cantidadServicios() {
        return repositorio.cantidad();
    }
    
}
