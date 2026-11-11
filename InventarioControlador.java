/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Mario Ramos
 */

import excepciones.StockInsuficienteException;
import modelo.Repuesto;
import repositorio.RepositorioGenerico;

import java.util.ArrayList;

public class InventarioControlador {

    // Repositorio que almacena los repuestos en memoria.
    private final RepositorioGenerico<Repuesto> repositorio =
            new RepositorioGenerico<>(Repuesto::getCodigo);

    // Registra un repuesto nuevo.
    public boolean registrarRepuesto(Repuesto repuesto) {

        validarRepuesto(repuesto);

        return repositorio.agregar(repuesto);
    }

    // Busca un repuesto por su código.
    public Repuesto buscarRepuesto(String codigo) {

        Repuesto repuesto = repositorio.buscarPorCodigo(codigo);

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "No se encontró un repuesto con ese código."
            );
        }

        return repuesto;
    }

    // Devuelve todos los repuestos registrados.
    public ArrayList<Repuesto> listarRepuestos() {
        return repositorio.listarTodos();
    }

    // Modifica la información de un repuesto.
    public boolean actualizarRepuesto(
            String codigo, Repuesto repuesto) {

        if (repositorio.buscarPorCodigo(codigo) == null) {
            throw new IllegalArgumentException(
                    "No existe el repuesto que deseas modificar."
            );
        }

        
        repuesto.setCodigo(codigo);

        validarRepuesto(repuesto);

        return repositorio.actualizar(codigo, repuesto);
    }

   
     public void registrarEntrada(String codigo, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de entrada debe ser mayor que cero."
            );
        }

        // Buscamos el repuesto original.
        Repuesto original = buscarRepuesto(codigo);

        // Creamos una copia independiente.
        Repuesto actualizado = new Repuesto(original);

        // Aumentamos las existencias de la copia.
        actualizado.setCantidadStock(
                original.getCantidadStock() + cantidad
        );

        // Guardamos la modificación en el repositorio.
        repositorio.actualizar(codigo, actualizado);
    }

    
    public void descontarStock(String codigo, int cantidad)
            throws StockInsuficienteException {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        // Buscamos el repuesto original.
        Repuesto original = buscarRepuesto(codigo);

        // Comprobamos que exista suficiente inventario.
        if (original.getCantidadStock() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el repuesto: "
                    + original.getNombre()
            );
        }

        // Creamos una copia independiente.
        Repuesto actualizado = new Repuesto(original);

        // Descontamos las unidades utilizadas.
        actualizado.setCantidadStock(
                original.getCantidadStock() - cantidad
        );

        // Actualizamos el repositorio.
        repositorio.actualizar(codigo, actualizado);
    }

    
    public ArrayList<Repuesto> listarStockBajo() {

        ArrayList<Repuesto> resultado = new ArrayList<>();

        for (Repuesto repuesto : repositorio.listarTodos()) {

            if (repuesto.necesitaReposicion()) {
                resultado.add(repuesto);
            }
        }

        return resultado;
    }

    
    public boolean eliminarRepuesto(String codigo) {

        if (repositorio.buscarPorCodigo(codigo) == null) {
            throw new IllegalArgumentException(
                    "No existe el repuesto que deseas eliminar."
            );
        }

        return repositorio.eliminar(codigo);
    }

    
    public boolean deshacerUltimaOperacion() {
        return repositorio.deshacer();
    }

    
    public int cantidadRepuestos() {
        return repositorio.cantidad();
    }

    
    private void validarRepuesto(Repuesto repuesto) {

        if (repuesto == null) {
            throw new IllegalArgumentException(
                    "El repuesto no puede estar vacío."
            );
        }

        if (repuesto.getCodigo() == null ||
                repuesto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código del repuesto es obligatorio."
            );
        }

        if (repuesto.getNombre() == null ||
                repuesto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del repuesto es obligatorio."
            );
        }

        if (repuesto.getCantidadStock() < 0) {
            throw new IllegalArgumentException(
                    "Las existencias no pueden ser negativas."
            );
        }

        if (repuesto.getStockMinimo() < 0) {
            throw new IllegalArgumentException(
                    "El stock mínimo no puede ser negativo."
            );
        }

        if (repuesto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero."
            );
        }

        if (repuesto.getDescuentoPorcentaje() < 0 ||
                repuesto.getDescuentoPorcentaje() > 100) {
            throw new IllegalArgumentException(
                    "El descuento debe estar entre 0 y 100."
            );
        }
    }
}
