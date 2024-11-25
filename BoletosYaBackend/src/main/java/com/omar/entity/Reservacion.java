package com.omar.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Reservacion {
    private int id;
    private Cliente cliente;
    private Asiento asiento;
    private LocalDate fechaReservacion;
    private EstadoReservacion estado;

    public Reservacion() {
    }

    public Reservacion(Cliente cliente, Asiento asiento) {
        this.cliente = cliente;
        this.asiento = asiento;
        this.fechaReservacion = LocalDate.now();
        this.estado = EstadoReservacion.PENDIENTE;
    }

    public void cancelarReservacion() {
        this.estado = EstadoReservacion.CANCELADA;
    }

    public void confirmarReservacion() {
        this.estado = EstadoReservacion.CONFIRMADA;
    }
}
