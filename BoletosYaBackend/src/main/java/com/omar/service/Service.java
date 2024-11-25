package com.omar.service;



import java.util.List;
import java.util.Map;

public interface Service<T> {
    T crear(T t) throws Exception;
    T obtenerPorId(Integer id) throws Exception;
    List<T> listarTodos() throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(T t) throws Exception;
    void eliminarPorId(Integer id) throws Exception;

}