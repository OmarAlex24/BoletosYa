package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Asiento;
import com.omar.entity.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsientoDAO implements DAO<Asiento> {
    private final Connection connection;

    public AsientoDAO() throws SQLException {
        this.connection = BD.getInstance().getConnection();
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
}
