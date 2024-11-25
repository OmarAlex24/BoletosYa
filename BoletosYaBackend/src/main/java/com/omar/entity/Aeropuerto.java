package com.omar.entity;

import lombok.Data;

@Data
public class Aeropuerto {
    private int id;
    private String codigo;
    private String nombre;
    private String ciudad;
    private String pais;

    public Aeropuerto() {
    }

    public Aeropuerto(Aeropuerto aeropuerto) {
    }
}
