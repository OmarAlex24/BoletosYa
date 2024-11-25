package com.omar.entity;

import lombok.Data;
import java.util.List;

@Data
public class Aerolinea {
    private int id;
    private String nombre;
    private String codigo;
    private List<Vuelo> vuelos;

    public Aerolinea() {
    }

    public Aerolinea(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public boolean agregarVuelo(Vuelo vuelo) {
        return vuelos.add(vuelo);
    }

    public boolean eliminarVuelo(Vuelo vuelo) {
        return vuelos.remove(vuelo);
    }

    public boolean modificarVuelo(Vuelo vuelo) {
        for (int i = 0; i < vuelos.size(); i++) {
            if (vuelos.get(i).getId() == vuelo.getId()) {
                vuelos.set(i, vuelo);
                return true;
            }
        }
        return false;
    }
}
