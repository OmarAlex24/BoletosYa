package com.omar.service;

import com.omar.DAO.AsientoDAO;
import com.omar.entity.Asiento;
import com.omar.entity.Vuelo;

import java.util.ArrayList;
import java.util.List;

public class AsientoService implements Service<Asiento> {

    private final AsientoDAO asientoDAO;

    public AsientoService(AsientoDAO asientoDAO) {
        this.asientoDAO = asientoDAO;
    }

    @Override
    public Asiento crear(Asiento asiento) throws Exception {
        return asientoDAO.agregar(asiento);
    }

    @Override
    public Asiento obtenerPorId(Integer id) throws Exception {
        return asientoDAO.obtenerPorId(id);
    }

    @Override
    public List<Asiento> listarTodos() throws Exception {
        return asientoDAO.listarTodos();
    }

    @Override
    public void actualizar(Asiento asiento) throws Exception {
        asientoDAO.modificar(asiento);
    }

    @Override
    public void eliminar(Asiento asiento) throws Exception {
        asientoDAO.eliminar(asiento);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        asientoDAO.eliminarPorId(id);
    }

    public void alternarDisponibilidad(Asiento asiento) throws Exception {
        asientoDAO.alternarDisponibilidad(asiento);
    }

    public ArrayList<Asiento> listarTodosVuelo(Vuelo vuelo) throws Exception {
        return asientoDAO.listarTodosVuelo(vuelo);
    }
}
