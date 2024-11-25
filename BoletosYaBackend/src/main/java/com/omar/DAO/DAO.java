package com.omar.DAO;

import java.util.ArrayList;

public interface DAO <T> {
    T agregar(T t);
    void eliminar(T t);
    void eliminarPorId(Integer id);
    void modificar(T t);
    T obtenerPorId(Integer id);
    ArrayList<T> listarTodos();
}
