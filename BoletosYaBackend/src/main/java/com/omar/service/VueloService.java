package com.omar.service;

import com.omar.DAO.VueloDAO;
import com.omar.entity.Vuelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class VueloService implements Service<Vuelo> {

    private final VueloDAO vueloDAO;

    public VueloService(VueloDAO vueloDAO) {
        this.vueloDAO = vueloDAO;
    }

    @Override
    public Vuelo crear(Vuelo vuelo) throws Exception {
       return vueloDAO.agregar(vuelo);
    }

    @Override
    public Vuelo obtenerPorId(Integer id) throws Exception {
        return vueloDAO.obtenerPorId(id);
    }

    @Override
    public List<Vuelo> listarTodos() throws Exception {
        return vueloDAO.listarTodos();
    }

    @Override
    public void actualizar(Vuelo vuelo) throws Exception {
        vueloDAO.modificar(vuelo);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        vueloDAO.eliminarPorId(id);
    }

    @Override
    public void eliminar(Vuelo vuelo) throws Exception {
        vueloDAO.eliminar(vuelo);
    }

    public List<Vuelo> buscarVuelosDirectos(String origen, String destino, LocalDateTime fecha) {
        return vueloDAO.buscarVuelosDirectos(origen, destino, fecha);
    }

    public Vuelo buscarVueloCodigo(String codigo) {
        return vueloDAO.buscarVueloCodigo(codigo);
    }
}
