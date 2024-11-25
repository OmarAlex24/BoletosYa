package com.omar.swing;

import com.google.protobuf.ServiceException;
import com.omar.entity.Aeropuerto;
import com.omar.entity.Vuelo;
import com.omar.service.AeropuertoService;
import com.omar.service.VueloService;
import com.toedter.calendar.JDateChooser;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class BusquedaVueloFrame extends JFrame {
    private JComboBox<String> comboOrigen;
    private JComboBox<String> comboDestino;
    private JDateChooser dateChooser;
    private JButton btnBuscar;

    private final AeropuertoService aeropuertoService;
    private final VueloService vueloService;

    public BusquedaVueloFrame(AeropuertoService aeropuertoService, VueloService vueloService) {
        this.aeropuertoService = aeropuertoService;
        this.vueloService = vueloService;
        setTitle("Búsqueda de Vuelos");
        initComponents();
        configurarAutocompletado();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuración del autocompletado para origen
        comboOrigen = new JComboBox<>();
        comboOrigen.setEditable(true);
        AutoCompleteDecorator.decorate(comboOrigen);

        // Configuración del autocompletado para destino
        comboDestino = new JComboBox<>();
        comboDestino.setEditable(true);
        AutoCompleteDecorator.decorate(comboDestino);

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

        btnBuscar = new JButton("Buscar Vuelos");
        gbc.gridx = 1; gbc.gridy = 3;
        add(btnBuscar, gbc);

        btnBuscar.addActionListener(e -> buscarVuelos());
    }

    private void configurarAutocompletado() {
        comboOrigen.getEditor().getEditorComponent().addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String texto = comboOrigen.getEditor().getItem().toString();
                        actualizarSugerencias(comboOrigen, texto);
                    }
                }
        );
    }

    private void actualizarSugerencias(JComboBox<String> combo, String texto) {
        try {
            List<Aeropuerto> aeropuertos =
                    aeropuertoService.buscarPorNombreOCodigo(texto);

            combo.removeAllItems();
            for (Aeropuerto aeropuerto : aeropuertos) {
                combo.addItem(aeropuerto.getCodigo() + " - " +
                        aeropuerto.getCiudad());
            }
            combo.showPopup();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar aeropuertos: " + ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
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

            LocalDate fecha = selectedDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            // Validar fecha
            if (fecha.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "La fecha seleccionada no puede ser anterior a hoy",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener aeropuertos seleccionados
            String origenTexto = comboOrigen.getSelectedItem().toString();
            String destinoTexto = comboDestino.getSelectedItem().toString();

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
            SeleccionVueloFrame seleccionVuelo = new SeleccionVueloFrame(vuelosDisponibles, aeropuertoService, vueloService);
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

    private Aeropuerto obtenerAeropuerto(String texto) throws Exception {
        // Extraer código de aeropuerto (asumiendo formato "XXX - Ciudad")
        String codigo = texto.split("-")[0].trim();

        // Buscar aeropuerto por código
        Aeropuerto aeropuerto = aeropuertoService.buscarPorCodigo(codigo);

        if (aeropuerto == null) {
            throw new ServiceException("No se encontró el aeropuerto con código: " + codigo);
        }

        return aeropuerto;
    }
}