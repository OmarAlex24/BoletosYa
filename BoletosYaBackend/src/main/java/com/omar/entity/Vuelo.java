package com.omar.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Vuelo {
    private int id;
    private String codigo;
    private Aerolinea aerolinea;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private double precio;
    private List<Asiento> asientos;

    public Vuelo() {
    }

    public Vuelo(Aerolinea aerolinea,String codigo, Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaSalida, LocalDateTime fechaLlegada, double precio) {
        this.aerolinea = aerolinea;
        this.origen = origen;
        this.codigo = codigo;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.precio = precio;
        this.asientos = new ArrayList<Asiento>();
    }

}
