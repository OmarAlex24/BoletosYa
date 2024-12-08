package com.omar.swing;

import com.omar.entity.Aeropuerto;
import com.omar.entity.Vuelo;
import com.omar.service.AeropuertoService;
import com.omar.service.ServiceFactory;
import com.omar.service.VueloService;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BusquedaVueloFrame extends JFrame {
    private JComboBox<String> comboOrigen;
    private JComboBox<String> comboDestino;
    private JDateChooser dateChooser;
    private final List<Aeropuerto> todosAeropuertos;

    private final VueloService vueloService;

    public BusquedaVueloFrame() throws Exception {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AeropuertoService aeropuertoService = serviceFactory.getAeropuertoService();
        this.vueloService = serviceFactory.getVueloService();
        this.todosAeropuertos = aeropuertoService.listarTodos();
        setTitle("Búsqueda de Vuelos");
        initComponents();
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cargarAeropuertos();
    }

    private void initComponents() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuración de los combos
        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();

        // Agregar listener al combo de origen
        comboOrigen.addActionListener(_ -> actualizarDestinos());

        // Agregar JDateChooser
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        add(dateChooser, gbc);

        // Agregar componentes
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Origen:"), gbc);

        gbc.gridx = 1;
        add(comboOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Destino:"), gbc);

        gbc.gridx = 1;
        add(comboDestino, gbc);

        JButton btnBuscar = new JButton("Buscar Vuelos");
        gbc.gridx = 1; gbc.gridy = 3;
        add(btnBuscar, gbc);

        btnBuscar.addActionListener(_ -> buscarVuelos());
    }
    private void cargarAeropuertos() {
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();

        for (Aeropuerto aeropuerto : todosAeropuertos) {
            String item = aeropuerto.getCodigo() + " - " + aeropuerto.getCiudad();
            comboOrigen.addItem(item);
            comboDestino.addItem(item);
        }
    }

    private void actualizarDestinos() {
        String origenSeleccionado = (String) comboOrigen.getSelectedItem();
        comboDestino.removeAllItems();

        for (Aeropuerto aeropuerto : todosAeropuertos) {
            String item = aeropuerto.getCodigo() + " - " + aeropuerto.getCiudad();
            if (!item.equals(origenSeleccionado)) {
                comboDestino.addItem(item);
            }
        }
    }

    private void buscarVuelos() {
        try {
            // Obtener fecha seleccionada
            Date selectedDate = dateChooser.getDate();

            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione una fecha",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime fecha = selectedDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

            // Validar fecha
            if (fecha.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this,
                        "La fecha seleccionada no puede ser anterior a hoy",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener aeropuertos seleccionados
            String origenTexto = Objects.requireNonNull(comboOrigen.getSelectedItem()).toString().substring(0,3);
            String destinoTexto = Objects.requireNonNull(comboDestino.getSelectedItem()).toString().substring(0,3);

            // Validar que origen y destino sean diferentes
            if (origenTexto.equals(destinoTexto)) {
                JOptionPane.showMessageDialog(this,
                        "El origen y destino no pueden ser iguales",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar vuelos disponibles
            List<Vuelo> vuelosDisponibles = vueloService.buscarVuelosDirectos(origenTexto, destinoTexto, fecha);

            if (vuelosDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron vuelos disponibles para la fecha seleccionada",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Abrir ventana de selección de vuelo
            SeleccionVueloFrame seleccionVuelo = new SeleccionVueloFrame(vuelosDisponibles);
            seleccionVuelo.setLocationRelativeTo(this);
            seleccionVuelo.setVisible(true);
            this.dispose(); // Cerrar ventana actual

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}