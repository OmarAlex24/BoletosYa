package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Aerolinea;
import com.omar.entity.Aeropuerto;
import com.omar.entity.Vuelo;
import com.omar.service.AerolineaService;
import com.omar.service.AeropuertoService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VueloDAO implements DAO<Vuelo> {
    private final Connection connection;
    private final AeropuertoService aeropuertoService;
    private final AerolineaService aerolineaService;

    public VueloDAO(AeropuertoService aeropuertoService, AerolineaService aerolineaService) throws SQLException {
        this.aeropuertoService = aeropuertoService;
        this.aerolineaService = aerolineaService;
        this.connection = BD.getInstance().getConnection();
    }

    @Override
    public Vuelo agregar(Vuelo vuelo) {
        String sql = "INSERT INTO boletos_ya_db.vuelo (aerolinea_id, origen_id, destino_id, fecha_salida, fecha_llegada, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, vuelo.getAerolinea().getId());
            ps.setInt(2, vuelo.getOrigen().getId());
            ps.setInt(3, vuelo.getDestino().getId());
            ps.setDate(4, java.sql.Date.valueOf(vuelo.getFechaSalida()));
            ps.setDate(5, java.sql.Date.valueOf(vuelo.getFechaLlegada()));
            ps.setDouble(6, vuelo.getPrecio());
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                vuelo.setId(generatedKeys.getInt(1));
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar vuelo", e);
        }
        return vuelo;
    }

    @Override
    public void eliminar(Vuelo vuelo) {
        String sql = "DELETE FROM boletos_ya_db.vuelo WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, vuelo.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar vuelo", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM boletos_ya_db.vuelo WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar vuelo", e);
        }
    }

    @Override
    public void modificar(Vuelo vuelo) {
        String sql = "UPDATE boletos_ya_db.vuelo SET aerolinea_id = ?, origen_id = ?, destino_id = ?, fecha_salida = ?, fecha_llegada = ?, precio = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, vuelo.getAerolinea().getId());
            ps.setInt(2, vuelo.getOrigen().getId());
            ps.setInt(3, vuelo.getDestino().getId());
            ps.setDate(4, Date.valueOf(vuelo.getFechaSalida()));
            ps.setDate(5, Date.valueOf(vuelo.getFechaLlegada()));
            ps.setDouble(6, vuelo.getPrecio());
            ps.setInt(7, vuelo.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar vuelo", e);
        }
    }

    @Override
    public Vuelo obtenerPorId(Integer id) {
        String sql = "SELECT * FROM boletos_ya_db.vuelo WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearVuelo(rs);
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vuelo", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Vuelo> listarTodos() {
        String sql = "SELECT * FROM boletos_ya_db.vuelo";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Vuelo> vuelos = new ArrayList<>();
            while (rs.next()) {
                Vuelo vuelo = mapearVuelo(rs);
                vuelos.add(vuelo);
            }
            ps.close();
            return vuelos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vuelos", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vuelo> buscarVuelosDirectos(String origen, String destino, LocalDate fecha) {
        List<Vuelo> vuelos = new ArrayList<>();
        String query = """
          SELECT v.*, a.nombre as aerolinea_nombre, a.codigo as aerolinea_codigo,
          ao.*, ad.*
          FROM boletos_ya_db.vuelo v
          JOIN boletos_ya_db.aerolinea a ON v.id = a.id
          JOIN boletos_ya_db.aeropuerto ao ON v.origen_id = ao.id
          JOIN boletos_ya_db.aeropuerto ad ON v.destino_id = ad.id
          WHERE ao.codigo = ? AND ad.codigo = ?
          AND DATE(fecha_salida) = ?
      """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, origen);
            stmt.setString(2, destino);
            stmt.setDate(3, Date.valueOf(fecha));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vuelo vuelo = mapearVuelo(rs);
                vuelos.add(vuelo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vuelos;
    }

    private Vuelo mapearVuelo(ResultSet rs) throws Exception {
        Vuelo vuelo = new Vuelo();
        vuelo.setId(rs.getInt("id"));

        Aerolinea aerolinea = aerolineaService.obtenerPorId(rs.getInt("aerolinea_id"));
        Aeropuerto origen = aeropuertoService.obtenerPorId(rs.getInt("origen_id"));
        Aeropuerto destino = aeropuertoService.obtenerPorId(rs.getInt("destino_id"));

        vuelo.setAerolinea(aerolinea);
        vuelo.setOrigen(origen);
        vuelo.setDestino(destino);
        vuelo.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
        vuelo.setFechaLlegada(rs.getDate("fecha_llegada").toLocalDate());
        vuelo.setPrecio(rs.getDouble("precio"));
        return vuelo;
    }
}
