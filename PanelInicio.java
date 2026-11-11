/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */

import controlador.ClienteControlador;
import controlador.VehiculoControlador;
import controlador.OrdenControlador;
import controlador.InventarioControlador;

import javax.swing.*;
import java.awt.*;

public class PanelInicio extends JPanel {

    // Controladores que proporcionan las cantidades reales.
    private final ClienteControlador clienteControlador;
    private final VehiculoControlador vehiculoControlador;
    private final OrdenControlador ordenControlador;
    private final InventarioControlador inventarioControlador;

    // Etiquetas donde mostraremos los valores.
    private JLabel lblClientes;
    private JLabel lblVehiculos;
    private JLabel lblOrdenes;
    private JLabel lblRepuestos;

    // Constructor que recibe los cuatro controladores.
    public PanelInicio(
            ClienteControlador clienteControlador,
            VehiculoControlador vehiculoControlador,
            OrdenControlador ordenControlador,
            InventarioControlador inventarioControlador) {

        this.clienteControlador = clienteControlador;
        this.vehiculoControlador = vehiculoControlador;
        this.ordenControlador = ordenControlador;
        this.inventarioControlador = inventarioControlador;

        setLayout(new BorderLayout(20, 20));

        setBackground(new Color(245, 247, 250));

        setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        // Creamos el encabezado.
        JLabel titulo = new JLabel("Panel de control");

        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        titulo.setForeground(new Color(30, 50, 80));

        JLabel subtitulo = new JLabel(
                "Bienvenido al sistema de gestión AutoTech Solutions"
        );

        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));

        subtitulo.setForeground(Color.GRAY);

        // Organizamos el título y el subtítulo.
        JPanel encabezado = new JPanel(new GridLayout(2, 1));

        encabezado.setOpaque(false);

        encabezado.add(titulo);

        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);

        // Creamos el área de indicadores.
        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 20, 20));

        tarjetas.setOpaque(false);

        // Agregamos las cuatro tarjetas.
        tarjetas.add(crearTarjeta("Clientes registrados", 1));

        tarjetas.add(crearTarjeta("Vehículos registrados", 2));

        tarjetas.add(crearTarjeta("Órdenes de trabajo", 3));

        tarjetas.add(crearTarjeta("Repuestos registrados", 4));

        add(tarjetas, BorderLayout.CENTER);

        // Cargamos las cantidades iniciales.
        actualizarIndicadores();
    }

    // Método reutilizable para crear las tarjetas.
    private JPanel crearTarjeta(String titulo, int tipo) {

        JPanel tarjeta = new JPanel(new BorderLayout());

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 238)
                        ),
                        BorderFactory.createEmptyBorder(
                                25, 20, 25, 20
                        )
                )
        );

        JLabel nombre = new JLabel(titulo);

        nombre.setFont(new Font("Arial", Font.BOLD, 16));

        nombre.setForeground(new Color(90, 100, 115));

        JLabel cantidad = new JLabel("0");

        cantidad.setFont(new Font("Arial", Font.BOLD, 35));

        cantidad.setForeground(new Color(30, 50, 80));

        // Guardamos la etiqueta correspondiente a cada tarjeta.
        switch (tipo) {

            case 1:
                lblClientes = cantidad;
                break;

            case 2:
                lblVehiculos = cantidad;
                break;

            case 3:
                lblOrdenes = cantidad;
                break;

            case 4:
                lblRepuestos = cantidad;
                break;

            default:
                break;
        }

        tarjeta.add(nombre, BorderLayout.NORTH);

        tarjeta.add(cantidad, BorderLayout.CENTER);

        return tarjeta;
    }

    // Actualiza las cantidades mostradas en el panel.
    public void actualizarIndicadores() {

        // Consultamos la cantidad real de clientes.
        lblClientes.setText(
                String.valueOf(
                        clienteControlador.cantidadClientes()
                )
        );

        // Consultamos la cantidad real de vehículos.
        lblVehiculos.setText(
                String.valueOf(
                        vehiculoControlador.cantidadVehiculos()
                )
        );

        // Consultamos la cantidad real de órdenes.
        lblOrdenes.setText(
                String.valueOf(
                        ordenControlador.cantidadOrdenes()
                )
        );

        // Consultamos la cantidad real de repuestos.
        lblRepuestos.setText(
                String.valueOf(
                        inventarioControlador.cantidadRepuestos()
                )
        );
    }
}