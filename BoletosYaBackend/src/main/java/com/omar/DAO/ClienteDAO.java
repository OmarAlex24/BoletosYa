package com.omar.DAO;

import com.omar.BD;
import com.omar.entity.Cliente;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO implements DAO<Cliente> {
    private final Connection connection;

    public ClienteDAO() throws SQLException {
        this.connection = BD.getInstance().getConnection();
    }

    public Cliente autenticar(String email, String password) {
        String sql = "SELECT * FROM cliente WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Obtener el hash almacenado
                    String hashedPassword = rs.getString("password");

                    // Verificar si la contraseña coincide
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return mapearCliente(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar usuario", e);
        }
        return null;
    }
    @Override
    public Cliente agregar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, apellidos, email, password) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getEmail());

            // Generar el hash del password
            String hashedPassword = BCrypt.hashpw(cliente.getPassword(), BCrypt.gensalt());
            ps.setString(4, hashedPassword);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del cliente falló");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                    cliente.setPassword(hashedPassword);
                } else {
                    throw new SQLException("La creación del cliente falló, no se obtuvo ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar cliente", e);
        }
        return cliente;
    }

    @Override
    public void eliminar(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cliente.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    }

    @Override
    public void eliminarPorId(Integer id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    }

    @Override
    public void modificar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, apellidos = ?, email = ?, password = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getPassword());
            ps.setInt(5, cliente.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar cliente", e);
        }
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setEmail(rs.getString("email"));
                cliente.setPassword(rs.getString("password"));
                return cliente;
            }
            ps.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente", e);
        }
    }

    @Override
    public ArrayList<Cliente> listarTodos() {
        String sql = "SELECT * FROM cliente";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            ArrayList<Cliente> clientes = new ArrayList<>();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setEmail(rs.getString("email"));
                cliente.setPassword(rs.getString("password"));
                clientes.add(cliente);
            }
            ps.close();
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar clientes", e);
        }
    }

    /**
     * Actualiza la contraseña de un cliente
     */
    public boolean modificar(int clienteId, String newPassword) {
        String sql = "UPDATE cliente SET password = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, clienteId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contraseña", e);
        }
    }


    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setEmail(rs.getString("email"));
        cliente.setPassword(rs.getString("password"));
        return cliente;
    }
}
