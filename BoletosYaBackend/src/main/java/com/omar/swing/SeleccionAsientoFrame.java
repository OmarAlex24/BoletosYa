package com.omar.swing;

import com.omar.entity.Asiento;
import com.omar.entity.Vuelo;

import javax.swing.*;
import java.awt.*;

public class SeleccionAsientoFrame extends JFrame {
    private JPanel panelAsientos;
    private final Vuelo vuelo;
    private JButton asientoSeleccionado;

    public SeleccionAsientoFrame(Vuelo vuelo) {
        this.vuelo = vuelo;
        setTitle("Selección de Asiento");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de información del vuelo
        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 5, 5));
        panelInfo.add(new JLabel("Vuelo: " +
                vuelo.getAerolinea().getCodigo() + vuelo.getId()));
        panelInfo.add(new JLabel("Fecha: " +(vuelo.getFechaSalida())));
        add(panelInfo, BorderLayout.NORTH);

        // Panel de asientos
        panelAsientos = new JPanel(new GridLayout(6, 6, 2, 2));
        crearAsientos();
        add(new JScrollPane(panelAsientos), BorderLayout.CENTER);
    }

    private void crearAsientos() {
        for (Asiento asiento : vuelo.getAsientos()) {
            JButton btnAsiento = new JButton(asiento.getNumeroAsiento());
            btnAsiento.setPreferredSize(new Dimension(50, 50));

            if (!asiento.isDisponible()) {
                btnAsiento.setBackground(Color.GRAY);
                btnAsiento.setEnabled(false);
            } else {
                btnAsiento.addActionListener(e -> seleccionarAsiento(
                        (JButton)e.getSource(), asiento));
            }

            panelAsientos.add(btnAsiento);
        }
    }

    private void seleccionarAsiento(JButton boton, Asiento asiento) {
        if (asientoSeleccionado != null) {
            asientoSeleccionado.setBackground(null);
        }
        asientoSeleccionado = boton;
        boton.setBackground(Color.GREEN);
    }
}
