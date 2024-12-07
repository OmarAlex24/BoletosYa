package com.omar.entity;

import lombok.Data;

@Data
public class Asiento {
    private int id;
    private int vueloId;
    private String numeroAsiento;
    private boolean disponible;

    public Asiento(int vueloId, String numeroAsiento, boolean disponible) {
        this.vueloId = vueloId;
        this.numeroAsiento = numeroAsiento;
        this.disponible = disponible;
    }

    public Asiento() {

    }

    public void alternarDisponibilidad() {
        this.disponible = !this.disponible;
    }
}
