package com.omar.service;

import com.omar.DAO.ClienteDAO;
import com.omar.entity.Cliente;

import java.sql.SQLException;

public class ClienteService {
    public Cliente autenticar(String email, String password) {
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            return clienteDAO.autenticar(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
