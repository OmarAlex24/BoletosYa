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

    public LoginForm() throws SQLException {
        setTitle("Inicio de Sesion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Usamos GridBagLayout para mayor control
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        JLabel titleLabel = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Email Label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        panel.add(emailLabel, gbc);

        // Email Field
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 20)); // Tamaño del campo
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        panel.add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(100, 20)); // Tamaño del campo
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Botón de inicio de sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(70, 130, 180)); // Color de fondo
        loginButton.setForeground(Color.black); // Color del texto
        loginButton.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        loginButton.addActionListener(new LoginAction());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Mensaje de error o éxito
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        add(panel);
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
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                busquedaVueloFrame.setVisible(true);

            } else {
                messageLabel.setText("Email o contraseña incorrectos");
            }
        }
    }
}