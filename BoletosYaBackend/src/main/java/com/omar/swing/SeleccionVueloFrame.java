package com.omar.swing;

import com.omar.entity.Vuelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SeleccionVueloFrame extends JFrame {
    private JTable tablaVuelos;
    private DefaultTableModel modeloTabla;
    private final List<Vuelo> vuelos;

    public SeleccionVueloFrame(List<Vuelo> vuelos) {
        this.vuelos = vuelos;
        setTitle("Selección de Vuelo");
        initComponents();
        cargarVuelos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel titleLabel = new JLabel("Tenemos estos vuelos");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Configurar tabla
        String[] columnas = {"Aerolínea", "Fecha Salida", "Hora Salida", "Fecha Llegada", "Hora Llegada", "Duración", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVuelos = new JTable(modeloTabla);
        tablaVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVuelos.setRowHeight(25); // Altura de las filas
        tablaVuelos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Fuente del encabezado
        tablaVuelos.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente de las celdas

        // Agregar tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaVuelos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSeleccionar = new JButton("Seleccionar Vuelo");
        JButton btnVolver = new JButton("Volver");

        // Estilizar botones
        estilizarBoton(btnSeleccionar);
        estilizarBoton(btnVolver);

        btnSeleccionar.addActionListener(_ -> {
            try {
                seleccionarVuelo();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnVolver.addActionListener(_ -> {
            try {
                volver();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        panelBotones.add(btnVolver);
        panelBotones.add(btnSeleccionar);
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar ventana
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cargarVuelos() {
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Vuelo vuelo : vuelos) {
            modeloTabla.addRow(new Object[]{
                    vuelo.getAerolinea().getNombre(),
                    vuelo.getFechaSalida().format(fechaFormatter),
                    vuelo.getFechaSalida().format(horaFormatter),
                    vuelo.getFechaLlegada().format(fechaFormatter),
                    vuelo.getFechaLlegada().format(horaFormatter),
                    calcularDuracion(vuelo),
                    String.format("$%.2f", vuelo.getPrecio())
            });
        }
    }

    private String calcularDuracion(Vuelo vuelo) {
        Duration duracion = Duration.between(vuelo.getFechaSalida(), vuelo.getFechaLlegada());
        long horas = duracion.toHours();
        long minutos = duracion.toMinutesPart();
        return String.format("%dh %dm", horas, minutos);
    }

    private void seleccionarVuelo() throws SQLException {
        int filaSeleccionada = tablaVuelos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un vuelo",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vuelo vueloSeleccionado = vuelos.get(filaSeleccionada);
        SeleccionAsientoFrame seleccionAsiento = new SeleccionAsientoFrame(vueloSeleccionado);
        seleccionAsiento.setLocationRelativeTo(this);
        seleccionAsiento.setVisible(true);
        this.dispose();
    }

    private void volver() throws Exception {
        BusquedaVueloFrame busqueda = new BusquedaVueloFrame();
        busqueda.setLocationRelativeTo(this);
        busqueda.setVisible(true);
        this.dispose();
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente y tamaño
        boton.setForeground(Color.BLACK); // Texto negro
        boton.setBackground(Color.WHITE); // Fondo blanco
        boton.setFocusPainted(false); // Quitar borde de enfoque
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
        boton.setPreferredSize(new Dimension(150, 30)); // Tamaño reducido
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
    }
}