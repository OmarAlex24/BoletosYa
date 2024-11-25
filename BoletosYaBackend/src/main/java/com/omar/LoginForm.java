package com.omar;

import com.omar.entity.Cliente;
import com.omar.service.ClienteService;
import com.omar.swing.BusquedaVueloFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class LoginForm extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private ClienteService clienteService = new ClienteService();

    public LoginForm() {
        setTitle("Inicio de Sesion");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        JButton loginButton = new JButton("Iniciar Sesion");
        loginButton.addActionListener(new LoginAction());

        panel.add(new JLabel("User:", SwingConstants.CENTER));
        panel.add(emailField);
        panel.add(new JLabel("Password:", SwingConstants.CENTER));
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.NORTH);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Cliente cliente = clienteService.autenticar(email, password);

            if (cliente != null) {
                messageLabel.setText("Inicio de sesión exitoso");
                JOptionPane.showMessageDialog(com.omar.LoginForm.this, "Bienvenido " + cliente.getNombre());
                dispose();

                BusquedaVueloFrame busquedaVueloFrame = null;
                try {
                    busquedaVueloFrame = new BusquedaVueloFrame();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                busquedaVueloFrame.setVisible(true);


            } else {
                messageLabel.setText("Email o contraseña incorrectos");
            }
        }

    }
}