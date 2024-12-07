package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Asiento;
import com.omar.entity.Vuelo;
import com.omar.service.ServiceFactory;
import com.omar.service.VueloService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AsientoDAO implements DAO<Asiento> {
    private final Connection connection;
    private final VueloService vueloService;

    public AsientoDAO() throws SQLException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.connection = BD.getInstance().getConnection();
        this.vueloService = serviceFactory.getVueloService();
    }

    @Override
    public Asiento agregar(Asiento asiento) {
        String sql = "INSERT INTO asiento (vuelo_id, numero_asiento) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, asiento.getVuelo().getId());
            ps.setString(2, asiento.getNumeroAsiento());

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                asiento.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La creación de asiento falló, no se obtuvo ID");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar asiento", e);
        }
        return asiento;
    }

    @Override
    public void eliminar(Asiento asiento) {
        String sql = "DELETE FROM asiento WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, asiento.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar asiento", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM asiento WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar asiento", e);
        }
    }

    @Override
    public void modificar(Asiento asiento) {
        String sql = "UPDATE asiento SET numero_asiento = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, asiento.getNumeroAsiento());
            ps.setInt(2, asiento.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar asiento", e);
        }
    }

    @Override
    public Asiento obtenerPorId(Integer id) {
        String sql = "SELECT * FROM asiento WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId(rs.getInt("id"));
                asiento.setNumeroAsiento(rs.getString("numero_asiento"));
                Vuelo vuelo = vueloService.obtenerPorId(rs.getInt("vuelo_id"));
                asiento.setVuelo(vuelo);
                return asiento;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asiento", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Asiento> listarTodos() {
        String sql = "SELECT * FROM asiento";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Asiento> asientos = new ArrayList<>();
            while (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId(rs.getInt("id"));
                asiento.setNumeroAsiento(rs.getString("numero_asiento"));
                Vuelo vuelo = vueloService.obtenerPorId(rs.getInt("vuelo_id"));
                asiento.setVuelo(vuelo);
                asientos.add(asiento);
            }
            ps.close();
            return asientos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asientos", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Asiento> listarTodosVuelo(Vuelo vuelo) throws Exception {
        String query = """
          SELECT a.*
          FROM boletos_ya_db.asiento a
          JOIN boletos_ya_db.vuelo v ON a.vuelo_id = v.id
          JOIN boletos_ya_db.aerolinea ao ON v.id = ao.id
          JOIN boletos_ya_db.aeropuerto ad ON v.destino_id = ad.id
          WHERE v.id = ?
          """;

        ArrayList<Asiento> asientos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vuelo.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId(rs.getInt("id"));
                asiento.setNumeroAsiento(rs.getString("numero_asiento"));
                asiento.setVuelo(vuelo);
                asiento.setDisponible(rs.getBoolean("disponible"));
                asientos.add(asiento);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asientos", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return asientos;
    }
}
