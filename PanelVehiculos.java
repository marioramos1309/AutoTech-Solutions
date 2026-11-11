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
import excepciones.ClienteNoEncontradoException;
import modelo.cliente;
import modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelVehiculos extends JPanel {

    // Controladores para administrar clientes y vehículos.
    private final VehiculoControlador vehiculoControlador;
    private final ClienteControlador clienteControlador;

    // Campos del formulario.
    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnio;
    private JTextField txtColor;

    // Lista de propietarios.
    private JComboBox<cliente> cmbPropietario;

    // Tabla de vehículos.
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;

    // Constructor del panel.
    public PanelVehiculos(
            VehiculoControlador vehiculoControlador,
            ClienteControlador clienteControlador) {

        this.vehiculoControlador = vehiculoControlador;
        this.clienteControlador = clienteControlador;

        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Encabezado del módulo.
        JLabel titulo = new JLabel("Gestión de vehículos");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitulo = new JLabel(
                "Registra y administra los vehículos de los clientes."
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 3, 3));
        encabezado.setOpaque(false);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);

        // Panel que contiene el formulario y la tabla.
        JPanel contenido = new JPanel(new BorderLayout(12, 0));
        contenido.setOpaque(false);

        // El formulario conserva su tamaño natural y queda a la izquierda.
        JPanel contenedorFormulario = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        contenedorFormulario.setOpaque(false);

        contenedorFormulario.add(crearFormulario());

        contenido.add(contenedorFormulario, BorderLayout.WEST);
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        actualizarTabla();
    }

    // Construye el formulario de vehículos.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Color.WHITE);

        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        "Información del vehículo"
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Inicializamos los campos.
        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtAnio = new JTextField();
        txtColor = new JTextField();

        cmbPropietario = new JComboBox<>();

        // Panel que organiza los campos en dos columnas.
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        panelCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Primera fila: placa y marca.
        agregarCampo(panelCampos, gbc, 0, 0, "Placa:", txtPlaca);
        agregarCampo(panelCampos, gbc, 1, 0, "Marca:", txtMarca);

        // Segunda fila: modelo y año.
        agregarCampo(panelCampos, gbc, 0, 1, "Modelo:", txtModelo);
        agregarCampo(panelCampos, gbc, 1, 1, "Año:", txtAnio);

        // Tercera fila: color.
        agregarCampo(panelCampos, gbc, 0, 2, "Color:", txtColor);

        // Cuarta fila: propietario.
        JPanel panelPropietario = new JPanel();
        panelPropietario.setLayout(
                new BoxLayout(panelPropietario, BoxLayout.Y_AXIS)
        );
        panelPropietario.setOpaque(false);

        JLabel lblPropietario = new JLabel("Propietario:");
        lblPropietario.setFont(new Font("Arial", Font.BOLD, 12));
        lblPropietario.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension tamanoCombo = new Dimension(310, 28);

        cmbPropietario.setPreferredSize(tamanoCombo);
        cmbPropietario.setMaximumSize(tamanoCombo);
        cmbPropietario.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelPropietario.add(lblPropietario);
        panelPropietario.add(Box.createVerticalStrut(4));
        panelPropietario.add(cmbPropietario);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);

        panelCampos.add(panelPropietario, gbc);

        // Incorporamos los campos al formulario.
        formulario.add(panelCampos);

        formulario.add(Box.createVerticalStrut(10));

        // Creamos los botones.
        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnDeshacer = new JButton("Deshacer");

        // Distribuimos los botones en dos columnas.
        JPanel panelBotones = new JPanel(
                new GridLayout(3, 2, 8, 8)
        );
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton[] botones = {
            btnGuardar,
            btnModificar,
            btnEliminar,
            btnBuscar,
            btnLimpiar,
            btnDeshacer
        };

        for (JButton boton : botones) {
            boton.setPreferredSize(new Dimension(150, 32));
            panelBotones.add(boton);
        }

        formulario.add(panelBotones);

        // Conectamos cada botón con su operación.
        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnModificar.addActionListener(e -> modificarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnBuscar.addActionListener(e -> buscarVehiculo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnDeshacer.addActionListener(e -> deshacerOperacion());

        return formulario;
    }

    // Agrega una etiqueta y su campo en la posición indicada.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int columna,
            int fila,
            String etiqueta,
            JComponent campo) {

        JPanel contenedor = new JPanel();
        contenedor.setLayout(
                new BoxLayout(contenedor, BoxLayout.Y_AXIS)
        );
        contenedor.setOpaque(false);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Todos los campos tienen el mismo tamaño.
        Dimension tamano = new Dimension(145, 28);

        campo.setPreferredSize(tamano);
        campo.setMinimumSize(tamano);
        campo.setMaximumSize(tamano);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenedor.add(label);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panel.add(contenedor, gbc);
    }

    // Construye la tabla de vehículos.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createTitledBorder(
                "Vehículos registrados"
        ));

        String[] columnas = {
            "Placa",
            "Marca",
            "Modelo",
            "Año",
            "Color",
            "Propietario"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaVehiculos = new JTable(modeloTabla);
        tablaVehiculos.setRowHeight(25);

        tablaVehiculos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Carga los datos cuando seleccionamos una fila.
        tablaVehiculos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarVehiculoSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaVehiculos);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Actualiza la lista desplegable de propietarios.
    private void actualizarPropietarios() {

        String codigoSeleccionado = null;

        cliente seleccionado =
                (cliente) cmbPropietario.getSelectedItem();

        if (seleccionado != null) {
            codigoSeleccionado = seleccionado.getCodigo();
        }

        cmbPropietario.removeAllItems();

        for (cliente cliente : clienteControlador.listarClientes()) {
            cmbPropietario.addItem(cliente);
        }

        // Conservamos el propietario seleccionado.
        if (codigoSeleccionado != null) {

            for (int i = 0; i < cmbPropietario.getItemCount(); i++) {

                cliente cliente = cmbPropietario.getItemAt(i);

                if (cliente.getCodigo().equals(codigoSeleccionado)) {
                    cmbPropietario.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // Registra un vehículo nuevo.
    private void guardarVehiculo() {

        try {

            Vehiculo vehiculo = obtenerDatosFormulario();

            boolean registrado =
                    vehiculoControlador.registrarVehiculo(vehiculo);

            if (!registrado) {
                mostrarError("Ya existe un vehículo con esa placa.");
                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehículo registrado correctamente."
            );

        } catch (ClienteNoEncontradoException |
                 IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Modifica un vehículo existente.
    private void modificarVehiculo() {

        try {

            String placa = txtPlaca.getText().trim();

            Vehiculo vehiculo = obtenerDatosFormulario();

            boolean actualizado =
                    vehiculoControlador.actualizarVehiculo(placa, vehiculo);

            if (!actualizado) {
                mostrarError("No se pudo actualizar el vehículo.");
                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehículo actualizado correctamente."
            );

        } catch (ClienteNoEncontradoException |
                 IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Elimina un vehículo por su placa.
    private void eliminarVehiculo() {

        try {

            String placa = txtPlaca.getText().trim();

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este vehículo?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            vehiculoControlador.eliminarVehiculo(placa);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehículo eliminado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Busca un vehículo por su placa.
    private void buscarVehiculo() {

        try {

            String placa = txtPlaca.getText().trim();

            Vehiculo vehiculo =
                    vehiculoControlador.buscarVehiculo(placa);

            mostrarDatos(vehiculo);

        } catch (IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Obtiene los datos del formulario.
    private Vehiculo obtenerDatosFormulario() {

        String placa = txtPlaca.getText().trim();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String color = txtColor.getText().trim();

        int anio;

        try {

            anio = Integer.parseInt(txtAnio.getText().trim());

        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "El año debe contener solamente números."
            );
        }

        cliente propietario =
                (cliente) cmbPropietario.getSelectedItem();

        return new Vehiculo(
                placa,
                marca,
                modelo,
                anio,
                color,
                propietario
        );
    }

    // Muestra los datos de un vehículo.
    private void mostrarDatos(Vehiculo vehiculo) {

        txtPlaca.setText(vehiculo.getPlaca());
        txtMarca.setText(vehiculo.getMarca());
        txtModelo.setText(vehiculo.getModelo());
        txtAnio.setText(String.valueOf(vehiculo.getAnio()));
        txtColor.setText(vehiculo.getColor());

        String codigoPropietario =
                vehiculo.getPropietario().getCodigo();

        for (int i = 0; i < cmbPropietario.getItemCount(); i++) {

            cliente cliente = cmbPropietario.getItemAt(i);

            if (cliente.getCodigo().equals(codigoPropietario)) {
                cmbPropietario.setSelectedIndex(i);
                break;
            }
        }
    }

    // Carga el vehículo seleccionado en la tabla.
    private void cargarVehiculoSeleccionado() {

        int fila = tablaVehiculos.getSelectedRow();

        if (fila >= 0) {

            int filaModelo = tablaVehiculos.convertRowIndexToModel(fila);

            String placa =
                    modeloTabla.getValueAt(filaModelo, 0).toString();

            try {

                Vehiculo vehiculo =
                        vehiculoControlador.buscarVehiculo(placa);

                mostrarDatos(vehiculo);

            } catch (IllegalArgumentException ex) {

                mostrarError(ex.getMessage());
            }
        }
    }

    // Actualiza los datos de la tabla.
    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        actualizarPropietarios();

        for (Vehiculo vehiculo :
                vehiculoControlador.listarVehiculos()) {

            modeloTabla.addRow(new Object[]{
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getColor(),
                vehiculo.getPropietario().getNombreCompleto()
            });
        }
    }

    // Limpia los campos.
    private void limpiarCampos() {

        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtColor.setText("");

        tablaVehiculos.clearSelection();
        txtPlaca.requestFocus();
    }

    // Deshace la última operación.
    private void deshacerOperacion() {

        boolean deshecho =
                vehiculoControlador.deshacerUltimaOperacion();

        if (deshecho) {

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Se deshizo la última operación."
            );

        } else {

            mostrarError("No hay operaciones para deshacer.");
        }
    }

    // Muestra mensajes de error.
    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }
}