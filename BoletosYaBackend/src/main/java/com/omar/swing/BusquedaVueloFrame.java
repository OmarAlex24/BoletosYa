package com.omar.swing;

import com.omar.LoginForm;
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
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes

        // Título
        JLabel titleLabel = new JLabel("¿A dónde quieres ir?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Etiqueta y combo de origen
        JLabel origenLabel = new JLabel("Origen:");
        origenLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(origenLabel, gbc);

        comboOrigen = new JComboBox<>();
        comboOrigen.setPreferredSize(new Dimension(200, 30));
        comboOrigen.addActionListener(_ -> actualizarDestinos());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(comboOrigen, gbc);

        // Etiqueta y combo de destino
        JLabel destinoLabel = new JLabel("Destino:");
        destinoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(destinoLabel, gbc);

        comboDestino = new JComboBox<>();
        comboDestino.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(comboDestino, gbc);

        // Etiqueta y selector de fecha
        JLabel fechaLabel = new JLabel("Fecha:");
        fechaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(fechaLabel, gbc);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(dateChooser, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Botón de cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente y tamaño
        btnCerrarSesion.setForeground(Color.BLACK); // Texto negro
        btnCerrarSesion.setBackground(Color.WHITE); // Fondo blanco
        btnCerrarSesion.setFocusPainted(false); // Quitar borde de enfoque
        btnCerrarSesion.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
        btnCerrarSesion.setPreferredSize(new Dimension(120, 30)); // Tamaño reducido
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelBotones.add(btnCerrarSesion);

        // Botón de búsqueda
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente y tamaño
        btnBuscar.setForeground(Color.BLACK); // Texto negro
        btnBuscar.setBackground(Color.WHITE); // Fondo blanco
        btnBuscar.setFocusPainted(false); // Quitar borde de enfoque
        btnBuscar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
        btnBuscar.setPreferredSize(new Dimension(120, 30)); // Tamaño reducido
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        btnBuscar.addActionListener(_ -> buscarVuelos());

        panelBotones.add(btnBuscar);
        add(panelBotones, gbc);
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
            String origenTexto = Objects.requireNonNull(comboOrigen.getSelectedItem()).toString().substring(0, 3);
            String destinoTexto = Objects.requireNonNull(comboDestino.getSelectedItem()).toString().substring(0, 3);

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

    private void cerrarSesion() {
        // Volver al frame de login
        try {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose(); // Cerrar el frame actual
    }
}