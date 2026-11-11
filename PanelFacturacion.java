/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Mario Ramos
 */

import controlador.FacturaControlador;
import controlador.OrdenControlador;

import modelo.Factura;
import modelo.Orden;

import excepciones.PagoInsuficienteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class PanelFacturacion extends JPanel {

    // Porcentaje fijo del Impuesto Sobre Ventas (ISV).
    private static final double ISV_PORCENTAJE = 15.0;

    // Controladores encargados de administrar las facturas y órdenes.
    private final FacturaControlador facturaControlador;
    private final OrdenControlador ordenControlador;

    // Componentes para seleccionar la orden y realizar el cobro.
    private JComboBox<Orden> comboOrdenes;
    private JTextField txtImpuesto;
    private JTextField txtSubtotal;
    private JTextField txtTotal;
    private JTextField txtEfectivo;
    private JTextField txtCambio;

    // Tabla donde se mostrarán las facturas.
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;

    // Constructor del panel.
    public PanelFacturacion(
            FacturaControlador facturaControlador,
            OrdenControlador ordenControlador) {

        this.facturaControlador = facturaControlador;
        this.ordenControlador = ordenControlador;

        // Establecemos el ISV fijo del 15 %.
        this.facturaControlador.setImpuestoPorcentaje(ISV_PORCENTAJE);

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        crearInterfaz();
        actualizarOrdenes();
        actualizarTabla();
    }

    // Convierte una cantidad numérica al formato de lempiras.
    // Ejemplo: 1250.50 se muestra como L. 1,250.50.
    private String formatoLempiras(double cantidad) {

        return "L. " + String.format(Locale.US, "%,.2f", cantidad);
    }

    // Construye los componentes de la pantalla.
    private void crearInterfaz() {

        JLabel titulo = new JLabel("Módulo de Facturación");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(30, 50, 80));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(titulo, BorderLayout.WEST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel principal que contiene el formulario y la tabla.
        JPanel contenido = new JPanel(new BorderLayout(10, 15));
        contenido.setOpaque(false);

        contenido.add(crearFormulario(), BorderLayout.NORTH);
        contenido.add(crearTabla(), BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);
    }

    // Construye el formulario para realizar una factura.
    private JPanel crearFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(210, 215, 225)),
            "Información de facturación"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        comboOrdenes = new JComboBox<>();
        comboOrdenes.setPreferredSize(new Dimension(250, 30));

        // Campos donde se mostrarán los montos.
        txtImpuesto = new JTextField(12);
        txtSubtotal = new JTextField(12);
        txtTotal = new JTextField(12);
        txtEfectivo = new JTextField(12);
        txtCambio = new JTextField(12);

        // Los campos calculados no pueden modificarse manualmente.
        txtImpuesto.setEditable(false);
        txtSubtotal.setEditable(false);
        txtTotal.setEditable(false);
        txtCambio.setEditable(false);

        // Mostramos el porcentaje fijo en la etiqueta.
        agregarCampo(formulario, gbc, 0, "Orden finalizada:", comboOrdenes);
        agregarCampo(formulario, gbc, 1, "15 % ISV:", txtImpuesto);
        agregarCampo(formulario, gbc, 2, "Subtotal:", txtSubtotal);
        agregarCampo(formulario, gbc, 3, "Total a pagar:", txtTotal);
        agregarCampo(formulario, gbc, 4, "Efectivo recibido:", txtEfectivo);
        agregarCampo(formulario, gbc, 5, "Cambio:", txtCambio);

        // Botones necesarios para la facturación.
        JButton btnFacturar = crearBoton("Registrar factura");
        JButton btnActualizar = crearBoton("Actualizar órdenes");

        JPanel botones = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 10, 5)
        );
        botones.setOpaque(false);

        botones.add(btnFacturar);
        botones.add(btnActualizar);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formulario.add(botones, gbc);

        // Calcula automáticamente al seleccionar otra orden.
        comboOrdenes.addActionListener(e -> calcularTotal());

        // Registra la factura.
        btnFacturar.addActionListener(e -> registrarFactura());

        // Actualiza las órdenes disponibles.
        btnActualizar.addActionListener(e -> actualizarOrdenes());

        return formulario;
    }

    // Agrega una etiqueta y su campo correspondiente al formulario.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            JComponent campo) {

        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(campo, gbc);
    }

    // Construye la tabla de facturas.
    private JPanel crearTabla() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            "Facturas registradas"
        ));

        String[] columnas = {
            "Código",
            "Orden",
            "Fecha",
            "Subtotal",
            "Impuesto",
            "Total",
            "Efectivo",
            "Cambio"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaFacturas);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Crea botones con el mismo estilo visual.
    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setBackground(new Color(35, 90, 150));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    // Carga las órdenes que ya fueron finalizadas y no tienen factura.
    public void actualizarOrdenes() {

        comboOrdenes.removeAllItems();

        ArrayList<Orden> ordenes = ordenControlador.listarOrdenes();

        for (Orden orden : ordenes) {

            boolean tieneFactura = false;

            // Comprobamos si la orden ya tiene una factura registrada.
            for (Factura factura : facturaControlador.listarFacturas()) {

                if (factura.getOrden().getCodigo()
                        .equalsIgnoreCase(orden.getCodigo())) {

                    tieneFactura = true;
                    break;
                }
            }

            // Solamente agregamos órdenes finalizadas sin factura.
            if ("Finalizada".equalsIgnoreCase(orden.getEstado())
                    && !tieneFactura) {

                comboOrdenes.addItem(orden);
            }
        }

        // Muestra los códigos de las órdenes en el selector.
        comboOrdenes.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean seleccionado,
                    boolean tieneFoco) {

                super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco
                );

                if (valor instanceof Orden) {
                    Orden orden = (Orden) valor;
                    setText(orden.getCodigo());
                }

                return this;
            }
        });

        // Calculamos el total de la orden seleccionada.
        calcularTotal();
    }

    // Calcula el subtotal, el ISV del 15 % y el total.
    private void calcularTotal() {

        Orden orden = (Orden) comboOrdenes.getSelectedItem();

        if (orden == null) {
            txtImpuesto.setText("");
            txtSubtotal.setText("");
            txtTotal.setText("");
            return;
        }

        double subtotal = orden.getTotal();

        // Calculamos el monto correspondiente al 15 % de ISV.
        double montoISV = subtotal * ISV_PORCENTAJE / 100;

        // Sumamos el subtotal y el ISV.
        double total = subtotal + montoISV;

        // Mantenemos el porcentaje fijo en el controlador.
        facturaControlador.setImpuestoPorcentaje(ISV_PORCENTAJE);

        // Mostramos los montos con el formato de lempiras.
        txtImpuesto.setText(formatoLempiras(montoISV));
        txtSubtotal.setText(formatoLempiras(subtotal));
        txtTotal.setText(formatoLempiras(total));
    }

    // Procesa el pago y registra la factura.
    private void registrarFactura() {

        Orden orden = (Orden) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hay órdenes finalizadas disponibles."
            );
            return;
        }

        try {

            // El usuario introduce únicamente el valor numérico.
            double efectivo = Double.parseDouble(
                txtEfectivo.getText().trim()
            );

            if (efectivo < 0) {
                throw new IllegalArgumentException(
                    "El efectivo no puede ser negativo."
                );
            }

            // El impuesto siempre será del 15 %.
            facturaControlador.setImpuestoPorcentaje(ISV_PORCENTAJE);

            // Generamos un código consecutivo para la factura.
            String codigo = String.format(
                "FAC%03d",
                facturaControlador.cantidadFacturas() + 1
            );

            // Procesamos el pago y registramos la factura.
            Factura factura = facturaControlador.procesarFactura(
                codigo,
                orden,
                efectivo
            );

            // Mostramos el cambio con el formato de lempiras.
            txtCambio.setText(formatoLempiras(factura.getCambio()));

            JOptionPane.showMessageDialog(
                this,
                "Factura registrada correctamente.\n"
                + "Código: " + factura.getCodigo() + "\n"
                + "Subtotal: " + formatoLempiras(factura.getSubtotal())
                + "\nISV (15 %): "
                + formatoLempiras(factura.getMontoImpuesto())
                + "\nTotal: " + formatoLempiras(factura.getTotal())
                + "\nCambio: " + formatoLempiras(factura.getCambio())
            );

            // Limpiamos los campos después de registrar correctamente.
            txtEfectivo.setText("");
            txtCambio.setText("");
            txtImpuesto.setText("");
            txtSubtotal.setText("");
            txtTotal.setText("");

            // Actualizamos la tabla y las órdenes disponibles.
            actualizarTabla();
            actualizarOrdenes();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Introduce un valor numérico válido."
            );

        } catch (PagoInsuficienteException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error de facturación",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // Actualiza la tabla con todas las facturas registradas.
    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Factura factura : facturaControlador.listarFacturas()) {

            Object[] fila = {
                factura.getCodigo(),
                factura.getOrden().getCodigo(),
                factura.getFecha(),
                formatoLempiras(factura.getSubtotal()),
                formatoLempiras(factura.getMontoImpuesto()),
                formatoLempiras(factura.getTotal()),
                formatoLempiras(factura.getPagoRecibido()),
                formatoLempiras(factura.getCambio())
            };

            modeloTabla.addRow(fila);
        }
    }
}