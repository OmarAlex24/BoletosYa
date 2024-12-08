package com.omar;

import com.omar.DAO.ClienteDAO;
import com.omar.entity.Cliente;
import com.omar.service.ClienteService;
import com.omar.service.ServiceFactory;
import com.omar.swing.BusquedaVueloFrame;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main{

    public static void main(String[] args) throws Exception {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        // Registro de usuario
        List<Cliente> clientes = serviceFactory.getClienteService().listarTodos();

        // Si el usuario de prueba no esta registrado, registrarlo
        if (clientes.isEmpty()) {
            try {
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setNombre("Pedro");
                nuevoCliente.setApellidos("Pérez");
                nuevoCliente.setEmail("pedro@example.com");
                nuevoCliente.setPassword("1234");

                Cliente clienteRegistrado = clienteDAO.agregar(nuevoCliente);
                System.out.println("Cliente registrado con ID: " + clienteRegistrado.getId());
            } catch (RuntimeException e) {
                System.err.println("Error en el registro: " + e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = null;
            try {
                loginForm = new LoginForm();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            loginForm.setVisible(true);
        });
    }
}