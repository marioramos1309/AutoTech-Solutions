/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */

import controlador.InventarioControlador;
import modelo.Repuesto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class PanelInventario extends JPanel {

    // Controlador encargado de administrar el inventario.
    private final InventarioControlador inventarioControlador;

    // Campos del formulario.
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCantidad;
    private JTextField txtStockMinimo;
    private JTextField txtPrecio;
    private JTextField txtDescuento;

    // Tabla que mostrará los repuestos registrados.
    private JTable tablaRepuestos;
    private DefaultTableModel modeloTabla;

    // Guarda el código original cuando modificamos un repuesto.
    private String codigoOriginal = null;

    // Constructor del panel.
    public PanelInventario(InventarioControlador inventarioControlador) {

        this.inventarioControlador = inventarioControlador;

        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));

        setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Encabezado del módulo.
        add(crearEncabezado(), BorderLayout.NORTH);

        // Contenedor principal del formulario y la tabla.
        JPanel contenido = new JPanel(new BorderLayout(12, 0));
        contenido.setOpaque(false);

        // El formulario conserva su tamaño natural a la izquierda.
        JPanel contenedorFormulario = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        contenedorFormulario.setOpaque(false);
        contenedorFormulario.add(crearFormulario());

        contenido.add(contenedorFormulario, BorderLayout.WEST);

        // La tabla utiliza todo el espacio restante.
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        actualizarTabla();
    }

    // Convierte una cantidad numérica al formato de lempiras.
    // Ejemplo: 1250.50 se muestra como L. 1,250.50.
    private String formatoLempiras(double cantidad) {

        return "L. " + String.format(Locale.US, "%,.2f", cantidad);
    }

    // Construye el encabezado del módulo.
    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JLabel titulo = new JLabel("Gestión de inventario");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setForeground(new Color(23, 43, 77));

        JLabel subtitulo = new JLabel(
                "Administración y control de repuestos."
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(subtitulo);

        return encabezado;
    }

    // Construye el formulario de información del repuesto.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Color.WHITE);

        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Información del repuesto"
                        ),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        // Inicializamos los campos.
        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtCantidad = new JTextField();
        txtStockMinimo = new JTextField();
        txtPrecio = new JTextField();
        txtDescuento = new JTextField();

        // Panel que organiza los campos en dos columnas.
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        panelCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Primera fila: código y nombre.
        agregarCampo(panelCampos, gbc, 0, 0, "Código:", txtCodigo);
        agregarCampo(panelCampos, gbc, 1, 0, "Nombre:", txtNombre);

        // Segunda fila: descripción ocupando las dos columnas.
        agregarCampoCompleto(
                panelCampos,
                gbc,
                0,
                1,
                "Descripción:",
                txtDescripcion
        );

        // Tercera fila: cantidad y stock mínimo.
        agregarCampo(panelCampos, gbc, 0, 2, "Cantidad:", txtCantidad);
        agregarCampo(panelCampos, gbc, 1, 2, "Stock mínimo:", txtStockMinimo);

        // Cuarta fila: precio y descuento.
        agregarCampo(panelCampos, gbc, 0, 3, "Precio unitario:", txtPrecio);
        agregarCampo(panelCampos, gbc, 1, 3, "Descuento (%):", txtDescuento);

        // Agregamos los campos al formulario.
        formulario.add(panelCampos);

        formulario.add(Box.createVerticalStrut(10));

        // Creamos los botones.
        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnEntrada = new JButton("Entrada");
        JButton btnStockBajo = new JButton("Stock bajo");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnDeshacer = new JButton("Deshacer");

        // Organizamos los botones en dos columnas.
        JPanel panelBotones = new JPanel(
                new GridLayout(4, 2, 8, 8)
        );
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton[] botones = {
            btnGuardar,
            btnModificar,
            btnEliminar,
            btnBuscar,
            btnEntrada,
            btnStockBajo,
            btnLimpiar,
            btnDeshacer
        };

        for (JButton boton : botones) {
            boton.setPreferredSize(new Dimension(145, 32));
            panelBotones.add(boton);
        }

        formulario.add(panelBotones);

        // Conectamos cada botón con su operación.
        btnGuardar.addActionListener(e -> guardarRepuesto());
        btnModificar.addActionListener(e -> modificarRepuesto());
        btnEliminar.addActionListener(e -> eliminarRepuesto());
        btnBuscar.addActionListener(e -> buscarRepuesto());
        btnEntrada.addActionListener(e -> registrarEntrada());
        btnStockBajo.addActionListener(e -> mostrarStockBajo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnDeshacer.addActionListener(e -> deshacerOperacion());

        return formulario;
    }

    // Agrega una etiqueta y un campo en una de las dos columnas.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int columna,
            int fila,
            String etiqueta,
            JTextField campo) {

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension tamano = new Dimension(145, 28);

        campo.setFont(new Font("Arial", Font.PLAIN, 13));
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
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(contenedor, gbc);
    }

    // Agrega un campo que ocupa las dos columnas.
    private void agregarCampoCompleto(
            JPanel panel,
            GridBagConstraints gbc,
            int columna,
            int fila,
            String etiqueta,
            JTextField campo) {

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension tamano = new Dimension(310, 28);

        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        campo.setPreferredSize(tamano);
        campo.setMinimumSize(tamano);
        campo.setMaximumSize(tamano);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenedor.add(label);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(contenedor, gbc);
    }

    // Construye la tabla de repuestos.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Repuestos registrados"
                )
        );

        String[] columnas = {
            "Código",
            "Nombre",
            "Cantidad",
            "Stock mínimo",
            "Precio",
            "Descuento",
            "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaRepuestos = new JTable(modeloTabla);
        tablaRepuestos.setRowHeight(28);
        tablaRepuestos.setFont(new Font("Arial", Font.PLAIN, 12));

        tablaRepuestos.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        tablaRepuestos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Cargamos los datos del repuesto seleccionado.
        tablaRepuestos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarRepuestoSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaRepuestos);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Registra un nuevo repuesto.
    private void guardarRepuesto() {

        try {

            Repuesto repuesto = obtenerDatosFormulario();

            boolean registrado =
                    inventarioControlador.registrarRepuesto(repuesto);

            if (!registrado) {
                mostrarError("Ya existe un repuesto con ese código.");
                return;
            }

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Repuesto registrado correctamente."
            );

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Modifica un repuesto existente.
    private void modificarRepuesto() {

        try {

            if (codigoOriginal == null) {
                mostrarError("Selecciona un repuesto de la tabla.");
                return;
            }

            Repuesto repuesto = obtenerDatosFormulario();

            boolean actualizado =
                    inventarioControlador.actualizarRepuesto(
                            codigoOriginal,
                            repuesto
                    );

            if (actualizado) {

                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(
                        this,
                        "Repuesto modificado correctamente."
                );
            }

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Elimina un repuesto del inventario.
    private void eliminarRepuesto() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Ingresa o selecciona un código.");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas eliminar este repuesto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                boolean eliminado =
                        inventarioControlador.eliminarRepuesto(codigo);

                if (eliminado) {

                    actualizarTabla();
                    limpiarCampos();

                    JOptionPane.showMessageDialog(
                            this,
                            "Repuesto eliminado correctamente."
                    );
                }
            }

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Busca un repuesto por su código.
    private void buscarRepuesto() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Ingresa el código que deseas buscar.");
                return;
            }

            Repuesto repuesto =
                    inventarioControlador.buscarRepuesto(codigo);

            mostrarDatos(repuesto);

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // Registra una entrada de unidades al inventario.
    private void registrarEntrada() {

        try {

            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Selecciona o ingresa el código del repuesto.");
                return;
            }

            String cantidadTexto = JOptionPane.showInputDialog(
                    this,
                    "¿Cuántas unidades deseas agregar?"
            );

            if (cantidadTexto == null) {
                return;
            }

            int cantidad = Integer.parseInt(cantidadTexto.trim());

            inventarioControlador.registrarEntrada(codigo, cantidad);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrada de inventario registrada correctamente."
            );

        } catch (NumberFormatException ex) {

            mostrarError("Ingresa una cantidad válida.");

        } catch (IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    // Muestra los repuestos que necesitan reposición.
    private void mostrarStockBajo() {

        actualizarTabla();

        ArrayList<Repuesto> repuestos =
                inventarioControlador.listarStockBajo();

        if (repuestos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay repuestos con stock bajo."
            );

            return;
        }

        StringBuilder mensaje = new StringBuilder();

        mensaje.append("Repuestos que necesitan reposición:\n\n");

        for (Repuesto repuesto : repuestos) {

            mensaje.append("Código: ")
                    .append(repuesto.getCodigo())
                    .append(" | ")
                    .append(repuesto.getNombre())
                    .append(" | Existencias: ")
                    .append(repuesto.getCantidadStock())
                    .append(" | Mínimo: ")
                    .append(repuesto.getStockMinimo())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "Alerta de inventario",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // Obtiene los datos escritos en el formulario.
    private Repuesto obtenerDatosFormulario() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {

            throw new IllegalArgumentException(
                    "El código y el nombre son obligatorios."
            );
        }

        try {

            int cantidad = Integer.parseInt(
                    txtCantidad.getText().trim()
            );

            int stockMinimo = Integer.parseInt(
                    txtStockMinimo.getText().trim()
            );

            // El precio se introduce como número sin el símbolo L.
            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            double descuento = Double.parseDouble(
                    txtDescuento.getText().trim()
            );

            return new Repuesto(
                    codigo,
                    nombre,
                    descripcion,
                    cantidad,
                    stockMinimo,
                    precio,
                    descuento
            );

        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "Revisa las cantidades, el precio y el descuento."
            );
        }
    }

    // Muestra los datos de un repuesto en el formulario.
    private void mostrarDatos(Repuesto repuesto) {

        codigoOriginal = repuesto.getCodigo();

        txtCodigo.setText(repuesto.getCodigo());
        txtNombre.setText(repuesto.getNombre());
        txtDescripcion.setText(repuesto.getDescripcion());

        txtCantidad.setText(
                String.valueOf(repuesto.getCantidadStock())
        );

        txtStockMinimo.setText(
                String.valueOf(repuesto.getStockMinimo())
        );

        // Mostramos el precio con formato de lempiras.
        txtPrecio.setText(
                formatoLempiras(repuesto.getPrecioUnitario())
        );

        txtDescuento.setText(
                String.valueOf(repuesto.getDescuentoPorcentaje())
        );
    }

    // Carga en el formulario el repuesto seleccionado en la tabla.
    private void cargarRepuestoSeleccionado() {

        int fila = tablaRepuestos.getSelectedRow();

        if (fila >= 0) {

            int filaModelo = tablaRepuestos.convertRowIndexToModel(fila);

            String codigo =
                    modeloTabla.getValueAt(filaModelo, 0).toString();

            try {

                Repuesto repuesto =
                        inventarioControlador.buscarRepuesto(codigo);

                mostrarDatos(repuesto);

            } catch (IllegalArgumentException ex) {

                mostrarError(ex.getMessage());
            }
        }
    }

    // Actualiza la información que aparece en la tabla.
    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Repuesto repuesto : inventarioControlador.listarRepuestos()) {

            String estado = repuesto.necesitaReposicion()
                    ? "STOCK BAJO" : "Disponible";

            Object[] fila = {
                repuesto.getCodigo(),
                repuesto.getNombre(),
                repuesto.getCantidadStock(),
                repuesto.getStockMinimo(),
                formatoLempiras(repuesto.getPrecioUnitario()),
                String.format(Locale.US, "%.2f %%", repuesto.getDescuentoPorcentaje()),
                estado
            };

            modeloTabla.addRow(fila);
        }
    }

    // Limpia todos los campos del formulario.
    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtStockMinimo.setText("");
        txtPrecio.setText("");
        txtDescuento.setText("");

        codigoOriginal = null;

        tablaRepuestos.clearSelection();
        txtCodigo.requestFocus();
    }

    // Deshace la última operación realizada.
    private void deshacerOperacion() {

        boolean deshecho =
                inventarioControlador.deshacerUltimaOperacion();

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