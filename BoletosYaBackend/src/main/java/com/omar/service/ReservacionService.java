package com.omar.service;

import com.omar.DAO.ReservacionDAO;
import com.omar.entity.Reservacion;

import java.util.List;

public class ReservacionService implements Service<Reservacion> {

    private final ReservacionDAO reservacionDAO;

    public ReservacionService() throws Exception {
        reservacionDAO = new ReservacionDAO();
    }

    public ReservacionService(ReservacionDAO reservacionDAO) {
        this.reservacionDAO = reservacionDAO;
    }

    @Override
    public Reservacion crear(Reservacion reservacion) throws Exception {
       return reservacionDAO.agregar(reservacion);
    }

    @Override
    public Reservacion obtenerPorId(Integer id) throws Exception {
        return reservacionDAO.obtenerPorId(id);
    }

    @Override
    public List<Reservacion> listarTodos() throws Exception {
        return reservacionDAO.listarTodos();
    }

    @Override
    public void actualizar(Reservacion reservacion) throws Exception {
        reservacionDAO.modificar(reservacion);
    }

    @Override
    public void eliminar(Reservacion reservacion) throws Exception {
        reservacionDAO.eliminar(reservacion);
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        reservacionDAO.eliminarPorId(id);
    }
}
