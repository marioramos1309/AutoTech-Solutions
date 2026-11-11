/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

/**
 *
 * @author Mario Ramos
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Function;


public class RepositorioGenerico<T> {

    
    private ArrayList<T> lista;

     private HashMap<String, T> mapa;

    private Stack<Operacion<T>> historial;

    private Function<T, String> obtenerCodigo;

    
    public RepositorioGenerico(Function<T, String> obtenerCodigo) {

        this.obtenerCodigo = obtenerCodigo;

        lista = new ArrayList<>();
        mapa = new HashMap<>();
        historial = new Stack<>();
    }

    
    public boolean agregar(T objeto) {

        String codigo = obtenerCodigo.apply(objeto);

        // Evitamos registrar códigos duplicados.
        if (mapa.containsKey(codigo)) {
            return false;
        }

        lista.add(objeto);
        mapa.put(codigo, objeto);

        
        historial.push(new Operacion<>(
            "AGREGAR", codigo, null, objeto,
            lista.size() - 1
        ));

        return true;
    }

    
    public T buscarPorCodigo(String codigo) {
        return mapa.get(codigo);
    }

    
    public ArrayList<T> listarTodos() {
        return new ArrayList<>(lista);
    }

    
    public boolean actualizar(String codigo, T nuevoObjeto) {

        T anterior = mapa.get(codigo);

        if (anterior == null) {
            return false;
        }

        String nuevoCodigo = obtenerCodigo.apply(nuevoObjeto);

        
        if (!codigo.equals(nuevoCodigo)
                && mapa.containsKey(nuevoCodigo)) {
            return false;
        }

        int indice = lista.indexOf(anterior);

        // Actualizamos ambas estructuras.
        lista.set(indice, nuevoObjeto);
        mapa.remove(codigo);
        mapa.put(nuevoCodigo, nuevoObjeto);

        historial.push(new Operacion<>(
            "ACTUALIZAR", codigo, anterior,
            nuevoObjeto, indice
        ));

        return true;
    }

    
    public boolean eliminar(String codigo) {

        T objeto = mapa.get(codigo);

        if (objeto == null) {
            return false;
        }

        int indice = lista.indexOf(objeto);

        lista.remove(objeto);
        mapa.remove(codigo);

        historial.push(new Operacion<>(
            "ELIMINAR", codigo, objeto,
            null, indice
        ));

        return true;
    }

    
    public boolean deshacer() {

        if (historial.isEmpty()) {
            return false;
        }

        Operacion<T> operacion = historial.pop();

        switch (operacion.tipo) {

            case "AGREGAR":

                lista.remove(operacion.indice);
                mapa.remove(operacion.codigo);

                break;

            case "ACTUALIZAR":

                String codigoActual =
                    obtenerCodigo.apply(operacion.nuevoObjeto);

                lista.set(
                    operacion.indice,
                    operacion.anterior
                );

                mapa.remove(codigoActual);

                mapa.put(
                    operacion.codigo,
                    operacion.anterior
                );

                break;

            case "ELIMINAR":

                lista.add(
                    operacion.indice,
                    operacion.anterior
                );

                mapa.put(
                    operacion.codigo,
                    operacion.anterior
                );

                break;
        }

        return true;
    }

    
    public int cantidad() {
        return lista.size();
    }

    
    private static class Operacion<T> {

        String tipo;
        String codigo;
        T anterior;
        T nuevoObjeto;
        int indice;

        Operacion(String tipo, String codigo,
                  T anterior, T nuevoObjeto, int indice) {

            this.tipo = tipo;
            this.codigo = codigo;
            this.anterior = anterior;
            this.nuevoObjeto = nuevoObjeto;
            this.indice = indice;
        }
    }
}
