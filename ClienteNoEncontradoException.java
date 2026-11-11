/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author Mario Ramos
 */
public class ClienteNoEncontradoException extends Exception {

    // Constructor que recibe el mensaje que queremos mostrar.
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }  
}
