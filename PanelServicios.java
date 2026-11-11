/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */
import controlador.ServicioControlador;
import modelo.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class PanelServicios extends JPanel {

    // Controlador encargado de administrar los servicios.
    private final ServicioControlador servicioControlador;

    // Campos del formulario.
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtDuracion;

    // Tabla donde se mostrarán los servicios.
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;

    // Guarda el código original cuando modificamos un servicio.
    private String codigoOriginal = null;

    // Constructor del panel.
    public PanelServicios(ServicioControlador servicioControlador) {

        this.servicioControlador = servicioControlador;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Encabezado del módulo.
        JLabel titulo = new JLabel("Catálogo de servicios");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitulo = new JLabel("Servicios del taller.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.setOpaque(false);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);

        // Panel que contiene el formulario y la tabla.
        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);

        contenido.add(crearFormulario(), BorderLayout.WEST);
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        actualizarTabla();
    }

    // Convierte una cantidad numérica al formato de lempiras.
    // Ejemplo: 1250.50 se muestra como L. 1,250.50.
    private String formatoLempiras(double cantidad) {

        return "L. " + String.format(Locale.US, "%,.2f", cantidad);
    }

    // Construye el formulario de servicios.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);

        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Información del servicio"
                        ),
                        BorderFactory.createEmptyBorder(20, 10, 10, 10)
                )
        );

        formulario.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // Inicializamos los campos.
        txtCodigo = new JTextField(18);
        txtNombre = new JTextField(18);
        txtDescripcion = new JTextField(18);
        txtPrecio = new JTextField(18);
        txtDuracion = new JTextField(18);

        // Agregamos los campos al formulario.
        agregarCampo(formulario, gbc, 0, "Código:", txtCodigo);
        agregarCampo(formulario, gbc, 2, "Nombre:", txtNombre);
        agregarCampo(formulario, gbc, 4, "Descripción:", txtDescripcion);
        agregarCampo(formulario, gbc, 6, "Precio (L.):", txtPrecio);
        agregarCampo(formulario, gbc, 8, "Duración (minutos):", txtDuracion);

        // Creamos los botones.
        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnDeshacer = new JButton("Deshacer");

        // Organizamos los botones en dos columnas.
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 8, 8));
        panelBotones.setBackground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnDeshacer);

        gbc.gridy = 10;
        formulario.add(panelBotones, gbc);

        // Conectamos los botones con sus operaciones.
        btnGuardar.addActionListener(e -> guardarServicio());
        btnModificar.addActionListener(e -> modificarServicio());
        btnEliminar.addActionListener(e -> eliminarServicio());
        btnBuscar.addActionListener(e -> buscarServicio());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnDeshacer.addActionListener(e -> deshacerOperacion());

        return formulario;
    }

    // Agrega una etiqueta y un campo al formulario.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            JComponent campo) {

        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridy = fila + 1;
        panel.add(campo, gbc);
    }

    // Construye la tabla de servicios.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createTitledBorder(
                "Servicios registrados"
        ));

        String[] columnas = {
            "Código",
            "Nombre",
            "Descripción",
            "Precio",
            "Duración (min)"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaServicios = new JTable(modeloTabla);
        tablaServicios.setRowHeight(25);

        tablaServicios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Cargamos los datos del servicio seleccionado.
        tablaServicios.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarServicioSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaServicios);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Registra un nuevo servicio.
    private void guardarServicio() {

        try {

            Servicio servicio = obtenerDatosFormulario();

            boolean registrado =
                    servicioControlador.registrarServicio(servicio);

            if (!registrado) {
                mostrarError("Ya existe un servicio con ese código.");
                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio registrado correctamente."
            );

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Modifica un servicio existente.
    private void modificarServicio() {

        try {

            if (codigoOriginal == null) {
                mostrarError("Selecciona un servicio de la tabla.");
                return;
            }

            Servicio servicio = obtenerDatosFormulario();

            boolean actualizado =
                    servicioControlador.actualizarServicio(
                            codigoOriginal, servicio
                    );

            if (actualizado) {

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Servicio modificado correctamente."
                );

            } else {
                mostrarError("No se pudo modificar el servicio.");
            }

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Elimina un servicio.
    private void eliminarServicio() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Ingresa o selecciona el código del servicio.");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de eliminar este servicio?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                boolean eliminado =
                        servicioControlador.eliminarServicio(codigo);

                if (eliminado) {

                    actualizarTabla();
                    limpiarCampos();

                    JOptionPane.showMessageDialog(
                            this,
                            "Servicio eliminado correctamente."
                    );
                }
            }

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Busca un servicio por su código.
    private void buscarServicio() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Ingresa el código que deseas buscar.");
                return;
            }

            Servicio servicio =
                    servicioControlador.buscarServicio(codigo);

            mostrarDatos(servicio);

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Obtiene los datos escritos en el formulario.
    private Servicio obtenerDatosFormulario() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {

            throw new IllegalArgumentException(
                    "El código y el nombre son obligatorios."
            );
        }

        if (txtPrecio.getText().trim().isEmpty()
                || txtDuracion.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Ingresa el precio y la duración del servicio."
            );
        }

        double precio;
        int duracion;

        try {

            // Se introduce el precio sin el símbolo L.
            precio = Double.parseDouble(txtPrecio.getText().trim());

            duracion = Integer.parseInt(
                    txtDuracion.getText().trim()
            );

        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "El precio y la duración deben ser números válidos."
            );
        }

        return new Servicio(
                codigo,
                nombre,
                descripcion,
                precio,
                duracion
        );
    }

    // Muestra los datos de un servicio en el formulario.
    private void mostrarDatos(Servicio servicio) {

        codigoOriginal = servicio.getCodigo();

        txtCodigo.setText(servicio.getCodigo());
        txtNombre.setText(servicio.getNombre());
        txtDescripcion.setText(servicio.getDescripcion());

        // Mostramos el precio con formato de lempiras.
        txtPrecio.setText(formatoLempiras(servicio.getPrecio()));

        txtDuracion.setText(
                String.valueOf(servicio.getDuracionMinutos())
        );
    }

    // Carga en el formulario el servicio seleccionado en la tabla.
    private void cargarServicioSeleccionado() {

        int fila = tablaServicios.getSelectedRow();

        if (fila >= 0) {

            int filaModelo = tablaServicios.convertRowIndexToModel(fila);

            String codigo =
                    modeloTabla.getValueAt(filaModelo, 0).toString();

            try {

                Servicio servicio =
                        servicioControlador.buscarServicio(codigo);

                mostrarDatos(servicio);

            } catch (IllegalArgumentException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    // Actualiza la tabla con todos los servicios registrados.
    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Servicio servicio : servicioControlador.listarServicios()) {

            Object[] fila = {
                servicio.getCodigo(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                formatoLempiras(servicio.getPrecio()),
                servicio.getDuracionMinutos()
            };

            modeloTabla.addRow(fila);
        }
    }

    // Limpia todos los campos del formulario.
    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtDuracion.setText("");

        codigoOriginal = null;

        tablaServicios.clearSelection();
        txtCodigo.requestFocus();
    }

    // Deshace la última operación realizada.
    private void deshacerOperacion() {

        boolean deshecho =
                servicioControlador.deshacerUltimaOperacion();

        if (deshecho) {

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "La última operación se deshizo correctamente."
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
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}