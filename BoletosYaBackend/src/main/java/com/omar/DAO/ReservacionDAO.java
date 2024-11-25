package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Asiento;
import com.omar.entity.Cliente;
import com.omar.entity.EstadoReservacion;
import com.omar.entity.Reservacion;
import com.omar.service.AsientoService;
import com.omar.service.ClienteService;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class ReservacionDAO implements DAO<Reservacion> {
    private final Connection connection;
    private final ClienteService clienteService;
    private final AsientoService asientoService;

    @Override
    public Reservacion agregar(Reservacion reservacion) {
        String sql = "INSERT INTO reservacion (cliente_id, asiento_id) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, reservacion.getCliente().getId());
            ps.setInt(2, reservacion.getAsiento().getId());

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservacion.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La creación de reservacion falló, no se obtuvo ID");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar reservacion", e);
        }
        return reservacion;
    }

    @Override
    public void eliminar(Reservacion reservacion) {
        String sql = "DELETE FROM reservacion WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, reservacion.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar reservacion", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM reservacion WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar reservacion", e);
        }
    }

    @Override
    public void modificar(Reservacion reservacion) {
        String sql = "UPDATE reservacion SET cliente_id = ?, asiento_id = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, reservacion.getCliente().getId());
            ps.setInt(2, reservacion.getAsiento().getId());
            ps.setInt(3, reservacion.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar reservacion", e);
        }
    }

    @Override
    public Reservacion obtenerPorId(Integer id) {
        String sql = "SELECT * FROM reservacion WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reservacion reservacion = new Reservacion();
                reservacion.setId(rs.getInt("id"));

                Cliente cliente = clienteService.obtenerPorId(rs.getInt("cliente_id"));
                Asiento asiento = asientoService.obtenerPorId(rs.getInt("asiento_id"));

                reservacion.setCliente(cliente);
                reservacion.setAsiento(asiento);
                reservacion.setFechaReservacion(rs.getDate("fecha_reservacion").toLocalDate());
                reservacion.setEstado(EstadoReservacion.valueOf(rs.getString("estado")));
                return reservacion;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reservacion", e);
        }
    }

    @Override
    public ArrayList<Reservacion> listarTodos() {
        String sql = "SELECT * FROM reservacion";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Reservacion> reservaciones = new ArrayList<>();
            while (rs.next()) {
                Reservacion reservacion = new Reservacion();
                reservacion.setId(rs.getInt("id"));
                Cliente cliente = clienteDAO.obtenerPorId(rs.getInt("cliente_id"));
                Asiento asiento = asientoDAO.obtenerPorId(rs.getInt("asiento_id"));

                reservacion.setCliente(cliente);
                reservacion.setAsiento(asiento);
                reservacion.setFechaReservacion(rs.getDate("fecha_reservacion").toLocalDate());
                reservacion.setEstado(EstadoReservacion.valueOf(rs.getString("estado")));
                reservaciones.add(reservacion);
            }
            ps.close();
            return reservaciones;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reservaciones", e);
        }
    }
}
