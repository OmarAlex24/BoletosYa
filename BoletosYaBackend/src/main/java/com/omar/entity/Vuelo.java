package com.omar.entity;

import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Vuelo {
    private int id;
    private Aerolinea aerolinea;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private LocalDate fechaSalida;
    private LocalDate fechaLlegada;
    private double precio;
    private List<Asiento> asientos;

    public Vuelo() {
    }

    public Vuelo(Aerolinea aerolinea, Aeropuerto origen, Aeropuerto destino, LocalDate fechaSalida, LocalDate fechaLlegada, double precio) {
        this.aerolinea = aerolinea;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.precio = precio;
        this.asientos = new ArrayList<Asiento>();
    }

}
