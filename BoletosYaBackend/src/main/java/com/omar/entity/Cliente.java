package com.omar.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private List<Reservacion> reservaciones;

    public Cliente(String nombre, String apellidos, String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
    }

    public boolean agregarReservacion(Reservacion reservacion) {
        return reservaciones.add(reservacion);
    }

    public boolean eliminarReservacion(Reservacion reservacion) {
        return reservaciones.remove(reservacion);
    }

    public boolean modificarReservacion(Reservacion reservacion) {
        for (int i = 0; i < reservaciones.size(); i++) {
            if (reservaciones.get(i).getId() == reservacion.getId()) {
                reservaciones.set(i, reservacion);
                return true;
            }
        }
        return false;
    }
}
