package com.omar.swing;

import com.google.protobuf.ServiceException;
import com.omar.entity.Asiento;
import com.omar.entity.Cliente;
import com.omar.entity.Reservacion;
import com.omar.entity.Vuelo;
import com.omar.service.ReservacionService;

import javax.swing.*;
import java.awt.*;

public class ConfirmacionReservaFrame extends JFrame {
    private final Vuelo vuelo;
    private final Asiento asiento;
    private final Cliente cliente;
    private final ReservacionService reservacionService;

    public ConfirmacionReservaFrame(Vuelo vuelo, Asiento asiento, Cliente cliente, ReservacionService reservacionService) {
        this.reservacionService = reservacionService;
        this.vuelo = vuelo;
        this.asiento = asiento;
        this.cliente = cliente;

        setTitle("Confirmación de Reserva");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel de detalles
        JPanel panelDetalles = new JPanel(new GridLayout(0, 2, 5, 5));
        panelDetalles.setBorder(BorderFactory.createTitledBorder(
                "Detalles de la Reserva"));

        // Agregar información
        agregarDetalle(panelDetalles, "Vuelo:",
                vuelo.getAerolinea().getCodigo() + vuelo.getId());
        agregarDetalle(panelDetalles, "Origen:",
                vuelo.getOrigen().getCiudad());
        agregarDetalle(panelDetalles, "Destino:",
                vuelo.getDestino().getCiudad());
        agregarDetalle(panelDetalles, "Fecha Salida:",
                String.valueOf((vuelo.getFechaSalida())));
        agregarDetalle(panelDetalles, "Fecha Llegada:",
                String.valueOf((vuelo.getFechaLlegada())));
        agregarDetalle(panelDetalles, "Asiento:", asiento.getNumeroAsiento());
        agregarDetalle(panelDetalles, "Precio:",
                String.format("$%.2f", vuelo.getPrecio()));

        add(panelDetalles, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton("Confirmar Reserva");
        JButton btnCancelar = new JButton("Cancelar");

        btnConfirmar.addActionListener(e -> confirmarReserva());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarDetalle(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }

    private void confirmarReserva() {
        try {
            // Lógica para confirmar la reserva
            Reservacion reserva = new Reservacion(cliente, asiento);
            reservacionService.crear(reserva);

            JOptionPane.showMessageDialog(this,
                    "Reserva confirmada exitosamente");
            dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al confirmar la reserva: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
