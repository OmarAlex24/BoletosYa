package com.omar.entity;

import lombok.Data;

@Data
public class Asiento {
    private int id;
    private Vuelo vuelo;
    private String numeroAsiento;
    private boolean disponible;

    public Asiento(Vuelo vuelo, String numeroAsiento, boolean disponible) {
        this.vuelo = vuelo;
        this.numeroAsiento = numeroAsiento;
        this.disponible = disponible;
    }

    public void alternarDisponibilidad() {
        this.disponible = !this.disponible;
    }
}
