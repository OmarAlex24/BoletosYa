package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Aeropuerto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AeropuertoDAO implements DAO<Aeropuerto> {
    private final Connection connection;

    public AeropuertoDAO() throws SQLException {
        this.connection = BD.getInstance().getConnection();
    }

    @Override
    public Aeropuerto agregar(Aeropuerto aeropuerto) {
        String sql = "INSERT INTO boletos_ya_db.aeropuerto (codigo, nombre, ciudad, pais) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aeropuerto.getCodigo());
            ps.setString(2, aeropuerto.getNombre());
            ps.setString(3, aeropuerto.getCiudad());
            ps.setString(4, aeropuerto.getPais());

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                aeropuerto.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La creación de aeropuerto falló, no se obtuvo ID");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar aeropuerto", e);
        }
        return aeropuerto;
    }

    @Override
    public void eliminar(Aeropuerto aeropuerto) {
        String sql = "DELETE FROM boletos_ya_db.aeropuerto WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, aeropuerto.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar aeropuerto", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM boletos_ya_db.aeropuerto WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar aeropuerto", e);
        }
    }

    @Override
    public void modificar(Aeropuerto aeropuerto) {
        String sql = "UPDATE boletos_ya_db.aeropuerto SET codigo = ?, nombre = ?, ciudad = ?, pais = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aeropuerto.getCodigo());
            ps.setString(2, aeropuerto.getNombre());
            ps.setString(3, aeropuerto.getCiudad());
            ps.setString(4, aeropuerto.getPais());
            ps.setInt(5, aeropuerto.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar aeropuerto", e);
        }
    }

    @Override
    public Aeropuerto obtenerPorId(Integer id) {
        String sql = "SELECT * FROM boletos_ya_db.aeropuerto WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Aeropuerto aeropuerto = new Aeropuerto();
                aeropuerto.setId(rs.getInt("id"));
                aeropuerto.setCodigo(rs.getString("codigo"));
                aeropuerto.setNombre(rs.getString("nombre"));
                aeropuerto.setCiudad(rs.getString("ciudad"));
                aeropuerto.setPais(rs.getString("pais"));
                return aeropuerto;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aeropuerto", e);
        }
    }

    @Override
    public ArrayList<Aeropuerto> listarTodos() {
        String sql = "SELECT * FROM boletos_ya_db.aeropuerto";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Aeropuerto> aeropuertos = new ArrayList<>();
            while (rs.next()) {
                Aeropuerto aeropuerto = new Aeropuerto();
                aeropuerto.setId(rs.getInt("id"));
                aeropuerto.setCodigo(rs.getString("codigo"));
                aeropuerto.setNombre(rs.getString("nombre"));
                aeropuerto.setCiudad(rs.getString("ciudad"));
                aeropuerto.setPais(rs.getString("pais"));
                aeropuertos.add(aeropuerto);
            }
            ps.close();
            return aeropuertos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aeropuertos", e);
        }
    }

    public List<Aeropuerto> buscarPorNombreOCodigo(String texto) throws Exception {
        String sql = "SELECT * FROM boletos_ya_db.aeropuerto WHERE nombre LIKE ? OR codigo LIKE ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            ResultSet rs = ps.executeQuery();
            List<Aeropuerto> aeropuertos = new ArrayList<>();
            while (rs.next()) {
                Aeropuerto aeropuerto = new Aeropuerto();
                aeropuerto.setId(rs.getInt("id"));
                aeropuerto.setCodigo(rs.getString("codigo"));
                aeropuerto.setNombre(rs.getString("nombre"));
                aeropuerto.setCiudad(rs.getString("ciudad"));
                aeropuerto.setPais(rs.getString("pais"));
                aeropuertos.add(aeropuerto);
            }
            ps.close();
            return aeropuertos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aeropuertos", e);
        }
    }

    public Aeropuerto buscarPorCodigo(String codigo) throws Exception {
        String sql = "SELECT * FROM boletos_ya_db.aeropuerto WHERE codigo = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, codigo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Aeropuerto aeropuerto = new Aeropuerto();
                aeropuerto.setId(rs.getInt("id"));
                aeropuerto.setCodigo(rs.getString("codigo"));
                aeropuerto.setNombre(rs.getString("nombre"));
                aeropuerto.setCiudad(rs.getString("ciudad"));
                aeropuerto.setPais(rs.getString("pais"));
                return aeropuerto;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aeropuerto", e);
        }
    }
}
