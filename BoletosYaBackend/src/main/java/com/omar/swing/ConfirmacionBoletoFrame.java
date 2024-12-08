package com.omar.swing;

import com.omar.entity.Asiento;
import com.omar.entity.Vuelo;
import com.omar.service.AsientoService;
import com.omar.service.ServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;

public class ConfirmacionBoletoFrame extends JFrame {
    private final Asiento asiento;
    private final Vuelo vuelo;
    private final AsientoService asientoService;

    public ConfirmacionBoletoFrame(Vuelo vuelo, Asiento asiento) throws SQLException, MalformedURLException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.asientoService = serviceFactory.getAsientoService();
        this.vuelo = vuelo;
        this.asiento = asiento;

        // Configuración básica de la ventana
        setTitle("Confirmar datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() throws MalformedURLException {
        // Configuración del layout principal
        setLayout(new BorderLayout(10, 10));
        setBorderPadding();

        // Título
        JLabel titleLabel = new JLabel("Confirmar datos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel intermedio (3x2) para los detalles de vuelo
        JPanel detallesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        detallesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar los detalles de vuelo
        detallesPanel.add(createFieldPanel("Aerolinea:", vuelo.getAerolinea().getNombre()));
        detallesPanel.add(createFieldPanel("Asiento:", asiento.getNumeroAsiento()));

        detallesPanel.add(createFieldPanel("Origen:", vuelo.getOrigen().getNombre() + ", " + vuelo.getOrigen().getCiudad() + ", " + vuelo.getOrigen().getPais()));
        detallesPanel.add(createFieldPanel("Destino:", vuelo.getDestino().getNombre() + ", " + vuelo.getDestino().getCiudad() + ", " + vuelo.getDestino().getPais()));

        detallesPanel.add(createFieldPanel("Fecha salida:", vuelo.getFechaSalida().getDayOfMonth() + "/" + vuelo.getFechaSalida().getMonthValue() + "/" + vuelo.getFechaSalida().getYear()));
        detallesPanel.add(createFieldPanel("Fecha llegada:", vuelo.getFechaLlegada().getDayOfMonth() + "/" + vuelo.getFechaLlegada().getMonthValue() + "/" + vuelo.getFechaLlegada().getYear()));

        add(detallesPanel, BorderLayout.CENTER);

        // Panel inferior (2x1) para horarios y botones
        JPanel inferiorPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel de horarios y precio (1x3)
        JPanel horariosPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        horariosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        horariosPanel.add(createFieldPanel("Horario salida:", (vuelo.getFechaSalida().getHour() + ":" + (vuelo.getFechaSalida().getMinute() == 0 ? "00" : vuelo.getFechaSalida().getMinute()))));
        horariosPanel.add(createFieldPanel("Horario llegada:", (vuelo.getFechaLlegada().getHour() + ":" + (vuelo.getFechaLlegada().getMinute() == 0 ? "00" : vuelo.getFechaLlegada().getMinute()))));
        horariosPanel.add(createFieldPanel("Precio:", "$ "+ vuelo.getPrecio()));

        inferiorPanel.add(horariosPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Cargar íconos desde el classpath
        ImageIcon cancelarIcon = new ImageIcon(getClass().getResource("/assets/images/cancel.png"));
        ImageIcon confirmarIcon = new ImageIcon(getClass().getResource("/assets/images/confirm.png"));

        JButton cancelarButton = new JButton("Cancelar", cancelarIcon);
        JButton confirmarButton = new JButton("Confirmar", confirmarIcon);

        // Estilo de los botones
        cancelarButton.setPreferredSize(new Dimension(150, 60));
        confirmarButton.setPreferredSize(new Dimension(150, 60));

        // Añadir action listeners
        cancelarButton.addActionListener(e -> cancelarReserva());
        confirmarButton.addActionListener(e -> confirmarReserva());

        buttonPanel.add(cancelarButton);
        buttonPanel.add(confirmarButton);

        inferiorPanel.add(buttonPanel);

        add(inferiorPanel, BorderLayout.SOUTH);
    }

    private JPanel createFieldPanel(String labelText, String fieldText) {
        // Crear un panel para apilar el JLabel y el JTextField
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Espaciado interno del panel

        // Crear el JLabel
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear completamente a la izquierda

        // Crear el JTextField
        JTextField field = new JTextField(fieldText);
        field.setEditable(false);
        field.setFont(new Font("Arial", Font.PLAIN, 16));

        // Calcular el ancho del texto
        FontMetrics metrics = field.getFontMetrics(field.getFont());
        int textWidth = metrics.stringWidth(fieldText); // Ancho del texto
        int textHeight = metrics.getHeight(); // Altura del texto

        // Establecer el tamaño preferido del JTextField (ancho + padding)
        int padding = 5;
        field.setSize(new Dimension(textWidth + padding * 2, textHeight + padding));

        // Agregar un borde compuesto para simular padding
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), // Borde externo
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding interno
        ));

        field.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear completamente a la izquierda

        // Añadir el JLabel y el JTextField al panel
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(5)); // Espaciado entre el JLabel y el JTextField
        fieldPanel.add(field);

        return fieldPanel;
    }

    private void setBorderPadding() {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void confirmarReserva() {
        try {
            asientoService.alternarDisponibilidad(asiento);
            JOptionPane.showMessageDialog(this, "Reserva confirmada");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al confirmar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva() {
        try {
            BusquedaVueloFrame busqueda = new BusquedaVueloFrame();
            busqueda.setLocationRelativeTo(this);
            busqueda.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cancelar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}