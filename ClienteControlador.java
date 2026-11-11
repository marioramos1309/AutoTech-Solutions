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
import repositorio.RepositorioGenerico;
import java.util.ArrayList;

public class ClienteControlador {

   
    private final RepositorioGenerico<cliente> repositorio =
            new RepositorioGenerico<>(cliente::getCodigo);

    // Registra un cliente nuevo.
    public boolean registrarCliente(cliente cliente) {

        // Comprobamos que el cliente y sus datos principales sean válidos.
        if (cliente == null
                || cliente.getCodigo() == null
                || cliente.getCodigo().isBlank()
                || cliente.getNombreCompleto() == null
                || cliente.getNombreCompleto().isBlank()) {

            throw new IllegalArgumentException(
                    "El código y el nombre del cliente son obligatorios."
            );
        }

        // El repositorio devuelve false si el código ya está registrado.
        return repositorio.agregar(cliente);
    }

    // Buscamos un cliente utilizando su código
    public cliente buscarCliente(String codigo)
            throws ClienteNoEncontradoException {

        cliente cliente = repositorio.buscarPorCodigo(codigo);

                if (cliente == null) {
            throw new ClienteNoEncontradoException(
                    "No se encontró ningún cliente con el código: " + codigo
            );
        }

        return cliente;
    }

    // Devuelve una lista con todos los clientes registrados
    public ArrayList<cliente> listarClientes() {
        return repositorio.listarTodos();
    }

    // Actualiza los datos de un cliente existente
    public boolean actualizarCliente(String codigo, cliente cliente)
            throws ClienteNoEncontradoException {

        // Primero verificamos que el cliente original exista
        buscarCliente(codigo);

        if (cliente == null
                || cliente.getCodigo() == null
                || cliente.getCodigo().isBlank()
                || cliente.getNombreCompleto() == null
                || cliente.getNombreCompleto().isBlank()) {

            throw new IllegalArgumentException(
                    "El código y el nombre del cliente son obligatorios."
            );
        }

        return repositorio.actualizar(codigo, cliente);
    }

    // Elimina un cliente utilizando su código
    public boolean eliminarCliente(String codigo)
            throws ClienteNoEncontradoException {

        // Verificamos si existe el cliente
        buscarCliente(codigo);

        return repositorio.eliminar(codigo);
    }

    
    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    // Devuelve la cantidad actual de clientes registrados.
    public int cantidadClientes() {
        return repositorio.cantidad();
    }
}
