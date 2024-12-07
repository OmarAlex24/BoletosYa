package com.omar.service;

import com.omar.DAO.ClienteDAO;
import com.omar.entity.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteService implements Service<Cliente> {
    private final ClienteDAO clienteDAO;

    public ClienteService() throws SQLException {
        this.clienteDAO = new ClienteDAO();
    }

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente autenticar(String email, String password) {
        return clienteDAO.autenticar(email, password);
    }

    @Override
    public Cliente crear(Cliente cliente) throws Exception {
        return clienteDAO.agregar(cliente);
    }

    @Override
    public Cliente obtenerPorId(Integer id) throws Exception {
        return clienteDAO.obtenerPorId(id);
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        return clienteDAO.listarTodos();
    }

    @Override
    public void actualizar(Cliente cliente) throws Exception {
        clienteDAO.modificar(cliente);
    }

    @Override
    public void eliminar(Cliente cliente) throws Exception {
        clienteDAO.eliminar(cliente);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        clienteDAO.eliminarPorId(id);
    }
}
