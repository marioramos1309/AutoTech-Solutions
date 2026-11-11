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

import modelo.Orden;
import modelo.DetalleServicio;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PanelReportes extends JPanel {

    // Controlador que proporciona las órdenes del sistema.
    private final OrdenControlador ordenControlador;

    // Paneles que dibujarán los gráficos mediante Java2D.
    private GraficoBarras graficoIngresos;
    private GraficoBarras graficoEstados;

    // Constructor del panel de reportes.
    public PanelReportes(OrdenControlador ordenControlador) {

        this.ordenControlador = ordenControlador;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        crearInterfaz();
        actualizarReportes();
    }

    // Construye la interfaz principal de reportes.
    private void crearInterfaz() {

        JLabel titulo = new JLabel("Reportes del Sistema");

        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(23, 43, 77));

        JButton btnActualizar = new JButton("Actualizar reportes");

        btnActualizar.setBackground(new Color(35, 90, 150));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);

        btnActualizar.addActionListener(e -> actualizarReportes());

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(btnActualizar, BorderLayout.EAST);

        add(encabezado, BorderLayout.NORTH);

        // Contenedor de los dos gráficos.
        JPanel contenedorGraficos = new JPanel(new GridLayout(1, 2, 15, 15));
        contenedorGraficos.setOpaque(false);

        graficoIngresos = new GraficoBarras("Ingresos por servicio");

        graficoEstados = new GraficoBarras("Órdenes por estado");

        contenedorGraficos.add(graficoIngresos);
        contenedorGraficos.add(graficoEstados);

        add(contenedorGraficos, BorderLayout.CENTER);
    }

    // Obtiene los datos y actualiza los dos gráficos.
    public void actualizarReportes() {

        ArrayList<Orden> ordenes = ordenControlador.listarOrdenes();

        // Guarda los ingresos acumulados por servicio.
        Map<String, Double> ingresosPorServicio = new LinkedHashMap<>();

        // Guarda la cantidad de órdenes por estado.
        Map<String, Double> ordenesPorEstado = new LinkedHashMap<>();

        for (Orden orden : ordenes) {

            // Contamos las órdenes según su estado.
            String estado = orden.getEstado();

            ordenesPorEstado.put(
                estado,
                ordenesPorEstado.getOrDefault(estado, 0.0) + 1
            );

            // Para los ingresos solo contamos órdenes finalizadas.
            if ("Finalizada".equalsIgnoreCase(estado)) {

                for (DetalleServicio detalle : orden.getServicios()) {

                    String nombreServicio =
                        detalle.getServicio().getNombre();

                    double ingreso = detalle.getPrecioUnitario();

                    ingresosPorServicio.put(
                        nombreServicio,
                        ingresosPorServicio.getOrDefault(nombreServicio, 0.0)
                            + ingreso
                    );
                }
            }
        }

        // Enviamos los datos a los gráficos.
        graficoIngresos.setDatos(ingresosPorServicio);

        graficoEstados.setDatos(ordenesPorEstado);
    }

    // Clase interna encargada de dibujar los gráficos con Java2D.
    private class GraficoBarras extends JPanel {

        private String titulo;
        private Map<String, Double> datos;

        public GraficoBarras(String titulo) {

            this.titulo = titulo;
            this.datos = new LinkedHashMap<>();

            setBackground(Color.WHITE);

            setBorder(
                BorderFactory.createLineBorder(
                    new Color(215, 220, 230)
                )
            );
        }

        // Recibe los datos que se deben representar.
        public void setDatos(Map<String, Double> nuevosDatos) {

            this.datos = new LinkedHashMap<>(nuevosDatos);

            repaint();
        }

        // Dibuja el gráfico utilizando Java2D.
        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            // Suavizamos los bordes y textos.
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int ancho = getWidth();
            int alto = getHeight();

            // Título del gráfico.
            g2.setColor(new Color(23, 43, 77));

            g2.setFont(new Font("Arial", Font.BOLD, 17));

            g2.drawString(titulo, 20, 30);

            // Mensaje cuando todavía no existen datos.
            if (datos.isEmpty()) {

                g2.setFont(new Font("Arial", Font.PLAIN, 14));

                g2.setColor(Color.GRAY);

                g2.drawString(
                    "No hay datos disponibles.",
                    30,
                    alto / 2
                );

                g2.dispose();

                return;
            }

            int margenIzquierdo = 65;
            int margenDerecho = 25;
            int margenSuperior = 65;
            int margenInferior = 100;

            int anchoGrafico =
                ancho - margenIzquierdo - margenDerecho;

            int altoGrafico =
                alto - margenSuperior - margenInferior;

            if (anchoGrafico <= 0 || altoGrafico <= 0) {
                g2.dispose();
                return;
            }

            // Buscamos el valor máximo para escalar las barras.
            double maximo = 0;

            for (double valor : datos.values()) {
                maximo = Math.max(maximo, valor);
            }

            if (maximo == 0) {
                maximo = 1;
            }

            // Dibujamos las líneas horizontales de referencia.
            g2.setColor(new Color(225, 230, 238));

            for (int i = 0; i <= 4; i++) {

                int y = margenSuperior + altoGrafico
                    - (i * altoGrafico / 4);

                g2.drawLine(
                    margenIzquierdo,
                    y,
                    margenIzquierdo + anchoGrafico,
                    y
                );

                double valorReferencia = maximo * i / 4.0;

                g2.setFont(new Font("Arial", Font.PLAIN, 10));

                g2.setColor(Color.DARK_GRAY);

                String etiqueta = String.format("%.0f", valorReferencia);

                g2.drawString(etiqueta, 12, y + 4);

                g2.setColor(new Color(225, 230, 238));
            }

            // Dibujamos los ejes.
            g2.setColor(Color.DARK_GRAY);

            g2.drawLine(
                margenIzquierdo,
                margenSuperior,
                margenIzquierdo,
                margenSuperior + altoGrafico
            );

            g2.drawLine(
                margenIzquierdo,
                margenSuperior + altoGrafico,
                margenIzquierdo + anchoGrafico,
                margenSuperior + altoGrafico
            );

            // Calculamos el tamaño de cada barra.
            int cantidad = datos.size();

            int espacioPorBarra = anchoGrafico / cantidad;

            int anchoBarra = Math.max(
                12,
                Math.min(55, espacioPorBarra / 2)
            );

            int indice = 0;

            Color[] colores = {
                new Color(35, 90, 150),
                new Color(40, 145, 110),
                new Color(230, 150, 50),
                new Color(130, 95, 180),
                new Color(200, 85, 85)
            };

            // Dibujamos cada barra y su etiqueta.
            for (Map.Entry<String, Double> entrada : datos.entrySet()) {

                double valor = entrada.getValue();

                int alturaBarra = (int) (
                    (valor / maximo) * altoGrafico
                );

                int x = margenIzquierdo
                    + indice * espacioPorBarra
                    + (espacioPorBarra - anchoBarra) / 2;

                int y = margenSuperior + altoGrafico - alturaBarra;

                // Dibujamos la barra.
                g2.setColor(colores[indice % colores.length]);

                g2.fillRoundRect(
                    x,
                    y,
                    anchoBarra,
                    alturaBarra,
                    8,
                    8
                );

                // Mostramos el valor encima de la barra.
                g2.setColor(Color.DARK_GRAY);

                g2.setFont(new Font("Arial", Font.BOLD, 11));

                String valorTexto;

                if (titulo.equals("Ingresos por servicio")) {

                    valorTexto = String.format("L.%.0f", valor);

                } else {

                    valorTexto = String.format("%.0f", valor);
                }

                g2.drawString(
                    valorTexto,
                    x - 5,
                    Math.max(margenSuperior - 5, y - 7)
                );

                // Dibujamos las etiquetas inclinadas para aprovechar espacio.
                AffineTransform transformacion = g2.getTransform();

                g2.translate(
                    x + anchoBarra / 2,
                    margenSuperior + altoGrafico + 12
                );

                g2.rotate(Math.toRadians(40));

                g2.setFont(new Font("Arial", Font.PLAIN, 10));

                g2.drawString(entrada.getKey(), 0, 0);

                g2.setTransform(transformacion);

                indice++;
            }

            g2.dispose();
        }
    }
}
