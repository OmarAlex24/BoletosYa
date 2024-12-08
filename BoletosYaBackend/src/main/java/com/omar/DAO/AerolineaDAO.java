package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Aerolinea;

import java.sql.*;
import java.util.ArrayList;

public class AerolineaDAO implements DAO<Aerolinea> {
    private final Connection connection;

    public AerolineaDAO() throws SQLException {
        this.connection = BD.getInstance().getConnection();
    }

    @Override
    public Aerolinea agregar(Aerolinea aerolinea) {
        String sql = "INSERT INTO aerolinea (nombre, codigo) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, aerolinea.getNombre());
            ps.setString(2, aerolinea.getCodigo());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                aerolinea.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La creación de aerolinea falló, no se obtuvo ID");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar aerolinea", e);
        }

        return aerolinea;
    }

    @Override
    public void eliminar(Aerolinea aerolinea) {
        String sql = "DELETE FROM aerolinea WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, aerolinea.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar aerolinea", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM aerolinea WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar aerolinea", e);
        }
    }

    @Override
    public void modificar(Aerolinea aerolinea) {
        String sql = "UPDATE aerolinea SET nombre = ?, codigo = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aerolinea.getNombre());
            ps.setString(2, aerolinea.getCodigo());
            ps.setInt(3, aerolinea.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar aerolinea", e);
        }
    }

    @Override
    public Aerolinea obtenerPorId(Integer id) {
        String sql = "SELECT * FROM aerolinea WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Aerolinea aerolinea = new Aerolinea();
                aerolinea.setId(rs.getInt("id"));
                aerolinea.setNombre(rs.getString("nombre"));
                aerolinea.setCodigo(rs.getString("codigo"));
                return aerolinea;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aerolinea", e);
        }
    }

    @Override
    public ArrayList<Aerolinea> listarTodos() {
        String sql = "SELECT * FROM aerolinea";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Aerolinea> aerolineas = new ArrayList<>();
            while (rs.next()) {
                Aerolinea aerolinea = new Aerolinea();
                aerolinea.setId(rs.getInt("id"));
                aerolinea.setNombre(rs.getString("nombre"));
                aerolinea.setCodigo(rs.getString("codigo"));
                aerolineas.add(aerolinea);
            }
            ps.close();
            return aerolineas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar aerolineas", e);
        }
    }
}
