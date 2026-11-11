/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */

import controlador.OrdenControlador;

import modelo.cliente;
import modelo.Vehiculo;
import modelo.Servicio;
import modelo.Repuesto;
import modelo.Orden;
import modelo.DetalleServicio;
import modelo.DetalleRepuesto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import hilos.ProcesadorOrdenes;
import java.util.concurrent.ExecutionException;

public class PanelOrdenes extends JPanel {

    // Controlador encargado de administrar las órdenes.
    private final OrdenControlador ordenControlador;

    // Campo para escribir el código de la orden.
    private JTextField txtCodigo;

    // Listas desplegables para seleccionar los datos.
    private JComboBox<String> cmbCliente;
    private JComboBox<String> cmbVehiculo;
    private JComboBox<String> cmbServicio;
    private JComboBox<String> cmbRepuesto;

    // Tabla donde mostraremos los detalles de la orden actual.
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    // Tabla donde mostraremos todas las órdenes registradas.
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTablaOrdenes;

    // Botones para administrar la orden actual.
    private JButton btnAgregarServicio;
    private JButton btnAgregarRepuesto;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    // Botones para administrar las órdenes registradas.
    private JButton btnActualizarOrdenes;
    private JButton btnFinalizarOrden;

    // Etiqueta que muestra el total de la orden.
    private JLabel lblTotal;

    // Listas temporales que almacenan los detalles antes de guardar.
    private final ArrayList<DetalleServicio> detallesServicios =
            new ArrayList<>();

    private final ArrayList<DetalleRepuesto> detallesRepuestos =
            new ArrayList<>();

    // Constructor del panel de órdenes.
    public PanelOrdenes(OrdenControlador ordenControlador) {

        this.ordenControlador = ordenControlador;

        setLayout(new BorderLayout(15, 15));

        setBackground(new Color(245, 247, 250));

        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Agregamos el encabezado del módulo.
        add(crearEncabezado(), BorderLayout.NORTH);

        // Panel principal que contiene el formulario,
        // las dos tablas y los botones.
        JPanel contenido = new JPanel(new BorderLayout(10, 10));

        contenido.setBackground(new Color(245, 247, 250));

        // Formulario para crear una nueva orden.
        contenido.add(crearFormulario(), BorderLayout.NORTH);

        // Divisor horizontal para colocar las dos tablas
        // una al lado de la otra.
        JSplitPane divisorTablas = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearTabla(),
                crearTablaOrdenes()
        );

        // Distribuimos el espacio entre ambas tablas.
        divisorTablas.setResizeWeight(0.50);

        // Permitimos ajustar el tamaño de las tablas con el mouse.
        divisorTablas.setOneTouchExpandable(true);

        divisorTablas.setContinuousLayout(true);

        divisorTablas.setDividerSize(10);

        divisorTablas.setBorder(null);

        // Evitamos que las tablas queden demasiado pequeñas.
        divisorTablas.setMinimumSize(new Dimension(500, 220));

        // Las dos tablas ocupan el centro.
        contenido.add(divisorTablas, BorderLayout.CENTER);

        // Los seis botones estarán debajo de las tablas.
        contenido.add(crearBotones(), BorderLayout.SOUTH);

        // Agregamos el contenido al panel principal.
        add(contenido, BorderLayout.CENTER);

        // Configuramos los eventos de los botones.
        configurarEventos();

        // Cuando seleccionamos un cliente, cargamos sus vehículos.
        cmbCliente.addActionListener(
                e -> cargarVehiculosDelCliente()
        );

        // Cargamos los datos iniciales.
        cargarDatosIniciales();

        // Cargamos las órdenes que ya existen.
        actualizarTablaOrdenes();

        // Centramos inicialmente el divisor entre las tablas.
        SwingUtilities.invokeLater(
                () -> divisorTablas.setDividerLocation(0.50)
        );
    }

    // Construye el encabezado del módulo.
    private JPanel crearEncabezado() {

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Gestión de órdenes de trabajo");

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        titulo.setForeground(new Color(23, 43, 77));

        JLabel descripcion = new JLabel(
                "Registro, consulta y finalización de órdenes del taller"
        );

        descripcion.setFont(new Font("Arial", Font.PLAIN, 14));

        descripcion.setForeground(Color.GRAY);

        panel.add(titulo);

        panel.add(Box.createVerticalStrut(5));

        panel.add(descripcion);

        return panel;
    }

    // Construye el formulario con los datos principales.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBackground(Color.WHITE);

        formulario.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 232)
                        ),
                        "Información de la orden"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6, 10, 6, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // Campo para el código de la orden.
        txtCodigo = new JTextField(15);

        // Listas desplegables.
        cmbCliente = new JComboBox<>();

        cmbVehiculo = new JComboBox<>();

        cmbServicio = new JComboBox<>();

        cmbRepuesto = new JComboBox<>();

        // Agregamos los campos al formulario.
        agregarCampo(
                formulario, gbc,
                "Código de orden:", txtCodigo, 0, 0
        );

        agregarCampo(
                formulario, gbc,
                "Cliente:", cmbCliente, 1, 0
        );

        agregarCampo(
                formulario, gbc,
                "Vehículo:", cmbVehiculo, 0, 1
        );

        agregarCampo(
                formulario, gbc,
                "Servicio:", cmbServicio, 1, 1
        );

        agregarCampo(
                formulario, gbc,
                "Repuesto:", cmbRepuesto, 0, 2
        );

        return formulario;
    }

    // Agrega una etiqueta y su campo correspondiente.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            String etiqueta,
            JComponent campo,
            int columna,
            int fila) {

        gbc.gridx = columna * 2;

        gbc.gridy = fila;

        gbc.weightx = 0;

        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = columna * 2 + 1;

        gbc.weightx = 1;

        campo.setPreferredSize(new Dimension(180, 30));

        panel.add(campo, gbc);
    }

    // Construye la tabla de detalles de la orden actual.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Detalles de la nueva orden"
                )
        );

        String[] columnas = {
            "Tipo",
            "Descripción",
            "Cantidad",
            "Precio unitario",
            "Subtotal"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloTabla);

        tablaDetalles.setRowHeight(28);

        tablaDetalles.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Ajustamos la presentación de las columnas.
        tablaDetalles.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane desplazamiento = new JScrollPane(tablaDetalles);

        desplazamiento.setMinimumSize(new Dimension(200, 100));

        panel.add(desplazamiento, BorderLayout.CENTER);

        // Mostramos el total debajo de la tabla.
        lblTotal = new JLabel("Total de la orden: L. 0.00");

        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        lblTotal.setForeground(new Color(23, 43, 77));

        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        lblTotal.setBorder(
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        );

        panel.add(lblTotal, BorderLayout.SOUTH);

        return panel;
    }

    // Construye la tabla de órdenes registradas.
    private JPanel crearTablaOrdenes() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Órdenes registradas"
                )
        );

        String[] columnas = {
            "Código",
            "Cliente",
            "Vehículo",
            "Fecha",
            "Estado",
            "Total"
        };

        modeloTablaOrdenes = new DefaultTableModel(columnas, 0) {

            // Evitamos que el usuario modifique la información.
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaOrdenes = new JTable(modeloTablaOrdenes);

        tablaOrdenes.setRowHeight(26);

        tablaOrdenes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaOrdenes.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        // Panel con desplazamiento para la tabla.
        JScrollPane desplazamiento = new JScrollPane(tablaOrdenes);

        desplazamiento.setMinimumSize(new Dimension(200, 100));

        panel.add(desplazamiento, BorderLayout.CENTER);

        return panel;
    }

    // Construye los seis botones debajo de las tablas.
    private JPanel crearBotones() {

        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 8));

        panel.setBackground(new Color(245, 247, 250));

        panel.setBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0)
        );

        // Creamos los botones.
        btnAgregarServicio = new JButton("Agregar servicio");

        btnAgregarRepuesto = new JButton("Agregar repuesto");

        btnGuardar = new JButton("Guardar orden");

        btnLimpiar = new JButton("Limpiar");

        btnActualizarOrdenes = new JButton("Actualizar órdenes");

        btnFinalizarOrden = new JButton("Finalizar orden");

        // Personalizamos los botones.
        btnAgregarServicio.setBackground(new Color(35, 100, 160));

        btnAgregarServicio.setForeground(Color.WHITE);

        btnAgregarRepuesto.setBackground(new Color(35, 100, 160));

        btnAgregarRepuesto.setForeground(Color.WHITE);

        btnGuardar.setBackground(new Color(30, 140, 85));

        btnGuardar.setForeground(Color.WHITE);

        btnLimpiar.setBackground(new Color(190, 65, 65));

        btnLimpiar.setForeground(Color.WHITE);

        btnActualizarOrdenes.setBackground(new Color(35, 100, 160));

        btnActualizarOrdenes.setForeground(Color.WHITE);

        btnFinalizarOrden.setBackground(new Color(30, 140, 85));

        btnFinalizarOrden.setForeground(Color.WHITE);

        // Quitamos el borde de enfoque.
        btnAgregarServicio.setFocusPainted(false);

        btnAgregarRepuesto.setFocusPainted(false);

        btnGuardar.setFocusPainted(false);

        btnLimpiar.setFocusPainted(false);

        btnActualizarOrdenes.setFocusPainted(false);

        btnFinalizarOrden.setFocusPainted(false);

        // Establecemos una altura uniforme para los botones.
        Dimension tamanoBoton = new Dimension(150, 36);

        btnAgregarServicio.setPreferredSize(tamanoBoton);

        btnAgregarRepuesto.setPreferredSize(tamanoBoton);

        btnGuardar.setPreferredSize(tamanoBoton);

        btnLimpiar.setPreferredSize(tamanoBoton);

        btnActualizarOrdenes.setPreferredSize(tamanoBoton);

        btnFinalizarOrden.setPreferredSize(tamanoBoton);

        // Primera fila de botones.
        panel.add(btnAgregarServicio);

        panel.add(btnAgregarRepuesto);

        panel.add(btnGuardar);

        // Segunda fila de botones.
        panel.add(btnLimpiar);

        panel.add(btnActualizarOrdenes);

        panel.add(btnFinalizarOrden);

        return panel;
    }

    // Configura las acciones de los botones.
    private void configurarEventos() {

        btnAgregarServicio.addActionListener(
                e -> agregarServicio()
        );

        btnAgregarRepuesto.addActionListener(
                e -> agregarRepuesto()
        );

        btnGuardar.addActionListener(
                e -> guardarOrden()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        btnActualizarOrdenes.addActionListener(
                e -> actualizarTablaOrdenes()
        );

        btnFinalizarOrden.addActionListener(
                e -> finalizarOrdenSeleccionada()
        );
    }

    // Carga los clientes, servicios y repuestos disponibles.
    private void cargarDatosIniciales() {

        cargarClientes();

        cargarServicios();

        cargarRepuestos();
    }

    // Carga los clientes registrados en el sistema.
    private void cargarClientes() {

        cmbCliente.removeAllItems();

        for (cliente clienteActual : ordenControlador.listarClientes()) {

            String informacion = clienteActual.getCodigo()
                    + " - " + clienteActual.getNombreCompleto();

            cmbCliente.addItem(informacion);
        }
    }

    // Carga los servicios disponibles.
    private void cargarServicios() {

        cmbServicio.removeAllItems();

        for (Servicio servicio : ordenControlador.listarServicios()) {

            String informacion = servicio.getCodigo()
                    + " - " + servicio.getNombre();

            cmbServicio.addItem(informacion);
        }
    }

    // Carga los repuestos registrados en el inventario.
    private void cargarRepuestos() {

        cmbRepuesto.removeAllItems();

        for (Repuesto repuesto : ordenControlador.listarRepuestos()) {

            String informacion = repuesto.getCodigo()
                    + " - " + repuesto.getNombre();

            cmbRepuesto.addItem(informacion);
        }
    }

    // Carga solamente los vehículos del cliente seleccionado.
    private void cargarVehiculosDelCliente() {

        cmbVehiculo.removeAllItems();

        String clienteSeleccionado =
                (String) cmbCliente.getSelectedItem();

        if (clienteSeleccionado == null) {
            return;
        }

        String codigoCliente = obtenerCodigo(clienteSeleccionado);

        for (Vehiculo vehiculo : ordenControlador.listarVehiculos()) {

            if (vehiculo.getPropietario() != null
                    && vehiculo.getPropietario().getCodigo()
                            .equals(codigoCliente)) {

                cmbVehiculo.addItem(vehiculo.getPlaca());
            }
        }

        if (cmbVehiculo.getItemCount() == 0) {

            cmbVehiculo.addItem("Sin vehículos registrados");
        }
    }

    // Extrae el código de una opción de las listas desplegables.
    private String obtenerCodigo(String informacion) {

        if (informacion == null) {
            return "";
        }

        String[] partes = informacion.split(" - ", 2);

        return partes[0].trim();
    }

    // Busca un cliente por su código.
    private cliente obtenerClienteSeleccionado() {

        String seleccionado = (String) cmbCliente.getSelectedItem();

        if (seleccionado == null) {
            return null;
        }

        String codigo = obtenerCodigo(seleccionado);

        for (cliente clienteActual : ordenControlador.listarClientes()) {

            if (clienteActual.getCodigo().equals(codigo)) {

                return clienteActual;
            }
        }

        return null;
    }

    // Busca el vehículo seleccionado.
    private Vehiculo obtenerVehiculoSeleccionado() {

        String placa = (String) cmbVehiculo.getSelectedItem();

        if (placa == null || placa.equals("Sin vehículos registrados")) {
            return null;
        }

        for (Vehiculo vehiculo : ordenControlador.listarVehiculos()) {

            if (vehiculo.getPlaca().equals(placa)) {

                return vehiculo;
            }
        }

        return null;
    }

    // Busca el servicio seleccionado.
    private Servicio obtenerServicioSeleccionado() {

        String seleccionado = (String) cmbServicio.getSelectedItem();

        if (seleccionado == null) {
            return null;
        }

        String codigo = obtenerCodigo(seleccionado);

        for (Servicio servicio : ordenControlador.listarServicios()) {

            if (servicio.getCodigo().equals(codigo)) {

                return servicio;
            }
        }

        return null;
    }

    // Busca el repuesto seleccionado.
    private Repuesto obtenerRepuestoSeleccionado() {

        String seleccionado = (String) cmbRepuesto.getSelectedItem();

        if (seleccionado == null) {
            return null;
        }

        String codigo = obtenerCodigo(seleccionado);

        for (Repuesto repuesto : ordenControlador.listarRepuestos()) {

            if (repuesto.getCodigo().equals(codigo)) {

                return repuesto;
            }
        }

        return null;
    }

    // Agrega un servicio a la orden.
    private void agregarServicio() {

        Servicio servicio = obtenerServicioSeleccionado();

        if (servicio == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes registrar y seleccionar un servicio.",
                    "Servicio no disponible",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Creamos el detalle del servicio.
        DetalleServicio detalle = new DetalleServicio(servicio);

        // Guardamos el detalle temporalmente.
        detallesServicios.add(detalle);

        // Agregamos la información a la tabla.
        modeloTabla.addRow(new Object[]{
            "Servicio",
            servicio.getNombre(),
            1,
            detalle.getPrecioUnitario(),
            detalle.getPrecioUnitario()
        });

        actualizarTotal();
    }

    // Agrega un repuesto a la orden.
    private void agregarRepuesto() {

        Repuesto repuesto = obtenerRepuestoSeleccionado();

        if (repuesto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes seleccionar un repuesto.",
                    "Repuesto no disponible",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Solicitamos la cantidad de repuestos.
        String entrada = JOptionPane.showInputDialog(
                this,
                "¿Cuántas unidades deseas agregar?",
                "Cantidad de repuestos",
                JOptionPane.QUESTION_MESSAGE
        );

        if (entrada == null) {
            return;
        }

        try {

            int cantidad = Integer.parseInt(entrada.trim());

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "La cantidad debe ser mayor que cero.",
                        "Cantidad incorrecta",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Sumamos las unidades del mismo repuesto
            // que ya se agregaron a esta orden.
            int cantidadAgregada = 0;

            for (DetalleRepuesto detalle : detallesRepuestos) {

                if (detalle.getRepuesto().getCodigo()
                        .equals(repuesto.getCodigo())) {

                    cantidadAgregada += detalle.getCantidad();
                }
            }

            // Comprobamos que exista suficiente stock.
            if (cantidadAgregada + cantidad > repuesto.getCantidadStock()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Stock insuficiente.\n"
                        + "Disponible: " + repuesto.getCantidadStock()
                        + "\nYa agregado: " + cantidadAgregada
                        + "\nSolicitado: " + cantidad,
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Creamos el detalle del repuesto.
            DetalleRepuesto detalle =
                    new DetalleRepuesto(repuesto, cantidad);

            // Guardamos el detalle temporalmente.
            detallesRepuestos.add(detalle);

            // Agregamos la información a la tabla.
            modeloTabla.addRow(new Object[]{
                "Repuesto",
                repuesto.getNombre(),
                cantidad,
                detalle.getPrecioUnitario(),
                detalle.calcularSubtotal()
            });

            actualizarTotal();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar una cantidad numérica válida.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Cantidad incorrecta",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Calcula y muestra el total de la orden.
    private void actualizarTotal() {

        double total = 0;

        // Sumamos los servicios.
        for (DetalleServicio detalle : detallesServicios) {

            total += detalle.getPrecioUnitario();
        }

        // Sumamos los repuestos.
        for (DetalleRepuesto detalle : detallesRepuestos) {

            total += detalle.calcularSubtotal();
        }

        lblTotal.setText(
                String.format("Total de la orden: L. %.2f", total)
        );
    }

    // Guarda la orden en el sistema.
    private void guardarOrden() {

        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar el código de la orden.",
                    "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCodigo.requestFocus();

            return;
        }

        cliente clienteSeleccionado = obtenerClienteSeleccionado();

        if (clienteSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un cliente.",
                    "Cliente obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Vehiculo vehiculoSeleccionado = obtenerVehiculoSeleccionado();

        if (vehiculoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "El cliente seleccionado no tiene un vehículo disponible.",
                    "Vehículo obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (detallesServicios.isEmpty()
                && detallesRepuestos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes agregar al menos un servicio o repuesto.",
                    "Orden vacía",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            // Creamos la orden con sus datos principales.
            Orden orden = new Orden(
                    codigo,
                    clienteSeleccionado,
                    vehiculoSeleccionado
            );

            // Incorporamos los servicios.
            for (DetalleServicio detalle : detallesServicios) {

                orden.agregarServicio(detalle.getServicio());
            }

            // Incorporamos los repuestos.
            for (DetalleRepuesto detalle : detallesRepuestos) {

                orden.agregarRepuesto(detalle);
            }

            // Calculamos el total de la orden.
            orden.calcularTotal();

            // Registramos la orden.
            ordenControlador.registrarOrden(orden);

            JOptionPane.showMessageDialog(
                    this,
                    "La orden se registró correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Actualizamos la tabla de órdenes registradas.
            actualizarTablaOrdenes();

            // Limpiamos el formulario después de guardar.
            limpiarFormulario();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar la orden:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Carga todas las órdenes registradas en la tabla.
    private void actualizarTablaOrdenes() {

        modeloTablaOrdenes.setRowCount(0);

        for (Orden orden : ordenControlador.listarOrdenes()) {

            modeloTablaOrdenes.addRow(new Object[]{
                orden.getCodigo(),
                orden.getCliente().getNombreCompleto(),
                orden.getVehiculo().getPlaca(),
                orden.getFecha(),
                orden.getEstado(),
                String.format("L. %.2f", orden.getTotal())
            });
        }
    }

    // Finaliza la orden seleccionada utilizando un hilo secundario.
    private void finalizarOrdenSeleccionada() {

        // Obtenemos la fila seleccionada.
        int fila = tablaOrdenes.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes seleccionar una orden de la tabla.",
                    "Orden no seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convertimos la fila seleccionada al índice del modelo.
        int filaModelo = tablaOrdenes.convertRowIndexToModel(fila);

        // Obtenemos el código de la orden.
        String codigo = modeloTablaOrdenes.getValueAt(
                filaModelo, 0
        ).toString();

        // Solicitamos confirmación antes de finalizar.
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de finalizar la orden "
                + codigo + "?\n\n"
                + "Al finalizarla se descontarán los repuestos "
                + "utilizados del inventario.",
                "Confirmar finalización",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Deshabilitamos el botón para evitar iniciar
        // varios procesos al mismo tiempo.
        btnFinalizarOrden.setEnabled(false);

        // Creamos la ventana que mostrará el progreso.
        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Procesando orden",
                Dialog.ModalityType.MODELESS
        );

        dialogo.setLayout(new BorderLayout(10, 10));

        dialogo.setSize(400, 140);

        dialogo.setLocationRelativeTo(this);

        dialogo.setDefaultCloseOperation(
                JDialog.DO_NOTHING_ON_CLOSE
        );

        // Mensaje informativo.
        JLabel mensaje = new JLabel(
                "Procesando la orden " + codigo + "...",
                SwingConstants.CENTER
        );

        mensaje.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 10)
        );

        // Barra que mostrará el avance del procesamiento.
        JProgressBar barraProgreso = new JProgressBar(0, 100);

        barraProgreso.setStringPainted(true);

        barraProgreso.setValue(0);

        barraProgreso.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 10, 15)
        );

        dialogo.add(mensaje, BorderLayout.NORTH);

        dialogo.add(barraProgreso, BorderLayout.CENTER);

        // Creamos el proceso que finalizará la orden
        // sin bloquear la interfaz gráfica.
        ProcesadorOrdenes procesador = new ProcesadorOrdenes(
                ordenControlador,
                codigo
        );

        // Escuchamos los cambios de progreso del hilo.
        procesador.addPropertyChangeListener(evento -> {

            if ("progress".equals(evento.getPropertyName())) {

                int progreso = (Integer) evento.getNewValue();

                barraProgreso.setValue(progreso);
            }
        });

        // Cuando termina el procesamiento, actualizamos
        // la interfaz y mostramos el resultado.
        procesador.addPropertyChangeListener(evento -> {

            if ("state".equals(evento.getPropertyName())
                    && "DONE".equals(evento.getNewValue().toString())) {

                try {

                    // Recuperamos el resultado del hilo.
                    procesador.get();

                    // Cerramos la ventana de progreso.
                    dialogo.dispose();

                    // Actualizamos las órdenes registradas.
                    actualizarTablaOrdenes();

                    // Actualizamos los repuestos disponibles.
                    cargarRepuestos();

                    // Informamos que la operación terminó.
                    JOptionPane.showMessageDialog(
                            this,
                            "La orden " + codigo
                            + " fue finalizada correctamente.\n"
                            + "Los repuestos utilizados fueron "
                            + "descontados del inventario.",
                            "Orden finalizada",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();

                    dialogo.dispose();

                    JOptionPane.showMessageDialog(
                            this,
                            "El procesamiento fue interrumpido.",
                            "Proceso interrumpido",
                            JOptionPane.ERROR_MESSAGE
                    );

                } catch (ExecutionException ex) {

                    dialogo.dispose();

                    // Obtenemos el mensaje real del error.
                    Throwable causa = ex.getCause();

                    String error = causa != null
                            ? causa.getMessage()
                            : ex.getMessage();

                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo finalizar la orden:\n"
                            + error,
                            "Error al finalizar",
                            JOptionPane.ERROR_MESSAGE
                    );

                } finally {

                    // Volvemos a habilitar el botón.
                    btnFinalizarOrden.setEnabled(true);
                }
            }
        });

        // Iniciamos el procesamiento en segundo plano.
        procesador.execute();

        // Mostramos la ventana de progreso.
        dialogo.setVisible(true);
    }

    // Limpia el formulario.
    private void limpiarFormulario() {

        txtCodigo.setText("");

        detallesServicios.clear();

        detallesRepuestos.clear();

        modeloTabla.setRowCount(0);

        actualizarTotal();

        if (cmbCliente.getItemCount() > 0) {

            cmbCliente.setSelectedIndex(0);

        } else {

            cmbVehiculo.removeAllItems();
        }

        txtCodigo.requestFocus();
    }

    // Actualiza los datos cada vez que entramos al módulo.
    public void actualizarDatos() {

        cargarDatosIniciales();

        cargarVehiculosDelCliente();

        actualizarTablaOrdenes();
    }
}