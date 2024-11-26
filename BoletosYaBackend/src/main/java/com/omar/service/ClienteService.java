package com.omar.service;

import com.omar.DAO.ClienteDAO;
import com.omar.entity.Cliente;

import java.sql.SQLException;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente autenticar(String email, String password) {
        return clienteDAO.autenticar(email, password);
    }
}
