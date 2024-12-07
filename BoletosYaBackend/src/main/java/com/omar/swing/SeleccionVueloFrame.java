package com.omar.swing;

import com.omar.BD;
import com.omar.entity.Vuelo;
import com.omar.service.AerolineaService;
import com.omar.service.AeropuertoService;
import com.omar.service.ServiceFactory;
import com.omar.service.VueloService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SeleccionVueloFrame extends JFrame {
    private JTable tablaVuelos;
    private DefaultTableModel modeloTabla;
    private List<Vuelo> vuelos;
    private final AeropuertoService aeropuertoService;
    private final VueloService vueloService;
    private final Connection connection;

    public SeleccionVueloFrame(List<Vuelo> vuelos) throws SQLException {
        this.vuelos = vuelos;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.connection = BD.getInstance().getConnection();
        this.aeropuertoService = serviceFactory.getAeropuertoService();
        this.vueloService = serviceFactory.getVueloService();
        setTitle("Selección de Vuelo");
        initComponents();
        cargarVuelos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Configurar tabla
        String[] columnas = {"Vuelo", "Salida", "Llegada", "Duración", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVuelos = new JTable(modeloTabla);
        tablaVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaVuelos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSeleccionar = new JButton("Seleccionar Vuelo");
        JButton btnVolver = new JButton("Volver");

        btnSeleccionar.addActionListener(e -> seleccionarVuelo());
        btnVolver.addActionListener(e -> {
            try {
                volver();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar ventana
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cargarVuelos() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Vuelo vuelo : vuelos) {
            modeloTabla.addRow(new Object[]{
                    vuelo.getAerolinea().getCodigo() + vuelo.getId(),
                    vuelo.getFechaSalida().format(formatter),
                    vuelo.getFechaLlegada().format(formatter),
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

    private void seleccionarVuelo() {
        int filaSeleccionada = tablaVuelos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un vuelo",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vuelo vueloSeleccionado = vuelos.get(filaSeleccionada);
        System.out.println(vueloSeleccionado);
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
}
