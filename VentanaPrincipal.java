/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */

import javax.swing.*;
import java.awt.*;

import controlador.ClienteControlador;
import controlador.VehiculoControlador;
import controlador.ServicioControlador;
import controlador.InventarioControlador;
import controlador.OrdenControlador;
import controlador.FacturaControlador;

import vista.PanelReportes;

public class VentanaPrincipal extends JFrame {

    // Panel que permitirá cambiar entre los diferentes módulos.
    private JPanel panelContenido;

    // CardLayout administra las diferentes pantallas.
    private CardLayout administradorPantallas;

    // Colores principales del ERP.
    private final Color AZUL_OSCURO = new Color(23, 43, 77);
    private final Color AZUL_CLARO = new Color(35, 60, 100);
    private final Color FONDO = new Color(245, 247, 250);

    // Controladores compartidos del sistema.
    private final ClienteControlador clienteControlador;
    private final VehiculoControlador vehiculoControlador;
    private final ServicioControlador servicioControlador;
    private final InventarioControlador inventarioControlador;
    private final OrdenControlador ordenControlador;
    private final FacturaControlador facturaControlador;

    // Paneles de los módulos.
    private PanelInicio panelInicio;
    private PanelClientes panelClientes;
    private PanelVehiculos panelVehiculos;
    private PanelServicios panelServicios;
    private PanelInventario panelInventario;
    private PanelOrdenes panelOrdenes;
    private PanelFacturacion panelFacturacion;
    private PanelReportes panelReportes;

    // Constructor de la ventana principal.
    public VentanaPrincipal() {

        setTitle("AutoTech Solutions | Sistema ERP");

        // Tamaño inicial de la ventana.
        setSize(1200, 850);

        // Centra la ventana en la pantalla.
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Evita que la ventana se reduzca demasiado.
        setMinimumSize(new Dimension(1000, 750));

        // Creamos una sola instancia de cada controlador.
        clienteControlador = new ClienteControlador();

        vehiculoControlador =
                new VehiculoControlador(clienteControlador);

        servicioControlador = new ServicioControlador();

        inventarioControlador = new InventarioControlador();

        // El controlador de órdenes comparte los controladores
        // de clientes, vehículos, servicios e inventario.
        ordenControlador = new OrdenControlador(
                clienteControlador,
                vehiculoControlador,
                servicioControlador,
                inventarioControlador
        );

        // Controlador encargado de administrar las facturas.
        facturaControlador = new FacturaControlador();

        setLayout(new BorderLayout());

        // Agregamos el menú lateral.
        add(crearMenuLateral(), BorderLayout.WEST);

        // Agregamos la barra superior.
        add(crearBarraSuperior(), BorderLayout.NORTH);

        // Creamos el administrador de pantallas.
        administradorPantallas = new CardLayout();

        panelContenido = new JPanel(administradorPantallas);
        panelContenido.setBackground(FONDO);

        // Creamos el panel de inicio.
        panelInicio = new PanelInicio(
                clienteControlador,
                vehiculoControlador,
                ordenControlador,
                inventarioControlador
        );

        panelContenido.add(panelInicio, "Inicio");

        // Creamos el panel de clientes.
        panelClientes = new PanelClientes(clienteControlador);

        panelContenido.add(panelClientes, "Clientes");

        // Creamos el panel de vehículos.
        panelVehiculos = new PanelVehiculos(
                vehiculoControlador,
                clienteControlador
        );

        panelContenido.add(panelVehiculos, "Vehículos");

        // Creamos el panel de servicios.
        panelServicios = new PanelServicios(servicioControlador);

        panelContenido.add(panelServicios, "Servicios");

        // Creamos el panel de inventario.
        panelInventario = new PanelInventario(inventarioControlador);

        panelContenido.add(panelInventario, "Inventario");

        // Creamos el panel de órdenes de trabajo.
        panelOrdenes = new PanelOrdenes(ordenControlador);

        panelContenido.add(panelOrdenes, "Órdenes");

        // Creamos el panel de facturación.
        panelFacturacion = new PanelFacturacion(
                facturaControlador,
                ordenControlador
        );

        panelContenido.add(panelFacturacion, "Facturación");

        // Creamos el panel de reportes.
        panelReportes = new PanelReportes(ordenControlador);

        panelContenido.add(panelReportes, "Reportes");

        // Agregamos el área central a la ventana.
        add(panelContenido, BorderLayout.CENTER);

        // Actualizamos los indicadores al iniciar el sistema.
        panelInicio.actualizarIndicadores();

        // Mostramos el panel de inicio al abrir el sistema.
        administradorPantallas.show(panelContenido, "Inicio");
    }

    // Método para construir el menú lateral.
    private JPanel crearMenuLateral() {

        JPanel menu = new JPanel();

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        menu.setBackground(AZUL_OSCURO);

        menu.setPreferredSize(new Dimension(230, 0));

        menu.setBorder(
                BorderFactory.createEmptyBorder(20, 12, 20, 12)
        );

        JLabel nombreSistema = new JLabel("AUTOTECH");

        nombreSistema.setFont(new Font("Arial", Font.BOLD, 24));

        nombreSistema.setForeground(Color.WHITE);

        nombreSistema.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descripcion = new JLabel("SOLUTIONS ERP");

        descripcion.setFont(new Font("Arial", Font.PLAIN, 12));

        descripcion.setForeground(new Color(190, 205, 225));

        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(nombreSistema);

        menu.add(descripcion);

        menu.add(Box.createVerticalStrut(35));

        // Botón principal.
        agregarBotonMenu(menu, "Inicio");

        menu.add(Box.createVerticalStrut(15));

        JLabel administracion = new JLabel("ADMINISTRACIÓN");

        administracion.setForeground(new Color(170, 185, 210));

        administracion.setFont(new Font("Arial", Font.BOLD, 11));

        administracion.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(administracion);

        menu.add(Box.createVerticalStrut(10));

        // Opciones de administración.
        agregarBotonMenu(menu, "Clientes");

        agregarBotonMenu(menu, "Vehículos");

        agregarBotonMenu(menu, "Servicios");

        agregarBotonMenu(menu, "Inventario");

        menu.add(Box.createVerticalStrut(20));

        JLabel operaciones = new JLabel("OPERACIONES");

        operaciones.setForeground(new Color(170, 185, 210));

        operaciones.setFont(new Font("Arial", Font.BOLD, 11));

        operaciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(operaciones);

        menu.add(Box.createVerticalStrut(10));

        // Módulos de operaciones.
        agregarBotonMenu(menu, "Órdenes");

        agregarBotonMenu(menu, "Facturación");

        agregarBotonMenu(menu, "Reportes");

        return menu;
    }

    // Método reutilizable para crear los botones del menú.
    private void agregarBotonMenu(JPanel menu, String nombre) {

        JButton boton = new JButton(nombre);

        boton.setMaximumSize(new Dimension(210, 42));

        boton.setPreferredSize(new Dimension(210, 42));

        boton.setHorizontalAlignment(SwingConstants.LEFT);

        boton.setFont(new Font("Arial", Font.PLAIN, 14));

        boton.setForeground(Color.WHITE);

        boton.setBackground(AZUL_CLARO);

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Acción que permite cambiar de pantalla.
        boton.addActionListener(e -> mostrarModulo(nombre));

        menu.add(boton);

        menu.add(Box.createVerticalStrut(5));
    }

    // Método para cambiar entre las diferentes pantallas.
    private void mostrarModulo(String nombre) {

        // Mostramos el panel de inicio.
        if (nombre.equals("Inicio")) {

            panelInicio.actualizarIndicadores();

            administradorPantallas.show(panelContenido, "Inicio");

        // Mostramos el módulo de clientes.
        } else if (nombre.equals("Clientes")) {

            panelClientes.actualizarTabla();

            administradorPantallas.show(panelContenido, "Clientes");

        // Mostramos el módulo de vehículos.
        } else if (nombre.equals("Vehículos")) {

            panelVehiculos.actualizarTabla();

            administradorPantallas.show(panelContenido, "Vehículos");

        // Mostramos el catálogo de servicios.
        } else if (nombre.equals("Servicios")) {

            panelServicios.actualizarTabla();

            administradorPantallas.show(panelContenido, "Servicios");

        // Mostramos el módulo de inventario.
        } else if (nombre.equals("Inventario")) {

            panelInventario.actualizarTabla();

            administradorPantallas.show(panelContenido, "Inventario");

        // Mostramos el módulo de órdenes.
        } else if (nombre.equals("Órdenes")) {

            panelOrdenes.actualizarDatos();

            administradorPantallas.show(panelContenido, "Órdenes");

        // Mostramos el módulo de facturación.
        } else if (nombre.equals("Facturación")) {

            panelFacturacion.actualizarOrdenes();

            panelFacturacion.actualizarTabla();

            administradorPantallas.show(panelContenido, "Facturación");

        // Mostramos y actualizamos los reportes.
        } else if (nombre.equals("Reportes")) {

            panelReportes.actualizarReportes();

            administradorPantallas.show(panelContenido, "Reportes");

        // Los módulos que todavía están en desarrollo.
        } else {

            String nombrePanel = "Temporal_" + nombre;

            if (!existePanel(nombrePanel)) {

                JPanel panelTemporal = new JPanel(new BorderLayout());

                panelTemporal.setBackground(FONDO);

                JLabel mensaje = new JLabel(
                        "Módulo de " + nombre + " - En desarrollo",
                        SwingConstants.CENTER
                );

                mensaje.setFont(new Font("Arial", Font.BOLD, 22));

                panelTemporal.add(mensaje, BorderLayout.CENTER);

                panelTemporal.setName(nombrePanel);

                panelContenido.add(panelTemporal, nombrePanel);
            }

            administradorPantallas.show(panelContenido, nombrePanel);
        }
    }

    // Comprueba si una pantalla temporal ya fue creada.
    private boolean existePanel(String nombre) {

        for (Component componente : panelContenido.getComponents()) {

            if (componente.getName() != null
                    && componente.getName().equals(nombre)) {

                return true;
            }
        }

        return false;
    }

    // Método para construir la barra superior.
    private JPanel crearBarraSuperior() {

        JPanel barra = new JPanel(new BorderLayout());

        barra.setBackground(Color.WHITE);

        barra.setPreferredSize(new Dimension(0, 65));

        barra.setBorder(
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        );

        JLabel titulo = new JLabel("Sistema de gestión automotriz");

        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        titulo.setForeground(AZUL_OSCURO);

        JLabel usuario = new JLabel("Administrador");

        usuario.setFont(new Font("Arial", Font.PLAIN, 14));

        usuario.setForeground(Color.DARK_GRAY);

        barra.add(titulo, BorderLayout.WEST);

        barra.add(usuario, BorderLayout.EAST);

        return barra;
    }
}