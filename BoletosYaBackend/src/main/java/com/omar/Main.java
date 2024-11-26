package com.omar;

import com.omar.DAO.ClienteDAO;
import com.omar.entity.Cliente;
import com.omar.swing.BusquedaVueloFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main{

    public static void main(String[] args) {

        // Registro de usuario
//        try {
//            ClienteDAO clienteDAO = new ClienteDAO();
//            Cliente nuevoCliente = new Cliente();
//            nuevoCliente.setNombre("Pedro");
//            nuevoCliente.setApellidos("Pérez");
//            nuevoCliente.setEmail("pedro@example.com");
//            nuevoCliente.setPassword("1234"); // Se hasheará automáticamente
//
//            Cliente clienteRegistrado = clienteDAO.agregar(nuevoCliente);
//            System.out.println("Cliente registrado con ID: " + clienteRegistrado.getId());
//        } catch (RuntimeException e) {
//            System.err.println("Error en el registro: " + e.getMessage());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}