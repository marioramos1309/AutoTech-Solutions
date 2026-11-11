/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author Mario Ramos
 */
public class StockInsuficienteException extends Exception {

    // Constructor que recibe el mensaje del error.
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
