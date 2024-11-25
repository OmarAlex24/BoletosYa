package com.omar.service;

import com.omar.DAO.AeropuertoDAO;
import com.omar.entity.Aeropuerto;

import java.sql.SQLException;
import java.util.List;

public class AeropuertoService implements Service<Aeropuerto> {

    private final AeropuertoDAO aeropuertoDAO;

    public AeropuertoService(AeropuertoDAO aeropuertoDAO) {
        this.aeropuertoDAO = aeropuertoDAO;
    }

    @Override
    public Aeropuerto crear(Aeropuerto aeropuerto) throws Exception {
        return aeropuertoDAO.agregar(aeropuerto);
    }

    @Override
    public Aeropuerto obtenerPorId(Integer id) throws Exception {
        return aeropuertoDAO.obtenerPorId(id);
    }

    @Override
    public List<Aeropuerto> listarTodos() throws Exception {
        return aeropuertoDAO.listarTodos();
    }

    @Override
    public void actualizar(Aeropuerto aeropuerto) throws Exception {
        aeropuertoDAO.modificar(aeropuerto);
    }

    @Override
    public void eliminar(Aeropuerto aeropuerto) throws Exception {
        aeropuertoDAO.eliminar(aeropuerto);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        aeropuertoDAO.eliminarPorId(id);
    }

    public List<Aeropuerto> buscarPorNombreOCodigo(String texto) throws Exception {
        return aeropuertoDAO.buscarPorNombreOCodigo(texto);
    }

    public Aeropuerto buscarPorCodigo(String codigo) throws Exception {
        return aeropuertoDAO.buscarPorCodigo(codigo);
    }
}
