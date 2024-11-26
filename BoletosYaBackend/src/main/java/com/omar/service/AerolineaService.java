package com.omar.service;

import com.omar.DAO.AerolineaDAO;
import com.omar.entity.Aerolinea;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AerolineaService implements Service<Aerolinea> {

    private final AerolineaDAO aerolineaDAO;

    public AerolineaService(AerolineaDAO aerolineaDAO) {
        this.aerolineaDAO = aerolineaDAO;
    }

    @Override
    public Aerolinea crear(Aerolinea aerolinea) throws Exception {
        return aerolineaDAO.agregar(aerolinea);
    }

    @Override
    public Aerolinea obtenerPorId(Integer id) throws Exception {
        return aerolineaDAO.obtenerPorId(id);
    }

    @Override
    public List<Aerolinea> listarTodos() throws Exception {
        return aerolineaDAO.listarTodos();
    }

    @Override
    public void actualizar(Aerolinea aerolinea) throws Exception {
        aerolineaDAO.modificar(aerolinea);
    }

    @Override
    public void eliminar(Aerolinea aerolinea) throws Exception {
        aerolineaDAO.eliminar(aerolinea);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        aerolineaDAO.eliminarPorId(id);
    }

}
