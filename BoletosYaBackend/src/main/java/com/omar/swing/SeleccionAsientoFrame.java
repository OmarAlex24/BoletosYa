package com.omar.swing;

import com.omar.entity.Asiento;
import com.omar.entity.Vuelo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class SeleccionAsientoFrame extends JFrame {
    private JPanel panelAsientos;
    private final Vuelo vuelo;
    private JButton botonAsientoSeleccionado;
    private Asiento asientoSeleccionado;
    private static final int GRID_SIZE = 5;
    private static final Color COLOR_DISPONIBLE = new Color(144, 238, 144); // Verde claro
    private static final Color COLOR_NO_DISPONIBLE = new Color(211, 211, 211); // Gris claro
    private static final Color COLOR_SELECCIONADO = new Color(0, 0, 250); // Azul fuerte

    public SeleccionAsientoFrame(Vuelo vuelo) throws SQLException {
        this.vuelo = vuelo;
        setTitle("Selección de Asiento - BoletosYa");
        initComponents();
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de información del vuelo con estilo
        JPanel panelInfo = createInfoPanel();
        mainPanel.add(panelInfo, BorderLayout.NORTH);

        // Panel de asientos
        JPanel seatContainer = createSeatPanel();
        mainPanel.add(seatContainer, BorderLayout.CENTER);

        // Leyenda de colores
        JPanel legendPanel = createLegendPanel();
        mainPanel.add(legendPanel, BorderLayout.SOUTH);

        // Botón de confirmación
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener(e -> {
            try {
                seleccionarAsiento();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnPanel.add(btnConfirmar);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panelInfo.setBackground(new Color(248, 248, 248));

        // Título del vuelo
        JLabel titleLabel = new JLabel("Vuelo " + vuelo.getCodigo(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Ruta
        JLabel rutaLabel = new JLabel(vuelo.getOrigen().getCiudad() + " → " + vuelo.getDestino().getCiudad(),
                SwingConstants.CENTER);
        rutaLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Fecha y hora
        JLabel fechaLabel = new JLabel("Salida: " + vuelo.getFechaSalida(),
                SwingConstants.CENTER);
        fechaLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panelInfo.add(titleLabel);
        panelInfo.add(rutaLabel);
        panelInfo.add(fechaLabel);

        return panelInfo;
    }

    private JPanel createSeatPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Seleccione su asiento",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        panelAsientos = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 8, 8));
        panelAsientos.setBorder(new EmptyBorder(20, 20, 20, 20));
        crearAsientos();

        container.add(panelAsientos, BorderLayout.CENTER);
        return container;
    }

    private void crearAsientos() {
        // Obtener la lista de asientos del vuelo
        java.util.List<Asiento> asientosVuelo = vuelo.getAsientos();
        int totalAsientos = GRID_SIZE * GRID_SIZE;

        for (int i = 0; i < totalAsientos; i++) {
            if (i < asientosVuelo.size()) {
                // Crear botón para asiento existente
                Asiento asiento = asientosVuelo.get(i);
                JButton btnAsiento = createSeatButton(asiento);
                panelAsientos.add(btnAsiento);
            } else {
                // Crear espacio vacío para mantener el grid
                JPanel emptyPanel = new JPanel();
                emptyPanel.setOpaque(false);
                panelAsientos.add(emptyPanel);
            }
        }
    }

    private JButton createSeatButton(Asiento asiento) {
        JButton btnAsiento = new JButton(asiento.getNumeroAsiento());
        btnAsiento.setPreferredSize(new Dimension(60, 60));
        btnAsiento.setFont(new Font("Arial", Font.BOLD, 14));
        btnAsiento.setFocusPainted(false);
        btnAsiento.setOpaque(true);

        if (!asiento.isDisponible()) {
            btnAsiento.setBackground(COLOR_NO_DISPONIBLE);
            btnAsiento.setEnabled(false);
        } else {
            btnAsiento.setBackground(COLOR_DISPONIBLE);
            btnAsiento.addActionListener(e -> seleccionarAsiento((JButton)e.getSource(), asiento));
        }

        return btnAsiento;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legendPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addLegendItem(legendPanel, "Disponible", COLOR_DISPONIBLE);
        addLegendItem(legendPanel, "No Disponible", COLOR_NO_DISPONIBLE);
        addLegendItem(legendPanel, "Seleccionado", COLOR_SELECCIONADO);

        return legendPanel;
    }

    private void addLegendItem(JPanel panel, String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));

        item.add(colorBox);
        item.add(label);
        panel.add(item);
    }

    private void seleccionarAsiento(JButton boton, Asiento asiento) {
        // Si el botón seleccionado no es el mismo que el que se está seleccionando
        if (botonAsientoSeleccionado != null) {
            // Restaurar el color del botón previamente seleccionado
            botonAsientoSeleccionado.setBackground(COLOR_DISPONIBLE);
        }

        // Actualizar el botón actual
        botonAsientoSeleccionado = boton;
        asientoSeleccionado = asiento;
        boton.setBackground(COLOR_SELECCIONADO); // Cambiar color

        // Mostrar confirmación
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea seleccionar el asiento " + asiento.getNumeroAsiento() + "?",
                "Confirmar selección",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.NO_OPTION) {
            // Restaurar color si se cancela la selección
            boton.setBackground(COLOR_DISPONIBLE);
            asientoSeleccionado = null;
            botonAsientoSeleccionado = null;
        }
    }

    private void seleccionarAsiento() throws Exception {
        if (asientoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un asiento",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ConfirmacionBoletoFrame confirmacionBoleto = new ConfirmacionBoletoFrame(vuelo, asientoSeleccionado);
        confirmacionBoleto.setLocationRelativeTo(this);
        confirmacionBoleto.setVisible(true);
        this.dispose();
    }
}