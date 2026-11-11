/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mario Ramos
 */

public class ConfiguracionFacturacion {

    
    private double impuestoPorcentaje = 15.0;

    
    public double getImpuestoPorcentaje() {
        return impuestoPorcentaje;
    }

        
    public void setImpuestoPorcentaje(double impuestoPorcentaje) {

        if (impuestoPorcentaje < 0 || impuestoPorcentaje > 100) {
            throw new IllegalArgumentException(
                "El impuesto debe estar entre 0 y 100."
            );
        }

        this.impuestoPorcentaje = impuestoPorcentaje;
    }
}
