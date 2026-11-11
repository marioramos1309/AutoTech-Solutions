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
import excepciones.StockInsuficienteException;

import modelo.Orden;
import modelo.cliente;
import modelo.Vehiculo;
import modelo.Servicio;
import modelo.Repuesto;
import modelo.DetalleRepuesto;

import repositorio.RepositorioGenerico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Controlador encargado de administrar las órdenes de trabajo.
public class OrdenControlador {

    // Repositorio donde almacenaremos las órdenes.
    private final RepositorioGenerico<Orden> repositorio;

    // Controladores de los demás módulos.
    private final ClienteControlador clienteControlador;
    private final VehiculoControlador vehiculoControlador;
    private final ServicioControlador servicioControlador;
    private final InventarioControlador inventarioControlador;

    // Constructor que recibe los controladores necesarios.
    public OrdenControlador(
            ClienteControlador clienteControlador,
            VehiculoControlador vehiculoControlador,
            ServicioControlador servicioControlador,
            InventarioControlador inventarioControlador) {

        this.clienteControlador = clienteControlador;
        this.vehiculoControlador = vehiculoControlador;
        this.servicioControlador = servicioControlador;
        this.inventarioControlador = inventarioControlador;

        // Inicializamos el repositorio utilizando el código de la orden.
        repositorio = new RepositorioGenerico<>(Orden::getCodigo);
    }

    // Registra una nueva orden.
    public void registrarOrden(Orden orden) {

        if (orden == null) {
            throw new IllegalArgumentException(
                    "La orden no puede estar vacía."
            );
        }

        if (orden.getCodigo() == null
                || orden.getCodigo().isBlank()) {

            throw new IllegalArgumentException(
                    "El código de la orden es obligatorio."
            );
        }

        // Comprobamos que el cliente exista.
        if (orden.getCliente() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar un cliente."
            );
        }

        try {

            cliente propietario = clienteControlador.buscarCliente(
                    orden.getCliente().getCodigo()
            );

            if (propietario == null) {
                throw new IllegalArgumentException(
                        "El cliente no existe."
                );
            }

        } catch (ClienteNoEncontradoException e) {

            throw new IllegalArgumentException(e.getMessage());
        }

        // Comprobamos que el vehículo exista.
        if (orden.getVehiculo() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar un vehículo."
            );
        }

        Vehiculo vehiculo = vehiculoControlador.buscarVehiculo(
                orden.getVehiculo().getPlaca()
        );

        // Verificamos que el vehículo pertenezca al cliente.
        if (vehiculo.getPropietario() == null
                || !vehiculo.getPropietario().getCodigo()
                        .equals(orden.getCliente().getCodigo())) {

            throw new IllegalArgumentException(
                    "El vehículo no pertenece al cliente seleccionado."
            );
        }

        // Verificamos que los servicios existan.
        for (var detalle : orden.getServicios()) {

            servicioControlador.buscarServicio(
                    detalle.getServicio().getCodigo()
            );
        }

        // Verificamos que los repuestos existan.
        for (DetalleRepuesto detalle : orden.getRepuestos()) {

            inventarioControlador.buscarRepuesto(
                    detalle.getRepuesto().getCodigo()
            );
        }

        // Guardamos la orden sin descontar todavía el inventario.
        if (!repositorio.agregar(orden)) {

            throw new IllegalArgumentException(
                    "Ya existe una orden con ese código."
            );
        }
    }

    // Busca una orden por su código.
    public Orden buscarOrden(String codigo) {

        Orden orden = repositorio.buscarPorCodigo(codigo);

        if (orden == null) {

            throw new IllegalArgumentException(
                    "No se encontró la orden solicitada."
            );
        }

        return orden;
    }

    // Devuelve todas las órdenes registradas.
    public ArrayList<Orden> listarOrdenes() {
        return repositorio.listarTodos();
    }

    // Devuelve los clientes disponibles.
    public ArrayList<cliente> listarClientes() {
        return clienteControlador.listarClientes();
    }

    // Devuelve los vehículos registrados.
    public ArrayList<Vehiculo> listarVehiculos() {
        return vehiculoControlador.listarVehiculos();
    }

    // Devuelve los servicios disponibles.
    public ArrayList<Servicio> listarServicios() {
        return servicioControlador.listarServicios();
    }

    // Devuelve los repuestos disponibles.
    public ArrayList<Repuesto> listarRepuestos() {
        return inventarioControlador.listarRepuestos();
    }

    // Devuelve la cantidad de órdenes.
    public int cantidadOrdenes() {
        return repositorio.cantidad();
    }

    // Finaliza una orden y descuenta los repuestos utilizados.
    public void finalizarOrden(String codigo)
            throws StockInsuficienteException {

        // Buscamos la orden que deseamos finalizar.
        Orden orden = buscarOrden(codigo);

        // Evitamos finalizar nuevamente una orden finalizada.
        if (orden.getEstado().equalsIgnoreCase("Finalizada")) {

            throw new IllegalArgumentException(
                    "Esta orden ya fue finalizada."
            );
        }

        // Agrupamos las cantidades por código de repuesto.
        // Esto evita errores si el mismo repuesto aparece varias veces.
        Map<String, Integer> cantidadesNecesarias = new HashMap<>();

        for (DetalleRepuesto detalle : orden.getRepuestos()) {

            String codigoRepuesto = detalle.getRepuesto().getCodigo();

            int cantidad = detalle.getCantidad();

            cantidadesNecesarias.put(
                    codigoRepuesto,
                    cantidadesNecesarias.getOrDefault(codigoRepuesto, 0)
                            + cantidad
            );
        }

        // PRIMERA FASE:
        // Comprobamos que todos los repuestos tengan existencias.
        // Todavía no modificamos el inventario.
        for (Map.Entry<String, Integer> entrada
                : cantidadesNecesarias.entrySet()) {

            Repuesto repuesto = inventarioControlador.buscarRepuesto(
                    entrada.getKey()
            );

            int cantidadNecesaria = entrada.getValue();

            if (repuesto.getCantidadStock() < cantidadNecesaria) {

                throw new StockInsuficienteException(
                        "No hay suficiente stock de "
                        + repuesto.getNombre()
                        + ". Disponible: "
                        + repuesto.getCantidadStock()
                        + ". Necesario: "
                        + cantidadNecesaria
                );
            }
        }

        // SEGUNDA FASE:
        // Descontamos los repuestos después de validar todos.
        for (Map.Entry<String, Integer> entrada
                : cantidadesNecesarias.entrySet()) {

            inventarioControlador.descontarStock(
                    entrada.getKey(),
                    entrada.getValue()
            );
        }

        // Cambiamos el estado después de descontar correctamente.
        orden.setEstado("Finalizada");
    }

    // Deshace la última operación realizada en órdenes.
    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }
}