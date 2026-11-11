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
import excepciones.ClienteNoEncontradoException;
import modelo.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelClientes extends JPanel {

    // Controlador encargado de administrar los clientes.
    private final ClienteControlador controlador;

    // Campos donde el usuario escribirá los datos.
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtIdentidad;
    private JTextField txtTelefono;
    private JTextField txtDireccion;

    // Tabla que mostrará los clientes registrados.
    private JTable tablaClientes;

    // Modelo que administra las filas y columnas de la tabla.
    private DefaultTableModel modeloTabla;

    // Constructor del panel.
    public PanelClientes(ClienteControlador controlador) {

        this.controlador = controlador;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título principal del módulo.
        JLabel titulo = new JLabel("Gestión de clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitulo = new JLabel(
                "Registra, consulta, modifica y elimina clientes."
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.setOpaque(false);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);

        // Panel central que contiene el formulario y la tabla.
        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);

        // Colocamos el formulario a la izquierda.
        contenido.add(crearFormulario(), BorderLayout.WEST);

        // Colocamos la tabla a la derecha.
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        // Cargamos los clientes existentes.
        actualizarTabla();
    }

    // Construye el formulario para ingresar los datos.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Información del cliente"
        ));

        // Establecemos el ancho del formulario.
        formulario.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // Campos de información del cliente.
        txtCodigo = new JTextField(18);
        txtNombre = new JTextField(18);
        txtIdentidad = new JTextField(18);
        txtTelefono = new JTextField(18);
        txtDireccion = new JTextField(18);

        // Agregamos los campos de manera vertical.
        agregarCampo(formulario, gbc, 0, "Código:", txtCodigo);
        agregarCampo(formulario, gbc, 2, "Nombre completo:", txtNombre);
        agregarCampo(formulario, gbc, 4, "Identidad:", txtIdentidad);
        agregarCampo(formulario, gbc, 6, "Teléfono:", txtTelefono);
        agregarCampo(formulario, gbc, 8, "Dirección:", txtDireccion);

        // Creamos los botones para ejecutar las operaciones.
        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnDeshacer = new JButton("Deshacer");

        // Organizamos los botones en dos columnas y tres filas.
        JPanel panelBotones = new JPanel(
                new GridLayout(3, 2, 8, 8)
        );
        panelBotones.setOpaque(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnDeshacer);

        // Colocamos los botones debajo de los campos.
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;

        formulario.add(panelBotones, gbc);

        // Conectamos cada botón con su operación.
        btnGuardar.addActionListener(e -> guardarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnDeshacer.addActionListener(e -> deshacerOperacion());

        return formulario;
    }

    // Agrega una etiqueta y su campo de texto al formulario.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            JTextField campo) {

        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 1;

        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridy = fila + 1;

        panel.add(campo, gbc);
    }

    // Construye la tabla donde se mostrarán los clientes.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                "Clientes registrados"
        ));

        String[] columnas = {
            "Código",
            "Nombre",
            "Identidad",
            "Teléfono",
            "Dirección"
        };

        // Evitamos que el usuario edite directamente las celdas.
        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25);
        tablaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Cuando seleccionamos una fila, cargamos sus datos.
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaClientes);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Registra un cliente nuevo.
    private void guardarCliente() {

        try {

            cliente cliente = obtenerDatosFormulario();

            boolean registrado = controlador.registrarCliente(cliente);

            if (!registrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ya existe un cliente con ese código."
                );

                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente."
            );

        } catch (IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Modifica los datos del cliente seleccionado.
    private void modificarCliente() {

        try {

            String codigo = txtCodigo.getText().trim();

            cliente cliente = obtenerDatosFormulario();

            boolean actualizado =
                    controlador.actualizarCliente(codigo, cliente);

            if (!actualizado) {

                mostrarError("No se pudo actualizar el cliente.");
                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente actualizado correctamente."
            );

        } catch (ClienteNoEncontradoException |
                 IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Elimina el cliente utilizando su código.
    private void eliminarCliente() {

        try {

            String codigo = txtCodigo.getText().trim();

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar este cliente?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            controlador.eliminarCliente(codigo);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente eliminado correctamente."
            );

        } catch (ClienteNoEncontradoException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Busca un cliente utilizando su código.
    private void buscarCliente() {

        try {

            String codigo = txtCodigo.getText().trim();

            cliente cliente = controlador.buscarCliente(codigo);

            mostrarDatos(cliente);

        } catch (ClienteNoEncontradoException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Obtiene los datos escritos en el formulario.
    private cliente obtenerDatosFormulario() {

        return new cliente(
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                txtIdentidad.getText().trim(),
                txtTelefono.getText().trim(),
                txtDireccion.getText().trim()
        );
    }

    // Muestra los datos de un cliente en el formulario.
    private void mostrarDatos(cliente cliente) {

        txtCodigo.setText(cliente.getCodigo());
        txtNombre.setText(cliente.getNombreCompleto());
        txtIdentidad.setText(cliente.getIdentidad());
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
    }

    // Carga en el formulario el cliente seleccionado en la tabla.
    private void cargarClienteSeleccionado() {

        int fila = tablaClientes.getSelectedRow();

        if (fila >= 0) {

            String codigo = modeloTabla.getValueAt(fila, 0).toString();

            try {

                cliente cliente = controlador.buscarCliente(codigo);

                mostrarDatos(cliente);

            } catch (ClienteNoEncontradoException ex) {

                mostrarError(ex.getMessage());
            }
        }
    }

    // Actualiza todas las filas de la tabla.
    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (cliente cliente : controlador.listarClientes()) {

            modeloTabla.addRow(new Object[]{
                cliente.getCodigo(),
                cliente.getNombreCompleto(),
                cliente.getIdentidad(),
                cliente.getTelefono(),
                cliente.getDireccion()
            });
        }
    }

    // Limpia todos los campos del formulario.
    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtIdentidad.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");

        tablaClientes.clearSelection();
        txtCodigo.requestFocus();
    }

    // Deshace la última operación del repositorio.
    private void deshacerOperacion() {

        boolean deshecho = controlador.deshacerUltimaOperacion();

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